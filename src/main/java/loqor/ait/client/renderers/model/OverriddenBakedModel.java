package loqor.ait.client.renderers.model;

import loqor.ait.AITMod;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * @implNote This model overrides the default ModelOverrideList functionality!
 * That means that resourcepacks won't be able to use predicates on this item!
 */
public class OverriddenBakedModel implements BakedModel {

    private final BakedModel original;
    private final ModelOverrideList overrides;
    private static final Renderer RENDERER = RendererAccess.INSTANCE.getRenderer();
    private static final int BLEND_MODE = BlendMode.values().length;
    public static final RenderMaterial[] EMISSIVE_MAT = new RenderMaterial[BLEND_MODE];
    private final BakedModelManager MODEL_MANAGER = MinecraftClient.getInstance().getBakedModelManager();
    public static final RenderMaterial[] NON_EMISSIVE_MAT = new RenderMaterial[BLEND_MODE];

    static {
        if (RendererAccess.INSTANCE.hasRenderer()) {
            for (int ordinal = 0; ordinal < BLEND_MODE; ordinal++) {
                BlendMode mode = BlendMode.values()[ordinal];
                EMISSIVE_MAT[ordinal] = RENDERER.materialFinder()
                        .blendMode(mode)
                        .emissive(true)
                        .ambientOcclusion(TriState.FALSE)
                        .disableDiffuse(true)
                        .find();
                NON_EMISSIVE_MAT[ordinal] = RENDERER.materialFinder()
                        .blendMode(mode)
                        .find();
            }
        } else {
            AITMod.LOGGER.warn("No emissive texture exists.");
        }
    }

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
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        if (!RendererAccess.INSTANCE.hasRenderer()) {
            BakedModel.super.emitItemQuads(stack, randomSupplier, context);
            return;
        }

        MeshBuilder builder = RENDERER.meshBuilder();
        RenderContext.QuadTransform transform = quad -> false;

        context.pushTransform(transform);
        BakedModel.super.emitItemQuads(stack, randomSupplier, context);
        context.popTransform();

        /* The overlay quads must be emitted after the main mesh has been rendered so that they render over
           other translucent quads. This also fixes an issue where the original item would be invisible
           when an overlay was rendered. */
        /*if (transform.emittedAny()) {
            context.meshConsumer().accept(builder.build());
        }*/
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

    /* private static class OverlayQuadTransform implements RenderContext.QuadTransform {
        private static final int VERTS_PER_QUAD = 4;
        private final QuadEmitter EMITTER;
        private final SpriteAtlasTexture ATLAS;
        private boolean emittedAny;

        /**
         * Creates a new overlay transform.
         * @param emitter       emitter to emit overlay quads to
         * @param atlasTexture    texture atlas for block textures
         */
        /* public OverlayQuadTransform(QuadEmitter emitter, SpriteAtlasTexture atlasTexture) {
            EMITTER = emitter;
            ATLAS = atlasTexture;
        }

       @Override
        public boolean transform(MutableQuadView quad) {
            SpriteAtlasTexture baseSprite = spriteFromQuad(quad);
            Optional<OverlayMetadata> metadataOptional = MetadataRegistry.INSTANCE
                    .metadataFromSpriteName(AITMod.MOD_ID, baseSprite.contents().name())
                    .map(((metadata) -> (OverlayMetadata) metadata));

            if (metadataOptional.isEmpty()) {
                return true;
            }

            EMITTER.copyFrom(quad);

            OverlayMetadata metadata = metadataOptional.get();
            BlendMode blendMode;

            if (metadata.transparencyMode() == Transparency.TRANSLUCENT) {
                blendMode = BlendMode.TRANSLUCENT;
            } else {
                blendMode = quad.material().blendMode();
            }

            EMITTER.material((metadata.isEmissive() ? EMISSIVE_MATERIAL : NON_EMISSIVE_MATERIAL)[blendMode.ordinal()]);

            int facing = quad.lightFace().ordinal();
            SpriteAtlasTexture overlaySprite = ATLAS.getSprite(metadata.overlaySpriteName());
            for (int vertexIndex = 0; vertexIndex < VERTS_PER_QUAD; vertexIndex++) {
                float x = EMITTER.x(vertexIndex);
                float y = EMITTER.y(vertexIndex);
                float z = EMITTER.z(vertexIndex);
                EMITTER.pos(vertexIndex, x + X_OFFSETS[facing], y + Y_OFFSETS[facing], z + Z_OFFSETS[facing]);
                EMITTER.uv(
                        vertexIndex,
                        OverlayQuadFunction.recomputeSpriteCoordinate(
                                EMITTER.u(vertexIndex),
                                baseSprite,
                                overlaySprite,
                                SpriteAtlasTexture::getU0,
                                SpriteAtlasTexture::getU1
                        ),
                        OverlayQuadFunction.recomputeSpriteCoordinate(
                                EMITTER.v(vertexIndex),
                                baseSprite,
                                overlaySprite,
                                SpriteAtlasTexture::getV0,
                                SpriteAtlasTexture::getV1
                        )
                );
            }

            EMITTER.emit();
            emittedAny = true;
            return true;
        }

        *//**
         * Checks if any quads were emitted from this transform.
         * @return whether any quads were emitted
         *//*
        public boolean emittedAny() {
            return emittedAny;
        }

        *//**
         * Gets the sprite used by a given quad.
         *
         * @param quad quad to get the sprite of
         * @return quad's sprite
         *//*
        private Sprite spriteFromQuad(QuadView quad) {
            return SpriteFinder.get(ATLAS).find(quad);
        }

    }*/
}
