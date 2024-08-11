package loqor.ait.registry.impl;

import loqor.ait.core.data.datapack.DatapackDesktop;
import loqor.ait.registry.unlockable.UnlockableRegistry;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.desktops.DefaultCaveDesktop;
import loqor.ait.tardis.desktops.DevDesktop;

public class DesktopRegistry extends UnlockableRegistry<TardisDesktopSchema> {

    private static final DesktopRegistry INSTANCE = new DesktopRegistry();

    protected DesktopRegistry() {
        super(DatapackDesktop::fromInputStream, DatapackDesktop.CODEC, "desktop", true);
    }

    @Override
    public TardisDesktopSchema fallback() {
        throw new UnsupportedOperationException("No desktops registered!");
    }

    public static DesktopRegistry getInstance() {
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
