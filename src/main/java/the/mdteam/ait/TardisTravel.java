package the.mdteam.ait;

import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITravel;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.data.AbsoluteBlockPos;

public class TardisTravel implements ITravel {

    private State state = State.LANDED;
    private AbsoluteBlockPos.Directed position;
    private AbsoluteBlockPos.Directed destination;

    private final ITardis tardis;

    public TardisTravel(ITardis tardis, AbsoluteBlockPos.Directed pos) {
        this.tardis = tardis;
        this.position = pos;
    }

    @Override
    public void setPosition(AbsoluteBlockPos.Directed pos) {
        this.position = pos;
    }

    @Override
    public AbsoluteBlockPos.Directed getPosition() {
        return position;
    }

    @Override
    public void materialise() {

    }

    @Override
    public void dematerialise(boolean withRemat) {

    }

    @Override
    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        this.destination = pos;
    }

    @Override
    public AbsoluteBlockPos.Directed getDestination() {
        return destination;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void toggleHandbrake() {

    }

    @Override
    public void placeExterior() {
        this.position.setBlockState(AITBlocks.EXTERIOR_BLOCK.getDefaultState());

        ExteriorBlockEntity exterior = new ExteriorBlockEntity(
                this.position, this.position.getBlockState()
        );

        exterior.setTardis(this.tardis);
        this.position.addBlockEntity(exterior);
    }

    @Override
    public void deleteExterior() {

    }
}
