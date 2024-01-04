package mdteam.ait.tardis.control.sequences;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import net.minecraft.util.Identifier;

import java.util.List;

// should this be an interface?
public abstract class Sequence {
    public abstract Identifier id();

    /**
     * A list of controls needed to execute the sequence, in the order they should be executed
     */
    public abstract List<Control> getControls();

    /**
     * Compares the recent controls to this sequence, if everything matches then it is finished
     * @param recent
     * @return
     */
    public boolean isFinished(RecentControls recent) {
        return recent.equals(this.getControls());
    }

    public abstract void execute(Tardis tardis);
}
