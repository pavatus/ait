package mdteam.ait.core;

import mdteam.ait.tardis.exterior.*;
import mdteam.ait.tardis.variant.door.*;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AITExteriors {
    private static final Map<Identifier, ExteriorSchema> map = new HashMap<>();

    /**
     * Exteriors are registered here:
     */
    public static void init() {
        register(new CapsuleExterior());
        register(new TardimExterior());
        register(new BoothExterior());
        register(new ClassicExterior());
        register(new PoliceBoxExterior());
        register(new EasterHeadExterior());
        //register(new CubeExterior());
    }

    public static void register(ExteriorSchema schema) {
        map.put(schema.id(), schema);
    }

    public static ExteriorSchema get(Identifier id) {
        return map.get(id);
    }

    public static Collection<ExteriorSchema> iterator() {
        return map.values();
    }
}
