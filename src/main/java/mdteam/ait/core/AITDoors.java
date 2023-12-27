package mdteam.ait.core;

import mdteam.ait.tardis.variant.door.*;
import net.minecraft.util.Identifier;

import java.util.*;

public class AITDoors {
    private static final Map<Identifier, DoorSchema> map = new HashMap<>();

    /**
     * Doors (STUCK) for different exteriors are registered here:
     */
    public static void init() {
        register(new TardimDoorVariant());
        register(new ClassicDoorVariant());
        register(new BoothDoorVariant());
        register(new CapsuleDoorVariant());
        register(new PoliceBoxDoorVariant());
        register(new EasterHeadDoorVariant());
    }

    public static void register(DoorSchema door) {
        map.put(door.id(), door);
    }

    public static DoorSchema get(Identifier id) {
        return map.get(id);
    }

    public static Collection<DoorSchema> iterator() {
        return map.values();
    }
}
