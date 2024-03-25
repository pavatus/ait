package loqor.ait.tardis.wrapper.client;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.exterior.category.ExteriorCategorySchema;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.TardisExterior;

public class ClientTardisExterior extends TardisExterior {

	public ClientTardisExterior(Tardis tardis, ExteriorCategorySchema exterior, ExteriorVariantSchema variant) {
		super(tardis, exterior, variant);
	}
}
