package loqor.ait.compat.immersive;

import java.util.ArrayList;
import java.util.List;

import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.my_util.DQuaternion;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.Vec3d;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.handler.DoorHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.data.DirectedBlockPos;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.data.bsp.Exclude;
import loqor.ait.data.schema.door.DoorSchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.TardisComponentRegistry;

public class PortalsHandler extends KeyedTardisComponent {

    static final IdLike ID = new AbstractId<>("PORTALS", PortalsHandler::new, PortalsHandler.class);

    @Exclude
    private List<TardisPortal> portals = new ArrayList<>();

    public static void init() {
        TardisComponentRegistry.getInstance().register(ID);

        TardisEvents.DOOR_OPEN.register(PortalsHandler::createPortals);
        TardisEvents.DOOR_CLOSE.register(PortalsHandler::removePortals);

        TardisEvents.EXTERIOR_CHANGE.register((tardis) -> {
            PortalsHandler.removePortals(tardis);
            PortalsHandler.createPortals(tardis);
        });

        TardisEvents.DOOR_MOVE.register(((tardis, previous) -> PortalsHandler.removePortals(tardis)));
    }

    public PortalsHandler() {
        super(ID);
    }

    @Override
    public void onLoaded() {
        this.portals = new ArrayList<>();
    }

    private static void createPortals(Tardis tardis) {
        if (tardis == null)
            return;

        if (!tardis.getExterior().getVariant().hasPortals())
            return;

        PortalsHandler handler = tardis.handler(ID);

        if (tardis.travel().getState() == TravelHandlerBase.State.LANDED)
            handler.portals.add(createExteriorPortal(tardis));

        handler.portals.add(createInteriorPortal(tardis));
    }

    private static void removePortals(Tardis tardis) {
        if (tardis.door() == null || tardis.door().getDoorState() != DoorHandler.DoorStateEnum.CLOSED)
            return;

        PortalsHandler handler = tardis.handler(ID);

        for (TardisPortal portal : handler.portals) {
            PortalManipulation.removeConnectedPortals(portal, (p) -> {
            });
            portal.discard();
        }
    }

    private static TardisPortal createExteriorPortal(Tardis tardis) {
        DirectedBlockPos doorPos = tardis.getDesktop().doorPos();
        DirectedGlobalPos.Cached exteriorPos = tardis.travel().getState() == TravelHandlerBase.State.LANDED
                ? tardis.travel().position()
                : tardis.travel().getProgress();

        Vec3d doorAdjust = adjustInteriorPos(tardis.getExterior().getVariant().door(), doorPos);
        Vec3d exteriorAdjust = adjustExteriorPos(tardis.getExterior().getVariant(), exteriorPos);

        TardisPortal portal = new TardisPortal(exteriorPos.getWorld(), tardis);

        portal.setOrientationAndSize(new Vec3d(1, 0, 0), // axisW
                new Vec3d(0, 1, 0), // axisH
                tardis.getExterior().getVariant().portalWidth(), // width
                tardis.getExterior().getVariant().portalHeight() // height
        );

        DQuaternion quat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0),
                180 + RotationPropertyHelper.toDegrees(exteriorPos.getRotation()));
        DQuaternion doorQuat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0),
                RotationPropertyHelper.toDegrees(doorPos.getRotation()));

        PortalAPI.setPortalOrientationQuaternion(portal, quat);
        portal.setOtherSideOrientation(doorQuat);

        portal.setOriginPos(
                new Vec3d(exteriorAdjust.getX() + 0.5, exteriorAdjust.getY() + 1, exteriorAdjust.getZ() + 0.5));

        portal.setDestinationDimension(WorldUtil.getTardisDimension().getRegistryKey());
        portal.setDestination(new Vec3d(doorAdjust.getX() + 0.5, doorAdjust.getY() + 1, doorAdjust.getZ() + 0.5));

        portal.renderingMergable = true;
        portal.getWorld().spawnEntity(portal);

        return portal;
    }

    private static TardisPortal createInteriorPortal(Tardis tardis) {
        DirectedBlockPos doorPos = tardis.getDesktop().doorPos();
        DirectedGlobalPos.Cached exteriorPos = tardis.travel().getState() == TravelHandlerBase.State.LANDED
                ? tardis.travel().position()
                : tardis.travel().getProgress();

        Vec3d doorAdjust = adjustInteriorPos(tardis.getExterior().getVariant().door(), doorPos);
        Vec3d exteriorAdjust = adjustExteriorPos(tardis.getExterior().getVariant(), exteriorPos);

        TardisPortal portal = new TardisPortal(WorldUtil.getTardisDimension(), tardis);

        portal.setOrientationAndSize(new Vec3d(1, 0, 0), // axisW
                new Vec3d(0, 1, 0), // axisH
                tardis.getExterior().getVariant().portalWidth(), // width
                tardis.getExterior().getVariant().portalHeight() // height
        );

        DQuaternion quat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0),
                RotationPropertyHelper.toDegrees(doorPos.getRotation()));
        DQuaternion extQuat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0),
                180 + RotationPropertyHelper.toDegrees(exteriorPos.getRotation()));

        PortalAPI.setPortalOrientationQuaternion(portal, quat);
        portal.setOtherSideOrientation(extQuat);

        portal.setOriginPos(new Vec3d(doorAdjust.getX() + 0.5, doorAdjust.getY() + 1, doorAdjust.getZ() + 0.5));

        portal.setDestinationDimension(exteriorPos.getWorld().getRegistryKey());
        portal.setDestination(
                new Vec3d(exteriorAdjust.getX() + 0.5, exteriorAdjust.getY() + 1, exteriorAdjust.getZ() + 0.5));

        portal.renderingMergable = true;
        portal.getWorld().spawnEntity(portal);

        return portal;
    }

    private static Vec3d adjustExteriorPos(ExteriorVariantSchema exterior, DirectedGlobalPos.Cached directed) {
        BlockPos pos = directed.getPos();

        return exterior.adjustPortalPos(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), directed.getRotation());
    }

    private static Vec3d adjustInteriorPos(DoorSchema door, DirectedBlockPos directed) {
        BlockPos pos = directed.getPos();
        return door.adjustPortalPos(new Vec3d(pos.getX(), pos.getY(), pos.getZ()),
                RotationPropertyHelper.toDirection(directed.getRotation()).get());
    }
}
