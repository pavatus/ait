package mdteam.ait.core.helper;

import com.mojang.logging.LogUtils;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.desktop.DesktopSchema;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.TardisHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;
import java.util.UUID;

public class TardisUtil {
    public static ServerWorld getTardisDimension(MinecraftServer server) {
        return server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
    }
    public static ServerWorld getTardisDimension() {
        return getTardisDimension(AITMod.mcServer);
    }
    public static Map<UUID, Tardis> getTardisMap() {
        return TardisHandler.tardisses;
    }
    public static Tardis getTardisFromUuid(UUID uuid) {
        return TardisHandler.getTardis(uuid);
    }
    public static Tardis create(AbsoluteBlockPos position, ExteriorEnum exterior, DesktopSchema schema, UUID id) {
        TARDISDesktop desktop = new TARDISDesktop(schema);
        System.out.println(schema);
        Tardis tardis = new Tardis();
        tardis.setExterior(exterior);
        tardis.setUuid(id);
        tardis.setPosition(position);
        tardis.setDesktop(desktop);
        TardisHandler.saveTardis(tardis);
        if(position != null) placeExterior(tardis);
        return tardis;
    }
    public static ExteriorBlockEntity placeExterior(Tardis tardis) {

        tardis.world().setBlockState(tardis.getPosition().toBlockPos(), AITBlocks.EXTERIOR_BLOCK.getDefaultState());

        ExteriorBlockEntity entity = new ExteriorBlockEntity(tardis.getPosition().toBlockPos(), tardis.world().getBlockState(tardis.getPosition().toBlockPos()));
        entity.setTardis(tardis);
        tardis.world().addBlockEntity(entity);

        return (ExteriorBlockEntity) tardis.world().getBlockEntity(tardis.getPosition().toBlockPos());
    }

    public static void updateBlockEntity(Tardis tardis) {
        if (tardis.world().isClient()) return;

        BlockEntity entity = tardis.world().getBlockEntity(tardis.getPosition().toBlockPos());

        if (!(entity instanceof ExteriorBlockEntity exteriorBlockEntity)) {
            LogUtils.getLogger().error("Could not find Exterior Block Entity at " + tardis.getPosition().toString() + " when trying to update!\nInstead got: " + entity);
        } else {
            exteriorBlockEntity.setTardis(tardis);
        }
    }
}
