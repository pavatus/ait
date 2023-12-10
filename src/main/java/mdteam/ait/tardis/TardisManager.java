package mdteam.ait.tardis;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.core.events.BlockEntityPreLoadEvent;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.Corners;
import mdteam.ait.data.SerialDimension;
import mdteam.ait.tardis.handler.PropertiesHandler;
import mdteam.ait.tardis.handler.PropertiesHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class TardisManager {

    protected final Map<UUID, Tardis> lookup = new HashMap<>();
    protected final Gson gson;

    public TardisManager() {
        GsonBuilder builder = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes field) {
                        return field.getAnnotation(Exclude.class) != null;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                }).registerTypeAdapter(TardisDesktopSchema.class, TardisDesktopSchema.serializer())
                .registerTypeAdapter(Corners.class, Corners.serializer());
        builder = this.init(builder);
        this.gson = builder.create();
    }

    public static void init() {
        // nicked this off theo

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

    public GsonBuilder init(GsonBuilder builder) {
        return builder;
    }

    public static TardisManager getInstance() {
//        return FabricLauncherBase.getLauncher().getEnvironmentType() == EnvType.SERVER ? ServerTardisManager.getInstance() : ClientTardisManager.getInstance();
        return TardisUtil.isServer() ? ServerTardisManager.getInstance() : ClientTardisManager.getInstance();
    }

    public void getTardis(UUID uuid, Consumer<Tardis> consumer) {
        if (this.lookup.containsKey(uuid)) {
            consumer.accept(this.lookup.get(uuid));
            return;
        }

        this.loadTardis(uuid, consumer);
    }

    public void link(UUID uuid, ILinkable linkable) {
        this.getTardis(uuid, linkable::setTardis);
    }

    public abstract void loadTardis(UUID uuid, Consumer<Tardis> consumer);

    public void reset() {
        this.lookup.clear();
    }

    public Map<UUID, Tardis> getLookup() {
        return this.lookup;
    }
}
