package mdteam.ait.compat.immersive;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


// NEVER EVER ACCESS THIS CLASS OR YO GAME GON CRAAASH
public class PortalsHandler {
    private static HashMap<UUID, List<Portal>> portals = new HashMap<>();

    public static void init() {
        if (!DependencyChecker.hasPortals()) {
            AITMod.LOGGER.info("no immersive stuff for u pal"); // shouldnt be possible to get here anyway
            return;
        }
        AITMod.LOGGER.info("AIT - Setting up BOTI");

        TardisEvents.DOOR_OPEN.register((PortalsHandler::createPortals));
        TardisEvents.DOOR_CLOSE.register((PortalsHandler::removePortals));
        TardisEvents.DOOR_MOVE.register(((tardis, previous) -> removePortals(tardis)));
    }

    private static void removePortals(Tardis tardis) {
        if (tardis == null) return;

        if (tardis.getHandlers().getDoor().getDoorState() != DoorHandler.DoorStateEnum.CLOSED) return; // todo move to a seperate method so we can remove without checks

        List<Portal> list = portals.get(tardis.getUuid());

        if (list == null) return;

        for (Portal portal : list) {
            PortalManipulation.removeConnectedPortals(portal, (p) -> {});
            portal.discard();
        }

        // i  have trust issues with code

        portals.remove(tardis.getUuid());
        tardis.markDirty();
    }

    private static void createPortals(Tardis tardis) {
        if (tardis == null) return;

        if (!tardis.getExterior().getType().hasPortals()) return;

        List<Portal> list = new ArrayList<>();

        Portal interior = createInteriorPortal(tardis);
        Portal exterior = createExteriorPortal(tardis);

        list.add(interior);
        list.add(exterior);

        portals.put(tardis.getUuid(), list);
        tardis.markDirty();
    }

    private static Portal createExteriorPortal(Tardis tardis) {
        AbsoluteBlockPos.Directed doorPos = tardis.getTravel().getDoorPos();
        AbsoluteBlockPos.Directed exteriorPos = tardis.getTravel().getExteriorPos();
        Vec3d doorAdjust = adjustInteriorPos(tardis.getExterior().getVariant().door(),doorPos);
        Vec3d exteriorAdjust = adjustExteriorPos(tardis.getExterior().getVariant(),exteriorPos);

        Portal portal = Portal.entityType.create(exteriorPos.getWorld());

        portal.setOrientationAndSize(
                new Vec3d(1, 0, 0), // axisW
                new Vec3d(0, 1, 0), // axisH
                tardis.getExterior().getVariant().portalWidth(), // width
                tardis.getExterior().getVariant().portalHeight() // height
        );

        DQuaternion quat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), exteriorPos.getDirection().asRotation());
        PortalAPI.setPortalOrientationQuaternion(portal, quat);

        DQuaternion doorQuat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), doorPos.getDirection().asRotation());
        portal.setOtherSideOrientation(doorQuat);

        portal.setOriginPos(new Vec3d(exteriorAdjust.getX() + 0.5, exteriorAdjust.getY() + 1, exteriorAdjust.getZ() + 0.5));

        portal.setDestinationDimension(doorPos.getWorld().getRegistryKey());
        portal.setDestination(new Vec3d(doorAdjust.getX() + 0.5, doorAdjust.getY() + 1, doorAdjust.getZ() + 0.5));

        portal.getWorld().spawnEntity(portal);

        ((ITardisPortal) portal).setTardis(tardis);

        return portal;
    }
    private static Portal createInteriorPortal(Tardis tardis) {
        AbsoluteBlockPos.Directed doorPos = tardis.getTravel().getDoorPos();
        AbsoluteBlockPos.Directed exteriorPos = tardis.getTravel().getExteriorPos();
        Vec3d doorAdjust = adjustInteriorPos(tardis.getExterior().getVariant().door(), doorPos);
        Vec3d exteriorAdjust = adjustExteriorPos(tardis.getExterior().getVariant(), exteriorPos);

        Portal portal = Portal.entityType.create(doorPos.getWorld());

        portal.setOrientationAndSize(
                new Vec3d(1, 0, 0), // axisW
                new Vec3d(0, 1, 0), // axisH
                tardis.getExterior().getVariant().portalWidth(), // width
                tardis.getExterior().getVariant().portalHeight() // height
        );

        DQuaternion quat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), doorPos.getDirection().asRotation());
        PortalAPI.setPortalOrientationQuaternion(portal, quat);

        DQuaternion extQuat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), exteriorPos.getDirection().asRotation());
        portal.setOtherSideOrientation(extQuat);

        portal.setOriginPos(new Vec3d(doorAdjust.getX() + 0.5, doorAdjust.getY() + 1, doorAdjust.getZ() + 0.5));
        portal.setDestinationDimension(exteriorPos.getWorld().getRegistryKey());
        portal.setDestination(new Vec3d(exteriorAdjust.getX() + 0.5, exteriorAdjust.getY() + 1, exteriorAdjust.getZ() + 0.5));
        portal.getWorld().spawnEntity(portal);

        ((ITardisPortal) portal).setTardis(tardis);

        return portal;
    }

    private static Vec3d adjustExteriorPos(ExteriorVariantSchema exterior, AbsoluteBlockPos.Directed pos) {
        return exterior.adjustPortalPos(new Vec3d(pos.getX(), pos.getY(),pos.getZ()), pos.getDirection());
    }
    private static Vec3d adjustInteriorPos(DoorSchema door, AbsoluteBlockPos.Directed pos) {
        return door.adjustPortalPos(new Vec3d(pos.getX(), pos.getY(),pos.getZ()), pos.getDirection());
    }
}
