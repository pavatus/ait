package dev.amble.ait.core.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import dev.amble.ait.core.AITEntityTypes;
import dev.amble.ait.core.AITItems;

public class RiftEntity extends Entity {
    private int interactAmount = 0;
    public RiftEntity(EntityType<?> type, World world) {
        super(AITEntityTypes.RIFT_ENTITY, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient()) return ActionResult.SUCCESS;
        if (interactAmount >= 3) {
            this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.BLOCK_SCULK_SHRIEKER_BREAK,
                    SoundCategory.MASTER, 1f, 1f);
            this.discard();
            return ActionResult.FAIL;
        }
        interactAmount += 1;
        if (player.isSneaking()) {
            if (this.getWorld().getRandom().nextBoolean() && this.getWorld().getRandom().nextBetween(0, 5) == 2) {
                player.damage(this.getWorld().getDamageSources().hotFloor(), 4);
                spawnItem(this.getWorld(), this.getBlockPos(), new ItemStack(AITItems.CORAL_FRAGMENT));
                this.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                        SoundCategory.MASTER, 1f, 1f);
                this.discard();
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }
        return super.interact(player, hand);
    }

    public static void spawnItem(World world, BlockPos pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        world.spawnEntity(entity);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        interactAmount = nbt.getInt("interactAmount");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("interactAmount", interactAmount);
    }
}