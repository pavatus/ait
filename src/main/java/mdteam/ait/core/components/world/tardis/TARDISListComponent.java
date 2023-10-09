package mdteam.ait.core.components.world.tardis;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.Tardis;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;

import javax.management.ListenerNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TARDISListComponent implements TARDISListWorldComponent, AutoSyncedComponent {
    private List<Tardis> tardisList = new ArrayList<>();

    public TARDISListComponent(World world) {

    }

    @Override
    public List<Tardis> getTardises() {
        return tardisList;
    }

    @Override
    public void setTardises(List<Tardis> tardisList) {
        this.tardisList = tardisList;
    }
    public void putTardis(Tardis tardis) {
        this.tardisList.add(tardis);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("tardisList")) {
            NbtList list = tag.getList("tardisList", NbtElement.COMPOUND_TYPE);
            list.forEach((nbt) -> {
                this.tardisList.add(new Tardis((NbtCompound) nbt));
            });
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.tardisList == null) return;

        NbtList list = new NbtList();
        for (Tardis tardis : this.tardisList) {
            list.add(tardis.writeToNbt());
        }
        tag.put("tardisList",list);
    }
}
