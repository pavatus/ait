package mdteam.ait.core.components.world.tardis;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.travel.TardisTravel;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.UUID;

import static mdteam.ait.core.tardis.Tardis.DESKTOP_SERIALIZER;
import static mdteam.ait.core.tardis.Tardis.TRAVEL_SERIALIZER;

public class TardisComponent implements ITardisComponent, AutoSyncedComponent {

    UUID uuid;
    AbsoluteBlockPos absoluteBlockPos;
    ExteriorEnum exterior;
    TARDISDesktop desktop;
    TardisTravel travel;

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setDesktop(TARDISDesktop desktop) {
        this.desktop = desktop;
    }

    @Override
    public TARDISDesktop getDesktop() {
        return this.desktop;
    }

    @Override
    public void setPosition(AbsoluteBlockPos pos) {
        this.absoluteBlockPos = pos;
    }

    @Override
    public AbsoluteBlockPos getPosition() {
        return this.absoluteBlockPos;
    }

    @Override
    public void setExterior(ExteriorEnum exterior, ExteriorBlockEntity blockEntity) {
        this.exterior = exterior;
        blockEntity.setExterior(this.exterior);
    }

    @Override
    public ExteriorEnum getExterior(ExteriorBlockEntity blockEntity) {
        if(blockEntity == null) return this.exterior;
        else return blockEntity.getExterior();
    }

    @Override
    public TardisTravel getTravel() {
        if (this.travel == null) this.travel = new TardisTravel(Tardis);

        return this.travel;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if(tag.contains("exterior")) this.exterior = ExteriorEnum.values()[tag.getInt("")];
        if(tag.contains("desktop")) this.desktop = DESKTOP_SERIALIZER.deserialize(tag.getCompound("desktop"));
        if(tag.contains("uuid")) this.uuid = tag.getUuid("uuid");
        if(tag.contains("position")) this.absoluteBlockPos = AbsoluteBlockPos.readFromNbt(tag.getCompound("position"));
        if(tag.contains("travel")) this.travel = TRAVEL_SERIALIZER.deserialize(tag.getCompound("travel"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("exterior", this.exterior.ordinal());
        tag.put("desktop",DESKTOP_SERIALIZER.serialize(this.desktop));
        tag.putUuid("uuid",this.uuid);
        tag.put("position",this.absoluteBlockPos.writeToNbt());
        tag.put("travel",TRAVEL_SERIALIZER.serialize(this.getTravel()));
    }
}
