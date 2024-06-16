package loqor.ait.tardis.control;

import loqor.ait.AITMod;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.impl.SecurityControl;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class Control {

	public String id; // a name to represent the control

	public Control(String id) {
		this.setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		return false;
	}

	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console, boolean leftClick) {
		return runServer(tardis, player, world, console);
	}

	public void addToControlSequence(Tardis tardis, ServerPlayerEntity player, BlockPos pos) {
		tardis.sequence().add(this, player, pos);

		if(AITMod.RANDOM.nextInt(0, 20) == 4) {
			tardis.loyalty().addLevel(player, 1);

			player.getServerWorld().spawnParticles(ParticleTypes.HEART, pos.toCenterPos().getX(), pos.toCenterPos().getY() + 1, pos.toCenterPos().getZ(), 1, 0f, 1F, 0f, 5.0F);
		}
	}

	public SoundEvent getSound() {
		return SoundEvents.BLOCK_NOTE_BLOCK_BIT.value();
	}

	public boolean shouldFailOnNoPower() {
		return true;
	}

	public void runAnimation(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
		// no animation
	}

	@Override
	public String toString() {
		return "Control{" +
				"id='" + id + '\'' +
				'}';
	}

	public long getDelayLength() {
		return 250L;
	}

	public boolean shouldHaveDelay() {
		return true;
	}

	public boolean shouldHaveDelay(Tardis tardis) {
		return this.shouldHaveDelay();
	}

	public boolean ignoresSecurity() {
		return false;
	}

	public static String createDelayId(Control control, Tardis tardis) {
		return "delay-" + control.id + "-" + tardis.getUuid();
	}

	public static void createDelay(Control control, Tardis tardis, long millis) {
		DeltaTimeManager.createDelay(createDelayId(control, tardis), millis);
	}

	public static void createDelay(Control control, Tardis tardis) {
		createDelay(control, tardis, control.getDelayLength());
	}

	public static boolean isOnDelay(Control control, Tardis tardis) {
		return DeltaTimeManager.isStillWaitingOnDelay(createDelayId(control, tardis));
	}

	public boolean canRun(Tardis tardis, ServerPlayerEntity user) {
		if ((this.shouldFailOnNoPower() && !tardis.engine().hasPower()) || tardis.sequence().isConsoleDisabled())
			return false;

		if (isOnDelay(this, tardis))
			return false;

		boolean security = PropertiesHandler.getBool(tardis.properties(), SecurityControl.SECURITY_KEY);

		if (!this.ignoresSecurity() && security)
			return SecurityControl.hasMatchingKey(user, tardis);

		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || this.getClass() != o.getClass())
			return false;

		Control control = (Control) o;
		return this.id.equals(control.getId());
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
