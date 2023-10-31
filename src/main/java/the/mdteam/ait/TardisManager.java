package the.mdteam.ait;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITardisManager;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TardisManager implements ITardisManager {

    protected final Map<UUID, ITardis> lookup = new HashMap<>();
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
        }).create();
    }

    @Override
    public ITardis getTardis(UUID uuid) {
        if (this.lookup.containsKey(uuid))
            return this.lookup.get(uuid);

        return this.loadTardis(uuid);
    }

    @Override
    public ITardis findTardisByExterior(BlockPos pos) {
        return null;
    }

    @Override
    public ITardis findTardisByInterior(BlockPos pos) {
        return null;
    }

    @Override
    public ITardis create(AbsoluteBlockPos.Directed pos, ExteriorEnum exterior, IDesktopSchema desktop) {
        return null;
    }

    @Override
    public ITardis loadTardis(UUID uuid) {
        return null;
    }

    @Override
    public ITardis loadTardis(File file) {
        return null;
    }

    @Override
    public void loadTardis() {

    }

    @Override
    public void saveTardis(ITardis tardis) {

    }

    @Override
    public void saveTardis() {

    }
}
