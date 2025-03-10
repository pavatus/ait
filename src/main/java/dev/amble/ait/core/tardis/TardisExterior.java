package dev.amble.ait.core.tardis;

import java.util.Optional;

import dev.amble.lib.data.CachedDirectedGlobalPos;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import dev.amble.ait.AITMod;
import dev.amble.ait.api.TardisComponent;
import dev.amble.ait.api.TardisEvents;
import dev.amble.ait.client.tardis.ClientTardis;
import dev.amble.ait.client.util.ClientTardisUtil;
import dev.amble.ait.core.blockentities.ExteriorBlockEntity;
import dev.amble.ait.core.tardis.manager.ServerTardisManager;
import dev.amble.ait.core.util.StackUtil;
import dev.amble.ait.data.schema.exterior.ExteriorCategorySchema;
import dev.amble.ait.data.schema.exterior.ExteriorVariantSchema;
import dev.amble.ait.registry.impl.CategoryRegistry;
import dev.amble.ait.registry.impl.exterior.ExteriorVariantRegistry;

public class TardisExterior extends TardisComponent {

    public static final Identifier CHANGE_EXTERIOR = AITMod.id("change_exterior");

    private static final ExteriorCategorySchema MISSING_CATEGORY = CategoryRegistry.getInstance().fallback();
    private static final ExteriorVariantSchema MISSING_VARIANT = ExteriorVariantRegistry.getInstance().fallback();

    private ExteriorCategorySchema category;
    private ExteriorVariantSchema variant;

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
    }

    private boolean update(ExteriorVariantSchema variant, boolean variantChange) {
        if (!tardis.isUnlocked(variant))
            return false;

        tardis.getExterior().setType(variant.category());

        if (variantChange)
            tardis.getExterior().setVariant(variant);

        CachedDirectedGlobalPos cached = tardis.travel().position();
        cached.getWorld().getChunkManager().markForUpdate(cached.getPos());

        TardisEvents.EXTERIOR_CHANGE.invoker().onChange(tardis);
        return true;
    }

    public TardisExterior(ExteriorVariantSchema variant) {
        super(Id.EXTERIOR);

        this.category = variant.category();
        this.variant = variant;
    }

    private void setMissing() {
        if (this.tardis instanceof ClientTardis clientTardis)
            ClientTardisUtil.changeExteriorWithScreen(clientTardis, MISSING_VARIANT.id(), true);

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
        if (tardis.travel().position().getWorld().isClient()) return Optional.empty();

        BlockEntity found = tardis.travel().position().getWorld().getBlockEntity(tardis.travel().position().getPos());

        if (!(found instanceof ExteriorBlockEntity exterior))
            return Optional.empty();

        return Optional.of(exterior);
    }

    public void playSound(SoundEvent sound, SoundCategory category, float volume, float pitch) {
        CachedDirectedGlobalPos pos = tardis.travel().position();

        pos.getWorld().playSound(null, pos.getPos(), sound, category, volume, pitch);
    }

    public void playSound(SoundEvent sound, SoundCategory category) {
        this.playSound(sound, category, 1f, 1f);
    }

    public void playSound(SoundEvent sound) {
        this.playSound(sound, SoundCategory.BLOCKS);
    }
}
