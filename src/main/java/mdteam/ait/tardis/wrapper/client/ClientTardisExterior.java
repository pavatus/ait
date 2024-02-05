package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.exterior.category.ExteriorCategory;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;

public class ClientTardisExterior extends TardisExterior {

    public ClientTardisExterior(Tardis tardis, ExteriorCategory exterior, ExteriorVariantSchema variant) {
        super(tardis, exterior, variant);
    }
}
