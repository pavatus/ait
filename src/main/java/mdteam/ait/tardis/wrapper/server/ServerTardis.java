package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.exterior.category.ExteriorCategorySchema;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;

import java.util.UUID;


public class ServerTardis extends Tardis {

	public ServerTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variantType, boolean locked) {
		super(uuid, tardis -> new ServerTardisTravel(tardis, pos), tardis -> new ServerTardisDesktop(tardis, schema), tardis -> new ServerTardisExterior(tardis, exteriorType, variantType), locked);
	}

	public void sync() {
		ServerTardisManager.getInstance().sendToSubscribers(this);
	}
}