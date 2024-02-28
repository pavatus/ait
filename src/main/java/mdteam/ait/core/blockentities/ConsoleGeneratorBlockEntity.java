package mdteam.ait.core.blockentities;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.item.SonicItem;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.registry.ConsoleVariantRegistry;
import mdteam.ait.tardis.console.type.ConsoleTypeSchema;
import mdteam.ait.tardis.console.variant.ConsoleVariantSchema;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static mdteam.ait.core.blockentities.ConsoleBlockEntity.nextConsole;
import static mdteam.ait.core.blockentities.ConsoleBlockEntity.nextVariant;

public class ConsoleGeneratorBlockEntity extends BlockEntity {
	public static final Identifier SYNC_TYPE = new Identifier(AITMod.MOD_ID, "sync_gen_type");
	public static final Identifier SYNC_VARIANT = new Identifier(AITMod.MOD_ID, "sync_gen_variant");
	private Identifier type;
	private Identifier variant;

	public ConsoleGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.CONSOLE_GENERATOR_ENTITY_TYPE, pos, state);
		this.type = ConsoleRegistry.HARTNELL.id();
	}

	public void useOn(World world, boolean sneaking, PlayerEntity player) {
		if (world != TardisUtil.getTardisDimension()) return;

		ItemStack stack = player.getMainHandStack();

		if (stack.getItem() instanceof SonicItem) {

			this.createConsole();

			return;
		} else if (stack.isOf(Items.BLAZE_POWDER)) {

			this.createConsole();

			stack.decrement(1);

			return;
		}

		world.playSound(null, this.pos, SoundEvents.BLOCK_SCULK_CHARGE, SoundCategory.BLOCKS, 0.5f, 1.0f);

		if (sneaking) {
			this.changeConsole(nextVariant(this.getConsoleVariant()));
		} else
			this.changeConsole(nextConsole(this.getConsoleSchema()));
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (this.type != null)
			nbt.putString("console", this.type.toString());
		if (this.variant != null)
			nbt.putString("variant", this.variant.toString());
	}

	private void createConsole() {
		ConsoleBlockEntity consoleBlockEntity = new ConsoleBlockEntity(pos, AITBlocks.CONSOLE.getDefaultState());

		consoleBlockEntity.setType(this.getConsoleSchema());
		consoleBlockEntity.setVariant(this.getConsoleVariant());

		this.getWorld().setBlockState(this.pos, AITBlocks.CONSOLE.getDefaultState());
		this.getWorld().addBlockEntity(consoleBlockEntity);

		world.playSound(null, this.pos, SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 0.5f, 1.0f);
	}

	public ConsoleTypeSchema getConsoleSchema() {
		if (type == null) {
			this.setConsoleSchema(ConsoleRegistry.HARTNELL.id());
		}

		return ConsoleRegistry.REGISTRY.get(type);
	}

	public void setConsoleSchema(Identifier type) {
		this.type = type;
		markDirty();
		this.syncType();
		if (this.getWorld() == null) return;
		WorldOps.updateIfOnServer(this.getWorld(), this.pos);
	}

	public ConsoleVariantSchema getConsoleVariant() {
		if (variant == null) {
			this.variant = this.getConsoleSchema().getDefaultVariant().id();
		}

		return ConsoleVariantRegistry.getInstance().get(this.variant);
	}

	public void setVariant(Identifier variant) {
		this.variant = variant;
		markDirty();
		this.syncVariant();
		if (this.getWorld() == null) return;
		WorldOps.updateIfOnServer(this.getWorld(), this.pos);
	}

	public void changeConsole(ConsoleTypeSchema schema) {
		this.setConsoleSchema(schema.id());
		this.setVariant(schema.getDefaultVariant().id());
	}

	public void changeConsole(ConsoleVariantSchema schema) {
		this.setConsoleSchema(schema.parent().id());
		this.setVariant(schema.id());
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

		buf.writeString(getConsoleVariant().id().toString());
		buf.writeBlockPos(getPos());

		for (PlayerEntity player : world.getPlayers()) {
			ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_VARIANT, buf); // safe cast as we know its server
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		if (nbt.contains("console")) {
			Identifier console = new Identifier(nbt.getString("console"));
			this.setConsoleSchema(console);
		}

		if (nbt.contains("variant")) {
			Identifier variant = new Identifier(nbt.getString("variant"));
			this.setVariant(variant);
		}

		super.readNbt(nbt);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
}
