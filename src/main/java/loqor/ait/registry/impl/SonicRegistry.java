package loqor.ait.registry.impl;

import loqor.ait.AITMod;
import loqor.ait.client.AITModClient;
import loqor.ait.core.data.datapack.DatapackSonic;
import loqor.ait.core.data.schema.BuiltinSonic;
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

    @Override
    public void readFromServer(PacketByteBuf buf) {
        super.readFromServer(buf);
        AITModClient.sonicModelPredicate();
    }

    @Override
    public void onClientInit() {
        this.defaults();
        super.onClientInit();
    }

    @Override
    protected void defaults() {
        DEFAULT = register(BuiltinSonic.create("prime"));

        register(BuiltinSonic.create("copper"));
        register(BuiltinSonic.create("mechanical"));
        register(BuiltinSonic.create("fob"));
        register(BuiltinSonic.create("coral"));
        register(BuiltinSonic.create("renaissance"));
        register(BuiltinSonic.create("song"));
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
        for (SonicSchema schema : REGISTRY.values()) {
            SonicSchema.Models models = schema.models();
            models.load(consumer);

            LOGGER.info("Loading sonic '" + schema.id() + "' with models: " + models);
        }
    }
}
