package loqor.ait.tardis.wrapper.server;

import com.google.gson.InstanceCreator;
import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.item.ChargedZeitonCrystalItem;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.core.util.TimeUtil;
import loqor.ait.registry.impl.DesktopRegistry;
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
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class ServerTardis extends Tardis {

	public ServerTardis(UUID uuid, TardisDesktopSchema schema, ExteriorVariantSchema variantType) {
		super(uuid, new ServerTardisDesktop(schema), new TardisExterior(variantType));
	}

	private ServerTardis() {
		super();
	}

	public void sync() {
		ServerTardisManager.getInstance().sendToSubscribers(this);
	}

	public void tick(MinecraftServer server) {
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
			if (this.door().isBothClosed()) {
				this.door().openDoors();
			} else {
				this.door().setLocked(false);
			}
		}

		if (this.siege().isActive() && !this.door().locked())
			this.door().setLocked(true);

		this.getHandlers().tick(server);

		// im sure this is great for your server performace
		if (TardisChunkUtil.shouldExteriorChunkBeForced(this) && !TardisChunkUtil.isExteriorChunkForced(this)) {
			TardisChunkUtil.forceLoadExteriorChunk(this);
		} else if (!TardisChunkUtil.shouldExteriorChunkBeForced(this) && TardisChunkUtil.isExteriorChunkForced(this)) {
			TardisChunkUtil.stopForceExteriorChunk(this);
		}

		if (PropertiesHandler.getBool(this.properties(), PropertiesHandler.IS_FALLING))
			DoorData.lockTardis(true, this, null, true);
	}

	protected void generateInteriorWithItem() {
		TardisUtil.getEntitiesInInterior(this, 50)
				.stream()
				.filter(entity -> (entity instanceof ItemEntity) &&
						(((ItemEntity) entity).getStack().getItem() == Items.NETHER_STAR ||
								isChargedCrystal(((ItemEntity) entity).getStack())) &&
						entity.isTouchingWater()).forEach(entity -> {
					DirectedGlobalPos position = this.travel().position();

					if (position == null)
						return;

					this.setFuelCount(8000);

					entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 10.0F, 0.75F);
					entity.getWorld().playSound(null, position.getPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 10.0F, 0.75F);

					InteriorChangingHandler interior = this.handler(TardisComponent.Id.INTERIOR);
					interior.queueInteriorChange(DesktopRegistry.getInstance().getRandom(this));

					if (interior.isGenerating())
						entity.discard();
				});
	}

	private boolean isChargedCrystal(ItemStack stack) {
		if (!(stack.getItem() instanceof ChargedZeitonCrystalItem))
			return false;

		NbtCompound nbt = stack.getOrCreateNbt();

		if (!nbt.contains(ChargedZeitonCrystalItem.FUEL_KEY))
			return false;

		return nbt.getDouble(ChargedZeitonCrystalItem.FUEL_KEY) >= ChargedZeitonCrystalItem.MAX_FUEL;
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