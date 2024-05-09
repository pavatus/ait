package loqor.ait.registry.impl;

import loqor.ait.AITMod;
import loqor.ait.client.AITModClient;
import loqor.ait.core.data.datapack.DatapackSonic;
import loqor.ait.core.data.schema.BuiltinSonic;
import loqor.ait.core.data.schema.SonicSchema;
import loqor.ait.registry.unlockable.UnlockableRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
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
    public void onCommonInit() {
        super.onCommonInit();
        LOGGER.info("SONIC: onCommonInit");
    }

    @Override
    public void onClientInit() {
        this.defaults();
        super.onClientInit();
        LOGGER.info("SONIC: onClientInit");
    }

    @Override
    protected void defaults() {
        DEFAULT = register(BuiltinSonic.create("prime", "Prime"));

        register(BuiltinSonic.create("copper", null));
        register(BuiltinSonic.create("mechanical", null));
        register(BuiltinSonic.create("fob", null));
        register(BuiltinSonic.create("coral", null));
        register(BuiltinSonic.create("renaissance", null));
        register(BuiltinSonic.create("song", null));
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
