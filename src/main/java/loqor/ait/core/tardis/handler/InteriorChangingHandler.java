package loqor.ait.core.tardis.handler;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.item.ChargedZeitonCrystalItem;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.handler.properties.Property;
import loqor.ait.tardis.handler.properties.Value;
import loqor.ait.tardis.handler.properties.bool.BoolProperty;
import loqor.ait.tardis.handler.properties.bool.BoolValue;
import loqor.ait.tardis.handler.travel.TravelHandler;
import loqor.ait.tardis.util.TardisUtil;

public class InteriorChangingHandler extends KeyedTardisComponent implements TardisTickable {

    private static final BoolProperty IS_REGENERATING_PROPERTY = new BoolProperty("regenerating", false);
    private final BoolValue isRegenerating = IS_REGENERATING_PROPERTY.create(this);
    private static final Property<Identifier> QUEUED_INTERIOR_PROPERTY = new Property<>(Property.Type.IDENTIFIER,
            "queued_interior", new Identifier(""));
    private final Value<Identifier> queuedInterior = QUEUED_INTERIOR_PROPERTY.create(this);
    public static final Identifier CHANGE_DESKTOP = new Identifier(AITMod.MOD_ID, "change_desktop");

    public InteriorChangingHandler() {
        super(Id.INTERIOR);
    }

    @Override
    public void onLoaded() {
        queuedInterior.of(this, QUEUED_INTERIOR_PROPERTY);
        isRegenerating.of(this, IS_REGENERATING_PROPERTY);
    }

    static {
        TardisEvents.DEMAT.register(tardis -> {
            if (tardis.isGrowth()
                    || tardis.<InteriorChangingHandler>handler(TardisComponent.Id.INTERIOR).isGenerating())
                return TardisEvents.Interaction.FAIL;

            return TardisEvents.Interaction.PASS;
        });

        TardisEvents.MAT.register(tardis -> {
            if (!tardis.isGrowth())
                return TardisEvents.Interaction.PASS;

            tardis.getExterior().setType(CategoryRegistry.CAPSULE);
            return TardisEvents.Interaction.SUCCESS; // force mat even if checks fail
        });
    }

    private void setGenerating(boolean var) {
        isRegenerating.set(var);
    }

    public boolean isGenerating() {
        return isRegenerating.get();
    }

    private void setQueuedInterior(TardisDesktopSchema schema) {
        queuedInterior.set(schema.id());
    }

    public TardisDesktopSchema getQueuedInterior() {
        return DesktopRegistry.getInstance().get(queuedInterior.get());
    }

    public void queueInteriorChange(TardisDesktopSchema schema) {
        if (!this.canQueue())
            return;

        if (tardis.fuel().getCurrentFuel() < 5000) {
            for (PlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis.asServer())) {
                player.sendMessage(
                        Text.translatable("tardis.message.interiorchange.not_enough_fuel").formatted(Formatting.RED),
                        true);
                return;
            }
        }

        AITMod.LOGGER.info("Queueing interior change for {} to {}", this.tardis, schema);

        setQueuedInterior(schema);
        setGenerating(true);

        DeltaTimeManager.createDelay("interior_change-" + tardis.getUuid().toString(), 100L);
        tardis.alarm().enabled().set(true);

        tardis.getDesktop().getConsolePos().clear();

        if (!tardis.hasGrowthDesktop())
            tardis.removeFuel(5000 * tardis.travel().instability());
    }

    private void onCompletion() {
        this.setGenerating(false);
        clearedOldInterior = false;

        tardis.alarm().enabled().set(false);

        boolean previouslyLocked = tardis.door().previouslyLocked().get();
        DoorHandler.lockTardis(previouslyLocked, tardis, null, false);

        tardis.engine().unlinkEngine();

        if (tardis.hasGrowthExterior()) {
            TravelHandler travel = tardis.travel();

            travel.autopilot(true);
            travel.forceDemat();
        }

        TardisUtil.sendMessageToLinked(tardis.asServer(), Text.translatable("tardis.message.interiorchange.success", tardis.stats().getName(), tardis.getDesktop().getSchema().name()));
    }

    private void warnPlayers() {
        for (PlayerEntity player : TardisUtil.getPlayersInsideInterior(this.tardis.asServer())) {
            player.sendMessage(Text.translatable("tardis.message.interiorchange.warning").formatted(Formatting.RED),
                    true);
        }
    }

    private boolean clearedOldInterior = false;

    @Override
    public void tick(MinecraftServer server) {
        boolean isGenerating = this.isGenerating();

        if (server.getTicks() % 10 == 0 && this.tardis.isGrowth()) {
            this.generateInteriorWithItem();

            if (!isGenerating) {
                if (this.tardis.door().isBothClosed()) {
                    this.tardis.door().openDoors();
                } else {
                    this.tardis.door().setLocked(false);
                }
            }
        }

        if (!isGenerating)
            return;

        if (DeltaTimeManager.isStillWaitingOnDelay("interior_change-" + this.tardis().getUuid().toString()))
            return;

        TravelHandler travel = this.tardis().travel();

        if (server.getTicks() % 10 == 0 && travel.getState() == TravelHandler.State.FLIGHT && !travel.isCrashing())
            travel.crash();

        if (this.isGenerating())
            tardis.alarm().enabled().set(true);

        if (!this.canQueue()) {
            this.setGenerating(false);
            this.tardis.alarm().enabled().set(false);
            return;
        }

        boolean isEmpty = TardisUtil.isInteriorEmpty(tardis.asServer());

        if (!isEmpty) {
            warnPlayers();
            return;
        }

        if (!this.tardis().door().locked())
            DoorHandler.lockTardis(true, this.tardis(), null, true);

        if (clearedOldInterior) {
            this.tardis().getDesktop().changeInterior(this.getQueuedInterior(), true);
            onCompletion();

            return;
        }

        this.tardis().getDesktop().clearOldInterior(this.getQueuedInterior());

        DeltaTimeManager.createDelay("interior_change-" + this.tardis().getUuid().toString(), 15000L);
        this.clearedOldInterior = true;
    }

    protected void generateInteriorWithItem() {
        TardisUtil.getEntitiesInInterior(this.tardis, 50).stream()
                .filter(entity -> entity instanceof ItemEntity item
                        && (item.getStack().getItem() == Items.NETHER_STAR || isChargedCrystal(item.getStack()))
                        && entity.isTouchingWater())
                .forEach(entity -> {
                    DirectedGlobalPos position = this.tardis.travel().position();

                    if (position == null)
                        return;

                    this.tardis.setFuelCount(8000);

                    entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT,
                            SoundCategory.BLOCKS, 10.0F, 0.75F);
                    entity.getWorld().playSound(null, position.getPos(), SoundEvents.BLOCK_BEACON_POWER_SELECT,
                            SoundCategory.BLOCKS, 10.0F, 0.75F);

                    this.queueInteriorChange(DesktopRegistry.getInstance().getRandom(this.tardis));

                    if (this.isGenerating())
                        entity.discard();
                });
    }

    private boolean isChargedCrystal(ItemStack stack) {
        if (!(stack.getItem() instanceof ChargedZeitonCrystalItem))
            return false;

        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains(ChargedZeitonCrystalItem.FUEL_KEY))
            return false;

        return nbt.getDouble(ChargedZeitonCrystalItem.FUEL_KEY) >= ChargedZeitonCrystalItem.MAX_FUEL;
    }

    private boolean canQueue() {
        return tardis.isGrowth() || tardis.engine().hasPower() || tardis.crash().isToxic();
    }
}
