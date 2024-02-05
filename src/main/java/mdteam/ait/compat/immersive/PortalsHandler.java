package mdteam.ait.compat.immersive;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.util.math.Vec3d;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.*;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


// NEVER EVER ACCESS THIS CLASS OR YO GAME GON CRAAASH
public class PortalsHandler {
    private static final HashMap<UUID, List<TardisPortal>> portals = new HashMap<>();

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

    public static void removePortals(Tardis tardis) {
        if (tardis == null) return;

        if (tardis.getHandlers().getDoor().getDoorState() != DoorData.DoorStateEnum.CLOSED) return; // todo move to a seperate method so we can remove without checks

        List<TardisPortal> list = portals.get(tardis.getUuid());

        if (list == null) return;

        for (TardisPortal portal : list) {
            PortalManipulation.removeConnectedPortals(portal, (p) -> {});
            portal.discard();
        }

        // i  have trust issues with code

        portals.remove(tardis.getUuid());
        tardis.markDirty();
    }

    private static void createPortals(Tardis tardis) {
        if (tardis == null) return;

        if (!tardis.getExterior().getVariant().hasPortals()) return;

        List<TardisPortal> list = new ArrayList<>();

        TardisPortal interior = createInteriorPortal(tardis);
        if (tardis.getTravel().getState() == TardisTravel.State.LANDED) {
            TardisPortal exterior = createExteriorPortal(tardis);
            list.add(exterior);
        }

        list.add(interior);

        portals.put(tardis.getUuid(), list);
        tardis.markDirty();
    }

    private static TardisPortal createExteriorPortal(Tardis tardis) {
        AbsoluteBlockPos.Directed doorPos = tardis.getTravel().getDoorPos();
        AbsoluteBlockPos.Directed exteriorPos = tardis.getTravel().getState() == TardisTravel.State.LANDED ? tardis.getTravel().getExteriorPos() : FlightUtil.getPositionFromPercentage(tardis.position(), tardis.destination(), tardis.getHandlers().getFlight().getDurationAsPercentage());
        Vec3d doorAdjust = adjustInteriorPos(tardis.getExterior().getVariant().door(),doorPos);
        Vec3d exteriorAdjust = adjustExteriorPos(tardis.getExterior().getVariant(),exteriorPos);

        TardisPortal portal = new TardisPortal(exteriorPos.getWorld(), tardis);

        portal.setOrientationAndSize(
                new Vec3d(1, 0, 0), // axisW
                new Vec3d(0, 1, 0), // axisH
                tardis.getExterior().getVariant().portalWidth(), // width
                tardis.getExterior().getVariant().portalHeight() // height
        );

        DQuaternion quat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), exteriorPos.getDirection().asRotation());
        DQuaternion doorQuat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), doorPos.getDirection().asRotation());

        PortalAPI.setPortalOrientationQuaternion(portal, quat);
        portal.setOtherSideOrientation(doorQuat);

        portal.setOriginPos(new Vec3d(exteriorAdjust.getX() + 0.5, exteriorAdjust.getY() + 1, exteriorAdjust.getZ() + 0.5));

        portal.setDestinationDimension(doorPos.getWorld().getRegistryKey());
        portal.setDestination(new Vec3d(doorAdjust.getX() + 0.5, doorAdjust.getY() + 1, doorAdjust.getZ() + 0.5));
        //portal.setInteractable(false);
        portal.renderingMergable = true;
        portal.getWorld().spawnEntity(portal);

        return portal;
    }

    // todo allow for multiple interior doors
    private static TardisPortal createInteriorPortal(Tardis tardis) {
        AbsoluteBlockPos.Directed doorPos = tardis.getTravel().getDoorPos();
        AbsoluteBlockPos.Directed exteriorPos = tardis.getTravel().getState() == TardisTravel.State.LANDED ? tardis.getTravel().getExteriorPos() : FlightUtil.getPositionFromPercentage(tardis.position(), tardis.destination(), tardis.getHandlers().getFlight().getDurationAsPercentage());
        Vec3d doorAdjust = adjustInteriorPos(tardis.getExterior().getVariant().door(), doorPos);
        Vec3d exteriorAdjust = adjustExteriorPos(tardis.getExterior().getVariant(), exteriorPos);

        TardisPortal portal = new TardisPortal(doorPos.getWorld(), tardis);

        portal.setOrientationAndSize(
                new Vec3d(1, 0, 0), // axisW
                new Vec3d(0, 1, 0), // axisH
                tardis.getExterior().getVariant().portalWidth(), // width
                tardis.getExterior().getVariant().portalHeight() // height
        );

        DQuaternion quat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), doorPos.getDirection().asRotation());
        DQuaternion extQuat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), exteriorPos.getDirection().asRotation());

        PortalAPI.setPortalOrientationQuaternion(portal, quat);
        portal.setOtherSideOrientation(extQuat);


        portal.setOriginPos(new Vec3d(doorAdjust.getX() + 0.5, doorAdjust.getY() + 1, doorAdjust.getZ() + 0.5));
        portal.setDestinationDimension(exteriorPos.getWorld().getRegistryKey());
        portal.setDestination(new Vec3d(exteriorAdjust.getX() + 0.5, exteriorAdjust.getY() + 1, exteriorAdjust.getZ() + 0.5));
        //portal.setInteractable(false);
        portal.renderingMergable = true;
        portal.getWorld().spawnEntity(portal);

        return portal;
    }

    private static Vec3d adjustExteriorPos(ExteriorVariantSchema exterior, AbsoluteBlockPos.Directed pos) {
        return exterior.adjustPortalPos(new Vec3d(pos.getX(), pos.getY(),pos.getZ()), pos.getDirection());
    }
    private static Vec3d adjustInteriorPos(DoorSchema door, AbsoluteBlockPos.Directed pos) {
        return door.adjustPortalPos(new Vec3d(pos.getX(), pos.getY(),pos.getZ()), pos.getDirection());
    }
}
