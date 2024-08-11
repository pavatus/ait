package loqor.ait.registry.impl.door;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.tardis.door.*;

public class ClientDoorRegistry {
    public static final SimpleRegistry<ClientDoorSchema> REGISTRY = FabricRegistryBuilder
            .createSimple(RegistryKey.<ClientDoorSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "client_door")))
            .buildAndRegister();

    public static ClientDoorSchema register(ClientDoorSchema schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    /**
     * Will return the clients version of a servers door variant
     *
     * @param parent
     * @return the first variant found as there should only be one client version
     */
    public static ClientDoorSchema withParent(DoorSchema parent) {
        for (ClientDoorSchema schema : REGISTRY) {
            if (schema.parent().equals(parent))
                return schema;
        }

        return null;
    }

    public static ClientDoorSchema TARDIM;
    public static ClientDoorSchema CLASSIC;
    public static ClientDoorSchema BOOTH;
    public static ClientDoorSchema CAPSULE;
    public static ClientDoorSchema BOX;
    public static ClientDoorSchema BOX_CORAL;
    public static ClientDoorSchema BOX_TOKAMAK;
    public static ClientDoorSchema HEAD;
    public static ClientDoorSchema GROWTH;
    public static ClientDoorSchema DOOM;
    public static ClientDoorSchema PLINTH;
    public static ClientDoorSchema RENEGADE;
    public static ClientDoorSchema BOOKSHELF;
    public static ClientDoorSchema GEOMETRIC;

    public static void init() {
        TARDIM = register(new ClientTardimDoorVariant());
        CLASSIC = register(new ClientClassicDoorVariant());
        BOOTH = register(new ClientBoothDoorVariant());
        CAPSULE = register(new ClientCapsuleDoorVariant());
        BOX = register(new ClientPoliceBoxDoorVariant());
        BOX_CORAL = register(new ClientPoliceBoxCoralDoorVariant());
        BOX_TOKAMAK = register(new ClientPoliceBoxTokamakDoorVariant());
        HEAD = register(new ClientEasterHeadDoorVariant());
        GROWTH = register(new ClientGrowthDoorVariant());
        DOOM = register(new ClientDoomDoorVariant());
        PLINTH = register(new ClientPlinthDoorVariant());
        RENEGADE = register(new ClientRenegadeDoorVariant());
        BOOKSHELF = register(new ClientBookshelfDoorVariant());
        GEOMETRIC = register(new ClientGeometricDoorVariant());
    }
}
