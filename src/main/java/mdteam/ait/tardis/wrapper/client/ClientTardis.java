package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.*;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

// Things saved here will likely get overwritten.
public class ClientTardis extends Tardis {
    public ClientTardis(UUID uuid, AbsoluteBlockPos.Client pos, TardisDesktopSchema schema, ExteriorSchema exteriorType, ExteriorVariantSchema variantType, boolean locked) {
        super(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), tardis -> new ClientTardisExterior(tardis, exteriorType, variantType), locked);
    }

    public void setDesktop(TardisDesktop desktop) {
        this.desktop = desktop;
    }

    public void setTravel(TardisTravel travel) {
        this.travel = travel;
    }

    public void setExterior(TardisExterior exterior) {
        this.exterior = exterior;
    }

    public void setDoor(DoorData door) {
        this.getHandlers().setDoor(door);
    }
}