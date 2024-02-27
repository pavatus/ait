package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.exterior.category.ExteriorCategorySchema;
import mdteam.ait.tardis.exterior.variant.*;
import mdteam.ait.tardis.exterior.variant.box.*;
import mdteam.ait.tardis.exterior.variant.classic.*;
import mdteam.ait.tardis.exterior.variant.renegade.RenegadeDefaultVariant;
import mdteam.ait.tardis.exterior.variant.renegade.RenegadeTronVariant;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.exterior.variant.booth.*;
import mdteam.ait.tardis.exterior.variant.capsule.CapsuleDefaultVariant;
import mdteam.ait.tardis.exterior.variant.capsule.CapsuleFireVariant;
import mdteam.ait.tardis.exterior.variant.capsule.CapsuleSoulVariant;
import mdteam.ait.tardis.exterior.variant.doom.DoomVariant;
import mdteam.ait.tardis.exterior.variant.easter_head.EasterHeadDefaultVariant;
import mdteam.ait.tardis.exterior.variant.easter_head.EasterHeadFireVariant;
import mdteam.ait.tardis.exterior.variant.easter_head.EasterHeadSoulVariant;
import mdteam.ait.tardis.exterior.variant.growth.CoralGrowthVariant;
import mdteam.ait.tardis.exterior.variant.plinth.PlinthDefaultVariant;
import mdteam.ait.tardis.exterior.variant.plinth.PlinthFireVariant;
import mdteam.ait.tardis.exterior.variant.plinth.PlinthSoulVariant;
import mdteam.ait.tardis.exterior.variant.tardim.TardimDefaultVariant;
import mdteam.ait.tardis.exterior.variant.tardim.TardimFireVariant;
import mdteam.ait.tardis.exterior.variant.tardim.TardimSoulVariant;
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

// TODO - Move this over to a datapack compatible state like DesktopRegistry and then add datapack support
/*
    Example of json layout
    {
        "category": "ait:police_box",
        "texture": "ait:exterior/coral.png",
        "emission": "ait:exterior/coral_emission.png",
        "parent": "ait:toyota" // this will be what portal placement and the model of the variant
    }
 */
public class ExteriorVariantRegistry extends DatapackRegistry<ExteriorVariantSchema> {

    public static final Identifier SYNC_TO_CLIENT = new Identifier(AITMod.MOD_ID, "sync_exterior_variants");
    private static ExteriorVariantRegistry INSTANCE;
    public static ExteriorVariantSchema registerStatic(ExteriorVariantSchema schema) {
        return ExteriorVariantRegistry.getInstance().register(schema);
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
        for (ExteriorVariantSchema schema : REGISTRY.values()) {
            if (schema instanceof DatapackExterior variant) {
                if (variant.category() == null) {
                    AITMod.LOGGER.error("Exterior variant " + variant.id() + " has null category!");
                    AITMod.LOGGER.error("Temporarily returning, fix this code!!!"); // todo
                    continue;
                }
                buf.encodeAsJson(DatapackExterior.CODEC, variant);
                continue;
            }
            buf.encodeAsJson(DatapackExterior.CODEC, new DatapackExterior(schema.id(), schema.category().id(), schema.id(), DatapackExterior.DEFAULT_TEXTURE, DatapackExterior.DEFAULT_TEXTURE, false));
        }
        ServerPlayNetworking.send(player, SYNC_TO_CLIENT, buf);
    }
    @Override
    public void readFromServer(PacketByteBuf buf) {
        REGISTRY.clear();
        registerDefaults();
        int size = buf.readInt();

        DatapackExterior variant;

        for (int i = 0; i < size; i++) {
            variant = buf.decodeAsJson(DatapackExterior.CODEC);
            if (!variant.wasDatapack()) continue;
            register(variant);
        }

        AITMod.LOGGER.info("Read {} exterior variants from server", size);
    }

    public static DatapackRegistry<ExteriorVariantSchema> getInstance() {
        if (INSTANCE == null) {
            AITMod.LOGGER.debug("ExteriorVariantRegistry was not initialized, Creating a new instance");
            INSTANCE = new ExteriorVariantRegistry();
        }

        return INSTANCE;
    }

    public static Collection<ExteriorVariantSchema> withParent(ExteriorCategorySchema parent) {
        List<ExteriorVariantSchema> list = new ArrayList<>();

        for (ExteriorVariantSchema schema : ExteriorVariantRegistry.getInstance().REGISTRY.values()) {
            //AITExteriors.iterator().forEach((System.out::println));

            if (schema.category().equals(parent)) list.add(schema);
        }

        return list;
    }
    public static List<ExteriorVariantSchema> withParentToList(ExteriorCategorySchema parent) {
        List<ExteriorVariantSchema> list = new ArrayList<>();

        for (ExteriorVariantSchema schema : ExteriorVariantRegistry.getInstance().REGISTRY.values()) {
            if (schema.category().equals(parent)) list.add(schema);
        }

        return list;
    }

