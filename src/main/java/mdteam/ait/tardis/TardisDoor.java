package mdteam.ait.tardis;

import mdteam.ait.core.util.data.AbsoluteBlockPos;

public class TardisDoor extends AbstractTardisComponent {

    private State state;
    private boolean locked;

    public TardisDoor(Tardis tardis) {
        this(tardis, false);
    }

    public TardisDoor(Tardis tardis, boolean locked) {
        this(tardis, State.CLOSED, locked);
    }

    protected TardisDoor(Tardis tardis, State state, boolean locked) {
        super(tardis);

        this.state = state;
        this.locked = locked;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void next() {
        if (this.locked)
            return;

        this.state = this.state.getNext();
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public AbsoluteBlockPos.Directed getInteriorDoorPosition() {
        return this.tardis.getDesktop().getInteriorDoorPos();
    }

    public AbsoluteBlockPos.Directed getExteriorDoorPosition() {
        return this.tardis.getTravel().getPosition();
    }

    public enum State {
        OPEN(1.2f, 1.2f) {
            @Override
            public State getNext() {
                return State.CLOSED;
            }
        },
        CLOSED(0f, 0f) {
            @Override
            public State getNext() {
                return State.SLIGHTLY_OPEN;
            }
        },
        SLIGHTLY_OPEN(1.2f, 0f) {
            @Override
            public State getNext() {
                return State.OPEN;
            }
        };

        private final float left;
        private final float right;

        State(float left, float right) {
            this.left = left;
            this.right = right;
        }

        public float getLeft() {
            return left;
        }

        public float getRight() {
            return right;
        }

        public abstract State getNext();
    }
}
