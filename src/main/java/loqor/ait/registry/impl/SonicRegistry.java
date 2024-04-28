package loqor.ait.registry.impl;

import loqor.ait.AITMod;
import loqor.ait.client.AITModClient;
import loqor.ait.core.data.schema.BuiltinSonic;
import loqor.ait.core.data.datapack.DatapackSonic;
import loqor.ait.core.data.schema.SonicSchema;
import loqor.ait.registry.unlockable.UnlockableRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static loqor.ait.AITMod.LOGGER;

public class SonicRegistry extends UnlockableRegistry<SonicSchema> {

    private static SonicRegistry INSTANCE;

    protected SonicRegistry() {
        super(DatapackSonic::fromInputStream, DatapackSonic.CODEC, "sonic", true);
    }

    @Override
    public SonicSchema fallback() {
        return SonicRegistry.DEFAULT;
    }

    public void readFromServer(PacketByteBuf buf) {
        super.readFromServer(buf);
        AITModClient.sonicModelPredicate();
    }

    @Override
    protected void defaults() {
        // why does this work?
        DEFAULT = register(BuiltinSonic.create("prime", "Prime"));
    }

    public static SonicRegistry getInstance() {
        if (INSTANCE == null) {
            AITMod.LOGGER.debug("SonicRegistry was not initialized, creating a new instance");
            INSTANCE = new SonicRegistry();
        }

        return INSTANCE;
    }

    public static SonicSchema DEFAULT;

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
