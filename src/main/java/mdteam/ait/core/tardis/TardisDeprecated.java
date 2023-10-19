/*package mdteam.ait.core.tardis;

import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.travel.TardisTravel;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class TardisDeprecated implements TardisData {
    public static final TARDISDesktop.Serializer DESKTOP_SERIALIZER = new TARDISDesktop.Serializer();
    public static final TardisTravel.Serializer TRAVEL_SERIALIZER = new TardisTravel.Serializer();
    private TARDISDesktop desktop;
    private UUID uuid;
    private AbsoluteBlockPos pos;
    private TardisTravel travel;

    public Tardis(UUID uuid, TARDISDesktop desktop, AbsoluteBlockPos pos) {
        this.uuid = uuid;
        this.desktop = desktop;
        this.desktop.link(this);
        this.pos = pos;
    }

    public Tardis(NbtCompound nbt) {
        this.readFromNbt(nbt);
    }

    public TardisTravel getTravel() {
        if (this.travel == null) this.travel = new TardisTravel(this);

        return this.travel;
    }

    public World world() {
        return this.getPosition().getDimension();
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.put("desktop",DESKTOP_SERIALIZER.serialize(this.desktop));
        nbt.putUuid("uuid",this.uuid);
        nbt.put("position",this.pos.writeToNbt());
        nbt.put("travel",TRAVEL_SERIALIZER.serialize(this.getTravel()));

        return nbt;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        if(nbt.contains("desktop")) this.desktop = DESKTOP_SERIALIZER.deserialize(nbt.getCompound("desktop"));
        if(nbt.contains("uuid")) this.uuid = nbt.getUuid("uuid");
        if(nbt.contains("position")) this.pos = AbsoluteBlockPos.readFromNbt(nbt.getCompound("position"));
        if(nbt.contains("travel")) this.travel = TRAVEL_SERIALIZER.deserialize(nbt.getCompound("travel"));
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
}*/
