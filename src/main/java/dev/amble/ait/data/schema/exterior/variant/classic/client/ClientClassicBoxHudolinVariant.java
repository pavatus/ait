package dev.amble.ait.data.schema.exterior.variant.classic.client;

import dev.amble.ait.client.models.exteriors.ClassicHudolinExteriorModel;
import dev.amble.ait.client.models.exteriors.ExteriorModel;

public class ClientClassicBoxHudolinVariant extends ClientClassicBoxVariant {

    public ClientClassicBoxHudolinVariant() {
        super("hudolin");
    }

    @Override
    public ExteriorModel model() {
        return new ClassicHudolinExteriorModel(ClassicHudolinExteriorModel.getTexturedModelData().createModel());
    }
}
