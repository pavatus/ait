package mdteam.ait.core.tardis;

import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.UUID;

import static mdteam.ait.core.helper.TardisUtil.getTardisComponent;

public class Tardis {

    UUID uuid;
    TARDISDesktop desktop;
    AbsoluteBlockPos pos;

    NbtCompound tag;

    public Tardis(UUID id, TARDISDesktop desktop, AbsoluteBlockPos pos) {
        this.uuid = this.getUuid();
        this.desktop = this.getDesktop();
        this.pos = this.getPosition();
    }

    public Tardis(NbtCompound nbt) {
        this.tag = nbt;
    }

    public UUID getUuid() {
        return getTardisComponent().getUuid();
    }

    public TARDISDesktop getDesktop() {
        return getTardisComponent().getDesktop();
    }

    public AbsoluteBlockPos getPosition() {
        return getTardisComponent().getPosition();
    }

    public World world() {
        return this.getPosition().getDimension();
    }

}
