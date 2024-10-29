package loqor.ait.core.blockentities;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.item.link.AbstractLinkItem;
import loqor.ait.core.util.StackUtil;

public class PlugBoardBlockEntity extends BlockEntity {

    private final List<ItemStack> links = new ArrayList<>(12);

    public PlugBoardBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.PLUGBOARD_ENTITY_TYPE, pos, state);
        this.clear();
    }

    // TODO use the tag instead of the item instanceof
    public void onClick(PlayerEntity player, Hand hand, int i) {
        if (this.world.isClient())
            return;

        System.out.println("SERVER: " + this.links);

        if (this.getLink(i) == null) {
            ItemStack stack = player.getStackInHand(hand);

            if (stack.isEmpty())
                return;

            if (!(stack.getItem() instanceof AbstractLinkItem))
                return;

            links.set(i, stack.copyWithCount(1));
            stack.decrement(1);

            this.sync();
            return;
        }

        StackUtil.spawn(this.world, this.getPos(), links.remove(i));
        this.sync();
    }

    private void sync() {
        world.updateListeners(pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    public void onUse(World world, ItemStack stack, PlayerEntity player) {
    }

    public List<ItemStack> getLinks() {
        return links;
    }

    public ItemStack getLink(int index) {
        return index < links.size() ? this.links.get(index) : null;
    }

    public int links() {
        return links.size();
    }

    private void clear() {
        this.links.clear();

        for (int i = 0; i < 12; i++) {
            this.links.add(new ItemStack(Items.AIR));
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        StackUtil.read(nbt, this.links);

        if (this.world.isClient())
            System.out.println("CLIENT: " + this.links);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        StackUtil.write(nbt, this.links);
    }

    @Nullable @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
