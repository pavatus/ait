package mdteam.ait.tardis;


import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Base class for all tardis components.
 * @implNote There should be NO logic run in the constructor. If you need to have such logic, implement it in {@link AbstractTardisComponent#init()} method!
 */
public abstract class AbstractTardisComponent {

    private UUID tardisId; // I know Theo would prefer this to be a Tardis class but i dont know how to get it working..
    private final String id;

    public AbstractTardisComponent(Tardis tardis, String id) {
        this.tardisId = tardis.getUuid();
        this.id = id;
    }

    /**
     * Syncs this object and all its properties to the client
     * Server only
     */
    protected void sync() {
        if (TardisUtil.isClient()) return; // todo bad check, is deprecated

        ServerTardisManager.getInstance().sendToSubscribers(this);
    }

    public void init() { }

    public Init getInitMode() {
        return Init.ALWAYS;
    }

    public @Nullable Tardis getTardis() {
        if (TardisUtil.isClient()) { // todo replace deprecated check
            if (!ClientTardisManager.getInstance().hasTardis(this.tardisId)) {
                ClientTardisManager.getInstance().loadTardis(this.tardisId, tardis -> {});
                return null; // todo add a bunch of null checks, because i dont want to rewrite a lot of the code base rn. this needs replacing badly but i am desperate for this release to come out and idc.
            }
            return ClientTardisManager.getInstance().getTardis(this.tardisId);
        }
        return ServerTardisManager.getInstance().getTardis(this.tardisId);
    }

    public String getId() {
        return id;
    }

    public void setTardis(Tardis tardis) {
        this.tardisId = tardis.getUuid();
    }

    public enum Init {
        NO_INIT,
        ALWAYS, // always init
        FIRST, // when tardis placed
        DESERIALIZE // when tardis deserialized
    }
}