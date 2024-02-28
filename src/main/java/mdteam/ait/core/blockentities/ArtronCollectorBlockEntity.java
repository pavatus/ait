package mdteam.ait.core.blockentities;

import mdteam.ait.api.tardis.ArtronHolder;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.item.ArtronCollectorItem;
import mdteam.ait.core.item.ChargedZeitonCrystalItem;
import mdteam.ait.core.managers.RiftChunkManager;
import mdteam.ait.core.util.DeltaTimeManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ArtronCollectorBlockEntity extends BlockEntity implements BlockEntityTicker<ArtronCollectorBlockEntity>, ArtronHolder {
    public double artronAmount = 0;
    public ArtronCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.ARTRON_COLLECTOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putDouble("artronAmount", this.artronAmount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if(nbt.contains("artronAmount"))
            this.setCurrentFuel(nbt.getDouble("artronAmount"));
        super.readNbt(nbt);
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if(!world.isClient()) {
            player.sendMessage(Text.literal(this.getCurrentFuel() + "/" + ArtronCollectorItem.COLLECTOR_MAX_FUEL).formatted(Formatting.GOLD));
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof ArtronCollectorItem) {
                double residual = ArtronCollectorItem.addFuel(stack, this.getCurrentFuel());
                this.setCurrentFuel(residual);
            } else if (stack.getItem() instanceof ChargedZeitonCrystalItem crystal) {
                double residual = crystal.addFuel(this.getCurrentFuel(), stack);
                this.setCurrentFuel(residual);
            }
            if (stack.getItem() == AITBlocks.ZEITON_CLUSTER.asItem()) {
                if(sneaking) {
                    this.setCurrentFuel(this.addFuel(15));
                    player.getActiveItem().decrement(1);
                    return;
                }
                player.getInventory().setStack(player.getInventory().selectedSlot, new ItemStack(AITItems.CHARGED_ZEITON_CRYSTAL));
            }
        }
    }

    @Override
    public void setCurrentFuel(double artronAmount) {
        this.artronAmount = artronAmount;
        markDirty();
        if(this.hasWorld())
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    @Override
    public double getMaxFuel() {
        return ArtronCollectorItem.COLLECTOR_MAX_FUEL;
    }

    @Override
    public double getCurrentFuel() {
        return this.artronAmount;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = super.toInitialChunkDataNbt();
        nbtCompound.putDouble("artronAmount", this.artronAmount);
        return nbtCompound;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, ArtronCollectorBlockEntity blockEntity) {
        if(world.isClient()) return;

        if (RiftChunkManager.isRiftChunk(pos) && RiftChunkManager.getArtronLevels(world, pos) >= 3  && this.getCurrentFuel() < ArtronCollectorItem.COLLECTOR_MAX_FUEL && (!DeltaTimeManager.isStillWaitingOnDelay(getDelay()))) {
            RiftChunkManager.setArtronLevels(world, pos,RiftChunkManager.getArtronLevels(world, pos) - 3);
            this.addFuel( 3);
            this.updateListeners();
            DeltaTimeManager.createDelay(getDelay(), 500L);
        }
    }

    private void updateListeners() {
        this.markDirty();
        if(this.getWorld() != null)
            this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public String getDelay() {
        return "collector-" + this.getPos() + "-collectdelay";
    }
}
