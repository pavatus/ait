package mdteam.ait.core.item;

import mdteam.ait.core.AITDesktops;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import mdteam.ait.tardis.TardisTravel;

@Deprecated
/**
 * Only for testing purposes to change interiors, will be removed and replaced with proper changing
 */
public class InteriorSelectItem extends Item {
    private final Identifier interior;

    public InteriorSelectItem(Settings settings, Identifier interiorId) {
        super(settings);
        this.interior = interiorId;
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

                exteriorBlock.getTardis().getDesktop().changeInterior(AITDesktops.get(this.interior));
            }
            if (entity instanceof DoorBlockEntity exteriorBlock) {
                TardisTravel.State state = exteriorBlock.getTardis().getTravel().getState();

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT))
                    return ActionResult.PASS;

                exteriorBlock.getTardis().getDesktop().changeInterior(AITDesktops.get(this.interior));
            }
        }

        return ActionResult.SUCCESS;
    }
}
