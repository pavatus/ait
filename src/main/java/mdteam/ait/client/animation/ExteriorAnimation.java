package mdteam.ait.client.animation;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Math;
import the.mdteam.ait.ClientTardisManager;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.TardisTravel;

import java.util.UUID;

public abstract class ExteriorAnimation {
    public static final Identifier SYNC = new Identifier(AITMod.MOD_ID, "sync_exterior_anims");

    protected float alpha;
    protected ExteriorBlockEntity exterior;
    protected int timeLeft;
    protected int maxTime;
    protected float alphaChangeAmount = 0.005f;

    public ExteriorAnimation(ExteriorBlockEntity exterior) {
        this.exterior = exterior;

        ClientPlayNetworking.registerGlobalReceiver(SYNC,
            (client, handler, buf, responseSender) -> {
                World level = MinecraftClient.getInstance().world;

                if (level == null) {return;}

                BlockPos pos = buf.readBlockPos();
                float pAlpha = buf.readFloat();

                ExteriorBlockEntity entity = (ExteriorBlockEntity) level.getBlockEntity(pos);
                if (entity == null) {return;}

                ExteriorAnimation animation = entity.getAnimation();
                animation.setAlpha(pAlpha);
            }
        );
    }

    protected void runAlphaChecks(TardisTravel.State state) {
        if (alpha <= 0f && state == TardisTravel.State.DEMAT) {
            exterior.getTardis().getTravel().setState(TardisTravel.State.FLIGHT);
            exterior.getTardis().getTravel().deleteExterior();
            exterior.getTardis().getTravel().checkShouldRemat();
        }
        if (alpha >= 1f && state == TardisTravel.State.MAT) {
            exterior.getTardis().getTravel().setState(TardisTravel.State.LANDED);
            exterior.getTardis().getTravel().runAnimations(exterior);
        }
    }

    public float getAlpha() {
        return Math.clamp(0.0F,1.0F,this.alpha);
    }

    public abstract void tick();
    public abstract void setupAnimation(TardisTravel.State state);

    public void setAlpha(float alpha) {
        this.alpha = Math.clamp(0.0F,1.0F, alpha);
    }

    public void setAlphaChangeAmount(float amount) {
        this.alphaChangeAmount = amount;
    }

    protected PacketByteBuf getUpdateData() {
        PacketByteBuf data = PacketByteBufs.create();

        data.writeBlockPos(this.exterior.getPos());
        data.writeFloat(this.alpha);

        return data;
    }

    public void updateClient() {
        if (this.exterior.getWorld().isClient()) {return;}

        ServerTardisManager.sendPacketToAll(SYNC, getUpdateData());
    }
}