package mdteam.ait.tardis.control.impl.pos;

public class PosManager { // todo can likely be moved into the properties / use properties instead
    // private final ConsoleBlockEntity console;
    public int increment;
    private int[] validIncrements = new int[] {
            1,
            10,
            100,
            1000
    };

    // this shouldnt be saved, just create a new one when its null bc imo i dont think the increment really NEEDS saving. but if it does then just create a save/load method here
    public PosManager() {
        this.increment = 1;
    }

    private int getIncrementPosition() {
        // since indexof doesnt seem to work..
        for (int i = 0; i < validIncrements.length; i++) {
            if (this.increment != validIncrements[i]) continue;

            return i;
        }
        return 0;
    }
    public int nextIncrement() {

        this.increment = validIncrements[(getIncrementPosition() + 1 > validIncrements.length - 1) ? 0 : getIncrementPosition() + 1];

        return this.increment;
    }
    public int prevIncrement() {
        this.increment = validIncrements[(getIncrementPosition() - 1 < 0) ? validIncrements.length - 1 : getIncrementPosition() - 1];

        return this.increment;
    }
}
