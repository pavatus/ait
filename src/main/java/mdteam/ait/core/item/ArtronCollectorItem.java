package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static mdteam.ait.tardis.handler.FuelHandler.MAX_FUEL;

public class ArtronCollectorItem extends Item {

    public static final String AU_LEVEL = "au_level";

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
        if(entity instanceof PlayerEntity player) {
            if(selected) {
                NbtCompound nbt = stack.getOrCreateNbt();
                if(TardisUtil.isRiftChunk((ServerWorld) world, player.getBlockPos())) {
                    if (nbt.contains(AU_LEVEL)) {
                        if(nbt.getDouble(AU_LEVEL) >= MAX_FUEL)
                            nbt.putDouble(AU_LEVEL, nbt.getDouble(AU_LEVEL) + TardisUtil.getArtronLevelsOfChunk((ServerWorld) world, player.getBlockPos()));
                        else
                            nbt.putDouble(AU_LEVEL, MAX_FUEL);
                    } else {
                        nbt.putDouble(AU_LEVEL, TardisUtil.getArtronLevelsOfChunk((ServerWorld) world, player.getBlockPos()));
                    }
                    TardisUtil.setArtronLevelsOfChunk((ServerWorld) world, player.getBlockPos(), 0);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
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
                if (exterior.tardis() == null)
                    return ActionResult.FAIL;
                double residual = exterior.tardis().addFuel(nbt.getDouble(AU_LEVEL));
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
        if (!Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Hold shift for more info").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
            return;
        }

        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains(AU_LEVEL) ? "" + tag.getDouble(AU_LEVEL) : "0.0au";
        tooltip.add(Text.literal(text + "au / 5000.0au").formatted(Formatting.BLUE));
    }
}
