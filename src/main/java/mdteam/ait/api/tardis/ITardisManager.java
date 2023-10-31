package mdteam.ait.api.tardis;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.util.math.BlockPos;
import the.mdteam.ait.ClientTardisManager;
import the.mdteam.ait.ServerTardisManager;

import java.io.File;
import java.util.UUID;

public interface ITardisManager {

    ITardis getTardis(UUID uuid);
    ITardis findTardisByExterior(BlockPos pos);
    ITardis findTardisByInterior(BlockPos pos);

    ITardis create(AbsoluteBlockPos.Directed pos, ExteriorEnum exterior, IDesktopSchema desktop);

    /**
     * @implNote DO NOT use this method. Instead, use {@link ITardisManager#getTardis(UUID)}
     * @param uuid the uuid of the tardis
     * @return the tardis instance
     */
    ITardis loadTardis(UUID uuid);
    ITardis loadTardis(File file);
    void loadTardis();

    void saveTardis(ITardis tardis);
    void saveTardis();

    default void saveTardis(UUID uuid) {
        this.saveTardis(this.getTardis(uuid));
    }

    static ITardisManager getInstance() {
        return TardisUtil.isServer() ? ServerTardisManager.getInstance() : ClientTardisManager.getInstance();
    }
}
