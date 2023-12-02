package mdteam.ait.tardis.manager;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mdteam.ait.core.events.BlockEntityPreLoadEvent;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.linkable.Linkable;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.core.util.data.Exclude;
import mdteam.ait.core.util.data.Corners;
import mdteam.ait.core.util.data.SerialDimension;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class TardisManager {

    protected final Map<UUID, Tardis> lookup = new HashMap<>();
    protected final Gson gson;

    public TardisManager() {
        this.gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getAnnotation(Exclude.class) != null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).registerTypeAdapter(TardisDesktopSchema.class, TardisDesktopSchema.serializer())
                .registerTypeAdapter(SerialDimension.class, SerialDimension.serializer())
                .registerTypeAdapter(Corners.class, Corners.serializer())
                .create();
    }

    public static void init() {
        // this will re-register the client tardis manager on every join (that includes local worlds as well)
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayConnectionEvents.INIT.register((handler, client) -> ClientTardisManager.init());
        }

        // this is a race between what happens first:
        // if it's a world with tardises - then we need to initialize server tardis manager before any of the block entities load
        BlockEntityPreLoadEvent.LOAD.register(() -> {
            if (ServerTardisManager.getInstance() == null) {
                ServerTardisManager.init();
            }
        });

        // if it's a brand-new world without any tardises - then there's no need to run ahead of the train and break things
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (ServerTardisManager.getInstance() == null) {
                ServerTardisManager.init();
            }
        });
    }

    public static TardisManager getInstance(Entity entity) {
        return TardisManager.getInstance(entity.getWorld());
    }

    public static TardisManager getInstance(BlockEntity entity) {
        return TardisManager.getInstance(entity.getWorld());
    }

    public static TardisManager getInstance(World world) {
        return TardisManager.getInstance(!world.isClient());
    }

    @Deprecated
    public static TardisManager getInstance() {
        return TardisManager.getInstance(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER);
    }

    public static TardisManager getInstance(boolean isServer) {
        return isServer ? ServerTardisManager.getInstance() : ClientTardisManager.getInstance();
    }

    public void getTardis(UUID uuid, Consumer<Tardis> consumer) {
        if (this.lookup.containsKey(uuid)) {
            consumer.accept(this.lookup.get(uuid));
            return;
        }

        this.loadTardis(uuid, consumer);
    }

    public void link(UUID uuid, Linkable linkable) {
        this.getTardis(uuid, linkable::setTardis);
    }

    public abstract void loadTardis(UUID uuid, Consumer<Tardis> consumer);

    public void reset() {
        this.lookup.clear();
    }

    @Deprecated
    public Map<UUID, Tardis> getLookup() {
        return this.lookup;
    }
}
