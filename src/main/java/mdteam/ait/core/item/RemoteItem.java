package mdteam.ait.core.item;

import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import the.mdteam.ait.ServerTardisManager;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisTravel;
import the.mdteam.ait.wrapper.ServerTardisTravel;

import java.util.List;

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

        if (world.isClient() || player == null)
            return ActionResult.PASS;

        NbtCompound nbt = itemStack.getOrCreateNbt();

        // Link to exteriors tardis if it exists and player is crouching
        if (player.isSneaking() && world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior) {
            if (exterior.getTardis() == null)
                return ActionResult.FAIL;

            nbt.putUuid("tardis", exterior.getTardis().getUuid());
            return ActionResult.SUCCESS;
        }

        // Move tardis to the clicked pos
        if (!nbt.contains("tardis"))
            return ActionResult.FAIL;

        Tardis tardis = ServerTardisManager.getInstance().getTardis(nbt.getUuid("tardis"));

        if (tardis != null) {
            TardisTravel travel = tardis.getTravel();

            travel.setDestination(new AbsoluteBlockPos.Directed(pos.up(), world, player.getMovementDirection().getOpposite()), true);
            // travel.toggleHandbrake();

            if (travel.getState() == TardisTravel.State.LANDED)
                travel.dematerialise(true);
            if (travel.getState() == TardisTravel.State.FLIGHT)
                travel.materialise();

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Text.of("Hold shift for more info"));
            return;
        }

        NbtCompound tag = stack.getOrCreateNbt();
        String text = tag.contains("tardis") ? tag.getUuid("tardis").toString()
                : "When a TARDIS is linked, it's UUID will show here.";

        tooltip.add(Text.literal(text).fillStyle(Style.EMPTY.withBold(true)));
    }
}
