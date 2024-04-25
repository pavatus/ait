package loqor.ait.tardis.desktops;

import loqor.ait.AITMod;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.desktops.textures.DesktopPreviewTexture;
import loqor.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class DevDesktop extends TardisDesktopSchema {

	public DevDesktop() {
		super(new Identifier(AITMod.MOD_ID, "dev"), new DesktopPreviewTexture(
				DesktopPreviewTexture.pathFromDesktopId(new Identifier(AITMod.MOD_ID, "dev")),
				800,
				800
		), new Loyalty(Loyalty.Type.PILOT));
	}

	@Override
	public boolean freebie() {
		return false;
	}
}
