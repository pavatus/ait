package loqor.ait.registry;

import loqor.ait.AITMod;
import loqor.ait.client.AITModClient;
import loqor.ait.core.item.sonic.BuiltinSonic;
import loqor.ait.core.item.sonic.DatapackSonic;
import loqor.ait.core.item.sonic.SonicSchema;
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
import java.util.function.Consumer;

import static loqor.ait.AITMod.LOGGER;

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

        AITMod.LOGGER.info("Read {} sonics from server", size);
        AITModClient.sonicModelPredicate();
    }

    public static SonicRegistry getInstance() {
        if (INSTANCE == null) {
            AITMod.LOGGER.debug("SonicRegistry was not initialized, creating a new instance");
            INSTANCE = new SonicRegistry();
        }

        return INSTANCE;
    }

    public static SonicSchema DEFAULT;

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
                SonicRegistry.this.clearCache();

                for (Identifier id : manager.findResources("sonic", filename -> filename.getPath().endsWith(".json")).keySet()) {
                    try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                        SonicSchema created = DatapackSonic.fromInputStream(stream);

                        if (created == null) {
                            stream.close();
                            continue;
                        }

                        SonicRegistry.this.register(created);
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

    @Override
    public void clearCache() {
        super.clearCache();

        DEFAULT = register(BuiltinSonic.create("prime", "Prime"));
    }

    private void giveOutSonics() {
        if (ServerTardisManager.getInstance() == null)
            return;

        for (ServerTardis tardis : ServerTardisManager.getInstance().getLookup().values()) {
            for (SonicSchema schema : SonicRegistry.getInstance().toList()) {
                tardis.unlockSonic(schema);
            }
        }
    }

    public void populateModels(Consumer<Identifier> consumer) {
        // the default sonic (prime) is ALWAYS registered
        if (REGISTRY.size() == 1) {
            /*
             * THOSE FIELDS ARE ONLY USED WHEN THE REST OF THE RESOURCES ARE NOT INITIALIZED
             * To not reload the game the second time, it's better to keep the builtin stuff semi-loaded
             * Shouldn't be a problem for custom sonics though, since they will require a resourcepack.
             */
            register(BuiltinSonic.create("copper", null));
            register(BuiltinSonic.create("mechanical", null));
            register(BuiltinSonic.create("fob", null));
            register(BuiltinSonic.create("coral", null));
            register(BuiltinSonic.create("renaissance", null));
        }

        for (SonicSchema schema : REGISTRY.values()) {
            SonicSchema.Models models = schema.models();
            models.load(consumer);

            LOGGER.info("Loading sonic '" + schema.id() + "' with models: " + models);
        }
    }
}
