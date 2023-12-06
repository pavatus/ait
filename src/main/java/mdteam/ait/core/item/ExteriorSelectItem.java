package mdteam.ait.core.item;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import mdteam.ait.tardis.TardisTravel;

import java.io.Console;

@Deprecated
/**
 * Only for testing purposes to change exteriors, will be removed and replaced with proper changing
 */
public class ExteriorSelectItem extends Item {
    public ExteriorSelectItem(Settings settings) {
        super(settings);
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

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT)) {
                    return ActionResult.PASS;
                }

                ExteriorEnum[] values = ExteriorEnum.values();
                int nextIndex = (exteriorBlock.getTardis().getExterior().getType().ordinal() + 1) % values.length;
                exteriorBlock.getTardis().getExterior().setType(values[nextIndex]);
                System.out.println(exteriorBlock.getTardis().getExterior().getType());
            }
            if (entity instanceof DoorBlockEntity doorBlock) {
                TardisTravel.State state = doorBlock.getTardis().getTravel().getState();

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT)) {
                    return ActionResult.PASS;
                }

                ExteriorEnum[] values = ExteriorEnum.values();
                int nextIndex = (doorBlock.getTardis().getExterior().getType().ordinal() + 1) % values.length;
                doorBlock.getTardis().getExterior().setType(values[nextIndex]);
            }
            if (entity instanceof ConsoleBlockEntity consoleBlock) {
                TardisTravel.State state = consoleBlock.getTardis().getTravel().getState();

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT)) {
                    return ActionResult.PASS;
                }

                ConsoleEnum[] values = ConsoleEnum.values();
                int nextIndex = (consoleBlock.getTardis().getConsole().getType().ordinal() + 1) % values.length;
                consoleBlock.killControls();
                consoleBlock.getTardis().getConsole().setType(values[nextIndex]);
                consoleBlock.spawnControls();
            }
        }

        return ActionResult.SUCCESS;
    }
}