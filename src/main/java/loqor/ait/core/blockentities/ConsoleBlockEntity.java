package loqor.ait.core.blockentities;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.entities.ConsoleControlEntity;
import loqor.ait.core.item.ChargedZeitonCrystalItem;
import loqor.ait.core.managers.RiftChunkManager;
import loqor.ait.registry.impl.console.ConsoleRegistry;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisConsole;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.control.ControlTypes;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.link.LinkableBlockEntity;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

import static loqor.ait.tardis.util.TardisUtil.findTardisByInterior;
import static loqor.ait.tardis.util.TardisUtil.isClient;

public class ConsoleBlockEntity extends LinkableBlockEntity implements BlockEntityTicker<ConsoleBlockEntity> {
	public final List<ConsoleControlEntity> controlEntities = new ArrayList<>();
	public final AnimationState ANIM_STATE = new AnimationState();
	public int age;
	private boolean needsControls = true;
	private boolean needsSync = true;
	private Identifier type;
	private Identifier variant;
	private final boolean wasPowered = false;
	private boolean needsReloading = true;
	private UUID parent;

	public static final Identifier SYNC_TYPE = new Identifier(AITMod.MOD_ID, "sync_console_type");
	public static final Identifier SYNC_VARIANT = new Identifier(AITMod.MOD_ID, "sync_console_variant");
	public static final Identifier SYNC_PARENT = new Identifier(AITMod.MOD_ID, "sync_console_parent");
	public static final Identifier ASK = new Identifier(AITMod.MOD_ID, "client_ask_console");

