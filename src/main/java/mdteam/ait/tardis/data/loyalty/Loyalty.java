package mdteam.ait.tardis.data.loyalty;

public enum Loyalty {
	REJECT("none", 0),
	NEUTRAL("neutral", 25),
	COMPANION("companion", 50),
	PILOT("pilot", 75),
	OWNER("owner", 100);

	public final String id;
	public final int level;

	Loyalty(String id, int level) {
		this.id = id;
		this.level = level;
	}

	public static Loyalty get(String id) {
		for (Loyalty loyalty : Loyalty.values()) {
			if (loyalty.id.equalsIgnoreCase(id)) return loyalty;
		}
		return null;
	}

	public static Loyalty get(int level) {
		Loyalty best = null;

		for (Loyalty loyalty : Loyalty.values()) {
			if (loyalty.level >= level) {
				if (best == null) best = loyalty;

				if (loyalty.level >= best.level) best = loyalty;
			}
		}
		return best;
	}
}
