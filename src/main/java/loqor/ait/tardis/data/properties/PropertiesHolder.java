package loqor.ait.tardis.data.properties;

import loqor.ait.tardis.data.TardisLink;

import java.util.HashMap;

public class PropertiesHolder extends TardisLink {
	private final HashMap<String, Object> data; // might replace the generic object with a property class that has impls eg Property.Boolean, etc

	public PropertiesHolder(HashMap<String, Object> data) {
		super(Id.PROPERTIES);
		this.data = data;
	}

	public PropertiesHolder() {
		this(PropertiesHandler.createDefaultProperties());
	}

	public HashMap<String, Object> getData() {
		return this.data;
	}
}
