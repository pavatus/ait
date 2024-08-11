package loqor.ait.core.item;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.ArtronHolderItem;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.schema.SonicSchema;
import loqor.ait.core.managers.RiftChunkManager;
import loqor.ait.core.sounds.sonic.ServerSonicSoundHandler;
import loqor.ait.core.util.AITModTags;
import loqor.ait.core.util.LegacyUtil;
import loqor.ait.registry.impl.SonicRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelUtil;
import loqor.ait.tardis.link.LinkableItem;
import loqor.ait.tardis.util.TardisUtil;

public class SonicItem extends LinkableItem implements ArtronHolderItem {
    public static final double MAX_FUEL = 1000;
    public static final String MODE_KEY = "mode";
    public static final String PREV_MODE_KEY = "PreviousMode";
    public static final String INACTIVE = "inactive";
    public static final String SONIC_TYPE = "sonic_type";
    public static final int SONIC_SFX_LENGTH = 30;
    private final ServerSonicSoundHandler sonicSoundHandler = new ServerSonicSoundHandler();
    private boolean shouldntContinue = false;

    public SonicItem(Settings settings) {
        super(settings.maxCount(1), true);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockPos pos = user.getBlockPos();

        if (world.isClient())
            return TypedActionResult.pass(stack);

        this.useSonic(world, user, pos, hand, stack);
        return TypedActionResult.consume(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (player == null)
            return ActionResult.PASS;

        this.useSonic(world, player, pos, context.getHand(), stack);
        return ActionResult.CONSUME;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        setPreviousMode(stack);
        // stop sound stuff for sonic
        if (!world.isClient())
            sonicSoundHandler.setPlaying(false, world.getServer());
        shouldntContinue = false;
        setMode(stack, Mode.INACTIVE);

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        setPreviousMode(stack);
        if (!world.isClient())
            sonicSoundHandler.setPlaying(false, world.getServer());
        shouldntContinue = false;
        setMode(stack, Mode.INACTIVE);

        return stack;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player))
            return;

        if (remainingUseTicks % SONIC_SFX_LENGTH != 0)
            return;

