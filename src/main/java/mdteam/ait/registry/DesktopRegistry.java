package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class DesktopRegistry {
    public static final SimpleRegistry<TardisDesktopSchema> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<TardisDesktopSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "desktops"))).buildAndRegister();
    public static TardisDesktopSchema register(TardisDesktopSchema schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    public static TardisDesktopSchema BOTANIST;
    public static TardisDesktopSchema CAVE;
    public static TardisDesktopSchema COPPER;
    public static TardisDesktopSchema DEFAULT_CAVE;
    public static TardisDesktopSchema DEV;
    public static TardisDesktopSchema OFFICE;
    public static TardisDesktopSchema PRISTINE;
    public static TardisDesktopSchema REGAL;
    public static TardisDesktopSchema TYPE_40;
    public static TardisDesktopSchema VICTORIAN;
    public static TardisDesktopSchema WAR;

    public static void init() {
        BOTANIST = register(new BotanistDesktop());
        CAVE = register(new CaveDesktop());
        COPPER = register(new CopperDesktop());
        DEFAULT_CAVE = register(new DefaultCaveDesktop());
        DEV = register(new DevDesktop());
        OFFICE = register(new OfficeDesktop());
        REGAL = register(new RegalDesktop());
        TYPE_40 = register(new Type40Desktop());
        VICTORIAN = register(new VictorianDesktop());
        WAR = register(new WarDesktop());
        PRISTINE = register(new PristineDesktop());
    }
}
