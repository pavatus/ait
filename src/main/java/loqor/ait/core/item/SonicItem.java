package loqor.ait.core.item;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import dev.pavatus.lib.data.CachedDirectedGlobalPos;
import dev.pavatus.planet.core.PlanetBlocks;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import loqor.ait.api.ArtronHolderItem;
import loqor.ait.api.link.LinkableItem;
import loqor.ait.client.sounds.ClientSoundManager;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITSounds;
import loqor.ait.core.AITTags;
import loqor.ait.core.advancement.TardisCriterions;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.animation.ExteriorAnimation;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.handler.travel.TravelUtil;
import loqor.ait.core.world.LandingPadManager;
import loqor.ait.core.world.RiftChunkManager;
import loqor.ait.core.world.TardisServerWorld;
import loqor.ait.data.Loyalty;
import loqor.ait.data.landing.LandingPadRegion;
import loqor.ait.data.landing.LandingPadSpot;
import loqor.ait.data.schema.sonic.SonicSchema;
import loqor.ait.registry.impl.SonicRegistry;


public class SonicItem extends LinkableItem implements ArtronHolderItem {
    public static final double MAX_FUEL = 1000;
    public static final String MODE_KEY = "mode";
    public static final String PREV_MODE_KEY = "PreviousMode";
    public static final String INACTIVE = "inactive";
    public static final String SONIC_TYPE = "sonic_type";

    public SonicItem(Settings settings) {
        super(settings.maxCount(1), true);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockPos pos = user.getBlockPos();

        if (world.isClient()) {
            return TypedActionResult.pass(stack);
        }

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
        setMode(stack, Mode.INACTIVE);

        if (world.isClient())
            ClientSoundManager.getSonicSound().onFinishUse((AbstractClientPlayerEntity) user);

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        setPreviousMode(stack);
        setMode(stack, Mode.INACTIVE);

        if (world.isClient())
            ClientSoundManager.getSonicSound().onFinishUse((AbstractClientPlayerEntity) user);

        return stack;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player))
            return;

        if (world.isClient())
            ClientSoundManager.getSonicSound().onUse((AbstractClientPlayerEntity) user);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!selected) {
            setMode(stack, Mode.INACTIVE);
        }
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
            Text message = null;

            if (Objects.equals(previousMode.asString(), "INACTIVE")) {
                message = Text.translatable("sonic.ait.mode.inactive").formatted(previousMode.format, Formatting.BOLD);
            } else if (Objects.equals(previousMode.asString(), "INTERACTION")) {
                message = Text.translatable("sonic.ait.mode.interaction").formatted(previousMode.format, Formatting.BOLD);
            } else if (Objects.equals(previousMode.asString(), "OVERLOAD")) {
                message = Text.translatable("sonic.ait.mode.overload").formatted(previousMode.format, Formatting.BOLD);
            } else if (Objects.equals(previousMode.asString(), "SCANNING")) {
                message = Text.translatable("sonic.ait.mode.scanning").formatted(previousMode.format, Formatting.BOLD);
            } else if (Objects.equals(previousMode.asString(), "TARDIS")) {
                message = Text.translatable("sonic.ait.mode.tardis").formatted(previousMode.format, Formatting.BOLD);
            }


            user.sendMessage(message, true);
            return;
        }

        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == AITBlocks.ZEITON_CLUSTER) {
            this.addFuel(200, stack);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            return;
        }
        if (state.isIn(BlockTags.PLANKS) || state.isIn(BlockTags.LOGS)) {
            TardisCriterions.SONIC_WOOD.trigger((ServerPlayerEntity) user);
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
        if (SonicRegistry.DEFAULT != null)
            nbt.putString(SONIC_TYPE, SonicRegistry.DEFAULT.id().toString());

        return stack;
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
        return stack.getRegistryEntry().isIn(AITTags.Items.SONIC_ITEM);
    }

    public static int findModeInt(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        if (!nbtCompound.contains(MODE_KEY))
            return 0;

        return nbtCompound.getInt(MODE_KEY);
    }

    public static SonicSchema findSchema(NbtCompound nbt) {
         String rawId = nbt.getString(SONIC_TYPE);

        if (rawId == null)
            return SonicRegistry.DEFAULT;

        Identifier id = Identifier.tryParse(rawId);
        SonicSchema schema = SonicRegistry.getInstance().get(id);

        return schema == null ? SonicRegistry.DEFAULT : schema;
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
        stack.getOrCreateNbt().putString(SONIC_TYPE, id.toString());
    }

    private static void setPreviousMode(ItemStack stack) {
        stack.getOrCreateNbt().putInt(PREV_MODE_KEY, findModeInt(stack));
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

        if (Objects.equals(mode.asString(), "INACTIVE")) {
            tooltip.add(Text.translatable("sonic.ait.mode.inactive").formatted(mode.format, Formatting.BOLD));
        } else if (Objects.equals(mode.asString(), "INTERACTION")) {
            tooltip.add(Text.translatable("sonic.ait.mode.interaction").formatted(mode.format, Formatting.BOLD));
        } else if (Objects.equals(mode.asString(), "OVERLOAD")) {
            tooltip.add(Text.translatable("sonic.ait.mode.overload").formatted(mode.format, Formatting.BOLD));
        } else if (Objects.equals(mode.asString(), "SCANNING")) {
            tooltip.add(Text.translatable("sonic.ait.mode.scanning").formatted(mode.format, Formatting.BOLD));
        } else if (Objects.equals(mode.asString(), "TARDIS")) {
            tooltip.add(Text.translatable("sonic.ait.mode.tardis").formatted(mode.format, Formatting.BOLD));
        }


        tooltip.add(Text.translatable("message.ait.tooltips.artron_units").formatted(Formatting.BLUE).append(
                Text.literal(String.valueOf(Math.round(this.getCurrentFuel(stack)))).formatted(this.getCurrentFuel(stack) > (this.getMaxFuel(stack) / 4) ? Formatting.GREEN : Formatting.RED)));


        if (tag.contains("tardis"))
            tooltip.add(ScreenTexts.EMPTY);

        super.appendTooltip(stack, world, tooltip, context);

        if (tag.contains("tardis")) { // Adding the sonics mode
            tooltip.add(Text.translatable("tooltip.ait.position").formatted(Formatting.BLUE));
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
                Block block = world.getBlockState(pos).getBlock();
                BlockState blockState = world.getBlockState(pos);

                if (block == Blocks.SNOW || block == Blocks.SNOW_BLOCK || block == Blocks.POWDER_SNOW)  {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.SNOWBALL)));
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block instanceof TntBlock) {
                    TntBlock.primeTnt(world, pos);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    return;
                }

                if (!world.getBlockState(pos).isIn(AITTags.Blocks.SONIC_INTERACTABLE))
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

                if (block.getDefaultState().isIn(ConventionalBlockTags.GLASS_PANES)) {
                    world.breakBlock(pos, false);
                    world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                    return;
                }

                if (block == Blocks.BRICKS || block == Blocks.BRICK_WALL)  {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.BRICK, 4)));
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == Blocks.NETHER_BRICKS || block == Blocks.RED_NETHER_BRICKS || block == Blocks.NETHER_BRICK_WALL || block == Blocks.RED_NETHER_BRICK_WALL)  {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.NETHER_BRICK, 4)));
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == Blocks.ICE || block == Blocks.BLUE_ICE || block == Blocks.FROSTED_ICE || block == Blocks.PACKED_ICE)  {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Blocks.AIR)));
                    world.setBlockState(pos, Blocks.WATER.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }



                if (block == AITBlocks.CONSOLE)  {
                    world.breakBlock(pos, true);
                    world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                    return;
                }

                if (block.getDefaultState().isIn(ConventionalBlockTags.GLASS_BLOCKS)) {
                    world.breakBlock(pos, false);
                    world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                    return;
                }

                if (block instanceof LeavesBlock) {
                    world.breakBlock(pos, false);
                    world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);

                    return;
                }

                if (!world.getBlockState(pos).isIn(AITTags.Blocks.SONIC_INTERACTABLE))
                    return;

                if (block instanceof TntBlock) {
                    TntBlock.primeTnt(world, pos);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    return;
                }


                if (block instanceof SandBlock) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.GLASS)));
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

