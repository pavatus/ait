package loqor.ait.tardis;

import java.util.List;
import java.util.Optional;

import io.wispforest.owo.ops.WorldOps;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.BlockData;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.util.StackUtil;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.exterior.variant.adaptive.AdaptiveVariant;
import loqor.ait.tardis.util.Gaslighter3000;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

public class TardisExterior extends TardisComponent {

    public static final Identifier CHANGE_EXTERIOR = new Identifier(AITMod.MOD_ID, "change_exterior");

    private static final ExteriorCategorySchema MISSING_CATEGORY = CategoryRegistry.getInstance().fallback();
    private static final ExteriorVariantSchema MISSING_VARIANT = ExteriorVariantRegistry.getInstance().fallback();

    private ExteriorCategorySchema category;
    private ExteriorVariantSchema variant;

    @Exclude
    private List<BlockData> disguiseCache;

    static {
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_EXTERIOR, ServerTardisManager.receiveTardis((tardis, server, player, handler, buf, responseSender) -> {
            Identifier exteriorValue = Identifier.tryParse(buf.readString());

            boolean variantChange = buf.readBoolean();
            Identifier variantValue = buf.readIdentifier();

            ExteriorVariantSchema schema = ExteriorVariantRegistry.getInstance()
                    .get(variantValue);

            // no hax
            if (!tardis.isUnlocked(schema))
                return;

            server.execute(() -> StackUtil.playBreak(player));
            tardis.getExterior().setType(CategoryRegistry.getInstance().get(exteriorValue));

            if (variantChange)
                tardis.getExterior().setVariant(schema);

            WorldOps.updateIfOnServer(server.getWorld(tardis.travel().position().getWorld().getRegistryKey()),
                    tardis.travel().position().getPos());

            TardisEvents.EXTERIOR_CHANGE.invoker().onChange(tardis);
        }));

        TardisEvents.ENTER_FLIGHT.register(tardis -> {
            if (isDisguised(tardis))
                tardis.getExterior().clearDisguise();
        });

        TardisEvents.LANDED.register(tardis -> {
            if (isDisguised(tardis))
                tardis.getExterior().recalcDisguise(tardis);
        });

        TardisEvents.SEND_TARDIS.register((tardis, player) -> {
            if (isDisguised(tardis))
                tardis.getExterior().recalcDisguise(tardis);
        });
    }

    private static boolean isDisguised(Tardis tardis) {
        return tardis.getExterior().getVariant() instanceof AdaptiveVariant;
    }

    public void clearDisguise() {
        if (this.disguiseCache == null)
            return;

        DirectedGlobalPos.Cached cached = tardis.travel().position();
        ServerWorld world = cached.getWorld();

        Gaslighter3000 gaslighter = new Gaslighter3000(world);

        for (BlockData data : this.disguiseCache) {
            BlockPos pos = data.pos();

            shitParticles(world, pos);
            gaslighter.spreadLies(pos, world.getBlockState(pos));
        }

        gaslighter.tweet();
        this.disguiseCache = null;
    }

    public void recalcDisguise(Tardis tardis) {
        long start = System.currentTimeMillis();
        DirectedGlobalPos.Cached cached = tardis.travel().position();
        ServerWorld world = cached.getWorld();

        if (this.disguiseCache == null)
            this.disguiseCache = tardis.<BiomeHandler>handler(Id.BIOME).testBiome(world, cached.getPos());

        if (this.disguiseCache == null)
            return;

        Gaslighter3000 gaslighter = new Gaslighter3000(world);

        for (BlockData data : this.disguiseCache) {
            BlockPos pos = data.pos();

            shitParticles(world, pos);
            gaslighter.spreadLies(pos, data.state());
        }

        gaslighter.tweet();
        System.out.println("Recalculated exterior in " + (System.currentTimeMillis() - start) + "ms");
    }

    private static void shitParticles(ServerWorld world, BlockPos pos) {
        world.spawnParticles(ParticleTypes.END_ROD, pos.getX(), pos.getY(), pos.getZ(), 12, 0.5, 0.5, 0.5, 0);
    }

    public TardisExterior(ExteriorVariantSchema variant) {
        super(Id.EXTERIOR);

        this.category = variant.category();
        this.variant = variant;
    }

    private void setMissing() {
        if (this.tardis instanceof ClientTardis clientTardis)
            ClientTardisUtil.changeExteriorWithScreen(clientTardis, MISSING_CATEGORY.id(), MISSING_VARIANT.id(), true);

        this.category = MISSING_CATEGORY;
        this.variant = MISSING_VARIANT;
    }

    public ExteriorCategorySchema getCategory() {
        if (this.category == null)
            this.setMissing();

        return category;
    }

    public ExteriorVariantSchema getVariant() {
        if (this.variant == null)
            this.setMissing();

        return variant;
    }

    public void setType(ExteriorCategorySchema exterior) {
        this.category = exterior;

        if (exterior != this.getVariant().category()) {
            AITMod.LOGGER.error("Force changing exterior variant to a random one to ensure it matches!");
            this.setVariant(ExteriorVariantRegistry.getInstance().pickRandomWithParent(exterior));
        }

        this.sync();
    }

    public void setVariant(ExteriorVariantSchema variant) {
        this.variant = variant;
        this.sync();
    }

    public Optional<ExteriorBlockEntity> findExteriorBlock() {
        BlockEntity found = tardis.travel().position().getWorld().getBlockEntity(tardis.travel().position().getPos());

        if (!(found instanceof ExteriorBlockEntity exterior))
            return Optional.empty();

        return Optional.of(exterior);
    }
}
