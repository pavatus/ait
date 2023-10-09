package mdteam.ait.core.tardis;

import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public interface TardisData {
    NbtCompound writeToNbt();
    void readFromNbt(NbtCompound nbt);

    UUID getUuid();
    void setUuid(UUID uuid);

    TARDISDesktop getDesktop();
    void setDesktop(TARDISDesktop desktop);

    AbsoluteBlockPos getPosition();
    void setPosition(AbsoluteBlockPos pos);
}
