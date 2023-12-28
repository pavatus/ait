package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.*;
import mdteam.ait.tardis.exterior.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ExteriorRegistry {
    public static final SimpleRegistry<ExteriorSchema> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<ExteriorSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "exterior"))).buildAndRegister();
    public static ExteriorSchema register(ExteriorSchema schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    // todo move to an "AITExteriors" type thing, same for all other registries. this is fine for now cus i cant b bothered
    public static ExteriorSchema CLASSIC;
    public static ExteriorSchema CAPSULE;
    public static ExteriorSchema POLICE_BOX;
    public static ExteriorSchema TARDIM;
    public static ExteriorSchema CUBE;
    public static ExteriorSchema BOOTH;
    public static ExteriorSchema EASTER_HEAD;

    public static void init() {
        CLASSIC = register(new ClassicExterior());
        CAPSULE = register(new CapsuleExterior());
        POLICE_BOX = register(new PoliceBoxExterior());
        TARDIM = register(new TardimExterior());
        CUBE = register(new CubeExterior());
        BOOTH = register(new BoothExterior());
        EASTER_HEAD = register(new EasterHeadExterior());
    }
}
