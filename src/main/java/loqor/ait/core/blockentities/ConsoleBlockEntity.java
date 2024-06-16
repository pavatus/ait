package loqor.ait.core.blockentities;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.core.entities.ConsoleControlEntity;
import loqor.ait.core.item.ChargedZeitonCrystalItem;
import loqor.ait.core.managers.RiftChunkManager;
import loqor.ait.registry.impl.console.ConsoleRegistry;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.control.ControlTypes;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.link.v2.interior.InteriorLinkableBlockEntity;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.*;

public class ConsoleBlockEntity extends InteriorLinkableBlockEntity implements BlockEntityTicker<ConsoleBlockEntity> {
	private static final Random RANDOM = new Random();

	public final List<ConsoleControlEntity> controlEntities = new ArrayList<>();
	public final AnimationState ANIM_STATE = new AnimationState();

	private boolean needsControls = true;
	private Identifier type;
	private Identifier variant;

	public int age;

	public ConsoleBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.CONSOLE_BLOCK_ENTITY_TYPE, pos, state);
	}

	@Override
	public void onLinked() {
		this.tardis().ifPresent(tardis -> tardis.getDesktop().getConsolePos().add(this.pos));
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		nbt.putString("type", type.toString());
		nbt.putString("variant", variant.toString());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		this.setType(Identifier.tryParse(nbt.getString("type")));
		this.setVariant(Identifier.tryParse(nbt.getString("variant")));

		this.spawnControls();
	}

	public ConsoleTypeSchema getConsoleSchema() {
		if (type == null)
			this.setType(ConsoleRegistry.HARTNELL);

		return ConsoleRegistry.REGISTRY.get(type);
	}

	public int getAge() {
		return age;
	}

	public void setType(Identifier var) {
		this.type = var;
		this.markDirty();
	}

	public void setType(ConsoleTypeSchema schema) {
		this.setType(schema.id());
	}

	public void setVariant(Identifier var) {
		this.variant = var;
		this.markDirty();
	}

	public void setVariant(ConsoleVariantSchema schema) {
		this.setVariant(schema.id());
	}

	public ConsoleVariantSchema getVariant() {
		if (variant == null)
			this.setVariant(this.getConsoleSchema().getDefaultVariant());

		return ConsoleVariantRegistry.getInstance().get(variant);
	}

	public void useOn(World world, boolean sneaking, PlayerEntity player) {
		if (!world.isClient())
			return;

		if (this.tardis().isEmpty())
			return;

		ItemStack itemStack = player.getMainHandStack();
		if (itemStack.getItem() == AITBlocks.ZEITON_CLUSTER.asItem()) {
			this.tardis().get().addFuel(15);

			if (!player.isCreative())
				itemStack.decrement(1);

			return;
		}

		if (itemStack.getItem() instanceof ChargedZeitonCrystalItem) {
			NbtCompound nbt = itemStack.getOrCreateNbt();

			if(!nbt.contains(ChargedZeitonCrystalItem.FUEL_KEY))
				return;

			this.tardis().get().addFuel(nbt.getDouble(ChargedZeitonCrystalItem.FUEL_KEY));
			nbt.putDouble(ChargedZeitonCrystalItem.FUEL_KEY, 0);
		}
	}

	@Override
	public void markRemoved() {
		this.killControls();

		this.tardis().get().getDesktop()
				.getConsolePos().remove(this.pos);

		super.markRemoved();
	}

	public void onBroken() {
		this.killControls();
	}

	public void killControls() {
		controlEntities.forEach(Entity::discard);
		controlEntities.clear();
		this.sync();
	}

	public void spawnControls() {
		BlockPos current = this.getPos();

		if (!(this.world instanceof ServerWorld serverWorld))
			return;

		if (this.world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
			return;

		this.killControls();
		ConsoleTypeSchema consoleType = getConsoleSchema();
		ControlTypes[] controls = consoleType.getControlTypes();

		for (ControlTypes control : controls) {
			ConsoleControlEntity controlEntity = new ConsoleControlEntity(AITEntityTypes.CONTROL_ENTITY_TYPE, getWorld());

			Vector3f position = current.toCenterPos().toVector3f().add(control.getOffset().x(), control.getOffset().y(), control.getOffset().z());
			controlEntity.setPosition(position.x(), position.y(), position.z());
			controlEntity.setYaw(0.0f);
			controlEntity.setPitch(0.0f);

			controlEntity.setControlData(consoleType, control, this.getPos());

			serverWorld.spawnEntity(controlEntity);
			this.controlEntities.add(controlEntity);
		}

		this.needsControls = false;
	}

	public void markNeedsControl() {
		this.needsControls = true;
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, ConsoleBlockEntity blockEntity) {
		if (this.needsControls)
			this.spawnControls();

		if (world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
			this.markRemoved();


		if (!(world instanceof ServerWorld serverWorld)) {
			this.age++;

			ANIM_STATE.startIfNotRunning(this.age);
			return;
		}

		if (this.tardis().isEmpty())
			return;

		SequenceHandler handler = this.tardis().get().sequence();

		if(this.tardis().get().travel().inFlight() || this.tardis().get().travel().getState() == TardisTravel.State.MAT) {
			if (handler.hasActiveSequence() && handler.getActiveSequence() != null) {
				List<Control> sequence = handler.getActiveSequence().getControls();

				// Convert the sequence to a Set for efficient lookups
				Set<Control> sequenceSet = new HashSet<>(sequence);

				// Iterate only through entities whose controls are in the sequenceSet
				this.controlEntities
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

		ServerTardis tardis = (ServerTardis) this.tardis().get();
		boolean isRiftChunk = RiftChunkManager.isRiftChunk(tardis.getExteriorPos());

		if (tardis.travel().isCrashing()) {
			serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 5, 0, 0, 0, 0.025f);

			serverWorld.spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 5, 0, 0, 0, 0.01f);

			serverWorld.spawnParticles(new DustColorTransitionParticleEffect(
							new Vector3f(0.75f, 0.75f, 0.75f), new Vector3f(0.1f, 0.1f, 0.1f), 1
					), pos.getX() + 0.5f, pos.getY() + 1.25, pos.getZ() + 0.5f, 1, 0, 0, 0, 0.01f
			);
		}

		if (tardis.crash().isToxic() || tardis.crash().isUnstable()) {
			serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 5, 0, 0, 0, 0.025f);

			serverWorld.spawnParticles(ParticleTypes.CLOUD, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 1, 0, 0.05f, 0, 0.025f);
		}

		if (tardis.crash().isToxic()) {
			serverWorld.spawnParticles(new DustColorTransitionParticleEffect(
					new Vector3f(0.75f, 0.85f, 0.75f), new Vector3f(0.15f, 0.25f, 0.15f), 1
			), pos.getX() + 0.5f, pos.getY() + 1.25, pos.getZ() + 0.5f, 1, RANDOM.nextBoolean()
					? 0.5f : -0.5f, 3f, RANDOM.nextBoolean() ? 0.5f : -0.5f, 0.025f
			);
		}

		if (tardis.isRefueling()) {
			serverWorld.spawnParticles((isRiftChunk) ? ParticleTypes.FIREWORK : ParticleTypes.END_ROD, pos.getX() + 0.5f, pos.getY() + 1.25,
					pos.getZ() + 0.5f, 1, 0, 0, 0, (isRiftChunk) ? 0.05f : 0.025f);
		}
	}

	public static ConsoleTypeSchema nextConsole(ConsoleTypeSchema current) {
		List<ConsoleTypeSchema> list = ConsoleRegistry.REGISTRY.stream().toList();

		int idx = list.indexOf(current);

		if (idx < 0 || idx + 1 == list.size())
			return list.get(0);

		return list.get(idx + 1);
	}

	public static ConsoleVariantSchema nextVariant(ConsoleVariantSchema current) {
		List<ConsoleVariantSchema> list = ConsoleVariantRegistry.withParent(current.parent());

		int idx = list.indexOf(current);

		if (idx < 0 || idx + 1 == list.size())
			return list.get(0);

		return list.get(idx + 1);
	}
}
