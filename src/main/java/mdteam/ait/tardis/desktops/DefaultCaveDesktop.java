package mdteam.ait.tardis.desktops;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.textures.DesktopPreviewTexture;
import net.minecraft.util.Identifier;

public class DefaultCaveDesktop extends TardisDesktopSchema {

	public DefaultCaveDesktop() {
		super(new Identifier(AITMod.MOD_ID, "default_cave"), new DesktopPreviewTexture(DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "cave"))));
	}

	@Override // fixme doesnt work
	public boolean freebie() {
		return false;
	}
}
