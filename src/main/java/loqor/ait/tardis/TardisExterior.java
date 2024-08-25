package loqor.ait.tardis;

import java.util.List;
import java.util.Optional;

import io.wispforest.owo.ops.WorldOps;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.BlockData;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.events.FakeBlockEvents;
import loqor.ait.core.util.StackUtil;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.exterior.variant.adaptive.AdaptiveVariant;
import loqor.ait.tardis.util.Gaslighter3000;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.util.math.Vec3d;

public class TardisExterior extends TardisComponent {

    public static final Identifier CHANGE_EXTERIOR = new Identifier(AITMod.MOD_ID, "change_exterior");

    private static final ExteriorCategorySchema MISSING_CATEGORY = CategoryRegistry.getInstance().fallback();
    private static final ExteriorVariantSchema MISSING_VARIANT = ExteriorVariantRegistry.getInstance().fallback();

    private ExteriorCategorySchema category;
    private ExteriorVariantSchema variant;

    @Exclude
    private List<BlockData> disguiseCache;

    @Exclude
    private Gaslighter3000 gaslighter;

    static {
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_EXTERIOR, ServerTardisManager.receiveTardis((tardis, server, player, handler, buf, responseSender) -> {
            boolean variantChange = buf.readBoolean();
            Identifier variantValue = buf.readIdentifier();

            ExteriorVariantSchema schema = ExteriorVariantRegistry.getInstance()
                    .get(variantValue);

            if (!tardis.getExterior().update(schema, variantChange))
                return;

            server.execute(() -> StackUtil.playBreak(player));
        }));

        TardisEvents.ENTER_FLIGHT.register(tardis -> {
            if (isDisguised(tardis))
                tardis.getExterior().clearDisguise();
        });

        TardisEvents.LANDED.register(tardis -> {
            if (isDisguised(tardis))
                tardis.getExterior().applyDisguise();
        });

        TardisEvents.SEND_TARDIS.register((tardis, player) -> {
            if (isDisguised(tardis) && !player.isInTeleportationState())
                tardis.getExterior().applyDisguise(player);
        });

        TardisEvents.EXTERIOR_CHANGE.register(tardis -> {
            TardisExterior exterior = tardis.getExterior();

            if (exterior.getVariant() instanceof AdaptiveVariant) {
                exterior.applyDisguise();
                return;
            }

            exterior.clearDisguise();
        });

        TardisEvents.USE_DOOR.register((tardis, player) -> {
            if (player == null)
                return;

            TardisExterior exterior = tardis.getExterior();

            if (!(exterior.getVariant() instanceof AdaptiveVariant))
                return;

            if (tardis.door().isClosed()) {
                exterior.applyDisguise(player);
                return;
            }

            DirectedGlobalPos.Cached cached = tardis.travel().position();
            Optional<ExteriorBlockEntity> blockEntity = exterior.findExteriorBlock();

            if (blockEntity.isEmpty())
                return;

            player.networkHandler.sendPacket(new BlockUpdateS2CPacket(cached.getWorld(), cached.getPos()));
            player.networkHandler.sendPacket(new BlockUpdateS2CPacket(cached.getWorld(), cached.getPos().up()));
            player.networkHandler.sendPacket(BlockEntityUpdateS2CPacket.create(blockEntity.get()));
        });

        FakeBlockEvents.CHECK.register((player, state, pos) -> {
            if (state.isOf(AITBlocks.EXTERIOR_BLOCK))
                return;

            shitParticles(player.getServerWorld(), pos);
            player.networkHandler.sendPacket(new BlockUpdateS2CPacket(player.getServerWorld(), pos));
        });
    }

    private boolean update(ExteriorVariantSchema variant, boolean variantChange) {
        if (!tardis.isUnlocked(variant))
            return false;

        tardis.getExterior().setType(variant.category());

        if (variantChange)
            tardis.getExterior().setVariant(variant);

        DirectedGlobalPos.Cached cached = tardis.travel().position();
        WorldOps.updateIfOnServer(cached.getWorld(), cached.getPos());

        TardisEvents.EXTERIOR_CHANGE.invoker().onChange(tardis);
        return true;
    }

    private static boolean isDisguised(Tardis tardis) {
        return tardis.getExterior().getVariant() instanceof AdaptiveVariant;
    }

    public void clearDisguise() {
        if (this.disguiseCache == null || this.gaslighter == null)
            return;

        DirectedGlobalPos.Cached cached = tardis.travel().position();
        ServerWorld world = cached.getWorld();

        this.gaslighter = new Gaslighter3000(world);

        for (BlockData data : this.disguiseCache) {
            shitParticles(world, data.pos());
            gaslighter.reset(data);
        }

        gaslighter.tweet();

        this.gaslighter = null;
        this.disguiseCache = null;
    }

    public boolean recalcDisguise() {
        long start = System.currentTimeMillis();
        DirectedGlobalPos.Cached cached = tardis.travel().position();
        ServerWorld world = cached.getWorld();

        if (this.disguiseCache == null)
            this.disguiseCache = tardis.<BiomeHandler>handler(Id.BIOME).testBiome(world, cached.getPos());

        if (this.disguiseCache == null)
            return false;

        this.gaslighter = new Gaslighter3000(world);

        for (BlockData data : this.disguiseCache) {
            shitParticles(world, data.pos());
            gaslighter.spreadLies(data);
        }

        System.out.println("Recalculated exterior in " + (System.currentTimeMillis() - start) + "ms");
        return true;
    }

    private void applyDisguise(ServerPlayerEntity player) {
        if (this.gaslighter == null && !this.recalcDisguise())
            return;

        this.gaslighter.tweet(player);
    }

    private void applyDisguise() {
        if (this.gaslighter == null && !this.recalcDisguise())
            return;

        this.gaslighter.tweet();
    }

    private static void shitParticles(ServerWorld world, BlockPos pos) {
        Vec3d center = pos.toCenterPos();
        world.spawnParticles(ParticleTypes.END_ROD, center.getX(), center.getY(), center.getZ(), 12, 0.3, 0.3, 0.3, 0);
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
