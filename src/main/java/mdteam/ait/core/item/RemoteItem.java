package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.TardisHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class RemoteItem extends Item {

    public RemoteItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();
        if (!world.isClient()) {

            // Link to exteriors tardis if it exists and player is crouching
            if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior && player.isSneaking()) {
                if (exterior.getTardis() == null) return ActionResult.FAIL;

                if(!itemStack.hasNbt()) {
                    itemStack.getOrCreateNbt().putUuid("tardis", exterior.getTardis().getUuid());
                    player.setStackInHand(context.getHand(), itemStack);
                }
                return ActionResult.SUCCESS;
            }

            // Move tardis to the clicked pos

            if(itemStack.hasNbt()) {
                if(itemStack.getNbt().contains("tardis")) {
                    Tardis tardis = TardisHandler.getTardis(itemStack.getNbt().getUuid("tardis"));
                    if(tardis != null) {
                        tardis.getTravel().moveTo(new AbsoluteBlockPos(pos.up(),player.getMovementDirection().getOpposite(),world));
                    }
                }
            }
        }

        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            NbtCompound tag = new NbtCompound();
            if(stack.hasNbt()) {
                tag = stack.getNbt();
            }
            if(!tag.contains("tardis")) {
                tooltip.add(Text.literal("When a TARDIS is linked, it's UUID will show here.").fillStyle(Style.EMPTY.withBold(true)));
            }
            else {
                tooltip.add(Text.literal(tag.getUuid("tardis").toString()).fillStyle(Style.EMPTY.withBold(true)));
            }
        } else {
            tooltip.add(Text.of("Hold shift for more info"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
