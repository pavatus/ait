package loqor.ait.api;

import loqor.ait.tardis.Tardis;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public interface WorldWithTardis {

    Lookup ait$lookup();

    boolean ait$hasTardis();

    final class Lookup extends HashMap<ChunkPos, Set<Tardis>> {

        public void put(ChunkPos pos, Tardis tardis) {
            this.computeIfAbsent(pos, chunkPos -> new HashSet<>()).add(tardis);
        }

        public void remove(ChunkPos pos, Tardis tardis) {
            Set<Tardis> set = this.get(pos);

            if (set == null)
                return;

            set.remove(tardis);
        }
    }
}
