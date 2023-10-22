package mdteam.ait.core.tardis;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.travel.TardisTravel;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.UUID;

import static mdteam.ait.AITMod.TARDISCLASSNBT;
import static mdteam.ait.core.helper.TardisUtil.getTardisComponent;

public class Tardis {

    public static final TARDISDesktop.Serializer DESKTOP_SERIALIZER = new TARDISDesktop.Serializer();
    public static final TardisTravel.Serializer TRAVEL_SERIALIZER = new TardisTravel.Serializer();

    AbsoluteBlockPos position;
    ExteriorEnum exterior;
    UUID uuid;
    public Tardis() {
        setUuid(getUuid());
        setDesktop(getDesktop());
        setPosition(getPosition());
        if(getDesktop() != null) getDesktop().link(this);
    }

    public Tardis(NbtCompound nbt) {
        getTardisComponent().setNbt(nbt);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setDesktop(TARDISDesktop desktop) {
        getTardisComponent().setDesktop(desktop);
    }

    public TARDISDesktop getDesktop() {
        return getTardisComponent().getDesktop();
    }

    public void setPosition(AbsoluteBlockPos blockPos) {
        this.position = blockPos;
    }

    public AbsoluteBlockPos getPosition() {
        return position;
    }

    public TardisTravel getTravel() {
        return getTardisComponent().getTravel();
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

    public NbtCompound getNbt() {
        return getTardisComponent().getNbt();
    }
}
