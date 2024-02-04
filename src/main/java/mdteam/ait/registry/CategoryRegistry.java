package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.exterior.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class CategoryRegistry {
    public static final SimpleRegistry<ExteriorCategory> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<ExteriorCategory>ofRegistry(new Identifier(AITMod.MOD_ID, "exterior"))).buildAndRegister();
    public static ExteriorCategory register(ExteriorCategory schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    // todo move to an "AITExteriors" type thing, same for all other registries. this is fine for now cus i cant b bothered
    public static ExteriorCategory CLASSIC;
    public static ExteriorCategory CAPSULE;
    public static ExteriorCategory POLICE_BOX;
    public static ExteriorCategory TARDIM;
    public static ExteriorCategory CUBE; // dont use
    public static ExteriorCategory BOOTH;
    public static ExteriorCategory EASTER_HEAD;
    public static ExteriorCategory CORAL_GROWTH;
    public static ExteriorCategory DOOM;
    public static ExteriorCategory PLINTH;

    public static void init() {
        CLASSIC = register(new ClassicCategory());
        CAPSULE = register(new CapsuleCategory());
        POLICE_BOX = register(new PoliceBoxCategory());
        TARDIM = register(new TardimCategory());
        // CUBE = register(new CubeExterior()); // fixme how could i do this, remove the cube instead of fixing a bug : (
        BOOTH = register(new BoothCategory());
        EASTER_HEAD = register(new EasterHeadCategory());
        CORAL_GROWTH = register(new GrowthCategory());
        DOOM = register(new DoomCategory());
        PLINTH = register(new PlinthCategory());
    }
}
