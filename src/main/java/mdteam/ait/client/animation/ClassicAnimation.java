package mdteam.ait.client.animation;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.TardisTravel;

public class ClassicAnimation extends ExteriorAnimation {
    private boolean firstRun;

    public ClassicAnimation(ExteriorBlockEntity exterior) {
        super(exterior);
    }

    @Override
    public void tick() {
        if (exterior.getTardis() == null) {
            if (!exterior.getWorld().isClient())
                exterior.refindTardis();

            return;
        }

        TardisTravel.State state = exterior.getTardis().getTravel().getState();

        if (state == TardisTravel.State.DEMAT)   {
            this.updateClient();
            alpha = alpha - alphaChangeAmount;
            timeLeft--;
        } else if (state == TardisTravel.State.MAT) {
            this.updateClient();
            alpha = alpha + alphaChangeAmount;
            timeLeft--;
            if (alpha >= 1f) {
                exterior.getTardis().getTravel().setState(TardisTravel.State.LANDED);
            }
        } /*else if (state == TardisTravel.State.LANDED) {
            this.updateClient();
            alpha = 1F;
        }*/
    }

    @Override
    public void setupAnimation(TardisTravel.State state) {
        if (state == TardisTravel.State.DEMAT) {
            alpha = 1f;
            timeLeft = 150;
        } else if (state == TardisTravel.State.MAT){
            alpha = 0f;
            timeLeft = 200;
        } else {
            alpha = 1f;
            timeLeft = 0;
            System.out.println(alpha);
        }

        firstRun = true;

        this.updateClient();
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    protected PacketByteBuf getUpdateData() {
        PacketByteBuf data = PacketByteBufs.create();

        data.writeBlockPos(this.exterior.getPos());
        data.writeFloat(this.alpha);
        data.writeBoolean(this.firstRun);

        return data;
    }
}
