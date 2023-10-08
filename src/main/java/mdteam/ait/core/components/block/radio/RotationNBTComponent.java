package mdteam.ait.core.components.block.radio;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface RotationNBTComponent extends Component {

    public double getTuner();
    public double getVolume();
    public boolean isOn();
    public void setTuner(double tuner);
    public void setVolume(double volume);
    public void turnOn(boolean isOn);

}
