package mdteam.ait.core.entities;

import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisDesktop;
import the.mdteam.ait.TardisTravel;

import java.util.Collections;

public abstract class BaseControlEntity extends LivingEntity implements ILinkable {

    private Tardis tardis;

    public BaseControlEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    protected BaseControlEntity(World world, Tardis tardis) {
        super(AITEntityTypes.CONTROL_ENTITY_TYPE, world);
        this.setTardis(tardis);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return Arm.LEFT;
    }

    @Override
    public Tardis getTardis() {
        return tardis;
    }

    @Override
    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return super.damage(source, amount);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        return super.interact(player, hand);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }
}
