package mdteam.ait.client.animation;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Math;
import mdteam.ait.tardis.TardisTravel;

public abstract class ExteriorAnimation {

    protected float alpha = 1;
    protected ExteriorBlockEntity exterior;
    protected int timeLeft, maxTime, startTime;
    protected static final Identifier UPDATE = new Identifier(AITMod.MOD_ID, "update_setup_anim");

    public ExteriorAnimation(ExteriorBlockEntity exterior) {
        this.exterior = exterior;

        if (!exterior.hasWorld()) return;
        if (exterior.getWorld().isClient()) {
            ClientPlayNetworking.registerGlobalReceiver(UPDATE,
                    (client, handler, buf, responseSender) -> {
                        int p = buf.readInt();
                        // System.out.println(TardisTravel.State.values()[p]);
                        this.setupAnimation(TardisTravel.State.values()[p]);
                    }
            );
        }
    }

    // fixme bug that sometimes happens where server doesnt have animation
    protected void runAlphaChecks(TardisTravel.State state) {
        if (this.exterior.getWorld().isClient())
            return;

        if (alpha <= 0f && state == TardisTravel.State.DEMAT) {
            exterior.getTardis().getTravel().toFlight();
        }
        if (alpha >= 1f && state == TardisTravel.State.MAT) {
            exterior.getTardis().getTravel().forceLand(this.exterior);
        }
    }

    public float getAlpha() {
        if (this.timeLeft < 0) {
            this.setupAnimation(exterior.getTardis().getTravel().getState()); // fixme is a jank fix for the timeLeft going negative on client
            return 1f;
        }

        return Math.clamp(0.0F, 1.0F, this.alpha);
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

        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            // System.out.println(player);
            tellClientToSetup(state, player);
        }
    }

    public void tellClientToSetup(TardisTravel.State state, ServerPlayerEntity player) {
        if (exterior.getWorld().isClient()) return;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(state.ordinal());

        ServerPlayNetworking.send(player, UPDATE, data);
    }
}