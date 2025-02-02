package loqor.ait.core.item;

import java.util.*;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.GameEvent.Emitter;

import loqor.ait.api.link.LinkableItem;
import loqor.ait.core.AITSounds;
import loqor.ait.core.AITTags;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.data.enummap.EnumSet;
import loqor.ait.data.enummap.Ordered;

public class TardisGoatHorn extends LinkableItem {
    private static final String INSTRUMENT_KEY = "instrument";
    private final TagKey<Instrument> instrumentTag;
    private final EnumSet<TardisGoatHorn.Protocols> protocols;

    public TardisGoatHorn(Settings settings, TagKey<Instrument> instrumentTag, TardisGoatHorn.Protocols... abs) {
        super(settings.maxCount(1), true);
        this.instrumentTag = instrumentTag;

        this.protocols = new EnumSet<>(TardisGoatHorn.Protocols::values);
        this.protocols.addAll(abs);
    }

    public enum Protocols implements Ordered {
        SNAP, HAIL, PERCEPTION, SKELETON;

        @Override
        public int index() {
            return ordinal();
        }
    }

    public boolean hasProtocol(TardisGoatHorn.Protocols var) {
        return this.protocols.contains(var);
    }

    public static boolean isHornInInventory(PlayerEntity player) {
        return player.getInventory().contains(AITTags.Items.KEY);
    }

    public static Collection<ItemStack> getHornInInventory(PlayerEntity player) {
        List<ItemStack> items = new ArrayList<>();

        for (ItemStack stack : player.getInventory().main) {
            if (stack != null && stack.getItem() instanceof KeyItem)
                items.add(stack);
        }

        return items;
    }

    public static boolean hasMatchingHornInInventory(PlayerEntity player, Tardis tardis) {
        Collection<ItemStack> keys = getHornInInventory(player);

        for (ItemStack stack : keys) {
            KeyItem key = (KeyItem) stack.getItem();

            Tardis found = KeyItem.getTardis(player.getWorld(), stack);

            if (found == tardis)
                return true;
        }

        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        Tardis tardis = KeyItem.getTardis(world, stack);

        if (tardis == null)
            return;
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        Entity owner = entity.getOwner();

        if (!(owner instanceof ServerPlayerEntity player))
            return;

        Tardis tardis = KeyItem.getTardis(entity.getWorld(), entity.getStack());

        if (tardis == null)
            return;

        tardis.loyalty().subLevel(player, 35);
        tardis.getDesktop().playSoundAtEveryConsole(AITSounds.CLOISTER);
    }

    private static void hailMary(Tardis tardis, ItemStack stack, PlayerEntity player) {
//        if (player.getItemCooldownManager().isCoolingDown(stack.getItem()))
//            return;
//
//        if (!tardis.stats().hailMary().get())
//            return;

        TravelHandler travel = tardis.travel();
        TardisGoatHorn hornType = (TardisGoatHorn) stack.getItem().asItem();

//        if (travel.handbrake())
//            return;
//
//        if (!keyType.hasProtocol(KeyItem.Protocols.HAIL))
//            return;
//
//        if (!tardis.loyalty().get(player).isOf(Loyalty.Type.PILOT))
//            return;
//
//        if (player.getHealth() > 4)
//            return;

        World world = player.getWorld();
        BlockPos pos = player.getBlockPos();



        tardis.travel().dematerialize();

    }

