package loqor.ait.tardis.data;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.entities.BaseControlEntity;
import loqor.ait.core.entities.FallingTardisEntity;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ShieldData extends TardisComponent implements TardisTickable {
	public static String IS_SHIELDED = "is_shielded";
	public static String IS_VISUALLY_SHIELDED = "is_visually_shielded";

	public ShieldData() {
		super(Id.SHIELDS);
	}

	public void enable() {
		PropertiesHandler.set(this.tardis(), IS_SHIELDED, true);
		TardisEvents.TOGGLE_SHIELDS.invoker().onShields(this.tardis, true, this.areVisualShieldsActive());
	}

	public void disable() {
		PropertiesHandler.set(this.tardis(), IS_SHIELDED, false);
		TardisEvents.TOGGLE_SHIELDS.invoker().onShields(this.tardis, false, this.areVisualShieldsActive());
	}

	public void toggle() {
		if (this.areShieldsActive())
			this.disable();
		else this.enable();
	}

	public void enableVisuals() {
		PropertiesHandler.set(this.tardis(), IS_VISUALLY_SHIELDED, true);
		TardisEvents.TOGGLE_SHIELDS.invoker().onShields(this.tardis, this.areShieldsActive(), true);
	}

	public void disableVisuals() {
		PropertiesHandler.set(this.tardis(), IS_VISUALLY_SHIELDED, false);
		TardisEvents.TOGGLE_SHIELDS.invoker().onShields(this.tardis, this.areShieldsActive(), false);
	}

	public void toggleVisuals() {
		if (this.areVisualShieldsActive())
			this.disableVisuals();
		else this.enableVisuals();
	}

	public void disableAll() {
		this.disableVisuals();
		this.disable();
	}

	public boolean areShieldsActive() {
		return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), IS_SHIELDED);
	}

	public boolean areVisualShieldsActive() {
		return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), IS_VISUALLY_SHIELDED);
	}

	@Override
	public void tick(MinecraftServer server) {
		if (this.areShieldsActive() && !this.tardis().engine().hasPower())
			this.disableAll();

		if (!this.areShieldsActive())
			return;

		if (this.tardis().getExteriorPos() == null)
			return;

		Tardis tardis = this.tardis();

		tardis.removeFuel(2 * (tardis.tardisHammerAnnoyance + 1)); // idle drain of 2 fuel per tick

		World world = tardis.getExteriorPos().getWorld();

		world.getOtherEntities(null,
						new Box(tardis.getExteriorPos()).expand(8f),
						EntityPredicates.EXCEPT_SPECTATOR)
				.stream()
				.filter(entity -> !(entity instanceof BaseControlEntity)) // Exclude control entities
				.filter(entity -> !(entity instanceof TardisRealEntity)) // Exclude real world flight TARDIS (I'm too lazy to fix this for now bcs RWF isn't releasable)
				.filter(entity -> !(entity instanceof ServerPlayerEntity player && player.isSpectator())) // Exclude players in spectator
				.filter(entity -> !(entity instanceof FallingTardisEntity falling && falling.getTardis() == tardis))
				.filter(entity -> !(entity instanceof PlayerEntity player && ((LoyaltyHandler) tardis.getHandlers().get(Id.LOYALTY)).get(player).level() >= Loyalty.Type.PILOT.level))
				.forEach(entity -> {
					if(entity instanceof ServerPlayerEntity && entity.isSubmergedInWater()) {
						((ServerPlayerEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 15, 3, true, false, false));
					}
					if(this.areVisualShieldsActive()) {
						if (entity.squaredDistanceTo(tardis.getExteriorPos().toCenterPos()) <= 8f) {
							Vec3d motion = entity.getBlockPos().toCenterPos().subtract(tardis.getExteriorPos().toCenterPos()).normalize().multiply(0.1f);
							if (entity instanceof ProjectileEntity projectile) {
								if (projectile instanceof TridentEntity) {
									projectile.getVelocity().add(motion.multiply(2f));

									world.playSoundFromEntity(null, projectile, SoundEvents.ITEM_TRIDENT_HIT, SoundCategory.BLOCKS, 1f, 1f);
									return;
								}

								world.playSoundFromEntity(null, projectile, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 1f);
								projectile.discard();
								return;
							}

							entity.setVelocity(entity.getVelocity().add(motion.multiply(2f)));
							entity.velocityDirty = true;
							entity.velocityModified = true;
						}
					}
				});
	}
}
