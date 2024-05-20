package loqor.ait.tardis.wrapper.server;

import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisTickable;

public class ServerTardisTravel extends TardisTravel implements TardisTickable {

	public ServerTardisTravel(AbsoluteBlockPos.Directed pos) {
		super(pos);
	}

	@Override
	public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
		super.setDestination(pos, withChecks);
		this.sync();
	}

	@Override
	public void setPosition(AbsoluteBlockPos.Directed pos) {
		super.setPosition(pos);
		this.sync();
	}

	@Override
	public void setState(State state) {
		super.setState(state);
		this.sync();
	}

	@Override
	public void dematerialise(boolean withRemat) {
		super.dematerialise(withRemat);
		this.sync();
	}

	@Override
	public void materialise() {
		super.materialise();
	}
}
