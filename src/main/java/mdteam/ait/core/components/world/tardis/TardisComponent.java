package mdteam.ait.core.components.world.tardis;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.world.WorldSyncCallback;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.travel.TardisTravel;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

import static mdteam.ait.AITMod.TARDISCLASSNBT;

public class TardisComponent implements ITardisComponent, AutoSyncedComponent {
    World world;
    UUID uuid;
    AbsoluteBlockPos absoluteBlockPos;
    ExteriorEnum exterior;
    TARDISDesktop desktop;
    TardisTravel travel;
    NbtCompound tag;
    public TardisComponent(World world) {
        this.world = world;
        this.tag = new NbtCompound();
    }

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
        TARDISCLASSNBT.sync(this.world);
    }

    @Override
    public TARDISDesktop getDesktop() {
        return this.desktop;
    }

    @Override
    public void setPosition(AbsoluteBlockPos pos) {
        this.absoluteBlockPos = pos;
        TARDISCLASSNBT.sync(this.world);
    }

    @Override
    public AbsoluteBlockPos getPosition() {
        return this.absoluteBlockPos;
    }

    @Override
    public void setExterior(ExteriorEnum exterior) {
        this.exterior = exterior;
        TARDISCLASSNBT.sync(this.world);
    }

    @Override
    public ExteriorEnum getExterior() {
        return this.exterior;
    }

    @Override
    public TardisTravel getTravel() {
        if (this.travel == null) this.travel = new TardisTravel(this.getUuid());

        return this.travel;
    }

    @Override
    public void setNbt(NbtCompound nbt) {
        this.tag = nbt;
    }
    @Override
    public NbtCompound getNbt() {
        return this.tag;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        //if(this.tag.contains("exterior"))
        //    this.exterior = ExteriorEnum.values()[this.tag.getInt("exterior")]; TARDISCLASSNBT.sync(this.world);
        //if(this.tag.contains("desktop"))
        //    this.desktop = DESKTOP_SERIALIZER.deserialize(this.tag.getCompound("desktop")); TARDISCLASSNBT.sync(this.world);
        //if(this.tag.contains("uuid"))
        //    this.uuid = tag.getUuid("uuid"); TARDISCLASSNBT.sync(this.world);
        //if(this.tag.contains("position"))
        //    this.absoluteBlockPos = AbsoluteBlockPos.readFromNbt(this.tag.getCompound("position")); TARDISCLASSNBT.sync(this.world);
        //if(this.tag.contains("travel"))
        //    this.travel = TRAVEL_SERIALIZER.deserialize(this.tag.getCompound("travel")); TARDISCLASSNBT.sync(this.world);
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        //if(this.exterior != null) this.tag.putInt("exterior", this.exterior.ordinal());
        //if(this.desktop != null) this.tag.put("desktop", DESKTOP_SERIALIZER.serialize(this.desktop));
        //if(this.uuid != null) this.tag.putUuid("uuid",this.uuid);
        //if(this.absoluteBlockPos != null) this.tag.put("position",this.absoluteBlockPos.writeToNbt());
        //if(this.getTravel() != null) this.tag.put("travel",TRAVEL_SERIALIZER.serialize(this.getTravel()));
    }
}
