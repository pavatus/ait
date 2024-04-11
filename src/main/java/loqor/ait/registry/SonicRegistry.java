package loqor.ait.registry;

import loqor.ait.AITMod;
import loqor.ait.core.item.sonic.DatapackSonic;
import loqor.ait.core.item.sonic.SonicSchema;
import loqor.ait.core.item.sonic.impl.BuiltInSonic;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.InputStream;

public class SonicRegistry extends DatapackRegistry<SonicSchema> {

    public static final Identifier SYNC_TO_CLIENT = new Identifier(AITMod.MOD_ID, "sync_sonics");
    private static SonicRegistry INSTANCE;

    public void syncToEveryone() {
        if (TardisUtil.getServer() == null) return;

        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            syncToClient(player);
        }
    }

    public void syncToClient(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(REGISTRY.size());

        for (SonicSchema schema : REGISTRY.values()) {
            buf.encodeAsJson(DatapackSonic.CODEC, schema);
        }

        ServerPlayNetworking.send(player, SYNC_TO_CLIENT, buf);
    }

    public void readFromServer(PacketByteBuf buf) {
        REGISTRY.clear();
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            register(buf.decodeAsJson(DatapackSonic.CODEC));
        }

        AITMod.LOGGER.info("Read {} desktops from server", size);
    }

    public static SonicRegistry getInstance() {
        if (INSTANCE == null) {
            AITMod.LOGGER.debug("SonicRegistry was not initialized, creating a new instance");
            INSTANCE = new SonicRegistry();
        }

        return INSTANCE;
    }

    // The reason why those 2 are set in stone, is because the PRIME sonic is the default and the MECHANICAL one requires special rendering handling
    public static SonicSchema PRIME;
    public static SonicSchema MECHANICAL;

    private void initSonics() {
        PRIME = register(new BuiltInSonic("prime", "Prime", 0));
        MECHANICAL = register(new BuiltInSonic("mechanical", "Mechanical", 1));
    }

    public int indexOf(SonicSchema schema) {
        return this.toList().indexOf(schema);
    }

    public void init() {
        super.init();

        // Reading from datapacks
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(AITMod.MOD_ID, "sonic");
            }

            @Override
            public void reload(ResourceManager manager) {
                DesktopRegistry.getInstance().clearCache();

                for (Identifier id : manager.findResources("sonic", filename -> filename.getPath().endsWith(".json")).keySet()) {
                    try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                        SonicSchema created = DatapackSonic.fromInputStream(stream);

                        if (created == null) {
                            stream.close();
                            continue;
                        }

                        SonicRegistry.getInstance().register(created);
                        stream.close();

                        AITMod.LOGGER.info("Loaded datapack sonic " + created.id().toString());
                    } catch (Exception e) {
                        AITMod.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
                    }
                }

                syncToEveryone();
                giveOutSonics();
            }
        });
    }

    public void clearCache() {
        REGISTRY.clear();
        initSonics();
    }

    /**
     * Unlocks all desktops for all tardises, usually when someone calls /reload as this wont be ran when the world starts
     * bad but oh well
     */
    private void giveOutSonics() {
        if (ServerTardisManager.getInstance() == null) return;

        for (ServerTardis tardis : ServerTardisManager.getInstance().getLookup().values()) {
            for (SonicSchema schema : SonicRegistry.getInstance().toList()) {
                tardis.unlockSonic(schema);
            }
        }
    }
}
