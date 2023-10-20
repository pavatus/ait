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
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static mdteam.ait.AITMod.TARDISNBT;

public class TARDISListComponent implements TARDISListWorldComponent {

    List<Tardis> tardisList = new ArrayList<>();

    @Override
    public List<Tardis> getTardises() {
        return tardisList;
    }

    @Override
    public void setTardises(List<Tardis> TardisList) {
        tardisList = TardisList;
    }

    public void putTardis(Tardis tardis) {
        tardisList.add(tardis);
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
        //try {
        //    NbtList nbtElements = tag.getList("tardisData", NbtElement.LIST_TYPE);
        //    tardisList = new ArrayList<>();
        //    int size = nbtElements.size();
        //    for (int i = 0; i <= size; i++) {
        //        UUID uuid = UUID.fromString(nbtElements.getCompound(i).getString("uuid"));
        //        TARDISDesktop desktop = DESKTOP_SERIALIZER.deserialize(nbtElements.getCompound(i).getCompound("desktop"));
        //        AbsoluteBlockPos position = AbsoluteBlockPos.readFromNbt(nbtElements.getCompound(i).getCompound("position"));
        //        Tardis tardis = new Tardis(uuid, desktop, position);
        //        tardisList.add(tardis);
        //    }
        //} catch (Exception e) {
        //    //Nothing
        //}
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound listTag = new NbtCompound();

        for (int i = 0; i < this.tardisList.size(); i++) {
            Tardis tardis = this.tardisList.get(i);

            NbtCompound nbt = tardis.getNbt();

            listTag.put(String.valueOf(i), nbt);
        }

        tag.put("tardisList", listTag);
        //NbtList nbtElements = new NbtList();
        //for(Tardis tardis : tardisList) {
        //    NbtCompound nbtCompound = new NbtCompound();
        //    nbtCompound.putString("uuid", tardis.getUuid().toString());
        //    nbtCompound.put("desktop", DESKTOP_SERIALIZER.serialize(tardis.getDesktop()));
        //    nbtCompound.put("position", tardis.getPosition().writeToNbt());
        //    nbtElements.add(nbtCompound);
        //}
        //tag.put("tardisData", nbtElements);
    }
}
