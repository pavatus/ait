package mdteam;

import mdteam.ait.client.RiftClampBullshit;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.Nullable;

public class RiftCompassTarget implements RiftClampBullshit.CompassTarget {
    private ClientWorld world;
    private ItemStack stack;
    private Entity entity;
    @Override
    public @Nullable GlobalPos getPos(ClientWorld world, ItemStack stack, Entity entity) {
        return GlobalPos.create(world.getRegistryKey(), entity.getBlockPos());
    }
    public RiftCompassTarget(ClientWorld world, ItemStack stack, Entity entity) {
        this.world = world;
        this.stack = stack;
        this.entity = entity;
    }
}
