package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.interfaces.RiftChunk;
import mdteam.ait.core.managers.DeltaTimeManager;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ArtronCollectorItem extends Item {

    public static final String AU_LEVEL = "au_level";
    public static final String UUID_KEY = "uuid";
    public static final Integer COLLECTOR_MAX_FUEL = 1500;

    public ArtronCollectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putDouble(AU_LEVEL, 0);
        return super.getDefaultStack();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(world.isClient()) return;
        if (!(entity instanceof ServerPlayerEntity) || !selected) return;

        RiftChunk riftChunk = (RiftChunk) world.getChunk(entity.getBlockPos());
        if (riftChunk.isRiftChunk() && riftChunk.getArtronLevels() > 0  && getFuel(stack) < COLLECTOR_MAX_FUEL && (!DeltaTimeManager.isStillWaitingOnDelay(getDelayId(stack)))) {
            riftChunk.setArtronLevels(riftChunk.getArtronLevels() - 1); // we shouldn't need to check how much it has because we can't even get here if don't have atleast five artron in the chunk
            addFuel(stack, 2);
            DeltaTimeManager.createDelay(getDelayId(stack), 500L);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static String getDelayId(ItemStack stack) {
        return "collector-" + getUuid(stack) + "-collectdelay";
    }

    public static UUID getUuid(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.contains(UUID_KEY)) return nbt.getUuid(UUID_KEY);
        nbt.putUuid(UUID_KEY, UUID.randomUUID());
        return nbt.getUuid(UUID_KEY);
    }

    public static double getFuel(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.contains(AU_LEVEL)) return nbt.getDouble(AU_LEVEL);
        nbt.putDouble(AU_LEVEL, 0);
        return 0d;
    }
    public static void addFuel(ItemStack stack, double fuel) {
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putDouble(AU_LEVEL, getFuel(stack) + fuel);

        if (getFuel(stack) > COLLECTOR_MAX_FUEL) nbt.putDouble(AU_LEVEL, COLLECTOR_MAX_FUEL);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos clickedPos = context.getBlockPos();
        Block block = world.getBlockState(clickedPos).getBlock();
        ItemStack cellItemStack = context.getStack();
        NbtCompound nbt = cellItemStack.getOrCreateNbt();

        if(world.isClient()) return ActionResult.SUCCESS;

        if(player.isSneaking()) {
            if (world.getBlockEntity(clickedPos) instanceof ExteriorBlockEntity exterior) {
                if (exterior.getTardis() == null)
                    return ActionResult.FAIL;
                double residual = exterior.getTardis().addFuel(nbt.getDouble(AU_LEVEL));
                nbt.putDouble(AU_LEVEL, residual);
                return ActionResult.CONSUME;
            } else if (world.getBlockEntity(clickedPos) instanceof ConsoleBlockEntity console) {
                if (console.getTardis() == null)
                    return ActionResult.FAIL;
                double residual = console.getTardis().addFuel(nbt.getDouble(AU_LEVEL));
                nbt.putDouble(AU_LEVEL, residual);
                return ActionResult.CONSUME;
            }
            return ActionResult.FAIL;
        }

        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains(AU_LEVEL) ? "" + tag.getDouble(AU_LEVEL) : "0.0";
        tooltip.add(Text.literal(text + "au / " + COLLECTOR_MAX_FUEL + ".0au").formatted(Formatting.BLUE));
    }
}