    /*
     * @Override public ActionResult useOnBlock(ItemUsageContext context) { World
     * world = context.getWorld(); BlockPos pos = context.getBlockPos();
     * PlayerEntity player = context.getPlayer(); ItemStack stack =
     * context.getStack();
     *
     * if (world.isClient()) return ActionResult.SUCCESS;
     *
     * if (player == null || !player.isSneaking()) return ActionResult.PASS;
     *
     * if (!(world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock))
     * return ActionResult.PASS;
     *
     * if (consoleBlock.tardis().isEmpty()) return ActionResult.FAIL;
     *
     * Tardis tardis = consoleBlock.tardis().get();
     *
     * if (tardis.loyalty().get(player).isOf(Loyalty.Type.COMPANION)) {
     * this.link(stack, consoleBlock.tardis().get()); return ActionResult.SUCCESS; }
     *
     * player.sendMessage(Text.translatable("message.ait.tardis.trust_issue",
     * true)); return ActionResult.FAIL; }
     */


    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Optional<RegistryKey<Instrument>> optional = this.getInstrument(stack).flatMap(RegistryEntry::getKey);
        if (optional.isPresent()) {
            MutableText mutableText = Text.translatable(Util.createTranslationKey("instrument", ((RegistryKey)optional.get()).getValue()));
            tooltip.add(mutableText.formatted(Formatting.GRAY));
        }

    }

    public static ItemStack getStackForInstrument(Item item, RegistryEntry<Instrument> instrument) {
        ItemStack itemStack = new ItemStack(item);
        setInstrument(itemStack, instrument);
        return itemStack;
    }

    public static void setRandomInstrumentFromTag(ItemStack stack, TagKey<Instrument> instrumentTag, Random random) {
        Optional<RegistryEntry<Instrument>> optional = Registries.INSTRUMENT.getEntryList(instrumentTag).flatMap((entryList) -> {
            return entryList.getRandom(random);
        });
        optional.ifPresent((instrument) -> {
            setInstrument(stack, instrument);
        });
    }

    private static void setInstrument(ItemStack stack, RegistryEntry<Instrument> instrument) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putString("instrument", ((RegistryKey)instrument.getKey().orElseThrow(() -> {
            return new IllegalStateException("Invalid instrument");
        })).getValue().toString());
    }

    public static Tardis getTardis(World world, ItemStack stack) {
        return LinkableItem.getTardisFromUuid(world, stack, "tardis");
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        /*Tardis tardis = TardisGoatHorn.getTardis(world, itemStack);

        Optional<? extends RegistryEntry<Instrument>> optional = this.getInstrument(itemStack);
        if (optional.isPresent()) {

            Instrument instrument = (Instrument)((RegistryEntry)optional.get()).value();
            user.setCurrentHand(hand);
            playSound(world, user, instrument);
            user.getItemCooldownManager().set(this, instrument.useDuration());
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            TardisGoatHorn.hailMary(tardis, itemStack, user);
            return TypedActionResult.consume(itemStack);

        } else {*/
            return TypedActionResult.fail(itemStack);
        //}
    }

    public int getMaxUseTime(ItemStack stack) {
        Optional<? extends RegistryEntry<Instrument>> optional = this.getInstrument(stack);
        return (Integer)optional.map((instrument) -> {
            return ((Instrument)instrument.value()).useDuration();
        }).orElse(0);
    }

    private Optional<RegistryEntry<Instrument>> getInstrument(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null && nbtCompound.contains("instrument", 8)) {
            Identifier identifier = Identifier.tryParse(nbtCompound.getString("instrument"));
            if (identifier != null) {
                // Cast the reference to the expected type
                Optional<RegistryEntry<Instrument>> entry = Registries.INSTRUMENT.getEntry(
                        RegistryKey.of(RegistryKeys.INSTRUMENT, identifier)
                ).map(entryRef -> (RegistryEntry<Instrument>) entryRef); // Safe cast
                return entry;
            }
        }

        Iterator<RegistryEntry<Instrument>> iterator = Registries.INSTRUMENT.iterateEntries(this.instrumentTag).iterator();
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }


    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    private static void playSound(World world, PlayerEntity player, Instrument instrument) {
        SoundEvent soundEvent = (SoundEvent)instrument.soundEvent().value();
        float f = instrument.range() / 16.0F;
        world.playSoundFromEntity(player, player, soundEvent, SoundCategory.RECORDS, f, 1.0F);
        world.emitGameEvent(GameEvent.INSTRUMENT_PLAY, player.getPos(), Emitter.of(player));
    }
}
