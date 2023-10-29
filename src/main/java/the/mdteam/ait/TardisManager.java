package the.mdteam.ait;

import com.google.gson.Gson;
import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITardisManager;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TardisManager implements ITardisManager {

    private static final TardisManager instance = new TardisManager();
    private static final Gson gson = new Gson();
    private final Map<UUID, ITardis> lookup = new HashMap<>();

    // Make the constructor private, to avoid bad stuff from happening.
    private TardisManager() {

    }

    @Override
    public ITardis getTardis(UUID uuid) {
        return this.loadTardis(uuid);
    }

    @Override
    public ITardis findTardisByExterior(BlockPos pos) {
        for (ITardis tardis : this.lookup.values()) {
            if (tardis.getTravel().getPosition().equals(pos))
                return tardis;
        }

        return null;
    }

    @Override
    public ITardis findTardisByInterior(BlockPos pos) {
        for (ITardis tardis : this.lookup.values()) {
            if (TardisUtil.inBox(tardis.getDesktop().getCorners(), pos))
                return tardis;
        }

        return null;
    }

    @Override
    public ITardis create(AbsoluteBlockPos.Directed pos, ExteriorEnum exteriorType, IDesktopSchema desktopSchema) {
        UUID uuid = UUID.randomUUID();
        ITardis tardis = new Tardis(uuid, pos, desktopSchema, exteriorType);
        this.lookup.put(uuid, tardis);

        tardis.getTravel().placeExterior();
        return tardis;
    }

    /**
     * Loads the tardis from a file and returns it, or, returns the cached instance.
     *
     * @param uuid uuid of the tardis
     * @return the tardis or null if no tardis file was found
     */
    @Override
    public ITardis loadTardis(UUID uuid) {
        if (this.lookup.containsKey(uuid))
            return this.lookup.get(uuid);

        File savePath = TardisManager.getSavePath(uuid);
        savePath.getParentFile().mkdirs();

        try {
            if (!savePath.exists())
                throw new IOException("Tardis file " + uuid + " doesn't exist!");

            ITardis tardis = gson.fromJson(Files.readString(savePath.toPath()), Tardis.class);
            this.lookup.put(uuid, tardis);

            return tardis;
        } catch (IOException e) {
            AITMod.LOGGER.warn("Tardis {} does not exist (or it got corrupted)!", uuid);
            AITMod.LOGGER.warn(e.getMessage());
        }

        return null;
    }

    @Override
    public void saveTardis(ITardis tardis) {
        File savePath = TardisManager.getSavePath(tardis.getUuid());
        savePath.getParentFile().mkdirs();

        try {
            Files.writeString(savePath.toPath(), gson.toJson(tardis));
        } catch (IOException e) {
            AITMod.LOGGER.warn("Couldn't save Tardis {}", tardis.getUuid());
            AITMod.LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void saveTardis() {
        for (ITardis tardis : this.lookup.values()) {
            this.saveTardis(tardis);
        }
    }

    public static ITardisManager getInstance() {
        return instance;
    }

    private static File getSavePath(UUID uuid) {
        // TODO: maybe, make WorldSavePath.AIT?
        return new File(TardisUtil.getServer().getSavePath(WorldSavePath.ROOT) + "ait/" + uuid + ".json");
    }

    private static File getSavePath(ITardis tardis) {
        return TardisManager.getSavePath(tardis.getUuid());
    }
}
