package loqor.ait.tardis.travel;

import loqor.ait.tardis.TardisTravel2;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class Action {

    private final List<Runnable> completion = new ArrayList<>();

    public Action() {

    }

    public void start(TardisTravel2 travel) {

    }

    public void end(TardisTravel2 travel) {

    }

    public void complete(TardisTravel2 travel) {
        this.end(travel);

        for (Runnable runnable : completion) {
            runnable.run();
        }
    }

    public void tick(TardisTravel2 travel, MinecraftServer server) {

    }

    public void onComplete(Runnable runnable) {
        this.completion.add(runnable);
    }
}
