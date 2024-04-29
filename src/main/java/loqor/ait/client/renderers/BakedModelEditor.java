package loqor.ait.client.renderers;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public interface BakedModelEditor {
    BakedModel getModel(Identifier identifier);
    void ait$setModel(Identifier identifier, BakedModel model);
    void ait$setModel(ModelIdentifier identifier, BakedModel model);
}
