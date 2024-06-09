package loqor.ait.tardis.travel;

import java.util.ArrayList;
import java.util.List;

public class TravelScript {

    public interface Builder<T extends Builder<?>> {

        T parent();
        Action action();

        default Builder<?> findRoot() {
            if (this.parent() != null)
                return this.parent().findRoot();

            return this;
        }

        default List<Builder<?>> streamline() {
            List<Builder<?>> streamlined = new ArrayList<>();
            this.streamline(streamlined);

            return streamlined;
        }

        default void streamline(List<Builder<?>> list) {
            if (this.parent() != null)
                this.parent().streamline(list);

            list.add(this);
        }

        abstract class Basic<T extends Builder<?>> implements Builder<T> {

            private final T parent;

            public Basic(T parent) {
                this.parent = parent;
            }

            @Override
            public T parent() {
                return parent;
            }
        }

        class Landed extends Basic<Landed> {

            public Landed() {
                super(null);
            }

            public Demat demat() {
                return new Demat(this);
            }
        }

        class Demat extends Basic<Landed> {

            public Demat(Landed parent) {
                super(parent);
            }

            public Flight flight() {
                return new Flight(this);
            }
        }

        final class Flight extends Basic<Demat> implements Rematable<Demat> {

            public Flight(Demat parent) {
                super(parent);
            }

            public Crash crash() {
                return new Crash(this);
            }

            @Override
            public Remat remat() {
                return new Remat(this);
            }
        }

        final class Crash extends Basic<Flight> implements Rematable<Flight> {

            public Crash(Flight parent) {
                super(parent);
            }

            @Override
            public Remat remat() {
                return new Remat(this);
            }
        }

        class Remat extends Basic<Rematable<?>> {

            public Remat(Flight parent) {
                super(parent);
            }

            public Remat(Crash parent) {
                super(parent);
            }
        }

        sealed interface Rematable<T extends Builder<?>> extends Builder<T> permits Flight, Crash {
            Remat remat();
        }
    }

    static void test() {
        new Builder.Landed()
                .demat()
                .flight()
                .remat();
    }
}
