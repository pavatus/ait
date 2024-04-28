package loqor.ait.tardis.wrapper.server;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.registry.unlockable.Unlockable;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisChunkUtil;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;

import java.util.List;
import java.util.Random;
import java.util.UUID;


public class ServerTardis extends Tardis {

	public ServerTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variantType, boolean locked) {
		super(uuid, tardis -> new ServerTardisTravel(tardis, pos), tardis -> new ServerTardisDesktop(tardis, schema), tardis -> new ServerTardisExterior(tardis, exteriorType, variantType));
	}

	public void sync() {
		ServerTardisManager.getInstance().sendToSubscribers(this);
	}

	@Override
	public void init(boolean dirty) {
		// FIXME we need to put like, a special meta file in the .ait folder
		// 	that will indicate what was the version that was used to save the data.
		// 	i dont think that unlocking the default stuff for every tardis loaded is a good thing to do
		//  so it'd make sense if we could check if the data was saved with an earlier version (so it needs to unlock the default stuff)
		// 	different solution: make default stuff just be unlocked without the properties stuff
		if (!dirty && this.getTravel().findTardis().isPresent()) {
			ConsoleVariantRegistry.getInstance().unlock(this, Loyalty.MIN, null);
			ExteriorVariantRegistry.getInstance().unlock(this, Loyalty.MIN, null);
			DesktopRegistry.getInstance().unlock(this, Loyalty.MIN, null);
		}
	}

	public void unlock(Unlockable unlockable) {
		PropertiesHandler.setUnlocked(this, unlockable, true);
	}

	public void startTick(MinecraftServer server) {
		if (this.isDirty()) {
			this.sync();
			this.markDirty();
		}
	}

	public void tick(MinecraftServer server) {
		// most of the logic is in the handlers, so we can just disable them if we're a growth
		if (!this.hasPower() && !DeltaTimeManager.isStillWaitingOnDelay(AITMod.MOD_ID + "-driftingmusicdelay")) {
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

		if (this.isGrowth() && !this.getHandlers().getInteriorChanger().isGenerating()) {
			if (this.getDoor().isBothClosed()) {
				this.getDoor().openDoors();
			} else {
				this.getDoor().setLocked(false);
			}
		}

		if (this.isSiegeMode() && !this.getDoor().locked())
			this.getDoor().setLocked(true);

		this.getHandlers().tick(server);

		// im sure this is great for your server performace
		if (TardisChunkUtil.shouldExteriorChunkBeForced(this) && !TardisChunkUtil.isExteriorChunkForced(this)) {
			TardisChunkUtil.forceLoadExteriorChunk(this);
		} else if (!TardisChunkUtil.shouldExteriorChunkBeForced(this) && TardisChunkUtil.isExteriorChunkForced(this)) {
			TardisChunkUtil.stopForceExteriorChunk(this);
		}

		if (PropertiesHandler.getBool(getHandlers().getProperties(), PropertiesHandler.IS_FALLING))
			DoorData.lockTardis(true, this, null, true);

		AbsoluteBlockPos.Directed pos = this.getExterior().getExteriorPos();
		// If we're falling nearly out of the world, freak out.
		if (PropertiesHandler.getBool(this.getHandlers().getProperties(), PropertiesHandler.IS_FALLING)
				&& pos.getY() <= pos.getWorld().getBottomY() + 2) {
			PropertiesHandler.set(this, PropertiesHandler.ANTIGRAVS_ENABLED, true);
			PropertiesHandler.set(this, PropertiesHandler.IS_FALLING, false);
		}

		this.getTravel().tick(server);
		this.getDesktop().tick(server);
	}
}