package loqor.ait.tardis.control.impl.pos;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.data.bsp.Exclude;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.handler.properties.integer.IntProperty;
import loqor.ait.tardis.handler.properties.integer.IntValue;

public class IncrementManager extends KeyedTardisComponent {
    private static final IntProperty INCREMENT_PROPERTY = new IntProperty("increment", 1);
    private final IntValue increment = INCREMENT_PROPERTY.create(this);

    @Exclude
    private static final int[] validIncrements = new int[]{1, 10, 100, 1000, 10000};

    public IncrementManager() {
        super(Id.INCREMENT);
    }

    @Override
    public void onLoaded() {
        increment.of(this, INCREMENT_PROPERTY);
    }

    public IntValue increment() {
        return increment;
    }

    public static int increment(Tardis tardis) {
        return tardis.<IncrementManager>handler(Id.INCREMENT).increment().get();
    }

    private static void setIncrement(Tardis tardis, int increment) {
        tardis.<IncrementManager>handler(Id.INCREMENT).increment().set(increment);
    }

    private static int getIncrementPosition(Tardis tardis) {
        for (int i = 0; i < validIncrements.length; i++) {
            if (increment(tardis) != validIncrements[i])
                continue;

            return i;
        }
        return 0;
    }

    public static void nextIncrement(Tardis tardis) {
        setIncrement(tardis,
                validIncrements[(getIncrementPosition(tardis) + 1 >= validIncrements.length)
                        ? 0
                        : getIncrementPosition(tardis) + 1]);

        increment(tardis);
    }

    public static void prevIncrement(Tardis tardis) {
        setIncrement(tardis,
                validIncrements[(getIncrementPosition(tardis) - 1 < 0)
                        ? validIncrements.length - 1
                        : getIncrementPosition(tardis) - 1]);

        getIncrementPosition(tardis);
    }
}
