package loqor.ait.tardis.wrapper.server;

import loqor.ait.core.item.sonic.SonicSchema;
import loqor.ait.registry.DesktopRegistry;
import loqor.ait.registry.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.exterior.category.ExteriorCategorySchema;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.util.AbsoluteBlockPos;

import java.util.UUID;


public class ServerTardis extends Tardis {

	public ServerTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variantType, boolean locked) {
		super(uuid, tardis -> new ServerTardisTravel(tardis, pos), tardis -> new ServerTardisDesktop(tardis, schema), tardis -> new ServerTardisExterior(tardis, exteriorType, variantType), locked);
	}

	public void sync() {
		ServerTardisManager.getInstance().sendToSubscribers(this);
	}

	@Override
	public void init(boolean dirty) {
		// FIXME we need to put like, a special meta file in the .ait folder
		// 	that will indicate what was the version that was used to save the data.
		// 	i dont think that unlocking the default stuff for every tardis loaded is a good thing to do
		//  so it'd make sense if we could check if the data was saved with an earlier version (so it needs to unlock the default stuff)
		// 	different solution: make default stuff just be unlocked without the properties stuff
		if (!dirty) {
			ExteriorVariantRegistry.getInstance().unlock(this, Loyalty.MIN, null);
			DesktopRegistry.getInstance().unlock(this, Loyalty.MIN, null);
		}
	}

	public void unlockExterior(ExteriorVariantSchema schema) {
		PropertiesHandler.setExteriorUnlocked(getHandlers().getProperties(), schema, true);
	}

	public void unlockSonic(SonicSchema schema) {
		PropertiesHandler.setSonicUnlocked(getHandlers().getProperties(), schema, true);
	}

	public void unlockDesktop(TardisDesktopSchema schema) {
		PropertiesHandler.setSchemaUnlocked(getHandlers().getProperties(), schema, true);
	}
}