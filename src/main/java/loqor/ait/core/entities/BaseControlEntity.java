package loqor.ait.core.entities;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.UUID;

// TODO: move stuff to LinkableMobEntity
public abstract class BaseControlEntity extends MobEntity {

	private UUID tardisId;

	public BaseControlEntity(EntityType<? extends MobEntity> type, World world) {
		super(type, world);
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
	public void equipStack(EquipmentSlot slot, ItemStack stack) { }

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		return true;
	}

	@Override
	public Arm getMainArm() {
		return Arm.LEFT;
	}

	public Tardis getTardis() {
		if (this.tardisId == null)
			this.findTardis();

		return TardisManager.with(this, (o, manager) -> manager.demandTardis(o, this.tardisId));
	}

	private void findTardis() {
		this.setTardis(TardisUtil.findTardisByInterior(this.getBlockPos(), !this.getWorld().isClient()));
	}

	public void setTardis(Tardis tardis) {
		if (tardis == null) {
			AITMod.LOGGER.error("Tardis was null in "+ this + " at " + this.getPos());
			return;
		}

		this.tardisId = tardis.getUuid();
	}

	public void setTardis(UUID uuid) {
		this.tardisId = uuid;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return super.damage(source, 0);
	}

	@Override
	public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
		return super.interactAt(player, hitPos, hand);
	}

	@Override
	public void setAiDisabled(boolean aiDisabled) {
		super.setAiDisabled(true);
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	public static DefaultAttributeContainer.Builder createControlAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
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