        if (sonicIsInUse(stack))
            playSonicSoundsHere(player);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!selected) {
            setMode(stack, Mode.INACTIVE);
        }
    }

    public boolean sonicIsInUse(ItemStack item) {
        if (item == null)
            return false;
        NbtCompound nbt = item.getOrCreateNbt();

        if (nbt.contains(SonicItem.MODE_KEY))
            return nbt.getInt(SonicItem.MODE_KEY) != 0;

        return false;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000; // prolly big enough
    }

    private void useSonic(World world, PlayerEntity user, BlockPos pos, Hand hand, ItemStack stack) {
        Mode mode = findMode(stack);

        if (!(world instanceof ServerWorld serverWorld))
            return;

        Tardis tardis = getTardis(world, stack);

        if (this.isOutOfFuel(stack))
            return;

        if (user.isSneaking()) {
            world.playSound(null, user.getBlockPos(), AITSounds.SONIC_SWITCH, SoundCategory.PLAYERS, 1f, 1f);
            cycleMode(stack);
            Mode previousMode = findPreviousMode(stack);
            user.sendMessage(
                    Text.literal(previousMode.asString()).formatted(previousMode.format).formatted(Formatting.BOLD),
                    true);
            return;
        }

        if (world.getBlockState(pos).getBlock() == AITBlocks.ZEITON_CLUSTER) {
            this.addFuel(200, stack);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            return;
        }

        if (mode == Mode.INACTIVE) {
            Mode prev = findPreviousMode(stack);

            if (prev == Mode.INACTIVE)
                return;

            setMode(stack, prev);
            mode = findMode(stack);
        }

        user.setCurrentHand(hand);
        this.removeFuel(stack);

        mode.run(tardis, serverWorld, pos, user, stack);
    }

    @Override
    public void link(ItemStack stack, UUID uuid) {
        super.link(stack, uuid);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt(MODE_KEY, 0);
        nbt.putBoolean(INACTIVE, true);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt(MODE_KEY, 0);
        nbt.putDouble(FUEL_KEY, getMaxFuel(stack));
        nbt.putString(SONIC_TYPE, SonicRegistry.DEFAULT.id().toString());

        return stack;
    }

    @Deprecated
    public static void playSonicSounds(PlayerEntity player) {
        // womp womp
    }

    public void playSonicSoundsHere(PlayerEntity player) {
        if (this.shouldntContinue)
            return;
        if (player.getWorld().isClient())
            return;
        sonicSoundHandler.setPlayerIdAndServer(player.getUuid(), player.getServer());
        sonicSoundHandler.setPlaying(true, player.getServer());
        this.shouldntContinue = true;
    }

    public static void cycleMode(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!(nbt.contains(PREV_MODE_KEY))) {
            setMode(stack, 0);
            setPreviousMode(stack);
        }

        SonicItem.setMode(stack,
                nbt.getInt(PREV_MODE_KEY) + 1 <= Mode.values().length - 1 ? nbt.getInt(PREV_MODE_KEY) + 1 : 0);
        setPreviousMode(stack);
        setMode(stack, 0);
    }

    public static boolean isSonic(ItemStack stack) {
        return stack.getRegistryEntry().isIn(AITModTags.Items.SONIC_ITEM);
    }

    public static int findModeInt(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        if (!nbtCompound.contains(MODE_KEY))
            return 0;

        return nbtCompound.getInt(MODE_KEY);
    }

    public static SonicSchema findSchema(NbtCompound nbt) {
        if (!nbt.contains(SONIC_TYPE))
            return SonicRegistry.DEFAULT;

        if (LegacyUtil.shouldFixSonicType(nbt))
            LegacyUtil.fixSonicType(nbt);

        Identifier id = Identifier.tryParse(nbt.getString(SONIC_TYPE));

        if (id == null) {
            AITMod.LOGGER.warn("Couldn't parse sonic id: '" + nbt.getString(SONIC_TYPE) + "'");
            return SonicRegistry.DEFAULT;
        }

        SonicSchema schema = SonicRegistry.getInstance().get(id);

        if (schema == null) {
            AITMod.LOGGER.warn("Couldn't find sonic with id: '" + id + "'! Allowed options: "
                    + SonicRegistry.getInstance().toList());
            return SonicRegistry.DEFAULT;
        }

        return schema;
    }

    public static SonicSchema findSchema(ItemStack stack) {
        return findSchema(stack.getOrCreateNbt());
    }

    public static void setMode(ItemStack stack, int mode) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt(MODE_KEY, mode);
    }

    public static void setMode(ItemStack stack, Mode mode) {
        setMode(stack, mode.ordinal());
    }

    public static void setSchema(ItemStack stack, SonicSchema schema) {
        setSchema(stack, schema.id());
    }

    public static void setSchema(ItemStack stack, Identifier id) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putString(SONIC_TYPE, id.toString());
    }

    private static void setPreviousMode(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(PREV_MODE_KEY, findModeInt(stack));
    }

    public static Mode findPreviousMode(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains(PREV_MODE_KEY))
            setPreviousMode(stack);

        return intToMode(nbt.getInt(PREV_MODE_KEY));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    // this smells
    public static Mode intToMode(int mode) {
        return Mode.values()[mode];
    }

    // ew
    public static Mode findMode(ItemStack stack) {
        return intToMode(findModeInt(stack));
    }

    // Fuel
    @Override
    public double getMaxFuel(ItemStack stack) {
        return MAX_FUEL;
    }

    @Override
    public boolean isOutOfFuel(ItemStack stack) {
        boolean outage = ArtronHolderItem.super.isOutOfFuel(stack);

        if (outage && intToMode(stack.getOrCreateNbt().getInt(MODE_KEY)) != Mode.INACTIVE) {
            setMode(stack, Mode.INACTIVE);
        }

        return outage;
    }

    protected void removeFuel(ItemStack stack) {
        this.removeFuel(1, stack);
    }

    protected void addFuel(ItemStack stack) {
        this.addFuel(1, stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tag = stack.getOrCreateNbt();
        String position = Text.translatable("message.ait.sonic.none").getString();

        if (world == null)
            return;

        Tardis tardis = LinkableItem.getTardis(world, stack);

        if (tardis != null)
            position = tardis.travel() == null || tardis.travel().position().getPos() == null
                    ? "In Flight..."
                    : tardis.travel().position().getPos().toShortString();

        tooltip.add(Text.translatable("message.ait.sonic.mode").formatted(Formatting.BLUE));

        Mode mode = findPreviousMode(stack);
        tooltip.add(Text.literal(mode.asString()).formatted(mode.format).formatted(Formatting.BOLD));

        tooltip.add(Text.literal("AU: ").formatted(Formatting.BLUE).append(
                Text.literal(String.valueOf(Math.round(this.getCurrentFuel(stack)))).formatted(Formatting.GREEN))); // todo
                                                                                                                    // translatable
                                                                                                                    // +
                                                                                                                    // changing
        // of colour based off fuel

        if (tag.contains("tardis"))
            tooltip.add(ScreenTexts.EMPTY);

        super.appendTooltip(stack, world, tooltip, context);

        if (tag.contains("tardis")) { // Adding the sonics mode
            tooltip.add(Text.literal("Position: ").formatted(Formatting.BLUE));
            tooltip.add(Text.literal("> " + position).formatted(Formatting.GRAY));
        }

        tooltip.add(
                Text.translatable("message.ait.sonic.currenttype").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
        tooltip.add(Text.literal(findSchema(stack).name()).formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
    }

    public enum Mode implements StringIdentifiable {
        INACTIVE(Formatting.GRAY) {
            @Override
            public void run(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
            }
        },
        INTERACTION(Formatting.GREEN) {
            @Override
            public void run(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                BlockState blockState = world.getBlockState(pos);

                if (!world.getBlockState(pos).isIn(AITModTags.Blocks.SONIC_INTERACTABLE))
                    return;

                if (blockState.getBlock() == Blocks.IRON_DOOR || blockState.getBlock() == Blocks.IRON_TRAPDOOR) {
                    world.setBlockState(pos, blockState.with(Properties.OPEN, !blockState.get(Properties.OPEN)),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player,
                            blockState.get(Properties.OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                    world.playSound(player, pos,
                            blockState.get(Properties.OPEN)
                                    ? SoundEvents.BLOCK_IRON_DOOR_OPEN
                                    : SoundEvents.BLOCK_IRON_DOOR_CLOSE,
                            SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
                    return;
                }

                if (!(CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState)
                        || CandleCakeBlock.canBeLit(blockState)))
                    return;

                world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                        world.getRandom().nextFloat() * 0.4f + 0.8f);
                world.setBlockState(pos, blockState.with(Properties.LIT, true),
                        Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
        },
        OVERLOAD(Formatting.RED) {
            @Override
            public void run(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                Block block = world.getBlockState(pos).getBlock();

                if (!world.getBlockState(pos).isIn(AITModTags.Blocks.SONIC_INTERACTABLE))
                    return;

                if (block instanceof TntBlock) {
                    TntBlock.primeTnt(world, pos);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    return;
                }

                if (block instanceof RedstoneLampBlock) {
                    world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                            world.getRandom().nextFloat() * 0.4f + 0.8f);
                    world.setBlockState(pos,
                            world.getBlockState(pos).with(Properties.LIT,
                                    !world.getBlockState(pos).get(Properties.LIT)),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }
            }
        },
        SCANNING(Formatting.AQUA) {
            @Override
            public void run(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if ((world.getRegistryKey() == World.OVERWORLD)) {
                    Text found = Text.translatable("message.ait.sonic.riftfound").formatted(Formatting.AQUA)
                            .formatted(Formatting.BOLD);
                    Text notfound = Text.translatable("message.ait.sonic.riftnotfound").formatted(Formatting.AQUA)
                            .formatted(Formatting.BOLD);
                    player.sendMessage((RiftChunkManager.isRiftChunk(pos) ? found : notfound), true);
                    if (RiftChunkManager.isRiftChunk(pos))
                        player.sendMessage(Text.literal("AU: " + (RiftChunkManager.getArtronLevels(world, pos)))
                                .formatted(Formatting.GOLD));
                    return;
                }

                if (world == TardisUtil.getTardisDimension()) {
                    if (tardis.crash().isUnstable() || tardis.crash().isToxic()) {
                        player.sendMessage(Text.literal("Repair time: " + tardis.crash().getRepairTicks())
                                .formatted(Formatting.DARK_RED, Formatting.ITALIC), true);
                    } else {
                        player.sendMessage(
                                Text.literal("AU: " + tardis.fuel().getCurrentFuel()).formatted(Formatting.GOLD), true);
                    }
                }
            }
        },
        TARDIS(Formatting.BLUE) {
            @Override
            public void run(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null)
                    return;

                if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exteriorBlockEntity) {
                    if (exteriorBlockEntity.tardis().isEmpty())
                        return;

                    int repairTicksLeft = exteriorBlockEntity.tardis().get().crash().getRepairTicks();
                    int repairMins = repairTicksLeft / 20 / 60;

                    if (repairTicksLeft == 0) {
                        player.sendMessage(Text.translatable("tardis.sonic.not_damaged").formatted(Formatting.GOLD),
                                true); // Your tardis is not damaged
                        return;
                    }

                    player.sendMessage(Text.literal(
                            "You have " + repairMins + (repairMins == 1 ? " minute" : " minutes") + " of repair left.")
                            .formatted(Formatting.GOLD), true);
                    return;
                }

                if (world == TardisUtil.getTardisDimension()) {
                    if (player.getPitch() == -90 && !tardis.travel().handbrake()) {
                        player.sendMessage(Text.translatable("message.ait.remoteitem.success1"), true);
                        tardis.travel().dematerialize();
                        return;
                    }
                    world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F,
                            0.2F);
                    player.sendMessage(Text.translatable("message.ait.remoteitem.warning3"), true);
                    return;
                }

                world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS);

                TravelHandler travel = tardis.travel();

                DirectedGlobalPos.Cached target = DirectedGlobalPos.Cached.create(world, pos,
                        (byte) RotationPropertyHelper.fromYaw(player.getBodyYaw()));

                boolean isPilot = tardis.loyalty().get(player).isOf(Loyalty.Type.PILOT);
                boolean isNearTardis = ExteriorAnimation.isNearTardis(player, tardis, 256);

                if (!isNearTardis || isPilot) {
                    travel.forceDestination(target);

                    if (isPilot)
                        TravelUtil.travelTo(tardis, target);
                }

                player.sendMessage(Text.translatable("message.ait.sonic.handbrakedisengaged"), true);
            }
        };

        public final Formatting format;

        Mode(Formatting format) {
            this.format = format;
        }

        public abstract void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player,
                ItemStack stack);

        @Override
        public String asString() {
            return StringUtils.capitalize(this.toString().replace("_", " "));
        }
    }
}
