package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.type.ConsoleTypeSchema;
import mdteam.ait.tardis.console.variant.*;
import mdteam.ait.tardis.console.variant.alnico.AlnicoVariant;
import mdteam.ait.tardis.console.variant.coral.BlueCoralVariant;
import mdteam.ait.tardis.console.variant.coral.CoralVariant;
import mdteam.ait.tardis.console.variant.coral.WhiteCoralVariant;
import mdteam.ait.tardis.console.variant.hartnell.HartnellVariant;
import mdteam.ait.tardis.console.variant.hartnell.KeltHartnellVariant;
import mdteam.ait.tardis.console.variant.hartnell.WoodenHartnellVariant;
import mdteam.ait.tardis.console.variant.steam.SteamCherryVariant;
import mdteam.ait.tardis.console.variant.steam.SteamVariant;
import mdteam.ait.tardis.console.variant.toyota.ToyotaBlueVariant;
import mdteam.ait.tardis.console.variant.toyota.ToyotaLegacyVariant;
import mdteam.ait.tardis.console.variant.toyota.ToyotaVariant;
import mdteam.ait.tardis.exterior.variant.DatapackExterior;
import mdteam.ait.tardis.util.TardisUtil;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConsoleVariantRegistry extends DatapackRegistry<ConsoleVariantSchema> {
    public static final Identifier SYNC_TO_CLIENT = new Identifier(AITMod.MOD_ID, "sync_console_variants");
    private static ConsoleVariantRegistry INSTANCE;
    public static ConsoleVariantSchema registerStatic(ConsoleVariantSchema schema) {
        return ConsoleVariantRegistry.getInstance().register(schema);
    }

    public void syncToEveryone() {
        if (TardisUtil.getServer() == null) return;

        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            syncToClient(player);
        }
    }

    @Override
    public void syncToClient(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(REGISTRY.size());
        for (ConsoleVariantSchema schema : REGISTRY.values()) {
            if (schema instanceof DatapackConsole variant) {
                buf.encodeAsJson(DatapackConsole.CODEC, variant);
                continue;
            }
            if (schema.parent() == null) {
                AITMod.LOGGER.error("Console variant " + schema.id() + " has null category!");
                AITMod.LOGGER.error("Temporarily returning, fix this code!!!"); // todo
                continue;
            }
            buf.encodeAsJson(DatapackConsole.CODEC, new DatapackConsole(schema.id(), schema.parent().id(), DatapackExterior.DEFAULT_TEXTURE, DatapackExterior.DEFAULT_TEXTURE, false));
        }
        ServerPlayNetworking.send(player, SYNC_TO_CLIENT, buf);
    }
    @Override
    public void readFromServer(PacketByteBuf buf) {
        REGISTRY.clear();
        registerDefaults();
        int size = buf.readInt();

        DatapackConsole variant;

        for (int i = 0; i < size; i++) {
            variant = buf.decodeAsJson(DatapackConsole.CODEC);
            if (!variant.wasDatapack()) continue;
            register(variant);
        }

        AITMod.LOGGER.info("Read {} console variants from server", size);
    }

    public static DatapackRegistry<ConsoleVariantSchema> getInstance() {
        if (INSTANCE == null) {
            AITMod.LOGGER.debug("ConsoleVariantRegistry was not initialized, Creating a new instance");
            INSTANCE = new ConsoleVariantRegistry();
        }

        return INSTANCE;
    }

    public static Collection<ConsoleVariantSchema> withParent(ConsoleTypeSchema parent) {
        List<ConsoleVariantSchema> list = new ArrayList<>();

        for (ConsoleVariantSchema schema : ConsoleVariantRegistry.getInstance().REGISTRY.values()) {
            if (schema.parent().equals(parent)) list.add(schema);
        }

        return list;
    }
    public static List<ConsoleVariantSchema> withParentToList(ConsoleTypeSchema parent) {
        List<ConsoleVariantSchema> list = new ArrayList<>();

        for (ConsoleVariantSchema schema : ConsoleVariantRegistry.getInstance().REGISTRY.values()) {
            if (schema.parent().equals(parent)) list.add(schema);
        }

        return list;
    }

    public static ConsoleVariantSchema HARTNELL;
    public static ConsoleVariantSchema HARTNELL_WOOD;
    public static ConsoleVariantSchema HARTNELL_KELT;
    public static ConsoleVariantSchema CORAL;
    public static ConsoleVariantSchema CORAL_BLUE;
    public static ConsoleVariantSchema CORAL_WHITE;
    public static ConsoleVariantSchema COPPER;
    public static ConsoleVariantSchema TOYOTA;
    public static ConsoleVariantSchema TOYOTA_BLUE;
    public static ConsoleVariantSchema TOYOTA_LEGACY;
    public static ConsoleVariantSchema ALNICO;
    public static ConsoleVariantSchema STEAM;
    public static ConsoleVariantSchema STEAM_CHERRY;

    private static void registerDefaults() {
        // Hartnell variants
        HARTNELL = registerStatic(new HartnellVariant());
        HARTNELL_KELT = registerStatic(new KeltHartnellVariant());
        HARTNELL_WOOD = registerStatic(new WoodenHartnellVariant()); // fixme this texture is awful - make tright remake it

        // Coral variants
        CORAL = registerStatic(new CoralVariant());
        CORAL_BLUE = registerStatic(new BlueCoralVariant());
        CORAL_WHITE = registerStatic(new WhiteCoralVariant());

        // Copper variants
        //COPPER = register(new CopperVariant());

        // Toyota variants
        TOYOTA = registerStatic(new ToyotaVariant());
        TOYOTA_BLUE = registerStatic(new ToyotaBlueVariant());
        TOYOTA_LEGACY = registerStatic(new ToyotaLegacyVariant());

        // Alnico variants
        ALNICO = registerStatic(new AlnicoVariant());

        // Steam variants
        STEAM = registerStatic(new SteamVariant());
        STEAM_CHERRY = registerStatic(new SteamCherryVariant());
    }

    // AAAAAAAAAAAAAAAAAAAAAAAAAAA SO MANY VARIABLE
    public void init() {


        // Reading from Datapacks
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(AITMod.MOD_ID, "console");
            }

            @Override
            public void reload(ResourceManager manager) {
                ConsoleVariantRegistry.getInstance().clearCache();
                registerDefaults();

                for(Identifier id : manager.findResources("console", filename -> filename.getPath().endsWith(".json")).keySet()) {
                    try(InputStream stream = manager.getResource(id).get().getInputStream()) {
                        ConsoleVariantSchema created = DatapackConsole.fromInputStream(stream);

                        if (created == null) {
                            stream.close();
                            continue;
                        }

                        ConsoleVariantRegistry.getInstance().register(created);
                        stream.close();
                        AITMod.LOGGER.info("Loaded datapack console variant " + created.id().toString());
                    } catch(Exception e) {
                        AITMod.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
                    }
                }

                syncToEveryone();
            }
        });
    }
}
