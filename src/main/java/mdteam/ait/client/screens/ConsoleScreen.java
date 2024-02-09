package mdteam.ait.client.screens;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisConsole;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class ConsoleScreen extends TardisScreen {
    protected final UUID console;

    protected ConsoleScreen(Text title, UUID tardis, UUID console) {
        super(title, tardis);
        this.console = console;
    }

    protected @Nullable TardisConsole findConsole() {
        if (this.updateTardis() == null) return null;

        Tardis tardis = this.tardis();

        return tardis.getDesktop().findConsole(this.console);
    }
}
