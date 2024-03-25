package loqor.ait.tardis.wrapper.server;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.exterior.category.ExteriorCategorySchema;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.util.AbsoluteBlockPos;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

import java.util.UUID;


public class ServerTardis extends Tardis {

	public ServerTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variantType, boolean locked) {
		super(uuid, tardis -> new ServerTardisTravel(tardis, pos), tardis -> new ServerTardisDesktop(tardis, schema), tardis -> new ServerTardisExterior(tardis, exteriorType, variantType), locked);
	}

	public void sync() {
		ServerTardisManager.getInstance().sendToSubscribers(this);
	}
}