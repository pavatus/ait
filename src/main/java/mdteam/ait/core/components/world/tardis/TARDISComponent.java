package mdteam.ait.core.components.world.tardis;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class TARDISComponent implements TARDISWorldComponent, AutoSyncedComponent {

    private ExteriorEnum exterior;
    private AbsoluteBlockPos pos;
    //private TARDISTravel travel;
    private UUID uuid;
    private TARDISDesktop interior;

    public TARDISComponent(World world) {

    }

    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }
}
