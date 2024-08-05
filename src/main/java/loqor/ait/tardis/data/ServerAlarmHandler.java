package loqor.ait.tardis.data;

import loqor.ait.core.AITSounds;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

// use this as reference for starting other looping sounds on the exterior
public class ServerAlarmHandler extends KeyedTardisComponent implements TardisTickable {
	@Exclude
	public static final int CLOISTER_LENGTH_TICKS = 3 * 20;
	@Exclude
	private int soundCounter = 0;
	private static final BoolProperty ALARMS_ENABLED = new BoolProperty("alarms_enabled", false);
	private final BoolValue alarmsEnabled = ALARMS_ENABLED.create(this);
	private static final BoolProperty HOSTILE_PRESENCE = new BoolProperty("hostile_presence", true);
	private final BoolValue hostilePresence = HOSTILE_PRESENCE.create(this);

	public ServerAlarmHandler() {
		super(Id.ALARMS);
	}

	@Override
	public void onLoaded() {
		alarmsEnabled.of(this, ALARMS_ENABLED);
		hostilePresence.of(this, HOSTILE_PRESENCE);
	}

	public void enable() {
		this.alarmsEnabled.set(true);
	}

	public void disable() {
		this.alarmsEnabled.set(false);
	}

	public BoolValue getAlarms() {
		return alarmsEnabled;
	}
	public BoolValue hostilePresence() {
		return hostilePresence;
	}

	public void toggle() {
		if (getAlarms().get()) disable();
		else enable();
	}

	@Override
	public void tick(MinecraftServer server) {
		if (!this.getAlarms().get() && this.hostilePresence().get()) {
			for (Entity entity : TardisUtil.getEntitiesInInterior(tardis(), 200)) {
				if ((entity instanceof HostileEntity && !entity.hasCustomName()) || entity instanceof ServerPlayerEntity player &&
						tardis.loyalty().get(player).level() == Loyalty.Type.REJECT.level) {
					this.enable();
				}
			}
			return;
		}

		if (!this.getAlarms().get())
			return;

		if (tardis.travel().getState() == TravelHandlerBase.State.FLIGHT)
			return;

		soundCounter++;

		if (soundCounter >= CLOISTER_LENGTH_TICKS) {
			soundCounter = 0;
			tardis.travel().position().getWorld()
					.playSound(null, tardis.travel().position().getPos(),
							AITSounds.CLOISTER, SoundCategory.AMBIENT, 0.5f, 0.5f);
		}
	}
}
