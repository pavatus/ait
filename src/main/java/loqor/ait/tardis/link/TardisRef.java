package loqor.ait.tardis.link;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.Disposable;

import java.util.UUID;
import java.util.function.Function;

public class TardisRef implements Disposable {

    private final LoadFunc load;
    private final UUID id;

    private Tardis cached;

    public TardisRef(UUID id, LoadFunc load) {
        this.id = id;
        this.load = load;
    }

    public TardisRef(Tardis tardis, LoadFunc load) {
        this.id = tardis.getUuid();
        this.load = load;

        this.cached = tardis;
    }

    public Tardis get() {
        if (this.cached != null && !this.cached.isAged())
            return this.cached;

        this.cached = this.load.apply(this.id);
        return this.cached;
    }

    public UUID getId() {
        return id;
    }

    public boolean isPresent() {
        return this.get() != null;
    }

    public boolean isEmpty() {
        return this.get() == null;
    }

    @Override
    public void dispose() {
        this.cached = null;
    }

    public interface LoadFunc extends Function<UUID, Tardis> { }
}
