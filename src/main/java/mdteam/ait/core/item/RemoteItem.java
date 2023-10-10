package mdteam.ait.core.item;

import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RemoteItem extends TardisLinkableItem {
    public RemoteItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        if (!world.isClient()) {

            // Link to exteriors tardis if it exists and player is crouching
            if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior && player.isSneaking()) {
                if (exterior.tardis() == null) return ActionResult.FAIL;

                this.link(exterior.tardis());

                return ActionResult.SUCCESS;
            }

            // Move tardis to the clicked pos

            if (this.hasTardis()) {
                this.tardis().getTravel().moveTo(new AbsoluteBlockPos(world, player.getMovementDirection().getOpposite(), pos.up()));
            }
        }

        return super.useOnBlock(context);
    }
}
