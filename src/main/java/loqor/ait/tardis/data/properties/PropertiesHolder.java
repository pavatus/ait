package loqor.ait.tardis.data.properties;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisLink;

import java.util.HashMap;

public class PropertiesHolder extends TardisLink {
	private final HashMap<String, Object> data; // might replace the generic object with a property class that has impls eg Property.Boolean, etc

	public PropertiesHolder(Tardis tardisId, HashMap<String, Object> data) {
		super(tardisId, TypeId.PROPERTIES);
		this.data = data;
	}

	public PropertiesHolder(Tardis tardis) {
		this(tardis, PropertiesHandler.createDefaultProperties());
	}

	public HashMap<String, Object> getData() {
		return this.data;
	}
}
