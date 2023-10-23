package mdteam.ait.core.tardis;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.travel.TardisTravel;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

public class Tardis implements Serializable {
    private AbsoluteBlockPos position;
    private ExteriorEnum exterior;
    private UUID uuid;
    private TardisTravel travel;
    private TARDISDesktop desktop;

    public Tardis() {
        setUuid(getUuid());
        setDesktop(getDesktop());
        setPosition(getPosition());
        if(getDesktop() != null) getDesktop().setTardis(this);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setDesktop(TARDISDesktop desktop) {
        this.desktop = desktop;
    }

    public TARDISDesktop getDesktop() {
        return desktop;
    }

    public void setPosition(AbsoluteBlockPos blockPos) {
        this.position = blockPos;
    }

    public AbsoluteBlockPos getPosition() {
        return position;
    }

    public TardisTravel getTravel() {
        if (this.travel == null) this.travel = new TardisTravel(this.getUuid());

        return this.travel;
    }

    public void setExterior(ExteriorEnum exterior) {
       this.exterior = exterior;
    }

    public ExteriorEnum getExterior() {
        return exterior;
    }

    public World world() {
        return this.getPosition().getDimension();
    }
}
