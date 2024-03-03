package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class DevDesktop extends TardisDesktopSchema {

	public DevDesktop() {
		super(new Identifier(AITMod.MOD_ID, "dev"), new DesktopPreviewTexture(
				DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "dev")),
				800,
				800
		));
	}

	@Override
	public boolean freebie() {
		return false;
	}
}
