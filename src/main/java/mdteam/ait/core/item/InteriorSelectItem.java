package mdteam.ait.core.item;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.AITDesktops;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import mdteam.ait.tardis.TardisTravel;

import java.util.Iterator;

@Deprecated
/**
 * Only for testing purposes to change interiors, will be removed and replaced with proper changing
 */
public class InteriorSelectItem extends Item {
    private final Identifier defaultInterior;

    public InteriorSelectItem(Settings settings, Identifier defaultInterior) {
        super(settings);
        this.defaultInterior = defaultInterior;
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

                Identifier nextInteriorId = getNextInteriorId(exteriorBlock.getTardis().getDesktop().getSchema().id());
                exteriorBlock.getTardis().getDesktop().changeInterior(AITDesktops.get(nextInteriorId));
                player.sendMessage(Text.literal(nextInteriorId.toString()));
            }
            if (entity instanceof DoorBlockEntity doorBlock) {
                TardisTravel.State state = doorBlock.getTardis().getTravel().getState();

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT))
                    return ActionResult.PASS;

                Identifier nextInteriorId = getNextInteriorId(doorBlock.getTardis().getDesktop().getSchema().id());
                doorBlock.getTardis().getDesktop().changeInterior(AITDesktops.get(nextInteriorId));
                player.sendMessage(Text.literal(nextInteriorId.toString()));
            }
            if (entity instanceof ConsoleBlockEntity consoleBlock) {
                TardisTravel.State state = consoleBlock.getTardis().getTravel().getState();

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT)) {
                    return ActionResult.PASS;
                }

                consoleBlock.killControls();
                consoleBlock.markRemoved();
            }
        }

        return ActionResult.SUCCESS;
    }

    private Identifier getNextInteriorId(Identifier currentId) {
        // Assuming AITDesktops.iterator() returns an iterator for TardisDesktopSchema
        //for (Iterator<TardisDesktopSchema> it = AITDesktops.iterator().iterator(); it.hasNext(); ) {
        //    TardisDesktopSchema interior = it.next();
        //    if(interior.id().equals(currentId))
        //        return AITDesktops.iterator().iterator().next().id();
        //}

        // If not found (shouldn't happen in a proper iterator)
        return getNextInterior(currentId.getPath());
    }

    public Identifier getNextInterior(String identifier) {
        System.out.println("OOGLY BOOGLY BITCH" + identifier);
        return switch (identifier) {
            default -> new Identifier(AITMod.MOD_ID, "cave");
            case "cave" -> new Identifier(AITMod.MOD_ID, "botanist");
            case "botanist" -> new Identifier(AITMod.MOD_ID, "copperweb");
            case "copperweb" -> new Identifier(AITMod.MOD_ID, "dev");
            case "dev" -> new Identifier(AITMod.MOD_ID, "office");
            case "office" -> new Identifier(AITMod.MOD_ID, "pristine");
            case "pristine" -> new Identifier(AITMod.MOD_ID, "regal");
            case "regal" -> new Identifier(AITMod.MOD_ID, "type_40");
            case "type_40" -> new Identifier(AITMod.MOD_ID, "victorian");
            case "victorian" -> new Identifier(AITMod.MOD_ID, "war");
            case "war" -> new Identifier(AITMod.MOD_ID, "default_cave");
        };
    }

}