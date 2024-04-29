package loqor.ait.mixin.client.rendering;

import loqor.ait.client.renderers.BakedModelEditor;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(BakedModelManager.class)
public abstract class BakedModelManagerMixin implements BakedModelEditor {

    @Shadow
    private Map<Identifier, BakedModel> models;

    @Shadow public abstract BakedModel getModel(ModelIdentifier id);

    @Override
    public abstract BakedModel getModel(Identifier id);

    @Override
    public void ait$setModel(Identifier identifier, BakedModel model) {
        this.models.put(identifier, model);
    }

    @Override
    public void ait$setModel(ModelIdentifier identifier, BakedModel model) {
        this.models.put(identifier, model);
    }
}
