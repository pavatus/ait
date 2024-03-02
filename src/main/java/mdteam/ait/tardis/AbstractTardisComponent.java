package mdteam.ait.tardis;


import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;

import java.util.Optional;
import java.util.UUID;

/**
 * Base class for all tardis components.
 *
 * @implNote There should be NO logic run in the constructor. If you need to have such logic, implement it in {@link AbstractTardisComponent#init()} method!
 */
public abstract class AbstractTardisComponent {

	protected UUID tardisId; // I know Theo would prefer this to be a Tardis class but i dont know how to get it working..
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

	public void init() {
	}

	public Init getInitMode() {
		return Init.ALWAYS;
	}

	/**
	 * Attempts to find the TARDIS related to this component
	 * It looks for the components tardis id stored in the lookup
	 * This is resource intensive and the result of this should be stored in a variable (you should be doing this anyway for things you are repeatedly calling)
	 * @return the found TARDIS, or empty.
	 */
	public Optional<Tardis> findTardis() {
		if (TardisUtil.isClient()) { // todo replace deprecated check
			if (!ClientTardisManager.getInstance().hasTardis(this.tardisId)) {
				if (this.tardisId != null)
					ClientTardisManager.getInstance().loadTardis(this.tardisId, tardis -> {});
				return Optional.empty();
			}
			return Optional.of(ClientTardisManager.getInstance().getTardis(this.tardisId));
		}
		return Optional.ofNullable(ServerTardisManager.getInstance().getTardis(this.tardisId));
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