//God forgive me for what i did to this code

                if (block == Blocks.COAL_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.COAL)));
                    world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == Blocks.DEEPSLATE_COAL_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.COAL)));
                    world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == PlanetBlocks.MARTIAN_COAL_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.COAL)));
                    world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == PlanetBlocks.ANORTHOSITE_COAL_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.COAL)));
                    world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == Blocks.LAPIS_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.LAPIS_LAZULI)));
                    world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == Blocks.DEEPSLATE_LAPIS_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.LAPIS_LAZULI)));
                    world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == PlanetBlocks.MARTIAN_LAPIS_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.LAPIS_LAZULI)));
                    world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == PlanetBlocks.ANORTHOSITE_LAPIS_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.LAPIS_LAZULI)));
                    world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == Blocks.DIAMOND_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.DIAMOND)));
                    world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == Blocks.DEEPSLATE_DIAMOND_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.DIAMOND)));
                    world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == PlanetBlocks.MARTIAN_DIAMOND_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.DIAMOND)));
                    world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == PlanetBlocks.ANORTHOSITE_DIAMOND_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.DIAMOND)));
                    world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == Blocks.IRON_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_IRON)));
                    world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == Blocks.DEEPSLATE_IRON_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_IRON)));
                    world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == PlanetBlocks.MARTIAN_IRON_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_IRON)));
                    world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == PlanetBlocks.ANORTHOSITE_IRON_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_IRON)));
                    world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == Blocks.GOLD_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_GOLD)));
                    world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == Blocks.DEEPSLATE_GOLD_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_GOLD)));
                    world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == PlanetBlocks.MARTIAN_GOLD_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_GOLD)));
                    world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == PlanetBlocks.ANORTHOSITE_GOLD_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_GOLD)));
                    world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == Blocks.NETHER_GOLD_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_GOLD)));
                    world.setBlockState(pos, Blocks.NETHERRACK.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == Blocks.COPPER_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_COPPER)));
                    world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == Blocks.DEEPSLATE_COPPER_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_COPPER)));
                    world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

                }

                if (block == PlanetBlocks.MARTIAN_COPPER_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_COPPER)));
                    world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }

                if (block == PlanetBlocks.ANORTHOSITE_COPPER_ORE) {
                    world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                            new ItemStack(Items.RAW_COPPER)));
                    world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                            Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

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
            private static final Text RIFT_FOUND = Text.translatable("message.ait.sonic.riftfound").formatted(Formatting.AQUA)
                    .formatted(Formatting.BOLD);
            private static final Text RIFT_NOT_FOUND = Text.translatable("message.ait.sonic.riftnotfound").formatted(Formatting.AQUA)
                    .formatted(Formatting.BOLD);

            @Override
            public void run(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                LandingPadRegion region = LandingPadManager.getInstance(world).getRegionAt(pos);
                if (region != null) {
                    if (world.getBlockState(pos).isAir()) return;

                    boolean wasSpotCreated = modifyRegion(tardis, world, pos.up(), player, stack, region);

                    float pitch = wasSpotCreated ? 1.1f : 0.75f;
                    world.playSound(null, pos, AITSounds.SONIC_SWITCH, SoundCategory.PLAYERS, 1f, pitch);

                    return;
                }

                if (!TardisServerWorld.isTardisDimension(world)) {
                    sendRiftInfo(tardis, world, pos, player, stack);
                    return;
                }

                if (TardisServerWorld.isTardisDimension(world))
                    sendTardisInfo(tardis, world, pos, player, stack);
            }

            private static boolean modifyRegion(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack, LandingPadRegion region) {
                LandingPadSpot spot = region.getSpotAt(pos).orElse(null);

                if (spot == null) {
                    addSpot(region, pos);

                    syncRegion(world, pos);
                    return true;
                }

                removeSpot(region, pos);
                syncRegion(world, pos);

                return false;
            }
            private static void addSpot(LandingPadRegion region, BlockPos pos) {
                region.createSpotAt(pos);
            }
            private static void removeSpot(LandingPadRegion region, BlockPos pos) {
                region.removeSpotAt(pos);
            }
            private static void syncRegion(ServerWorld world, BlockPos pos) {
                LandingPadManager.Network.syncTracked(LandingPadManager.Network.Action.ADD, world, new ChunkPos(pos));
            }

            private static void sendRiftInfo(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                boolean isRift = RiftChunkManager.isRiftChunk(world, pos);

                player.sendMessage(isRift ? RIFT_FOUND : RIFT_NOT_FOUND, true);

                if (!isRift) return;

                int artronValue = (int) RiftChunkManager.getInstance(world).getArtron(new ChunkPos(pos));
                player.sendMessage(
                        Text.translatable("message.ait.artron_units", artronValue)
                                .formatted(Formatting.GOLD)
                );
            }
            private static void sendTardisInfo(Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null)
                    return;

                if (tardis.crash().isUnstable() || tardis.crash().isToxic()) {
                    player.sendMessage(Text.literal("Repair time: " + tardis.crash().getRepairTicks())
                            .formatted(Formatting.DARK_RED, Formatting.ITALIC), true);
                    return;
                }

                player.sendMessage(
                        Text.translatable("message.ait.artron_units", tardis.fuel().getCurrentFuel()).formatted(Formatting.GOLD), true);
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

                if (TardisServerWorld.isTardisDimension(world)) {
                    if (player.getPitch() == -90 && !tardis.travel().handbrake()) {
                        player.sendMessage(Text.translatable("message.ait.remoteitem.success1"), true);
                        tardis.travel().dematerialize();
                        return;
                    }

                    if (player.getPitch() == 90 && !tardis.travel().inFlight()) {
                        player.sendMessage(Text.translatable("message.ait.remoteitem.success2"), true);
                        tardis.travel().handbrake(true);
                        tardis.setRefueling(true);
                        return;
                    }

                    world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F,
                            0.2F);
                    player.sendMessage(Text.translatable("message.ait.remoteitem.warning3"), true);
                    return;
                }

                world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS);

                TravelHandler travel = tardis.travel();

                float rotation = player.getBodyYaw();

                if (!world.getBlockState(pos).isAir()) {
                    rotation = 180 - rotation;
                }

                CachedDirectedGlobalPos target = CachedDirectedGlobalPos.create(world, pos, (byte) RotationPropertyHelper.fromYaw(rotation));

                boolean isPilot = tardis.loyalty().get(player).isOf(Loyalty.Type.PILOT);
                boolean isNearTardis = ExteriorAnimation.isNearTardis(player, tardis, 256);

                if (!isNearTardis || isPilot) {
                    travel.destination(target);

                    if (isPilot)
                        TravelUtil.travelTo(tardis, target);

                    player.sendMessage(Text.translatable("message.ait.sonic.handbrakedisengaged"), true);
                }
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
