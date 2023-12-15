package mdteam.ait.client.animation;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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

        if(FabricLauncherBase.getLauncher().getEnvironmentType() == EnvType.CLIENT) {
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
        if (this.exterior.getWorld().isClient)
            return;

        if (alpha <= 0f && state == TardisTravel.State.DEMAT) {
            exterior.tardis().getTravel().toFlight();
        }
        if (alpha >= 1f && state == TardisTravel.State.MAT) {
            exterior.tardis().getTravel().forceLand(this.exterior);
        }
    }

    public float getAlpha() {
        return Math.clamp(0.0F,1.0F,this.alpha);
    }

    public abstract void tick();

    public void setupAnimation(TardisTravel.State state) {
//        if () {
//            tellClientsToSetup(state);
//        }
    }

    public void setAlpha(float alpha) {
        this.alpha = Math.clamp(0.0F,1.0F, alpha);
    }

    public void tellClientsToSetup(TardisTravel.State state) {
        if (TardisUtil.isClient()) return;

        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            // System.out.println(player);
            tellClientToSetup(state, player);
        }
    }
    public void tellClientToSetup(TardisTravel.State state, ServerPlayerEntity player) {
        if (TardisUtil.isClient()) return;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeInt(state.ordinal());

        ServerPlayNetworking.send(player, UPDATE, data);
    }
}