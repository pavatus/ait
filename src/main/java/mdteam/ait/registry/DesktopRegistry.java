package mdteam.ait.registry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.*;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DesktopRegistry {
    public static final Identifier SYNC_TO_CLIENT = new Identifier(AITMod.MOD_ID, "sync_desktops");
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
    public static ArrayList<TardisDesktopSchema> toArrayList() {
        return new ArrayList<>(REGISTRY.values());
    }
    public static Iterator<TardisDesktopSchema> iterator() {
        return REGISTRY.values().iterator();
    }
    public static int size() {
        return REGISTRY.size();
    }

    public static void syncToEveryone() {
        if (TardisUtil.getServer() == null) return;

        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            syncToClient(player);
        }
    }

    public static void syncToClient(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(REGISTRY.size());
        for (TardisDesktopSchema schema : REGISTRY.values()) {
            buf.encodeAsJson(DatapackDesktop.CODEC, schema);
        }
        ServerPlayNetworking.send(player, SYNC_TO_CLIENT, buf);
    }
    public static void readFromServer(PacketByteBuf buf) {
        REGISTRY.clear();
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            register(buf.decodeAsJson(DatapackDesktop.CODEC));
        }
    }

    public static TardisDesktopSchema DEFAULT_CAVE;
    public static TardisDesktopSchema DEV;

    private static void initAitDesktops() {
        // AIT's Default (non-datapack) Desktops
        DEFAULT_CAVE = register(new DefaultCaveDesktop());
        DEV = register(new DevDesktop());
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
                        TardisDesktopSchema created = DatapackDesktop.fromInputStream(stream);

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

                syncToEveryone();
            }
        });
    }

    public static void clearCache() {
        REGISTRY.clear();
        initAitDesktops(); // i know we're "clearing" but we need the AIT Desktops no?
    }
}