    public static ExteriorVariantSchema TARDIM_DEFAULT;
    public static ExteriorVariantSchema TARDIM_FIRE;
    public static ExteriorVariantSchema TARDIM_SOUL;
    public static ExteriorVariantSchema BOX_DEFAULT;
    public static ExteriorVariantSchema BOX_FIRE;
    public static ExteriorVariantSchema BOX_SOUL;
    public static ExteriorVariantSchema BOX_FUTURE;
    public static ExteriorVariantSchema BOX_CORAL;
    public static ExteriorVariantSchema BOX_TOKAMAK;
    public static ExteriorVariantSchema PRIME;
    public static ExteriorVariantSchema YETI;
    public static ExteriorVariantSchema DEFINITIVE;
    public static ExteriorVariantSchema PTORED;
    public static ExteriorVariantSchema MINT;
    public static ExteriorVariantSchema CAPSULE_DEFAULT;
    public static ExteriorVariantSchema CAPSULE_SOUL;
    public static ExteriorVariantSchema CAPSULE_FIRE;
    public static ExteriorVariantSchema BOOTH_DEFAULT;
    public static ExteriorVariantSchema BOOTH_FIRE;
    public static ExteriorVariantSchema BOOTH_SOUL;
    public static ExteriorVariantSchema BOOTH_VINTAGE;
    public static ExteriorVariantSchema BOOTH_BLUE;
    public static ExteriorVariantSchema COOB; // dont use : (
    public static ExteriorVariantSchema HEAD_DEFAULT;
    public static ExteriorVariantSchema HEAD_SOUL;
    public static ExteriorVariantSchema HEAD_FIRE;
    public static ExteriorVariantSchema CORAL_GROWTH;
    public static ExteriorVariantSchema DOOM;
    public static ExteriorVariantSchema PLINTH_DEFAULT;
    public static ExteriorVariantSchema PLINTH_SOUL;
    public static ExteriorVariantSchema PLINTH_FIRE;
    public static ExteriorVariantSchema RENEGADE_DEFAULT;
    public static ExteriorVariantSchema RENEGADE_TRON;

    private void registerDefaults() {
        // todo make this not static

        // TARDIM
        TARDIM_DEFAULT = register(new TardimDefaultVariant());
        TARDIM_FIRE = register(new TardimFireVariant());
        TARDIM_SOUL = register(new TardimSoulVariant());

        // Police Box
        BOX_DEFAULT = register(new PoliceBoxDefaultVariant());
        BOX_SOUL = register(new PoliceBoxSoulVariant());
        BOX_FIRE = register(new PoliceBoxFireVariant());
        BOX_FUTURE = register(new PoliceBoxFuturisticVariant());
        BOX_CORAL = register(new PoliceBoxCoralVariant());
        BOX_TOKAMAK = register(new PoliceBoxTokamakVariant());

        // Classic Box
        PRIME = register(new ClassicBoxPrimeVariant());
        YETI = register(new ClassicBoxYetiVariant());
        DEFINITIVE = register(new ClassicBoxDefinitiveVariant());
        PTORED = register(new ClassicBoxPtoredVariant());
        MINT = register(new ClassicBoxMintVariant());

        // Capsule
        CAPSULE_DEFAULT = register(new CapsuleDefaultVariant());
        CAPSULE_SOUL = register(new CapsuleSoulVariant());
        CAPSULE_FIRE = register(new CapsuleFireVariant());

        // Booth
        BOOTH_DEFAULT = register(new BoothDefaultVariant());
        BOOTH_FIRE = register(new BoothFireVariant());
        BOOTH_SOUL = register(new BoothSoulVariant());
        BOOTH_VINTAGE = register(new BoothVintageVariant());
        BOOTH_BLUE = register(new BoothBlueVariant());

        // funny
        // COOB = register(new RedCoobVariant()); // fixme CUBE HAS BEEN REMOVED, REPEAT, CUBE HAS BEEN REMOVED. DO NOT PANIC!!

        // Easter Head
        HEAD_DEFAULT = register(new EasterHeadDefaultVariant());
        HEAD_SOUL = register(new EasterHeadSoulVariant());
        HEAD_FIRE = register(new EasterHeadFireVariant());

        // Coral Growth
        CORAL_GROWTH = register(new CoralGrowthVariant());

        // Doom
        DOOM = register(new DoomVariant());

        // Plinth
        PLINTH_DEFAULT = register(new PlinthDefaultVariant());
        PLINTH_SOUL = register(new PlinthSoulVariant());
        PLINTH_FIRE = register(new PlinthFireVariant());

        // Renegade
        RENEGADE_DEFAULT = register(new RenegadeDefaultVariant());
        RENEGADE_TRON = register(new RenegadeTronVariant());

        System.out.println(this.toList());
    }

    // AAAAAAAAAAAAAAAAAAAAAAAAAAA SO MANY VARIABLE
    public void init() {


        // Reading from Datapacks
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(AITMod.MOD_ID, "exterior");
            }

            @Override
            public void reload(ResourceManager manager) {
                ExteriorVariantRegistry.getInstance().clearCache();
                registerDefaults();

                for(Identifier id : manager.findResources("exterior", filename -> filename.getPath().endsWith(".json")).keySet()) {
                    try(InputStream stream = manager.getResource(id).get().getInputStream()) {
                        ExteriorVariantSchema created = DatapackExterior.fromInputStream(stream);

                        if (created == null) {
                            stream.close();
                            continue;
                        }

                        ExteriorVariantRegistry.getInstance().register(created);
                        stream.close();
                        AITMod.LOGGER.info("Loaded datapack exterior variant " + created.id().toString());
                    } catch(Exception e) {
                        AITMod.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
                    }
                }

                syncToEveryone();
            }
        });
    }
}
