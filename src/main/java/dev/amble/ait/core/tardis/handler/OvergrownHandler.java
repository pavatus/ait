package dev.amble.ait.core.tardis.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import dev.amble.ait.api.KeyedTardisComponent;
import dev.amble.ait.api.TardisEvents;
import dev.amble.ait.api.TardisTickable;
import dev.amble.ait.core.advancement.TardisCriterions;
import dev.amble.ait.core.tardis.handler.travel.TravelHandlerBase;
import dev.amble.ait.data.Exclude;
import dev.amble.ait.data.properties.bool.BoolProperty;
import dev.amble.ait.data.properties.bool.BoolValue;
import dev.amble.ait.data.schema.exterior.ClientExteriorVariantSchema;

public class OvergrownHandler extends KeyedTardisComponent implements TardisTickable {

    private static final BoolProperty IS_OVERGROWN_PROPERTY = new BoolProperty("is_overgrown", false);
    private final BoolValue overgrown = IS_OVERGROWN_PROPERTY.create(this);

    @Exclude
    private static final int TIME_TO_OVERGROW = 24000;

    public static String TEXTURE_PATH = "textures/blockentities/exteriors/";
    private int ticks = 24000;
    private boolean ticking = false;

    static {
        TardisEvents.USE_DOOR.register((tardis, interior, world, player, pos) -> {
            if (!tardis.overgrown().isOvergrown() || player == null)
                return DoorHandler.InteractionResult.CONTINUE;

            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

            if (stack.getItem() instanceof ShearsItem) {
                player.swingHand(Hand.MAIN_HAND);
                tardis.overgrown().removeVegetation();
                stack.damage(1, player, (p) -> p.sendToolBreakStatus(Hand.MAIN_HAND));

                TardisCriterions.VEGETATION.trigger(player);
                return DoorHandler.InteractionResult.BANG;
            }

            return DoorHandler.InteractionResult.KNOCK;
        });
    }

    public OvergrownHandler() {
        super(Id.OVERGROWN);
    }

    @Override
    public void onLoaded() {
        overgrown.of(this, IS_OVERGROWN_PROPERTY);
    }

    public boolean isOvergrown() {
        return overgrown.get();
    }

    public void setOvergrown(boolean var) {
        overgrown.set(var);
    }

    public void removeVegetation() {
        this.setOvergrown(false);
        this.ticks = 0;
        this.ticking = false;
    }

    @Environment(EnvType.CLIENT)
    public Identifier getOvergrownTexture() {
        ClientExteriorVariantSchema variant = tardis.getExterior().getVariant().getClient();
        Identifier baseTexture = variant.texture();

        if (baseTexture.getPath().contains("police_box")) {
            return new Identifier("ait", "textures/blockentities/exteriors/police_box/overgrown.png");
        }

        return baseTexture.withSuffixedPath("_overgrown");
    }

    @Override
    public void tick(MinecraftServer server) {
        if (!tardis().fuel().hasPower()) {
            if (tardis.isGrowth() || this.isOvergrown()) {
                this.ticking = false;
                return;
            }

            if (tardis.travel().getState() != TravelHandlerBase.State.LANDED) {
                this.ticking = false;
                return;
            }

            if (tardis.travel().getState() == TravelHandlerBase.State.FLIGHT) {
                this.setOvergrown(false);
                this.ticking = false;
                this.ticks = 0;
                return;
            }

            if (!this.ticking) {
                this.ticking = true;
                this.ticks = 0;
            }

            if (++this.ticks >= TIME_TO_OVERGROW) {
                this.setOvergrown(true);
                this.ticking = false;
            }
        } else {
            this.ticking = false;
        }
    }
}
