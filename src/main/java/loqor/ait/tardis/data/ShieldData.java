package loqor.ait.tardis.data;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
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

		if (this.tardis().travel().getState() == TravelHandlerBase.State.FLIGHT)
			return;

		Tardis tardis = this.tardis();
		tardis.removeFuel(2 * (tardis.tardisHammerAnnoyance + 1)); // idle drain of 2 fuel per tick

		DirectedGlobalPos.Cached globalExteriorPos = tardis.travel().position();

		World world = globalExteriorPos.getWorld();
		BlockPos exteriorPos = globalExteriorPos.getPos();

		world.getOtherEntities(null, new Box(exteriorPos).expand(8f))
				.stream().filter(entity -> entity.isPushable() || entity instanceof ProjectileEntity)
				.filter(entity -> !(entity instanceof ServerPlayerEntity player
						&& tardis.loyalty().get(player).isOf(Loyalty.Type.PILOT))) // Exclude players with loyalty
				.forEach(entity -> {
					if (entity instanceof ServerPlayerEntity player && entity.isSubmergedInWater())
						player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 15, 3, true, false, false));

					if (this.areVisualShieldsActive()) {
						Vec3d centerExteriorPos = exteriorPos.toCenterPos();

						if (entity.squaredDistanceTo(centerExteriorPos) <= 8f) {
							Vec3d motion = entity.getBlockPos().toCenterPos().subtract(centerExteriorPos).normalize().multiply(0.1f);

							if (entity instanceof ProjectileEntity projectile) {
								BlockPos pos = projectile.getBlockPos();

								if (projectile instanceof TridentEntity) {
									projectile.getVelocity().add(motion.multiply(2f));

									world.playSound(null, pos, SoundEvents.ITEM_TRIDENT_HIT, SoundCategory.BLOCKS, 1f, 1f);
									return;
								}

								world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.BLOCKS, 1f, 1f);

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
