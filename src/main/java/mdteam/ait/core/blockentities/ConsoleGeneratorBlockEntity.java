package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.blocks.ConsoleGeneratorBlock;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.item.SonicItem;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.registry.ConsoleVariantRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.console.ConsoleSchema;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.variant.console.ConsoleVariantSchema;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
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

import java.util.List;
import java.util.UUID;

import static mdteam.ait.core.blockentities.ConsoleBlockEntity.nextConsole;
import static mdteam.ait.core.blockentities.ConsoleBlockEntity.nextVariant;
import static mdteam.ait.tardis.util.TardisUtil.isClient;

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
        //if(world != TardisUtil.getTardisDimension()) return;
        if(player.getMainHandStack().getItem() instanceof SonicItem) {

            NbtCompound nbt = player.getMainHandStack().getOrCreateNbt();

            if(!nbt.contains("tardis")) return;

            ConsoleBlockEntity consoleBlockEntity = new ConsoleBlockEntity(pos,AITBlocks.CONSOLE.getDefaultState());

            consoleBlockEntity.setType(this.getConsoleSchema());
            consoleBlockEntity.setVariant(this.getConsoleVariant());

            this.getWorld().setBlockState(this.pos, AITBlocks.CONSOLE.getDefaultState());
            this.getWorld().addBlockEntity(consoleBlockEntity);

            world.playSound(null, this.pos, SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 0.5f, 1.0f);

            /*if(!player.isCreative()) {
                player.getMainHandStack().decrement(1);
                // ItemEntity item = new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), new ItemStack(AITBlocks.CONSOLE_GENERATOR));
                // this.getWorld().spawnEntity(item);
            }*/
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
        if(this.type != null)
            nbt.putString("console", this.type.toString());
        if (this.variant != null)
            nbt.putString("variant", this.variant.toString());
    }

    public ConsoleSchema getConsoleSchema() {
        if (type == null) {
            this.setConsoleSchema(ConsoleRegistry.HARTNELL.id());
        }

        return ConsoleRegistry.REGISTRY.get(type);
    }

    public void setConsoleSchema(Identifier type) {
        this.type = type;
        markDirty();
        this.syncType();
        if(this.getWorld() == null) return;
        this.getWorld().updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    public ConsoleVariantSchema getConsoleVariant() {
        if (variant == null) {
            this.variant = ConsoleVariantRegistry.withParent(this.getConsoleSchema()).get(0).id();
        }

        return ConsoleVariantRegistry.REGISTRY.get(this.variant);
    }
    public void setVariant(Identifier variant) {
        this.variant = variant;
        markDirty();
        this.syncVariant();
        if(this.getWorld() == null) return;
        this.getWorld().updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    public void changeConsole(ConsoleSchema schema) {
        this.setConsoleSchema(schema.id());
        this.setVariant(ConsoleVariantRegistry.withParent(schema).get(0).id());
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
