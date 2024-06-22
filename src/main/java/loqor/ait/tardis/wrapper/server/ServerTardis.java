package loqor.ait.tardis.wrapper.server;

import com.google.gson.InstanceCreator;
import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.InteriorChangingHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.TardisChunkUtil;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class ServerTardis extends Tardis {

	@Exclude private boolean lock;

	public ServerTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorVariantSchema variantType) {
		super(uuid, new ServerTardisTravel(pos), new ServerTardisDesktop(schema), new TardisExterior(variantType));
	}

	private ServerTardis() {
		super();
		this.lock = false;
	}

	public void sync() {
		ServerTardisManager.getInstance().sendToSubscribers(this);
	}

	public void setLocked(boolean lock) {
		this.lock = lock;
	}

	public void startTick(MinecraftServer server) {
		if (this.isDirty()) {
			this.sync();
			this.markDirty();
		}
	}

	public void tick(MinecraftServer server) {
		if (this.lock)
			return;

		// most of the logic is in the handlers, so we can just disable them if we're a growth
		if (!this.engine().hasPower() && !DeltaTimeManager.isStillWaitingOnDelay(AITMod.MOD_ID + "-driftingmusicdelay")) {
			List<PlayerEntity> playerEntities = TardisUtil.getPlayersInsideInterior(this);
			for (PlayerEntity player : playerEntities) {
				player.playSound(AITSounds.DRIFTING_MUSIC, SoundCategory.MUSIC, 1, 1);
			}
			DeltaTimeManager.createDelay(AITMod.MOD_ID + "-driftingmusicdelay", (long) TimeUtil.minutesToMilliseconds(new Random().nextInt(7, 9)));
		}

		if (this.tardisHammerAnnoyance > 0 && !DeltaTimeManager.isStillWaitingOnDelay(AITMod.MOD_ID + "-tardisHammerAnnoyanceDecay")) {
			this.tardisHammerAnnoyance--;
			DeltaTimeManager.createDelay(AITMod.MOD_ID + "-tardisHammerAnnoyanceDecay", (long) TimeUtil.secondsToMilliseconds(10));
		}

		// @TODO if tnt explodes in the interior (near the console), then it should crash
		if (this.isGrowth())
			this.generateInteriorWithItem();

		if (this.isGrowth() && !this.<InteriorChangingHandler>handler(TardisComponent.Id.INTERIOR).isGenerating()) {
			if (this.getDoor().isBothClosed()) {
				this.getDoor().openDoors();
			} else {
				this.getDoor().setLocked(false);
			}
		}

		if (this.siege().isActive() && !this.getDoor().locked())
			this.getDoor().setLocked(true);

		this.getHandlers().tick(server);

		// im sure this is great for your server performace
		if (TardisChunkUtil.shouldExteriorChunkBeForced(this) && !TardisChunkUtil.isExteriorChunkForced(this)) {
			TardisChunkUtil.forceLoadExteriorChunk(this);
		} else if (!TardisChunkUtil.shouldExteriorChunkBeForced(this) && TardisChunkUtil.isExteriorChunkForced(this)) {
			TardisChunkUtil.stopForceExteriorChunk(this);
		}

		if (PropertiesHandler.getBool(this.properties(), PropertiesHandler.IS_FALLING))
			DoorData.lockTardis(true, this, null, true);

		this.travel().tick(server);
		this.getDesktop().tick(server);
	}

	public static Object creator() {
		return new ServerTardisCreator();
	}

	static class ServerTardisCreator implements InstanceCreator<ServerTardis> {

		@Override
		public ServerTardis createInstance(Type type) {
			return new ServerTardis();
		}
	}
}