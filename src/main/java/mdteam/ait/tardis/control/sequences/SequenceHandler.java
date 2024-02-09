package mdteam.ait.tardis.control.sequences;

import mdteam.ait.registry.SequenceRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.TardisLink;
import mdteam.ait.tardis.util.FlightUtil;
import net.minecraft.server.MinecraftServer;

public class SequenceHandler extends TardisLink {
    private static final int REMOVE_CONTROL_TICK = FlightUtil.convertSecondsToTicks(2);

    private RecentControls recent;
    private int ticks = 0;

    public SequenceHandler(Tardis tardisId) {
        super(tardisId, "sequence");
        recent = new RecentControls(tardisId.getUuid());
    }

    public void add(Control control) {
        recent.add(control);
        ticks = 0;
        this.compareToSequences();
    }

    private void compareToSequences() {
        for (Sequence sequence : SequenceRegistry.REGISTRY) {
            if (sequence.isFinished(this.recent) && this.findTardis().isPresent())
                    sequence.execute(this.findTardis().get());
        }
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        ticks++;
        if (ticks >= REMOVE_CONTROL_TICK) {
            recent.clear();
            ticks = 0;
        }
    }
}
