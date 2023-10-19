package mdteam.ait.core.components.world.tardis;

import dev.onyxstudios.cca.api.v3.component.Component;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.travel.TardisTravel;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public interface ITardisComponent extends Component {

    void setUuid(UUID uuid);
    UUID getUuid();
    void setDesktop(TARDISDesktop desktop);
    TARDISDesktop getDesktop();
    void setPosition(AbsoluteBlockPos pos);
    AbsoluteBlockPos getPosition();
    void setExterior(ExteriorEnum exterior, ExteriorBlockEntity blockEntity);
    ExteriorEnum getExterior(ExteriorBlockEntity blockEntity);
    TardisTravel getTravel();
}
