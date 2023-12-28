package mdteam.ait.core;

import mdteam.ait.tardis.desktops.*;
import net.minecraft.util.Identifier;
import mdteam.ait.tardis.TardisDesktopSchema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AITDesktops {

    private static final Map<Identifier, TardisDesktopSchema> interiors = new HashMap<>();

    /**
     * Desktops are registered here:
     */
    public static void init() {

        //Necessary
        AITDesktops.register(new DefaultCaveDesktop());
        AITDesktops.register(new CaveDesktop());
        AITDesktops.register(new DevDesktop());
        AITDesktops.register(new OfficeDesktop());

        //In Order (without the necessities)
        AITDesktops.register(new RegalDesktop());
        AITDesktops.register(new BotanistDesktop());
        AITDesktops.register(new PristineDesktop());

        // Canon Desktops (zzzzz)
        AITDesktops.register(new Type40Desktop());
        AITDesktops.register(new VictorianDesktop());
        AITDesktops.register(new WarDesktop());
        AITDesktops.register(new CoralDesktop());
        AITDesktops.register(new CopperDesktop());
        AITDesktops.register(new ToyotaDesktop());
        AITDesktops.register(new CrystallineDesktop());
    }

    public static void register(TardisDesktopSchema interior) {
        interiors.put(interior.id(), interior);
    }

    public static TardisDesktopSchema get(Identifier id) {
        return interiors.get(id);
    }

    public static Collection<TardisDesktopSchema> iterator() {
        return interiors.values();
    }
}
