package mdteam.ait.registry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DesktopRegistry {
    private static final HashMap<Identifier, TardisDesktopSchema> REGISTRY = new HashMap<>();
    public static TardisDesktopSchema register(TardisDesktopSchema schema) {
        return register(schema, schema.id());
    }
    public static TardisDesktopSchema register(TardisDesktopSchema schema, Identifier id) {
        REGISTRY.put(id, schema);
        return schema;
    }
    public static TardisDesktopSchema get(Identifier id) {
        return REGISTRY.get(id);
    }
    // todo idk how well this works..
    public static TardisDesktopSchema get(int index) {
        return toList().get(index);
    }

    public static List<TardisDesktopSchema> toList() {
        return List.copyOf(REGISTRY.values());
    }
    public static TardisDesktopSchema[] toArray() {
        return (TardisDesktopSchema[]) REGISTRY.values().stream().toArray();
    }
    public static Iterator<TardisDesktopSchema> iterator() {
        return REGISTRY.values().iterator();
    }
    public static int size() {
        return REGISTRY.size();
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
    public static TardisDesktopSchema NEWBURY;
    public static TardisDesktopSchema VICTORIAN;
    public static TardisDesktopSchema WAR;
    public static TardisDesktopSchema CRYSTALLINE;
    public static TardisDesktopSchema CORAL;
    public static TardisDesktopSchema TOYOTA;

    private static void initAitDesktops() {
        // AIT's Desktops
        BOTANIST = register(new BotanistDesktop());
        CAVE = register(new CaveDesktop());
        COPPER = register(new CopperDesktop());
        DEFAULT_CAVE = register(new DefaultCaveDesktop());
        DEV = register(new DevDesktop());
        OFFICE = register(new OfficeDesktop());
        REGAL = register(new RegalDesktop());
        TYPE_40 = register(new Type40Desktop());
        NEWBURY = register(new NewburyDesktop());
        VICTORIAN = register(new VictorianDesktop());
        // WAR = register(new WarDesktop());
        PRISTINE = register(new PristineDesktop());
        CRYSTALLINE = register(new CrystallineDesktop());
        CORAL = register(new CoralDesktop());
        TOYOTA = register(new ToyotaDesktop());
    }

    public static void init() {
        REGISTRY.clear();
        initAitDesktops();

        // Reading from Datapacks
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(AITMod.MOD_ID, "desktop");
            }

            @Override
            public void reload(ResourceManager manager) {
                DesktopRegistry.clearCache();

                for(Identifier id : manager.findResources("desktop", filename -> filename.getPath().endsWith(".json")).keySet()) {
                    try(InputStream stream = manager.getResource(id).get().getInputStream()) {
                        DatapackDesktop created = DatapackDesktop.fromInputStream(stream);

                        if (created == null) {
                            stream.close();
                            continue;
                        }

                        DesktopRegistry.register(created);
                        stream.close();
                        AITMod.LOGGER.info("Loaded datapack desktop " + created.id().toString());
                    } catch(Exception e) {
                        AITMod.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
                    }
                }
            }
        });
    }

    public static void clearCache() {
        REGISTRY.clear();
        initAitDesktops(); // i know we're "clearing" but we need the AIT Desktops no?
    }
}
