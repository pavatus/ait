package loqor.ait.registry.impl;

import static loqor.ait.AITMod.LOGGER;

import java.util.function.Consumer;

import dev.pavatus.lib.register.unlockable.UnlockableRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.data.datapack.DatapackSonic;
import loqor.ait.data.schema.sonic.BuiltinSonic;
import loqor.ait.data.schema.sonic.SonicSchema;

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
        //AITModClient.sonicModelPredicate();
    }

    @Override
    public void onClientInit() {
        this.defaults();
        super.onClientInit();
    }

    @Override
    public void onCommonInit() {
        super.onCommonInit();
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this);
    }

    @Override
    protected void defaults() {
        DEFAULT = register(BuiltinSonic.create("prime"));

        register(BuiltinSonic.create("copper"));
        register(BuiltinSonic.create("mechanical"));
        register(BuiltinSonic.create("fob"));
        register(BuiltinSonic.create("coral"));
        register(BuiltinSonic.create("renaissance"));
        register(BuiltinSonic.create("crystalline"));
        register(BuiltinSonic.create("song"));
        register(BuiltinSonic.create("singularity"));
        register(BuiltinSonic.create("candy_cane"));
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

            LOGGER.debug("Loading sonic '{}' with models: {}", schema.id(), models);
        }
    }




}
