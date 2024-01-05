package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

import java.util.List;
import java.util.UUID;

import static mdteam.ait.tardis.TardisTravel.State.*;

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

        if (world.isClient() || player == null) return ActionResult.PASS;

        NbtCompound nbt = itemStack.getOrCreateNbt();

        // Link to exteriors tardis if it exists and player is crouching
        if (player.isSneaking()) {
            if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlock && consoleBlock.getTardis() != null) {
                nbt.putString("tardis", consoleBlock.getTardis().getUuid().toString());
                return ActionResult.SUCCESS; // Return early if the Tardis is successfully linked to the clicked block
            } else {
                return ActionResult.FAIL; // If no valid Tardis instance found, do not proceed with any further operations
            }
        }

        // Move tardis to the clicked pos
        if (!nbt.contains("tardis"))
            return ActionResult.FAIL;

        Tardis tardis = ServerTardisManager.getInstance().getTardis(UUID.fromString(nbt.getString("tardis")));
        //System.out.println(ServerTardisManager.getInstance().getTardis(nbt.getUuid("tardis")));

        if (tardis != null) {
            if (tardis.getFuel() <= 0) {
                player.sendMessage(Text.translatable("message.ait.remoteitem.warning1"));
                return ActionResult.FAIL;
            }
            if (tardis.isRefueling()) {
                player.sendMessage(Text.translatable("message.ait.remoteitem.warning2"));
                return ActionResult.FAIL;
            }
            // Check if the Tardis is already present at this location before moving it there
            AbsoluteBlockPos.Directed currentPosition = tardis.getTravel().getPosition();
            if (!currentPosition.equals(pos)) {
                if (world != TardisUtil.getTardisDimension()) {
                    world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS);

                    BlockPos temp = pos.up();

                    if (world.getBlockState(pos).isReplaceable()) temp = pos;

                    FlightUtil.travelTo(tardis, new AbsoluteBlockPos.Directed(temp, world, player.getMovementDirection().getOpposite()));
                } else {
                    world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                    player.sendMessage(Text.translatable("message.ait.remoteitem.warning3"), true);
                    return ActionResult.PASS;
                }
            } else {
                // If the Tardis is already present at this location, do not proceed with any further operations
                return ActionResult.FAIL;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.ait.remoteitem.holdformoreinfo").formatted(Formatting.GRAY).formatted(Formatting.ITALIC));
            return;
        }

        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains("tardis") ? tag.getString("tardis").substring(0, 8)
                : Text.translatable("tooltip.ait.remoteitem.notardis").getString();

        tooltip.add(Text.literal("→ " + text).formatted(Formatting.BLUE));

        if (tag.contains("tardis")) {
            Tardis tardis = ClientTardisManager.getInstance().getLookup().get(UUID.fromString(tag.getString("tardis")));
            if (tardis.getTravel().getState() != LANDED)
                tooltip.add(Text.literal("→ " + tardis.getHandlers().getFlight().getDurationAsPercentage() + "%").formatted(Formatting.GOLD));
        }
    }
}
