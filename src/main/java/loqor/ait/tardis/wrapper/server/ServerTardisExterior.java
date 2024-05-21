package loqor.ait.tardis.wrapper.server;

import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.base.TardisTickable;

public class ServerTardisExterior extends TardisExterior implements TardisTickable {

	public ServerTardisExterior(ExteriorVariantSchema variant) {
		super(variant);
	}

	@Override
	public void setType(ExteriorCategorySchema exterior) {
		super.setType(exterior);
		this.sync();
	}

	@Override
	public void setVariant(ExteriorVariantSchema variant) {
		super.setVariant(variant);
		this.sync();
	}
}
