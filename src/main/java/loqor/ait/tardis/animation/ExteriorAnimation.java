package loqor.ait.tardis.animation;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.AITMod;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.CloakData;
import loqor.ait.tardis.data.TravelHandler;
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
	public static final double MAX_CLOAK_DISTANCE = 5d;
	protected float alpha = 1;
	protected ExteriorBlockEntity exterior;
	protected int timeLeft, maxTime, startTime;
	public static final Identifier UPDATE = new Identifier(AITMod.MOD_ID, "update_setup_anim");

	public ExteriorAnimation(ExteriorBlockEntity exterior) {
		this.exterior = exterior;
	}

	// fixme bug that sometimes happens where server doesnt have animation
	protected void runAlphaChecks(TardisTravel.State state) {
		if (this.exterior.getWorld().isClient() || this.exterior.tardis().isEmpty())
			return;

		if (alpha <= 0f && state == TardisTravel.State.DEMAT) {
			exterior.tardis().get().travel().toFlight();
		}
		if (alpha >= 1f && state == TardisTravel.State.MAT) {
			exterior.tardis().get().travel().forceLand(this.exterior);
		}
	}

	public float getAlpha() {
		return this.exterior.tardis().ifPresent(tardis -> {
			if (this.timeLeft < 0) {
				this.setupAnimation(tardis.travel().getState()); // fixme is a jank fix for the timeLeft going negative on client
				return 1f;
			}

			if (tardis.travel().getState() == TravelHandler.State.LANDED
					&& tardis.<CloakData>handler(TardisComponent.Id.CLOAK).isEnabled()) {
				return 0.105f;
			}

			return Math.clamp(0.0F, 1.0F, this.alpha);
		}).orElse(1f);
	}

	public static boolean isNearTardis(PlayerEntity player, Tardis tardis, double radius) {
		return radius >= distanceFromTardis(player, tardis);
	}

	public static double distanceFromTardis(PlayerEntity player, Tardis tardis) {
		BlockPos pPos = player.getBlockPos();
		BlockPos tPos = tardis.travel().getPosition().getPos();
        return Math.sqrt(tPos.getSquaredDistance(pPos));
	}

	public abstract void tick();

	public abstract void setupAnimation(TravelHandler.State state);

	public void setAlpha(float alpha) {
		this.alpha = Math.clamp(0.0F, 1.0F, alpha);
	}

	public boolean hasAnimationStarted() {
		return this.timeLeft < this.startTime;
	}

	public void tellClientsToSetup(TravelHandler.State state) {
		if (exterior.getWorld() == null)
			return; // happens when tardis spawns above world limit, so thats nice

		if (exterior.getWorld().isClient())
			return;

		if (exterior.tardis().isEmpty())
			return;

		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(exterior.tardis().get())) {
			tellClientToSetup(state, player);
		}
	}

	public void tellClientToSetup(TravelHandler.State state, ServerPlayerEntity player) {
		if (exterior.getWorld().isClient() || exterior.tardis().isEmpty())
			return;

		PacketByteBuf data = PacketByteBufs.create();
		data.writeInt(state.ordinal());
		data.writeUuid(exterior.tardis().get().getUuid());

		ServerPlayNetworking.send(player, UPDATE, data);
	}
}