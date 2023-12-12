package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import net.minecraft.block.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SonicItem extends Item {

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
        nbt.putBoolean(INACTIVE, false);
        return stack;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if(player == null)
            return ActionResult.FAIL;

        NbtCompound nbt = itemStack.getOrCreateNbt();

        if (player.isSneaking()) {
            if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock) {
                if (consoleBlock.getTardis() == null)
                    return ActionResult.PASS;

                if(!nbt.contains("tardis")) {
                    nbt.putUuid("tardis", consoleBlock.getTardis().getUuid());
                    nbt.putInt(MODE_KEY, 0);
                }
                return ActionResult.SUCCESS;
            }
        }
        BlockState blockState = world.getBlockState(pos);
        if (CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)) {
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            world.setBlockState(pos, blockState.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return ActionResult.success(world.isClient());
        }
        return ActionResult.FAIL;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(user.isSneaking()) {
            NbtCompound nbt = stack.getOrCreateNbt();
            if(nbt.contains("tardis")) {
                if (nbt.contains(MODE_KEY)) {
                    SonicItem.setMode(stack, nbt.getInt(MODE_KEY) + 1 <= 3 ? nbt.getInt(MODE_KEY) + 1 : 0);
                    //System.out.println(SonicItem.whatMode(stack));
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound nbt = itemStack.getOrCreateNbt();
        if(nbt == null || !nbt.contains("tardis")) {
            return TypedActionResult.fail(itemStack);
        }
        if (user.isSneaking()) {
            user.setCurrentHand(hand);
            nbt.putBoolean(INACTIVE, true);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    public static int whatMode(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound == null || !nbtCompound.contains("tardis"))
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
            tooltip.add(Text.literal("Hold shift for more info").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
            return;
        }

        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains("tardis") ? tag.getUuid("tardis").toString().substring(0, 8)
                : "Sonic does not identify with any TARDIS";

        if(tag.contains("tardis")) {
            tooltip.add(Text.literal("Mode:").formatted(Formatting.BLUE));
            String mode = intToMode(tag.getInt(MODE_KEY));
            tooltip.add(Text.literal(mode).formatted(modeToColour(mode)).formatted(Formatting.BOLD));
            tooltip.add(ScreenTexts.EMPTY);
        }
        tooltip.add(Text.literal("TARDIS: ").formatted(Formatting.BLUE));
        tooltip.add(Text.literal("> " + text).formatted(Formatting.GRAY));
    }

    public String intToMode(int mode) {
        return switch (mode) {
            case 0 -> "Interaction";
            case 1 -> "Overload";
            case 2 -> "Scanning";
            case 3 -> "TARDIS";
            default -> "None";
        };
    }

    public Formatting modeToColour(String mode) {
        return switch (mode) {
            case "Interaction" -> Formatting.GREEN;
            case "Overload" -> Formatting.RED;
            case "Scanning" -> Formatting.AQUA;
            case "TARDIS" -> Formatting.BLUE;
            default -> Formatting.DARK_GRAY;
        };
    }
}
