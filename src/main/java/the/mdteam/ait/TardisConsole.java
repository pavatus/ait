package the.mdteam.ait;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;

public class TardisConsole {

    @Exclude
    protected final Tardis tardis;
    private ConsoleEnum console;

    public TardisConsole(Tardis tardis, ConsoleEnum console) {
        this.tardis = tardis;
        this.console = console;
    }

    public ConsoleEnum getType() {
        return console;
    }

    public void setType(ConsoleEnum console) {
        this.console = console;
    }
}
