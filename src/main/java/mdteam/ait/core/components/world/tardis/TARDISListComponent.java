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

import static mdteam.ait.AITMod.TARDISNBT;

public class TARDISListComponent implements TARDISListWorldComponent, AutoSyncedComponent {
    private List<Tardis> tardisList = new ArrayList<>();
    private World world;

    public TARDISListComponent(World world) {
        this.world = world;
    }

    @Override
    public List<Tardis> getTardises() {
        return this.tardisList;
    }

    @Override
    public void setTardises(List<Tardis> tardisList) {
        this.tardisList = tardisList;
        TARDISNBT.sync(this.world);
    }
    public void putTardis(Tardis tardis) {
        this.tardisList.add(tardis);
        TARDISNBT.sync(this.world);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("tardisList")) {
            NbtList list = tag.getList("tardisList", NbtElement.COMPOUND_TYPE);
            list.forEach((nbt) -> {
                this.tardisList.add(new Tardis((NbtCompound) nbt));
            });
        }
        TARDISNBT.sync(this.world);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.tardisList == null) return;

        System.out.println(this.tardisList);

        NbtList list = new NbtList();
        for (Tardis tardis : this.tardisList) {
            list.add(tardis.writeToNbt());
        }
        tag.put("tardisList",list);
    }
}
