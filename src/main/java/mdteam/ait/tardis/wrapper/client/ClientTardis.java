package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.tardis.ExteriorEnum;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;

import java.util.UUID;

// Things saved here will likely get overwritten.
public class ClientTardis extends Tardis {
    public ClientTardis(UUID uuid, AbsoluteBlockPos.Client pos, TardisDesktopSchema schema, ExteriorEnum exteriorType, ExteriorVariantSchema variantType, boolean locked) {
        super(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), tardis -> new ClientTardisExterior(tardis, exteriorType, variantType), locked);
    }
}