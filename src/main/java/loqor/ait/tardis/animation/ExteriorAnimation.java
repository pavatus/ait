package loqor.ait.tardis.animation;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.CloakHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.NetworkUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Math;

public abstract class ExteriorAnimation {

	public static final Identifier UPDATE = new Identifier(AITMod.MOD_ID, "update_setup_anim");
	public static final double MAX_CLOAK_DISTANCE = 5d;

	protected ExteriorBlockEntity exterior;
	protected int timeLeft, maxTime, startTime;
	protected float alpha = 1;

	private boolean isDone = false;

	public ExteriorAnimation(ExteriorBlockEntity exterior) {
		this.exterior = exterior;
	}

	public float getAlpha() {
		if (this.exterior.tardis().isEmpty())
			return 1f;

		if (this.exterior.tardis().get().travel().getState() == TravelHandlerBase.State.LANDED) {
			if (this.exterior.tardis().get().<CloakHandler>handler(TardisComponent.Id.CLOAK).cloaked().get()) {
				return 0.105f;
			}
			return 1.0f;
		}

		return this.alpha;
	}

	public static boolean isNearTardis(PlayerEntity player, Tardis tardis, double radius) {
		return radius >= distanceFromTardis(player, tardis);
	}

	public static double distanceFromTardis(PlayerEntity player, Tardis tardis) {
		BlockPos pPos = player.getBlockPos();
		BlockPos tPos = tardis.travel().position().getPos();
		return Math.sqrt(tPos.getSquaredDistance(pPos));
	}

	public abstract void tick(Tardis tardis);

	public boolean reset() {
		if (this.isDone)
			return false;

		this.isDone = true;
		return true;
	}

	public boolean setupAnimation(TravelHandlerBase.State state) {
		if (exterior.tardis().isEmpty()) {
            AITMod.LOGGER.error("Tardis for exterior {} was null!", exterior);
			this.alpha = 0f; // just make me vanish.
			return false;
		}

		Tardis tardis = exterior.tardis().get();

		if (tardis.getExterior().getCategory() == null) {
			AITMod.LOGGER.error("Exterior category {} was null!", exterior);
			this.alpha = 0f; // just make me vanish.
			return false;
		}

		this.alpha = switch (state) {
			case DEMAT, LANDED -> 1f;
			case MAT -> 0f;

			default -> {
				AITMod.LOGGER.error("Can't get alpha for a TARDIS in FLIGHT state! Using default!");
				yield 0;
			}
		};

		this.tellClientsToSetup(state);
		MatSound sound = tardis.getExterior().getVariant().getSound(state);

		if (sound == null)
			return false;

		this.timeLeft = sound.timeLeft();
		this.maxTime = sound.maxTime();
		this.startTime = sound.startTime();

		return true;
	}

	public void setAlpha(float alpha) {
		this.alpha = Math.clamp(0.0F, 1.0F, alpha);
	}

	public void tellClientsToSetup(TravelHandlerBase.State state) {
		if (exterior.getWorld() == null)
			return; // happens when tardis spawns above world limit, so thats nice

		if (exterior.getWorld().isClient() || exterior.tardis().isEmpty())
			return;

		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(exterior.tardis().get())) {
			tellClientToSetup(state, player);
		}
	}

	public void tellClientToSetup(TravelHandlerBase.State state, ServerPlayerEntity player) {
		if (exterior.getWorld().isClient() || exterior.tardis().isEmpty())
			return;

		PacketByteBuf data = PacketByteBufs.create();
		data.writeInt(state.ordinal());
		data.writeUuid(exterior.tardis().getId());

		ServerPlayNetworking.send(player, UPDATE, data);
	}
}