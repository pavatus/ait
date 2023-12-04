package mdteam.ait.core.item;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import mdteam.ait.tardis.TardisTravel;

@Deprecated
/**
 * Only for testing purposes to change exteriors, will be removed and replaced with proper changing
 */
public class ExteriorSelectItem extends Item {
    private final ExteriorEnum exterior;

    public ExteriorSelectItem(Settings settings, ExteriorEnum exterior) {
        super(settings);
        this.exterior = exterior;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (world.isClient() || player == null)
            return ActionResult.PASS;

        if (context.getHand() == Hand.MAIN_HAND) {
            BlockEntity entity = world.getBlockEntity(context.getBlockPos());

            if (entity instanceof ExteriorBlockEntity exteriorBlock) {
                TardisTravel.State state = exteriorBlock.getTardis().getTravel().getState();

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT))
                    return ActionResult.PASS;

                exteriorBlock.getTardis().getExterior().setType(this.exterior);
            } else if (entity instanceof ConsoleBlockEntity consoleBlock) {
                TardisTravel.State state = consoleBlock.getTardis().getTravel().getState();

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT))
                    return ActionResult.PASS;

                consoleBlock.getTardis().getExterior().setType(this.exterior);
            }
        }

        return ActionResult.SUCCESS;
    }
}
