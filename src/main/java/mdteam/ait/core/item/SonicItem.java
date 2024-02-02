package mdteam.ait.core.item;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.LinkableItem;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.interfaces.RiftChunk;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SonicItem extends LinkableItem {

    public static final String MODE_KEY = "mode";
    public static final String INACTIVE = "inactive";

    public SonicItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(MODE_KEY, 0);
        return stack;
    }

    public enum Mode implements StringIdentifiable {
        INACTIVE(Formatting.GRAY) {
            @Override
            public void run(Tardis tardis, World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
            }
        },
        INTERACTION(Formatting.GREEN) {
            @Override
            public void run(Tardis tardis, World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                BlockState blockState = world.getBlockState(pos);

                if (!(CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)))
                    return;

                world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
                world.setBlockState(pos, blockState.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
        },
        OVERLOAD(Formatting.RED) {
            @Override
            public void run(Tardis tardis, World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                // fixme temporary replacement for exterior changing

                BlockEntity entity = world.getBlockEntity(pos);
                Block block = world.getBlockState(pos).getBlock();

                // fixme this doesnt work because a dispenser requires that you have redstone power input or the state wont trigger :/ - Loqor
                /*if(player.isSneaking() && block instanceof DispenserBlock dispenser) {
                    world.setBlockState(pos, world.getBlockState(pos).with(Properties.TRIGGERED, true), Block.NO_REDRAW);
                    //world.emitGameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
                }*/

                if(block instanceof TntBlock tnt) {
                    TntBlock.primeTnt(world, pos);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }
            }
        },
        SCANNING(Formatting.AQUA) {
            @Override
            public void run(Tardis tardis, World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                // fixme temporary replacement for interior changing

                if (!(world.getRegistryKey() == World.OVERWORLD && !world.isClient())) return;

                Text found = Text.translatable("message.ait.sonic.riftfound").formatted(Formatting.AQUA).formatted(Formatting.BOLD);
                Text notfound = Text.translatable("message.ait.sonic.riftnotfound").formatted(Formatting.AQUA).formatted(Formatting.BOLD);
                player.sendMessage((TardisUtil.isRiftChunk((ServerWorld) world, pos) ? found : notfound), true);
                if(TardisUtil.isRiftChunk((ServerWorld) world, pos))
                    player.sendMessage(Text.literal("AU: " + ((RiftChunk) world.getChunk(pos)).getArtronLevels()).formatted(Formatting.GOLD));
            }
        },
        TARDIS(Formatting.BLUE) {
            @Override
            public void run(Tardis tardis, World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null) return;

                if (world == TardisUtil.getTardisDimension()) {
                    world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                    player.sendMessage(Text.translatable("message.ait.remoteitem.warning3"), true);
                    return;
                }

                world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS);

                TardisTravel travel = tardis.getTravel();
                BlockPos temp = player.getBlockPos();

                if (world.getBlockState(pos).isReplaceable()) temp = pos;

                AbsoluteBlockPos.Directed playerPos = new AbsoluteBlockPos.Directed(temp, world, player.getMovementDirection());

                if (!ExteriorAnimation.isNearTardis(player, tardis, 256)) {
                    travel.setDestination(playerPos, true);
                    return;
                }

                FlightUtil.travelTo(tardis, playerPos);

                player.sendMessage(Text.translatable("message.ait.sonic.handbrakedisengaged"), true);
            }
        };

        public Formatting format;

        Mode(Formatting format) {
            this.format = format;
        }

        public abstract void run(@Nullable Tardis tardis, World world, BlockPos pos, PlayerEntity player, ItemStack stack);

        @Override
        public String asString() {
            return StringUtils.capitalize(this.toString().replace("_", " "));
        }
    }

    // fixme no me gusta nada
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if (player == null)
            return ActionResult.FAIL;
        if (world.isClient()) return ActionResult.SUCCESS;

        NbtCompound nbt = itemStack.getOrCreateNbt();

        if (!nbt.contains(MODE_KEY)) return ActionResult.FAIL;

        if(intToMode(nbt.getInt(MODE_KEY)) == Mode.INACTIVE) return ActionResult.FAIL;

        playSonicSounds(player);

        Tardis tardis = getTardis(itemStack);

        Mode mode = intToMode(nbt.getInt(MODE_KEY));
        mode.run(tardis, world, pos, player, itemStack);

        return ActionResult.SUCCESS;
    }

    public static Tardis getTardis(ItemStack item) {
        NbtCompound nbt = item.getOrCreateNbt();

        if (!nbt.contains("tardis")) return null;

        return ServerTardisManager.getInstance().getTardis(UUID.fromString(nbt.getString("tardis")));
    }

    @Override
    public void link(ItemStack stack, UUID uuid) {
        super.link(stack, uuid);

        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt(MODE_KEY, 0);
        nbt.putBoolean(INACTIVE, true);
    }

    public static void playSonicSounds(PlayerEntity player) {
        player.getWorld().playSound(null, player.getBlockPos(), AITSounds.SONIC_USE, SoundCategory.PLAYERS, 1f, 1f);
    }

    public static void cycleMode(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!(nbt.contains(MODE_KEY))) {
            setMode(stack, 0);
        }

        SonicItem.setMode(stack, nbt.getInt(MODE_KEY) + 1 <= Mode.values().length - 1 ? nbt.getInt(MODE_KEY) + 1 : 0);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbt = itemStack.getOrCreateNbt();
        BlockPos pos = user.getBlockPos();

        if (world.isClient()) return TypedActionResult.pass(itemStack);

        if (user.isSneaking()) {
            world.playSound(null, user.getBlockPos(), AITSounds.SONIC_SWITCH, SoundCategory.PLAYERS, 1f, 1f);
            cycleMode(itemStack);
        } else {
            if(intToMode(nbt.getInt(MODE_KEY)) != Mode.INACTIVE) {
                playSonicSounds(user);

                Tardis tardis = getTardis(itemStack);

                Mode mode = intToMode(nbt.getInt(MODE_KEY));
                mode.run(tardis, world, pos, user, itemStack);
            }
        }

        return TypedActionResult.pass(itemStack);
    }

    public static int findModeInt(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (!nbtCompound.contains(MODE_KEY))
            return 0;
        return nbtCompound.getInt(MODE_KEY);
    }

    public static void setMode(ItemStack stack, int mode) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt(MODE_KEY, mode);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.ait.remoteitem.holdformoreinfo").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
            return;
        }

        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains("tardis") ? tag.getString("tardis").substring(0, 8)
                : Text.translatable("message.ait.sonic.none").getString();
        String position = Text.translatable("message.ait.sonic.none").getString();
        if(tag.contains("tardis")) {
            Tardis tardis = ClientTardisManager.getInstance().getTardis(UUID.fromString(tag.getString("tardis")));
            if (tardis != null)
                position = tardis.getTravel() == null || tardis.getTravel().getExteriorPos() == null ? "In Flight..." : tardis.getTravel().getExteriorPos().toShortString();
        }

        tooltip.add(Text.translatable("message.ait.sonic.mode").formatted(Formatting.BLUE));

        Mode mode = intToMode(tag.getInt(MODE_KEY));
        tooltip.add(Text.literal(mode.asString()).formatted(mode.format).formatted(Formatting.BOLD));

        tooltip.add(ScreenTexts.EMPTY);

        if (tag.contains("tardis")) { // Adding the sonics mode
            tooltip.add(Text.literal("Position: ").formatted(Formatting.BLUE));
            tooltip.add(Text.literal("> " + position).formatted(Formatting.GRAY));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }

    public static Mode intToMode(int mode) {
        return Mode.values()[mode];
    }
}
