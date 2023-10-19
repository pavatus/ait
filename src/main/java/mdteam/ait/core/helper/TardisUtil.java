package mdteam.ait.core.helper;

import com.mojang.logging.LogUtils;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.components.world.tardis.TARDISListComponent;
import mdteam.ait.core.components.world.tardis.TardisComponent;
import mdteam.ait.core.helper.desktop.DesktopSchema;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.Tardis;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class TardisUtil {
    public static ServerWorld getTardisDimension(MinecraftServer server) {
        return server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
    }
    public static ServerWorld getTardisDimension() {
        return getTardisDimension(AITMod.mcServer);
    }
    public static TARDISListComponent getListComponent() {
        return AITMod.tardisListComponent;
    }
    public static TardisComponent getTardisComponent() {
        return AITMod.tardisComponent;
    }
    public static List<Tardis> getTardises() {
        return getListComponent().getTardises();
    }
    public static Tardis getTardisFromUuid(UUID uuid) {
        // @TODO slow
        System.out.println("@1 UUID???: " + uuid + " | LIST UUID?? " + getTardises().get(0).getUuid());
        for(Tardis tardis : getTardises()) {
            if(tardis.getUuid().equals(uuid)) {
                return tardis;
            }

            System.out.println("THIS IS THE TARDIS" + tardis);

        }
        return null;
    }
    public static Tardis create(AbsoluteBlockPos position, ExteriorEnum exterior, DesktopSchema schema, UUID id) {
        TARDISDesktop desktop = new TARDISDesktop(schema);

        System.out.println(schema);

        Tardis tardis = new Tardis(id,desktop,position);
        getListComponent().putTardis(tardis);
        if(position != null) placeExterior(tardis);

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
