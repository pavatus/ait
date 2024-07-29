package loqor.ait.registry.impl;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITItems;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.CloakData;
import loqor.ait.tardis.data.EngineHandler;
import loqor.ait.tardis.data.ServerAlarmHandler;
import loqor.ait.tardis.data.ShieldData;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.mood.MoodDictatedEvent;
import loqor.ait.tardis.data.mood.TardisMood;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class MoodEventPoolRegistry {
    public static final SimpleRegistry<MoodDictatedEvent> REGISTRY =
            FabricRegistryBuilder.createSimple(RegistryKey.<MoodDictatedEvent>ofRegistry(
                    new Identifier(AITMod.MOD_ID, "mood_event_pool"))).buildAndRegister();
    private static final Random random = Random.create();
    public static MoodDictatedEvent register(MoodDictatedEvent schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    // Negative mood events
    public static MoodDictatedEvent CHANGE_DIM;
    public static MoodDictatedEvent RANDOM_THROTTLING_UP;
    public static MoodDictatedEvent KICK_PLAYER_OUT;
    public static MoodDictatedEvent CLOAKING_WHEN_AFRAID;
    public static MoodDictatedEvent FAST_RETURN_AND_TAKEOFF;
    public static MoodDictatedEvent RANDOM_ALARM_ACTIVATION;
    public static MoodDictatedEvent RANDOM_POWER_OFF;

    // Neutral mood events
    public static MoodDictatedEvent ACTIVATE_AUTOPILOT;
    public static MoodDictatedEvent LOCK_DOORS;
    public static MoodDictatedEvent SHIELD_ACTIVATION;

    // Positive mood events
    public static MoodDictatedEvent ADD_LOYALTY;
    public static MoodDictatedEvent RANDOM_XP_GRANT;

    /**
     * @author Loqor
     * ...
     * @implNote cost values should ONLY range between 16 and 256.
     * ...
     * for this, basically the pool will roll after the moods "race", resulting in a pool roll like so:
     * ...
     * # if the mood that won the race is negative:
     * - if it lands on a negative, it will trigger the event.
     * - if it lands on a positive, it will re-roll.
     * - if it lands on a neutral with no negative or positive moods, it will trigger the event.
     * - if it lands on a neutral with a positive mood, it will have a higher chance of re-rolling, but a small possibility of triggering the event.
     * - if it lands on a neutral with a negative mood, it will have a higher chance of triggering the event, but a small possibility of re-rolling.
     * ...
     * # if the mood that won the race is positive:
     * - if it lands on a positive, it will trigger the event.
     * - if it lands on a negative, it will re-roll.
     * - if it lands on a neutral with no negative or positive moods, it will trigger the event.
     * - if it lands on a neutral with a negative mood, it will have a higher chance of re-rolling, but a small possibility of triggering the event.
     * - if it lands on a neutral with a positive mood, it will have a higher chance of triggering the event, but a small possibility of re-rolling.
     * ...
     * # if the mood that won the race is neutral:
     * - depending on the swayed weight of the neutral mood, it will trigger the event that corresponds to said swayed weight.
     * - it does have a possibility of having little to no sway and just triggering any event it lands on.
     */
    public static void init() {

        CHANGE_DIM = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "change_dim"), (tardis) -> {
            List<ServerWorld> listOfDims = TardisUtil.getDimensions(tardis.travel().destination().getWorld().getServer());
            ServerWorld randomWorld = listOfDims.get(random.nextInt(listOfDims.size()));
            tardis.travel().destination(cached -> cached.world(randomWorld));
        }, 80, TardisMood.MoodType.NEGATIVE, TardisMood.Moods.HATEFUL, TardisMood.Moods.HURT));

        FAST_RETURN_AND_TAKEOFF = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "fast_return_and_takeoff"), (tardis) -> {
            TravelHandler travel = tardis.travel();
            if (tardis.door().isOpen()) tardis.door().closeDoors();
            travel.handbrake(false);
            boolean same = travel.destination().equals(travel.previousPosition());
            if (travel.previousPosition() != null) {
                travel.destination(same ? travel.position() : travel.previousPosition());
            }
            travel.dematerialize();
        }, 128, TardisMood.MoodType.NEGATIVE));

        RANDOM_POWER_OFF = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "random_power_off"), (tardis) -> {
            if (!tardis.travel().inFlight()) {
                tardis.getHandlers().<EngineHandler>get(TardisComponent.Id.ENGINE).disablePower();
            }
        }, 256, TardisMood.MoodType.NEGATIVE));

        RANDOM_ALARM_ACTIVATION = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "random_alarm_activation"), (tardis) -> {
            tardis.getHandlers().<ServerAlarmHandler>get(TardisComponent.Id.ALARMS).enable();
        }, 32, TardisMood.MoodType.NEGATIVE));

        RANDOM_THROTTLING_UP = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "random_throttling_up"), (tardis) -> {
            if (tardis.travel().inFlight())
                tardis.travel().increaseSpeed();
        }, 16, TardisMood.MoodType.NEGATIVE, TardisMood.Moods.HATEFUL, TardisMood.Moods.HURT));

        KICK_PLAYER_OUT = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "kick_player_out"), (tardis) -> {
            tardis.door().setLocked(false);
            tardis.door().openDoors();

            if (tardis.getDesktop().doorPos() != null)
                TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
                    SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1f, 1f);

            if (tardis.travel().position() != null)
                tardis.travel().position().getWorld().playSound(null, tardis.travel().position().getPos(),
                        SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1f, 1f);
        }, 32, TardisMood.MoodType.NEUTRAL));

        SHIELD_ACTIVATION = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "shield_activation"), (tardis) -> {
            tardis.getHandlers().<ShieldData>get(TardisComponent.Id.SHIELDS).enable();
            if (tardis.getHandlers().<ShieldData>get(TardisComponent.Id.SHIELDS).areVisualShieldsActive()) {
                tardis.getHandlers().<ShieldData>get(TardisComponent.Id.SHIELDS).disableVisuals();
            }
        }, 96, TardisMood.MoodType.NEUTRAL));

        CLOAKING_WHEN_AFRAID = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "cloaking_when_afraid"), (tardis) -> {
            tardis.getHandlers().<CloakData>get(TardisComponent.Id.CLOAK).enable();
        }, 96, TardisMood.MoodType.NEUTRAL, TardisMood.Moods.FEARFUL));

        ACTIVATE_AUTOPILOT = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "activate_autopilot"), (tardis) -> {
            tardis.travel().autopilot(true);
        }, 128, TardisMood.MoodType.NEUTRAL));

        ADD_LOYALTY = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "add_loyalty"), (tardis) -> {
            TardisUtil.getPlayersInsideInterior(tardis).forEach(player -> tardis.getHandlers().<LoyaltyHandler>get(TardisComponent.Id.LOYALTY).get(player).add(7));
        }, 64, TardisMood.MoodType.POSITIVE));

        RANDOM_XP_GRANT = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "random_xp_grant"), (tardis) -> {
            TardisUtil.getPlayersInsideInterior(tardis).forEach(player -> player.addExperience(600));
        }, 32, TardisMood.MoodType.POSITIVE));


        // TODO: should make this happen when a low loyalty player approaches the TARDIS

        LOCK_DOORS = register(MoodDictatedEvent.Builder.create(new Identifier(AITMod.MOD_ID, "lock_doors"), (tardis) -> {
            tardis.door().setLocked(true);
            if (tardis.getDesktop().doorPos() != null)
                TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
                        SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.BLOCKS, 1f, 1f);

            if (tardis.travel().position() != null)
                tardis.travel().position().getWorld().playSound(null, tardis.travel().position().getPos(),
                        SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.BLOCKS, 1f, 1f);
        }, 48, TardisMood.MoodType.NEUTRAL));

    }
}
