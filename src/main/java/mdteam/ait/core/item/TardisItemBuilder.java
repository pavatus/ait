package mdteam.ait.core.item;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.DesktopUtil;
import mdteam.ait.core.helper.TARDISUtil;
import mdteam.ait.core.helper.desktop.DesktopInit;
import mdteam.ait.core.helper.desktop.DesktopSchema;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.helper.desktop.impl.WarDesktop;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TardisItemBuilder extends Item {
    public static final DesktopSchema DEFAULT_INTERIOR = DesktopInit.get("war");
    public static final ExteriorEnum DEFAULT_EXTERIOR = ExteriorEnum.SHELTER;

    private DesktopSchema desktop;
    private ExteriorEnum exterior;

    public TardisItemBuilder(Settings settings,ExteriorEnum exterior, DesktopSchema desktop) {
        super(settings);
        this.exterior = exterior;
        this.desktop = desktop;
    }
    public TardisItemBuilder(Settings settings, ExteriorEnum exterior) {
        this(settings,exterior,DEFAULT_INTERIOR);
    }
    public TardisItemBuilder(Settings settings) {
        this(settings,DEFAULT_EXTERIOR,DEFAULT_INTERIOR);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World level = context.getWorld();
        AbsoluteBlockPos absolutePos = new AbsoluteBlockPos(level,pos);
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();

        if (!level.isClient() && hand == Hand.MAIN_HAND) {
            TARDISUtil.create(absolutePos,exterior,desktop);
            context.getStack().decrement(1);
        }
        return ActionResult.SUCCESS;
    }
}
