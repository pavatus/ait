package loqor.ait.api;

import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public interface WorldWithTardis {

    Lookup ait$lookup();

    boolean ait$hasTardis();

    final class Lookup extends HashMap<ChunkPos, Set<ServerTardis>> {

        public void put(ChunkPos pos, ServerTardis tardis) {
            this.computeIfAbsent(pos, chunkPos -> new HashSet<>()).add(tardis);
        }

        public void remove(ChunkPos pos, ServerTardis tardis) {
            Set<ServerTardis> set = this.get(pos);

            if (set == null)
                return;

            set.remove(tardis);
        }
    }
}
