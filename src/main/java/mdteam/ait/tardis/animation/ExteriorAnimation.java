package mdteam.ait.tardis.animation;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.util.NetworkUtil;
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
        if (this.exterior.getWorld().isClient() || this.exterior.getTardis().isEmpty())
            return;

        if (alpha <= 0f && state == TardisTravel.State.DEMAT) {
            exterior.getTardis().get().getTravel().toFlight();
        }
        if (alpha >= 1f && state == TardisTravel.State.MAT) {
            exterior.getTardis().get().getTravel().forceLand(this.exterior);
        }
    }

    public float getAlpha() {

        if(this.exterior.getTardis().isEmpty()) return 1f;

        if (this.timeLeft < 0) {
            this.setupAnimation(exterior.getTardis().get().getTravel().getState()); // fixme is a jank fix for the timeLeft going negative on client
            return 1f;
        }
        if (this.exterior.getTardis().get().getTravel().getState() == TardisTravel.State.LANDED && this.exterior.getTardis().get().getHandlers().getCloak().isEnabled()) {
            return 0.105f;
        }

        return Math.clamp(0.0F, 1.0F, this.alpha);
    }
    private boolean isServer() {
        return !this.exterior.getWorld().isClient();
    }

    public static boolean isNearTardis(PlayerEntity player, Tardis tardis, double radius) {
        return radius >= distanceFromTardis(player, tardis);
    }
    public static double distanceFromTardis(PlayerEntity player, Tardis tardis) {
        BlockPos pPos = player.getBlockPos();
        BlockPos tPos = tardis.position();
        double distance = Math.sqrt(tPos.getSquaredDistance(pPos));
        return distance;
    }

    public abstract void tick();

    public abstract void setupAnimation(TardisTravel.State state);

    public void setAlpha(float alpha) {
        this.alpha = Math.clamp(0.0F, 1.0F, alpha);
    }

    public boolean hasAnimationStarted() {
        return this.timeLeft < this.startTime;
    }

    public void tellClientsToSetup(TardisTravel.State state) {
        if (exterior.getWorld() == null) return; // happens when tardis spawns above world limit, so thats nice
        if (exterior.getWorld().isClient()) return;
        if (exterior.getTardis().isEmpty()) return;

        for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(exterior.getTardis().get())) {
            // System.out.println(player);
            tellClientToSetup(state, player);
        }
    }

    public void tellClientToSetup(TardisTravel.State state, ServerPlayerEntity player) {
        if (exterior.getWorld().isClient() && exterior.getTardis().isEmpty()) return;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(state.ordinal());
        data.writeUuid(exterior.getTardis().get().getUuid());

        ServerPlayNetworking.send(player, UPDATE, data);
    }
}