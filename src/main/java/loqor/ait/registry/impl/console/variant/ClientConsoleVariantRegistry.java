package loqor.ait.registry.impl.console.variant;

import org.joml.Vector3f;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.data.datapack.DatapackConsole;
import loqor.ait.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.console.variant.alnico.client.ClientAlnicoVariant;
import loqor.ait.data.schema.console.variant.alnico.client.ClientBlueAlnicoVariant;
import loqor.ait.data.schema.console.variant.copper.client.ClientCopperVariant;
import loqor.ait.data.schema.console.variant.coral.client.ClientBlueCoralVariant;
import loqor.ait.data.schema.console.variant.coral.client.ClientGreenCoralVariant;
import loqor.ait.data.schema.console.variant.coral.client.ClientWhiteCoralVariant;
import loqor.ait.data.schema.console.variant.hartnell.client.ClientHartnellVariant;
import loqor.ait.data.schema.console.variant.hartnell.client.ClientKeltHartnellVariant;
import loqor.ait.data.schema.console.variant.hartnell.client.ClientMintHartnellVariant;
import loqor.ait.data.schema.console.variant.hartnell.client.ClientWoodenHartnellVariant;
import loqor.ait.data.schema.console.variant.steam.client.ClientSteamCherryVariant;
import loqor.ait.data.schema.console.variant.steam.client.ClientSteamGildedVariant;
import loqor.ait.data.schema.console.variant.steam.client.ClientSteamSteelVariant;
import loqor.ait.data.schema.console.variant.steam.client.ClientSteamVariant;
import loqor.ait.data.schema.console.variant.toyota.client.ClientToyotaBlueVariant;
import loqor.ait.data.schema.console.variant.toyota.client.ClientToyotaLegacyVariant;
import loqor.ait.data.schema.console.variant.toyota.client.ClientToyotaVariant;
import loqor.ait.registry.datapack.DatapackRegistry;

public class ClientConsoleVariantRegistry extends DatapackRegistry<ClientConsoleVariantSchema> {
    private static ClientConsoleVariantRegistry INSTANCE;

    public static DatapackRegistry<ClientConsoleVariantSchema> getInstance() {
        if (INSTANCE == null) {
            AITMod.LOGGER.info("ClientConsoleVariantRegistry was not initialized, Creating a new instance");
            INSTANCE = new ClientConsoleVariantRegistry();
        }

        return INSTANCE;
    }

    /**
     * Will return the clients version of a servers door variant
     *
     * @return the first variant found as there should only be one client version
     */
    public static ClientConsoleVariantSchema withParent(ConsoleVariantSchema parent) {
        for (ClientConsoleVariantSchema schema : ClientConsoleVariantRegistry.getInstance().toList()) {
            if (schema.parent() == null)
                continue;

            if (schema.parent().id().equals(parent.id()))
                return schema;
        }

        return null;
    }

    public static ClientConsoleVariantSchema withSameParent(ClientConsoleVariantSchema schema) {
        for (ClientConsoleVariantSchema s : ClientConsoleVariantRegistry.getInstance().toList()) {
            if (s.parent() == null)
                continue;

            if (schema.equals(s))
                continue;

            if (s.parent().parent().id().equals(schema.parent().parent().id()))
                return s;
        }

        return null;
    }

    @Override
    public ClientConsoleVariantSchema fallback() {
        return null;
    }

    @Override
    public void syncToClient(ServerPlayerEntity player) {
    }

    @Override
    public void readFromServer(PacketByteBuf buf) {
        int size = buf.readInt();

        for (int i = 0; i < size; i++) {
            this.register(convertDatapack(buf.decodeAsJson(DatapackConsole.CODEC)));
        }

        AITMod.LOGGER.info("Read {} console variants from server", size);
    }

    public static ClientConsoleVariantSchema convertDatapack(DatapackConsole variant) {
        if (!variant.wasDatapack())
            return convertNonDatapack(variant);

        return new ClientConsoleVariantSchema(variant.id()) {
            private final ClientConsoleVariantSchema parentVariant = withSameParent(this);

            @Override
            public Identifier texture() {
                return variant.texture();
            }

            @Override
            public Identifier emission() {
                return variant.emission();
            }

            @Override
            public ConsoleModel model() {
                return parentVariant.model();
            }

            @Override
            public float[] sonicItemRotations() {
                if (variant.sonicRotation() == null) {
                    return parentVariant.sonicItemRotations();
                }

                float[] result = new float[2];

                for (int i = 0; i < 2; i++) {
                    result[i] = variant.sonicRotation().get(i);
                }

                return result;
            }

            @Override
            public Vector3f sonicItemTranslations() {
                if (variant.sonicTranslation() == null) {
                    return parentVariant.sonicItemTranslations();
                }

                return variant.sonicTranslation();
            }
        };
    }

    private static ClientConsoleVariantSchema convertNonDatapack(DatapackConsole variant) {
        if (variant.wasDatapack())
            return convertDatapack(variant);

        return getInstance().get(variant.id());
    }

    public static ClientConsoleVariantSchema HARTNELL;
    public static ClientConsoleVariantSchema HARTNELL_WOOD;
    public static ClientConsoleVariantSchema HARTNELL_KELT;
    public static ClientConsoleVariantSchema HARTNELL_MINT;
    public static ClientConsoleVariantSchema CORAL_GREEN;
    public static ClientConsoleVariantSchema CORAL_BLUE;
    public static ClientConsoleVariantSchema CORAL_WHITE;
    public static ClientConsoleVariantSchema TOYOTA;
    public static ClientConsoleVariantSchema TOYOTA_BLUE;
    public static ClientConsoleVariantSchema TOYOTA_LEGACY;
    public static ClientConsoleVariantSchema ALNICO;
    public static ClientConsoleVariantSchema ALNICO_BLUE;
    public static ClientConsoleVariantSchema STEAM;
    public static ClientConsoleVariantSchema STEAM_CHERRY;
    public static ClientConsoleVariantSchema STEAM_STEEL;
    public static ClientConsoleVariantSchema STEAM_GILDED;
    public static ClientConsoleVariantSchema HUDOLIN;
    public static ClientConsoleVariantSchema COPPER;

    @Override
    public void onClientInit() {
        // Hartnell variants
        HARTNELL = register(new ClientHartnellVariant());
        HARTNELL_KELT = register(new ClientKeltHartnellVariant());
        HARTNELL_MINT = register(new ClientMintHartnellVariant());
        HARTNELL_WOOD = register(new ClientWoodenHartnellVariant());

        // Coral variants
        CORAL_GREEN = register(new ClientGreenCoralVariant());
        CORAL_BLUE = register(new ClientBlueCoralVariant());
        CORAL_WHITE = register(new ClientWhiteCoralVariant());

        // Toyota variants
        TOYOTA = register(new ClientToyotaVariant());
        TOYOTA_BLUE = register(new ClientToyotaBlueVariant());
        TOYOTA_LEGACY = register(new ClientToyotaLegacyVariant());

        // Alnico variants
        ALNICO = register(new ClientAlnicoVariant());
        ALNICO_BLUE = register(new ClientBlueAlnicoVariant());

        // Steam variants
        STEAM = register(new ClientSteamVariant());
        STEAM_CHERRY = register(new ClientSteamCherryVariant());
        STEAM_STEEL = register(new ClientSteamSteelVariant());
        STEAM_GILDED = register(new ClientSteamGildedVariant());

        // Hudolin variants
        // HUDOLIN = register(new ClientHudolinVariant());

        // Copper variants
        COPPER = register(new ClientCopperVariant());
    }
}
