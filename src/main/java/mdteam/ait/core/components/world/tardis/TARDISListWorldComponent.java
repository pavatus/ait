package mdteam.ait.core.components.world.tardis;

import dev.onyxstudios.cca.api.v3.component.Component;
import mdteam.ait.core.tardis.Tardis;

import java.util.List;

public interface TARDISListWorldComponent extends Component {
    List<Tardis> getTardises();
    void setTardises(List<Tardis> tardisList);
}
