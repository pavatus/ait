package loqor.ait.registry.impl;

import loqor.ait.AITMod;
import loqor.ait.registry.unlockable.UnlockableRegistry;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.core.data.datapack.DatapackDesktop;
import loqor.ait.tardis.desktops.DefaultCaveDesktop;
import loqor.ait.tardis.desktops.DevDesktop;

public class DesktopRegistry extends UnlockableRegistry<TardisDesktopSchema> {
	private static DesktopRegistry INSTANCE;

	protected DesktopRegistry() {
		super(DatapackDesktop::fromInputStream, DatapackDesktop.CODEC, "desktop", true);
	}

	@Override
	public TardisDesktopSchema fallback() {
		throw new UnsupportedOperationException("No desktops registered!");
	}

	public static DesktopRegistry getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.debug("DesktopRegistry was not initialized, Creating a new instance");
			INSTANCE = new DesktopRegistry();
		}

		return INSTANCE;
	}

	public static TardisDesktopSchema DEFAULT_CAVE;
	public static TardisDesktopSchema DEV;

	@Override
	protected void defaults() {
		DEFAULT_CAVE = register(new DefaultCaveDesktop());
		DEV = register(new DevDesktop());
	}
}
