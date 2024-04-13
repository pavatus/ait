package loqor.ait.client.renderers.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @implNote This model overrides the default ModelOverrideList functionality!
 * That means that resourcepacks won't be able to use predicates on this item!
 */
public class OverriddenBakedModel implements BakedModel {

    private final BakedModel original;
    private final ModelOverrideList overrides;

    public OverriddenBakedModel(BakedModel original, BakedModelOverride override, UnbakedModel source, Baker baker) {
        this.original = original;
        this.overrides = new ModelOverrideList(baker, (JsonUnbakedModel) source, Collections.emptyList()) {
            @Nullable
            @Override
            public BakedModel apply(BakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
                return override.apply(model, stack, world, entity, seed);
            }
        };
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return original.getQuads(state, face, random);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return original.useAmbientOcclusion();
    }

    @Override
    public boolean hasDepth() {
        return original.hasDepth();
    }

    @Override
    public boolean isSideLit() {
        return original.isSideLit();
    }

    @Override
    public boolean isBuiltin() {
        return original.isBuiltin();
    }

    @Override
    public Sprite getParticleSprite() {
        return original.getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return original.getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return this.overrides;
    }

    @FunctionalInterface
    public interface ModelOverride {
        Identifier apply(BakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed);

        default BakedModelOverride asBaked() {
            return (model, stack, world, entity, seed) -> MinecraftClient.getInstance()
                    .getBakedModelManager().getModel(this.apply(model, stack, world, entity, seed));
        }
    }

    @FunctionalInterface
    public interface BakedModelOverride {
        BakedModel apply(BakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed);
    }
}
