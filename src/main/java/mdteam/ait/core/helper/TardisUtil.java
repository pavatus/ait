package mdteam.ait.core.helper;

import com.mojang.logging.LogUtils;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.components.world.tardis.TARDISListComponent;
import mdteam.ait.core.helper.desktop.DesktopSchema;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.Tardis;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

import static mdteam.ait.AITMod.TARDISNBT;
import static mdteam.ait.AITMod.mcServer;

public class TardisUtil {
    public static ServerWorld getTardisDimension(MinecraftServer server) {
        return server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
    }
    public static ServerWorld getTardisDimension() {
        return getTardisDimension(AITMod.mcServer);
    }
    public static TARDISListComponent getComponent() {
        return TARDISNBT.get(AITMod.tardisListComponent);
    }
    public static List<Tardis> getTardises() {
        return getComponent().getTardises();
    }
    public static Tardis getTardisFromUuid(UUID uuid) {
        // @TODO slow
        for (Tardis tardis : getTardises()) {
            if (tardis.getUuid() == uuid) return tardis;
        }
        return null;
    }
    public static Tardis create(AbsoluteBlockPos position, ExteriorEnum exterior, DesktopSchema schema) {
        UUID uuid = UUID.randomUUID();
        TARDISDesktop desktop = new TARDISDesktop(schema);

        System.out.println(schema);

        Tardis tardis = new Tardis(uuid,desktop,position);
        getComponent().putTardis(tardis);
        placeExterior(tardis);

        return tardis;
    }
    public static ExteriorBlockEntity placeExterior(Tardis tardis) {
        World world = tardis.getPosition().getDimension();

        world.setBlockState(tardis.getPosition(),AITBlocks.EXTERIOR_BLOCK.getDefaultState());

        ExteriorBlockEntity entity = new ExteriorBlockEntity(tardis.getPosition(),world.getBlockState(tardis.getPosition()));
        entity.link(tardis);
        world.addBlockEntity(entity);

        return (ExteriorBlockEntity) world.getBlockEntity(tardis.getPosition());
    }

    public static void updateBlockEntity(Tardis tardis) {
        if (tardis.world().isClient()) return;

        BlockEntity entity = tardis.world().getBlockEntity(tardis.getPosition());

        if (!(entity instanceof ExteriorBlockEntity)) {
            LogUtils.getLogger().error("Could not find Exterior Block Entity at " + tardis.getPosition().toString() + " when trying to update!\nInstead got: " + entity);
            return;
        }

        ((ExteriorBlockEntity) entity).link(tardis);
    }
}
