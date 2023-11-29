package mdteam.ait.client.animation;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.TardisTravel;

public class ClassicAnimation extends ExteriorAnimation {
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

            runAlphaChecks(state);
        } else if (state == TardisTravel.State.MAT) {
            this.updateClient();
            alpha = alpha + alphaChangeAmount;
            timeLeft--;

            runAlphaChecks(state);
        } else if (!exterior.getWorld().isClient() && state == TardisTravel.State.LANDED && alpha != 1f)
            this.setAlpha(1f);

        this.updateClient();
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
        }

        maxTime = timeLeft;

        this.updateClient();
    }
}
