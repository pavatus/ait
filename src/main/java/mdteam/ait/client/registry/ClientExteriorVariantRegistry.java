package mdteam.ait.client.registry;

import mdteam.ait.AITMod;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.registry.exterior.impl.booth.ClientBoothDefaultVariant;
import mdteam.ait.client.registry.exterior.impl.booth.ClientBoothFireVariant;
import mdteam.ait.client.registry.exterior.impl.booth.ClientBoothSoulVariant;
import mdteam.ait.client.registry.exterior.impl.box.ClientPoliceBoxDefaultVariant;
import mdteam.ait.client.registry.exterior.impl.box.ClientPoliceBoxFireVariant;
import mdteam.ait.client.registry.exterior.impl.box.ClientPoliceBoxFuturisticVariant;
import mdteam.ait.client.registry.exterior.impl.box.ClientPoliceBoxSoulVariant;
import mdteam.ait.client.registry.exterior.impl.capsule.ClientCapsuleDefaultVariant;
import mdteam.ait.client.registry.exterior.impl.capsule.ClientCapsuleFireVariant;
import mdteam.ait.client.registry.exterior.impl.capsule.ClientCapsuleSoulVariant;
import mdteam.ait.client.registry.exterior.impl.classic.ClientClassicBoxDefinitiveVariant;
import mdteam.ait.client.registry.exterior.impl.classic.ClientClassicBoxPrimeVariant;
import mdteam.ait.client.registry.exterior.impl.classic.ClientClassicBoxPtoredVariant;
import mdteam.ait.client.registry.exterior.impl.classic.ClientClassicBoxYetiVariant;
import mdteam.ait.client.registry.exterior.impl.easter_head.ClientEasterHeadDefaultVariant;
import mdteam.ait.client.registry.exterior.impl.easter_head.ClientEasterHeadFireVariant;
import mdteam.ait.client.registry.exterior.impl.easter_head.ClientEasterHeadSoulVariant;
import mdteam.ait.client.registry.exterior.impl.growth.ClientGrowthVariant;
import mdteam.ait.client.registry.exterior.impl.tardim.ClientTardimDefaultVariant;
import mdteam.ait.client.registry.exterior.impl.tardim.ClientTardimFireVariant;
import mdteam.ait.client.registry.exterior.impl.tardim.ClientTardimSoulVariant;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ClientExteriorVariantRegistry {
    public static final SimpleRegistry<ClientExteriorVariantSchema> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<ClientExteriorVariantSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "client_exterior_variant"))).buildAndRegister();
    public static ClientExteriorVariantSchema register(ClientExteriorVariantSchema schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    /**
     * Will return the clients version of a servers door variant
     * @param parent
     * @return the first variant found as there should only be one client version
     */
    public static ClientExteriorVariantSchema withParent(ExteriorVariantSchema parent) {
        for (ClientExteriorVariantSchema schema : REGISTRY) {
            if (schema.parent().equals(parent)) return schema;
        }

        return null;
    }

    public static ClientExteriorVariantSchema TARDIM_DEFAULT;
    public static ClientExteriorVariantSchema TARDIM_FIRE;
    public static ClientExteriorVariantSchema TARDIM_SOUL;
    public static ClientExteriorVariantSchema BOX_DEFAULT;
    public static ClientExteriorVariantSchema BOX_FIRE;
    public static ClientExteriorVariantSchema BOX_SOUL;
    public static ClientExteriorVariantSchema BOX_FUTURE;
    public static ClientExteriorVariantSchema PRIME;
    public static ClientExteriorVariantSchema YETI;
    public static ClientExteriorVariantSchema DEFINITIVE;
    public static ClientExteriorVariantSchema PTORED;
    public static ClientExteriorVariantSchema CAPSULE_DEFAULT;
    public static ClientExteriorVariantSchema CAPSULE_SOUL;
    public static ClientExteriorVariantSchema CAPSULE_FIRE;
    public static ClientExteriorVariantSchema BOOTH_DEFAULT;
    public static ClientExteriorVariantSchema BOOTH_FIRE;
    public static ClientExteriorVariantSchema BOOTH_SOUL;
    public static ClientExteriorVariantSchema COOB; // dont use : (
    public static ClientExteriorVariantSchema HEAD_DEFAULT;
    public static ClientExteriorVariantSchema HEAD_SOUL;
    public static ClientExteriorVariantSchema HEAD_FIRE;
    public static ClientExteriorVariantSchema CORAL_GROWTH;

    // AAAAAAAAAAAAAAAAAAAAAAAAAAA SO MANY VARIABLE
    public static void init() {
        // TARDIM
        TARDIM_DEFAULT = register(new ClientTardimDefaultVariant());
        TARDIM_FIRE = register(new ClientTardimFireVariant());
        TARDIM_SOUL = register(new ClientTardimSoulVariant());

        // Police Box
        BOX_DEFAULT = register(new ClientPoliceBoxDefaultVariant());
        BOX_SOUL = register(new ClientPoliceBoxSoulVariant());
        BOX_FIRE = register(new ClientPoliceBoxFireVariant());
        BOX_FUTURE = register(new ClientPoliceBoxFuturisticVariant());

        // Classic Box
        PRIME = register(new ClientClassicBoxPrimeVariant());
        YETI = register(new ClientClassicBoxYetiVariant());
        DEFINITIVE = register(new ClientClassicBoxDefinitiveVariant());
        PTORED = register(new ClientClassicBoxPtoredVariant());

        // Capsule
        CAPSULE_DEFAULT = register(new ClientCapsuleDefaultVariant());
        CAPSULE_SOUL = register(new ClientCapsuleSoulVariant());
        CAPSULE_FIRE = register(new ClientCapsuleFireVariant());

        // Booth
        BOOTH_DEFAULT = register(new ClientBoothDefaultVariant());
        BOOTH_FIRE = register(new ClientBoothFireVariant());
        BOOTH_SOUL = register(new ClientBoothSoulVariant());

        // funny
        // COOB = register(new RedCoobVariant()); // fixme CUBE HAS BEEN REMOVED, REPEAT, CUBE HAS BEEN REMOVED. DO NOT PANIC!!

        // Easter Head
        HEAD_DEFAULT = register(new ClientEasterHeadDefaultVariant());
        HEAD_SOUL = register(new ClientEasterHeadSoulVariant());
        HEAD_FIRE = register(new ClientEasterHeadFireVariant());

        // Coral
        CORAL_GROWTH = register(new ClientGrowthVariant());
    }
}
