package mdteam.ait.tardis.control.sequences;

import com.mojang.datafixers.TypeRewriteRule;
import mdteam.ait.registry.SequenceRegistry;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.TardisLink;
import mdteam.ait.tardis.util.FlightUtil;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.UUID;

public class SequenceHandler extends TardisLink {
    private static final int REMOVE_CONTROL_TICK = FlightUtil.covertSecondsToTicks(2);

    private RecentControls recent;
    private int ticks = 0;

    public SequenceHandler(UUID tardisId) {
        super(tardisId);
        recent = new RecentControls(tardisId);
    }

    public void add(Control control) {
        recent.add(control);
        ticks = 0;
        this.compareToSequences();
    }

    private void compareToSequences() {
        for (Sequence sequence : SequenceRegistry.REGISTRY) {
            if (sequence.isFinished(this.recent))
                sequence.execute(this.tardis());
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
