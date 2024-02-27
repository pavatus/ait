package mdteam.ait.client.registry.exterior.impl.box;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.PoliceBoxCoralModel;
import mdteam.ait.client.models.exteriors.PoliceBoxModel;
import org.joml.Vector3f;

public class ClientPoliceBoxTokamakVariant extends ClientPoliceBoxVariant {
    public ClientPoliceBoxTokamakVariant() {
        super("tokamak");
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0.56f, 1.45f, 1.2f);
    }
}
