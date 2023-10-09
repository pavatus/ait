package mdteam.ait.core.tardis;

import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

import static mdteam.ait.AITMod.EXTERIORNBT;

public class Tardis implements TardisData {
    public static final TARDISDesktop.Serializer DESKTOP_SERIALIZER = new TARDISDesktop.Serializer();
    private TARDISDesktop desktop;
    private UUID uuid;
    private AbsoluteBlockPos pos;

    public Tardis(UUID uuid, TARDISDesktop desktop, AbsoluteBlockPos pos) {
        this.uuid = uuid;
        this.desktop = desktop;
        this.desktop.link(this);
        this.pos = pos;
    }

    public Tardis(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.put("desktop",DESKTOP_SERIALIZER.serialize(this.desktop));
        nbt.putUuid("uuid",this.uuid);
        nbt.put("position",this.pos.writeToNbt());

        return nbt;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        if(nbt.contains("desktop")) this.desktop = DESKTOP_SERIALIZER.deserialize(nbt.getCompound("desktop"));
        if(nbt.contains("uuid")) this.uuid = nbt.getUuid("uuid");
        if(nbt.contains("position")) this.pos = AbsoluteBlockPos.readFromNbt(nbt.getCompound("position"));
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public TARDISDesktop getDesktop() {
        return this.desktop;
    }

    @Override
    public void setDesktop(TARDISDesktop desktop) {
        this.desktop = desktop;
    }

    @Override
    public AbsoluteBlockPos getPosition() {
        return this.pos;
    }

    @Override
    public void setPosition(AbsoluteBlockPos pos) {
        this.pos = pos;
    }
}
