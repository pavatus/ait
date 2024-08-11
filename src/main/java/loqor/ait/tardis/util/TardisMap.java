package loqor.ait.tardis.util;

import java.util.HashMap;
import java.util.UUID;

import loqor.ait.tardis.Tardis;

public class TardisMap<T extends Tardis> extends HashMap<UUID, T> {

    public T put(T t) {
        return this.put(t.getUuid(), t);
    }
}
