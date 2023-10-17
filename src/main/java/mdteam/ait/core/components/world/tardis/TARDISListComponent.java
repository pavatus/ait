package mdteam.ait.core.components.world.tardis;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.world.WorldSyncCallback;
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

import static mdteam.ait.AITMod.TARDISNBT;

public class TARDISListComponent implements TARDISListWorldComponent, AutoSyncedComponent {
    private List<Tardis> tardisList = new ArrayList<>();

    @Override
    public List<Tardis> getTardises() {
        return this.tardisList;
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
        this.tardisList.clear(); // Clear the list to avoid duplicates when reading from NBT

        if (tag.contains("tardisList")) {
            NbtCompound listTag = tag.getCompound("tardisList");

            for (String key : listTag.getKeys()) {
                NbtCompound nbt = listTag.getCompound(key);
                this.tardisList.add(new Tardis(nbt));
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound listTag = new NbtCompound();

        for (int i = 0; i < this.tardisList.size(); i++) {
            Tardis tardis = this.tardisList.get(i);

            NbtCompound nbt = tardis.writeToNbt();

            listTag.put(String.valueOf(i), nbt);
        }

        tag.put("tardisList", listTag);
    }
}
