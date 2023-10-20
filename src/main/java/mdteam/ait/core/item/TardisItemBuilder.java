package mdteam.ait.core.item;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.helper.desktop.DesktopInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

import static mdteam.ait.core.helper.TardisUtil.getTardisComponent;

public class TardisItemBuilder extends Item {
    public static final String DEFAULT_INTERIOR = "war";
    public static final ExteriorEnum DEFAULT_EXTERIOR = ExteriorEnum.SHELTER;

    private String desktop;
    private ExteriorEnum exterior;

    public TardisItemBuilder(Settings settings,ExteriorEnum exterior, String desktopId) {
        super(settings);
        this.exterior = exterior;
        this.desktop = desktopId;
    }
    public TardisItemBuilder(Settings settings, ExteriorEnum exterior) {
        this(settings,exterior,DEFAULT_INTERIOR);
    }
    public TardisItemBuilder(Settings settings) {
        this(settings,DEFAULT_EXTERIOR);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos().up();
        World level = context.getWorld();
        AbsoluteBlockPos absolutePos = new AbsoluteBlockPos(level, pos);
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();

        if (!level.isClient() && hand == Hand.MAIN_HAND) {
            System.out.println(this.desktop);
            TardisUtil.create(absolutePos, this.exterior, DesktopInit.get(this.desktop), UUID.randomUUID()/*new UUID(1, 1)*/);
            context.getStack().decrement(1);
        }
        return ActionResult.SUCCESS;
    }
}