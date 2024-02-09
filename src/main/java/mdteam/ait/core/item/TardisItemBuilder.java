package mdteam.ait.core.item;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.registry.CategoryRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.exterior.category.CapsuleCategory;
import mdteam.ait.tardis.exterior.category.ExteriorCategory;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;

import java.util.Random;

public class TardisItemBuilder extends Item {

    public static final Identifier DEFAULT_INTERIOR = new Identifier(AITMod.MOD_ID, "coral"); //new Identifier(AITMod.MOD_ID, "war");
    public static final Identifier DEFAULT_EXTERIOR = CapsuleCategory.REFERENCE;

    private final Identifier exterior;
    private final Identifier desktop;

    public TardisItemBuilder(Settings settings, Identifier exterior, Identifier desktopId) {
        super(settings);

        this.exterior = exterior;
        this.desktop = desktopId;
    }

    public TardisItemBuilder(Settings settings, Identifier exterior) {
        this(settings, exterior, DEFAULT_INTERIOR);
    }

    public TardisItemBuilder(Settings settings) {
        this(settings, DEFAULT_EXTERIOR);
    }

    public static ExteriorVariantSchema findRandomVariant(ExteriorCategory exterior) {
        Random rnd = new Random();
        if (ExteriorVariantRegistry.withParent(exterior).size() == 0) {
            AITMod.LOGGER.error("Variants for " + exterior + " are empty! Panicking!!!!");
            return ExteriorVariantRegistry.BOX_DEFAULT;
        }
        int randomized = rnd.nextInt(Math.abs(ExteriorVariantRegistry.withParent(exterior).size()));
        return (ExteriorVariantSchema) ExteriorVariantRegistry.withParent(exterior).toArray()[randomized];
    }
    public static ExteriorCategory findRandomExterior() {
        Random rnd = new Random();
        int randomized = rnd.nextInt(Math.abs(CategoryRegistry.getInstance().size()));
        return CategoryRegistry.getInstance().toArrayList().get(randomized) == CategoryRegistry.CORAL_GROWTH ? CategoryRegistry.TARDIM : CategoryRegistry.getInstance().toArrayList().get(randomized);
    }

    public static TardisDesktopSchema findRandomDesktop() {
        Random rnd = new Random();
        int randomized = rnd.nextInt(Math.abs(DesktopRegistry.getInstance().size()));
        return DesktopRegistry.getInstance().toArrayList().get(randomized);
    }
    public static TardisDesktopSchema findRandomDesktop(Tardis tardis) { // todo this may cause looping crashes
        TardisDesktopSchema found = findRandomDesktop();

        if (tardis.isDesktopUnlocked(found)) return found;

        return findRandomDesktop(tardis);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (world.isClient() || player == null)
            return ActionResult.PASS;

        AbsoluteBlockPos.Directed pos = new AbsoluteBlockPos.Directed(context.getBlockPos().up(), world, Direction.NORTH);

        if (context.getHand() == Hand.MAIN_HAND) {
            BlockEntity entity = world.getBlockEntity(context.getBlockPos());

            if (entity instanceof ConsoleBlockEntity consoleBlock) {
                if (consoleBlock.findTardis().isEmpty()) return ActionResult.FAIL;

                TardisTravel.State state = consoleBlock.findTardis().get().getTravel().getState();

                if (!(state == TardisTravel.State.LANDED || state == TardisTravel.State.FLIGHT)) {
                    return ActionResult.PASS;
                }

                consoleBlock.killControls();
                world.removeBlock(context.getBlockPos(), false);
                world.removeBlockEntity(context.getBlockPos());
                return ActionResult.SUCCESS;
            }

            //System.out.println(this.exterior);

            ServerTardisManager.getInstance().create(pos, CategoryRegistry.getInstance().get(this.exterior), findRandomVariant(CategoryRegistry.getInstance().get(this.exterior)) , DesktopRegistry.getInstance().get(this.desktop), false);
            context.getStack().decrement(1);
        }

        return ActionResult.SUCCESS;
    }
}