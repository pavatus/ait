package loqor.ait.client.renderers;

import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.renderers.model.OverriddenBakedModel;

public class CustomItemRendering {

    private static final Set<Identifier> MODEL_POOL = new HashSet<>();

    /**
     * This method force loads the model *when the next model override will be
     * registered*.
     *
     * @param model
     *            the model to load
     */
    public static void load(Identifier model) {
        AITMod.LOGGER.info("Loaded " + model + " to the force-load list");
        MODEL_POOL.add(model);
    }

    public static void register(Identifier item, OverriddenBakedModel.ModelOverride override) {
        new Unit(item, override);
    }

    static class Unit {

        private final ModelIdentifier id;
        private final OverriddenBakedModel.BakedModelOverride override;

        public Unit(Identifier item, OverriddenBakedModel.ModelOverride override) {
            this(item, override.asBaked());
        }

        public Unit(Identifier item, OverriddenBakedModel.BakedModelOverride override) {
            this.id = new ModelIdentifier(item, "inventory");
            this.override = override;

            ModelLoadingPlugin.register(context -> {
                context.addModels(MODEL_POOL);

                context.modifyModelAfterBake().register(this::transform);
            });
        }

        private BakedModel transform(BakedModel original, ModelModifier.AfterBake.Context context) {
            if (original instanceof OverriddenBakedModel)
                return original;

            if (!this.id.equals(context.id()))
                return original;


            BakedModel overriden = new OverriddenBakedModel(original, this.override, context.sourceModel(), context.baker());
            return overriden;
        }
    }
}