	public ConsoleBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.CONSOLE_BLOCK_ENTITY_TYPE, pos, state);
		if (!this.hasWorld()) return;
		if (this.findTardis().isEmpty()) return;
		this.linkDesktop();
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		if (type != null)
			nbt.putString("type", type.toString());
		if (variant != null)
			nbt.putString("variant", variant.toString());
		if (parent != null) {
			nbt.put("parent", NbtHelper.fromUuid(parent));
		}

		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		if (nbt.contains("type"))
			setType(Identifier.tryParse(nbt.getString("type")));
		if (nbt.contains("variant")) {
			setVariant(Identifier.tryParse(nbt.getString("variant")));
		}
		if (nbt.contains("parent")) {
			setParent(NbtHelper.toUuid(nbt.get("parent")));
		}

		spawnControls();
		markNeedsSyncing();
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = super.toInitialChunkDataNbt();
		if (nbt.contains("type"))
			setType(ConsoleRegistry.REGISTRY.get(Identifier.tryParse(nbt.getString("type"))));
		if (nbt.contains("variant")) {
			setVariant(Identifier.tryParse(nbt.getString("variant")));
		}
		if (nbt.contains("parent"))
			setParent(NbtHelper.toUuid(nbt.get("parent")));

		if (type != null)
			nbt.putString("type", type.toString());
		if (variant != null)
			nbt.putString("variant", variant.toString());
		if (parent != null)
			nbt.put("parent", NbtHelper.fromUuid(parent));
		markNeedsControl();
		markNeedsSyncing();
		return nbt;
	}

	@Override
	public Optional<Tardis> findTardis() {
		if (this.tardisId == null) {
			this.refindTardis();
		}

		Optional<Tardis> found = super.findTardis();

		if (found.isEmpty())
			refindTardis();

		return found;
	}

	private void refindTardis() {
		Tardis found = findTardisByInterior(pos, !this.getWorld().isClient());
		if (found != null)
			this.setTardis(found);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void ask() {
		if (!getWorld().isClient()) return;
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(this.getPos());
		ClientPlayNetworking.send(ASK, buf);
	}

	public void sync() {
		if (isClient()) return;
		syncType();
		syncVariant();
		this.syncParent();
		needsSync = false;
	}

	private void syncType() {
		if (!hasWorld() || world.isClient()) return;

		PacketByteBuf buf = PacketByteBufs.create();

		buf.writeString(getConsoleSchema().id().toString());
		buf.writeBlockPos(getPos());

		for (PlayerEntity player : world.getPlayers()) {
			ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_TYPE, buf); // safe cast as we know its server
		}
	}

	private void syncVariant() {
		if (!hasWorld() || world.isClient()) return;

		PacketByteBuf buf = PacketByteBufs.create();

		buf.writeString(getVariant().id().toString());
		buf.writeBlockPos(getPos());

		for (PlayerEntity player : world.getPlayers()) {
			ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_VARIANT, buf); // safe cast as we know its server
		}
	}

	public void setTardis(Tardis tardis) {
		super.setTardis(tardis);
		this.markDirty();
		// force re-link a desktop if it's not null
		this.linkDesktop();
	}

	public void setTardis(UUID uuid) {
		this.tardisId = uuid;
		markDirty();

		this.linkDesktop();
	}

	public void linkDesktop() {
		if (this.findTardis().isEmpty())
			return;
		this.setDesktop(this.getDesktop());
	}

	public TardisDesktop getDesktop() {
		if (this.findTardis().isEmpty()) return null;
		return this.findTardis().get().getDesktop();
	}

	public ConsoleTypeSchema getConsoleSchema() {
		if (type == null) setType(ConsoleRegistry.HARTNELL);

		return ConsoleRegistry.REGISTRY.get(type);
	}

	public void setType(Identifier var) {
		type = var;

		syncType();
		markDirty();
	}

	public void setType(ConsoleTypeSchema schema) {
		setType(schema.id());
	}


	public ConsoleVariantSchema getVariant() {
		if (variant == null) {
			// oh no : (
			// lets just pick any
			setVariant(this.getConsoleSchema().getDefaultVariant());
		}

		return ConsoleVariantRegistry.getInstance().get(variant);
	}

	public void setVariant(Identifier var) {
		variant = var;

		if (!(getVariant().parent().id().equals(type))) {
			AITMod.LOGGER.warn("Variant was set and it doesnt match this consoles type!");
			AITMod.LOGGER.warn(variant + " | " + type);

			if (hasWorld() && getWorld().isClient()) ask();
		}

		syncVariant();
		markDirty();
	}

	public void setVariant(ConsoleVariantSchema schema) {
		setVariant(schema.id());
	}

	public void setParent(UUID uuid) {
		this.parent = uuid;

		this.syncParent();
	}

	private void syncParent() {
		if (!hasWorld() || world.isClient() || this.findParent().isEmpty()) return;

		PacketByteBuf buf = PacketByteBufs.create();

		buf.writeUuid(this.parent);
		buf.writeBlockPos(getPos());

		for (PlayerEntity player : world.getPlayers()) {
			ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_PARENT, buf); // safe cast as we know its server
		}
	}

	public Optional<TardisConsole> findParent() {
		if (this.findTardis().isEmpty()) return Optional.empty();

		TardisDesktop desktop = this.getDesktop();

		if (this.parent == null) {
			TardisConsole found = desktop.findConsole(new AbsoluteBlockPos(this.getPos(), this.getWorld()));

			if (found == null) {
				found = new TardisConsole(new AbsoluteBlockPos(this.getPos(), this.getWorld()));
				found.init(this.findTardis().get(), false);
				desktop.addConsole(found);
			}

			this.setParent(found.uuid());
		}

		return Optional.ofNullable(desktop.findConsole(this.parent));
	}

	/**
	 * Sets the new {@link ConsoleTypeSchema} and refreshes the console entities
	 */
	private void changeConsole(ConsoleTypeSchema var) {
		changeConsole(var, ConsoleVariantRegistry.withParent(var).stream().findAny().get());
	}

	private void changeConsole(ConsoleTypeSchema var, ConsoleVariantSchema variant) {
		setType(var);
		setVariant(variant);

		if (!world.isClient() && world == TardisUtil.getTardisDimension())
			redoControls();
	}

	private void redoControls() {
		killControls();
		markNeedsControl();
	}

	public static ConsoleTypeSchema nextConsole(ConsoleTypeSchema current) {
		List<ConsoleTypeSchema> list = ConsoleRegistry.REGISTRY.stream().toList();

		int idx = list.indexOf(current);
		if (idx < 0 || idx + 1 == list.size()) return list.get(0);
		return list.get(idx + 1);
	}

	public static ConsoleVariantSchema nextVariant(ConsoleVariantSchema current) {
		List<ConsoleVariantSchema> list = ConsoleVariantRegistry.withParent(current.parent());

		int idx = list.indexOf(current);
		if (idx < 0 || idx + 1 == list.size()) return list.get(0);
		return list.get(idx + 1);
	}

	public void useOn(World world, boolean sneaking, PlayerEntity player) {

		if (!world.isClient()) return;
		if (this.findTardis().isEmpty()) return;

		ItemStack itemStack = player.getMainHandStack();
		if (itemStack.getItem() == AITBlocks.ZEITON_CLUSTER.asItem()) {
			this.findTardis().get().addFuel(15);
			if (!player.isCreative()) itemStack.decrement(1);
		} else if (itemStack.getItem() instanceof ChargedZeitonCrystalItem) {
			NbtCompound nbt = itemStack.getOrCreateNbt();
			if(!nbt.contains(ChargedZeitonCrystalItem.FUEL_KEY)) return;
			this.findTardis().get().addFuel(nbt.getDouble(ChargedZeitonCrystalItem.FUEL_KEY));
			nbt.putDouble(ChargedZeitonCrystalItem.FUEL_KEY, 0);
		}

	}

	@Override
	public void markRemoved() {
		this.killControls();
		super.markRemoved();
	}

	public void setDesktop(TardisDesktop desktop) {
		if (this.getWorld() == null || this.getWorld().isClient()) return;

		AITMod.LOGGER.info("Linking desktop " + this.findTardis().get().getUuid());

		this.findParent();
	}

	public boolean wasPowered() {
		if (this.findTardis().isEmpty()) return false;
		return this.wasPowered ^ this.findTardis().get().hasPower();
	}


	public void onBroken() {
		this.killControls();
	}

	public void killControls() {
		controlEntities.forEach(Entity::discard);
		controlEntities.clear();
		sync();
	}

	public void spawnControls() {
		BlockPos current = getPos();

		if (!(getWorld() instanceof ServerWorld server))
			return;
		if (getWorld().getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
			return;

		killControls();
		ConsoleTypeSchema consoleType = getConsoleSchema();
		ControlTypes[] controls = consoleType.getControlTypes();
		Arrays.stream(controls).toList().forEach(control -> {

			ConsoleControlEntity controlEntity = new ConsoleControlEntity(AITEntityTypes.CONTROL_ENTITY_TYPE, getWorld());

			Vector3f position = current.toCenterPos().toVector3f().add(control.getOffset().x(), control.getOffset().y(), control.getOffset().z());
			controlEntity.setPosition(position.x(), position.y(), position.z());
			controlEntity.setYaw(0.0f);
			controlEntity.setPitch(0.0f);

			controlEntity.setControlData(consoleType, control, this.getPos());

			server.spawnEntity(controlEntity);
			this.controlEntities.add(controlEntity);
		});

		this.needsControls = false;
	}

	public void tickAge() {
		age++;
		ANIM_STATE.startIfNotRunning(age);
	}

	public void markNeedsControl() {
		this.needsControls = true;
	}

	public void markNeedsSyncing() {
		this.needsSync = true;
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, ConsoleBlockEntity blockEntity) {
		Random random = new Random();
		if (this.needsControls) {
			spawnControls();
		}
		if (needsSync)
			sync();
		if (needsReloading) {
			markNeedsSyncing();
			needsReloading = false;
		}

		if (world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) {
			this.markRemoved();
		}

		// idk
		if (world.isClient()) {
			this.tickAge();
		}

		if (world.isClient()) return;

		if (this.findTardis().isEmpty()) return;

		this.findParent().ifPresent(parent -> parent.tickConsole(this));

		SequenceHandler handler = this.findTardis().get().sequence();

		if(this.findTardis().get().getTravel().inFlight() || this.findTardis().get().getTravel().isMaterialising()) {
			if (handler.hasActiveSequence() && handler.getActiveSequence() != null) {
				//System.out.println("yes active sequence yum" + handler.getActiveSequence());
				List<Control> sequence = handler.getActiveSequence().getControls();

				// Convert the sequence to a Set for efficient lookups
				Set<Control> sequenceSet = new HashSet<>(sequence);

				// Iterate only through entities whose controls are in the sequenceSet
				this.controlEntities/*.stream() // Convert to a stream for easy filtering
						.filter(entity -> sequenceSet.contains(entity.getControl())) // Filter entities with controls in the sequence*/
						.forEach(entity -> {
							// Since we're here, the entity's control is part of the sequence
							Control control = entity.getControl();
							entity.setPartOfSequence(sequenceSet.contains(control));
							entity.setWasSequenced(handler.doesControlIndexMatch(control));
							entity.setSequenceColor(sequence.indexOf(control)); // Note: This still incurs O(n), consider optimization if needed
						});
			} else {
				this.controlEntities.forEach(entity -> entity.setPartOfSequence(false));
			}
		}

		ServerTardis tardis = (ServerTardis) this.findTardis().get();

		boolean isRiftChunk = RiftChunkManager.isRiftChunk(tardis.getExterior().getExteriorPos());

		if (tardis.getTravel().isCrashing()) {
			((ServerWorld) world).spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 5, 0, 0, 0, 0.025f);
			((ServerWorld) world).spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 5, 0, 0, 0, 0.01f);
			((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(
							new Vector3f(0.75f, 0.75f, 0.75f), new Vector3f(0.1f, 0.1f, 0.1f), 1), pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 1, 0, 0, 0, 0.01f);
		}

		if (tardis.crash().isToxic() || tardis.crash().isUnstable()) {
			((ServerWorld) world).spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 5, 0, 0, 0, 0.025f);
			((ServerWorld) world).spawnParticles(ParticleTypes.CLOUD, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 1, 0, 0.05f, 0, 0.025f);
		}

		if (tardis.crash().isToxic()) {
			((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(
							new Vector3f(0.75f, 0.85f, 0.75f), new Vector3f(0.15f, 0.25f, 0.15f), 1),
					pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 1, random.nextBoolean() ? 0.5f : -0.5f, 3f, random.nextBoolean() ? 0.5f : -0.5f, 0.025f);
		}
		if (tardis.isRefueling()) {
			((ServerWorld) world).spawnParticles((isRiftChunk) ? ParticleTypes.FIREWORK : ParticleTypes.END_ROD, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 1, 0, 0, 0, (isRiftChunk) ? 0.05f : 0.025f);
		}
	}
}
