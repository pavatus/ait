package loqor.ait.tardis.control.impl.pos;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;

public class IncrementManager { // todo can likely be moved into the properties / use properties instead
	// private final ConsoleBlockEntity console;
	public static final String INCREMENT = "increment";
	private static final int[] validIncrements = new int[]{
			1,
			10,
			100,
			1000,
			10000
	};

	public static int increment(Tardis tardis) {
		if (!tardis.getHandlers().getProperties().getData().containsKey(INCREMENT))
			setIncrement(tardis, 1);

		return PropertiesHandler.getInt(tardis.getHandlers().getProperties(), INCREMENT);
	}

	private static void setIncrement(Tardis tardis, int increment) {
		PropertiesHandler.set(tardis, INCREMENT, increment);
	}

	private static int getIncrementPosition(Tardis tardis) {
		// since indexof doesnt seem to work..
		for (int i = 0; i < validIncrements.length; i++) {
			if (increment(tardis) != validIncrements[i]) continue;

			return i;
		}
		return 0;
	}

	public static int nextIncrement(Tardis tardis) {
		setIncrement(tardis, validIncrements[(getIncrementPosition(tardis) + 1 >= validIncrements.length) ? 0 : getIncrementPosition(tardis) + 1]);

		return increment(tardis);
	}

	public static int prevIncrement(Tardis tardis) {
		setIncrement(tardis, validIncrements[(getIncrementPosition(tardis) - 1 < 0) ? validIncrements.length - 1 : getIncrementPosition(tardis) - 1]);
		return getIncrementPosition(tardis);
	}
}
