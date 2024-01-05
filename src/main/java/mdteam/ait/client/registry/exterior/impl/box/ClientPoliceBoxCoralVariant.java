package mdteam.ait.client.registry.exterior.impl.box;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.PoliceBoxCoralModel;

public class ClientPoliceBoxCoralVariant extends ClientPoliceBoxVariant {
    public ClientPoliceBoxCoralVariant() {
        super("coral");
    }

    @Override
    public ExteriorModel model() {
        return new PoliceBoxCoralModel(PoliceBoxCoralModel.getTexturedModelData().createModel());
    }
}
