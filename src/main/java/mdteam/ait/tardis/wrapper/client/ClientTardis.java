package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.tardis.*;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.SonicHandler;
import mdteam.ait.tardis.exterior.category.ExteriorCategorySchema;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
import mdteam.ait.tardis.util.AbsoluteBlockPos;

import java.util.UUID;

// Things saved here will likely get overwritten.
public class ClientTardis extends Tardis {
	public ClientTardis(UUID uuid, AbsoluteBlockPos.Client pos, TardisDesktopSchema schema, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variantType, boolean locked) {
		super(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), tardis -> new ClientTardisExterior(tardis, exteriorType, variantType), locked);
	}

	public void setDesktop(TardisDesktop desktop) {
		this.desktop = desktop;
	}

	public void setTravel(TardisTravel travel) {
		this.travel = travel;
	}

	public void setSonic(SonicHandler sonicHandler) {
		this.handlers.setSonic(sonicHandler);
	}

	public void setExterior(TardisExterior exterior) {
		this.exterior = exterior;
	}

	public void setDoor(DoorData door) {
		this.getHandlers().setDoor(door);
	}
}