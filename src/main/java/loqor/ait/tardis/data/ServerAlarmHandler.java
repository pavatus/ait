package loqor.ait.tardis.data;

import loqor.ait.core.AITSounds;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;

import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

// use this as reference for starting other looping sounds on the exterior
public class ServerAlarmHandler extends TardisComponent implements TardisTickable {
	public static final int CLOISTER_LENGTH_TICKS = 3 * 20;
	private int soundCounter = 0; // decides when to start the next cloister sound

	public ServerAlarmHandler() {
		super(Id.ALARMS);
	}

	public void enable() {
		this.set(true);
	}

	public void disable() {
		this.set(false);
	}

	private void set(boolean var) {
		PropertiesHandler.set(this.tardis(), PropertiesHandler.ALARM_ENABLED, var);
	}

	public boolean isEnabled() {
		return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);
	}

	public void toggle() {
		if (isEnabled()) disable();
		else enable();
	}

	@Override
	public void tick(MinecraftServer server) {
		if(tardis.travel().position().getWorld().isClient()) return;

		// @TODO make a new control that makes it (by default) detect hostile entities in the interior plus a check when it's been cleared of all hostile entities - Loqor
		if (!isEnabled() && PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HOSTILE_PRESENCE_TOGGLE)) {
			for (Entity entity : TardisUtil.getEntitiesInInterior(tardis(), 200)) {
				if ((entity instanceof HostileEntity && !entity.hasCustomName()) || entity instanceof ServerPlayerEntity player &&
						tardis.getHandlers().getLoyalties().get(player).level() == Loyalty.Type.REJECT.level) {
					this.enable();
				}
			}
			return;
		}

		if (!tardis.getHandlers().getAlarms().isEnabled()) return;
		if (tardis.travel().getState() == TravelHandler.State.FLIGHT) return;

		soundCounter++;

		if (soundCounter >= CLOISTER_LENGTH_TICKS) {
			soundCounter = 0;
			tardis.travel().position().getWorld().playSound(null, tardis.travel().position().getPos(), AITSounds.CLOISTER, SoundCategory.AMBIENT, 0.5f, 0.5f);
		}
	}
}
