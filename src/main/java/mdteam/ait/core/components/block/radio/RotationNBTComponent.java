package mdteam.ait.core.components.block.radio;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface RotationNBTComponent extends Component {

    double getTuner();
    double getVolume();
    boolean isOn();
    void setTuner(double tuner);
    void setVolume(double volume);
    void turnOn(boolean isOn);

}
