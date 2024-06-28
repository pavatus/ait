package loqor.ait.compat.immersive;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.Vec3d;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

// NEVER EVER ACCESS THIS CLASS OR YO GAME GON CRAAASH
public class PortalsHandler {
	private static final HashMap<UUID, List<TardisPortal>> portals = new HashMap<>();

	public static void init() {
		AITMod.LOGGER.info("AIT - Setting up BOTI");

		TardisEvents.DOOR_OPEN.register((PortalsHandler::createPortals));
		TardisEvents.DOOR_CLOSE.register((PortalsHandler::removePortals));
		TardisEvents.DOOR_MOVE.register(((tardis, previous) -> removePortals(tardis)));
	}

	public static void removePortals(Tardis tardis) {
		if (tardis == null) return;

		if (tardis.door().getDoorState() != DoorData.DoorStateEnum.CLOSED)
			return; // todo move to a seperate method so we can remove without checks

		List<TardisPortal> list = portals.get(tardis.getUuid());

		if (list == null)
			return;

		for (TardisPortal portal : list) {
			PortalManipulation.removeConnectedPortals(portal, (p) -> {});
			portal.discard();
		}

		// i  have trust issues with code
		portals.remove(tardis.getUuid());
	}

	private static void createPortals(Tardis tardis) {
		if (tardis == null)
			return;

		if (!tardis.getExterior().getVariant().hasPortals())
			return;

		List<TardisPortal> list = new ArrayList<>();

		TardisPortal interior = createInteriorPortal(tardis);

		if (tardis.travel2().getState() == TravelHandlerBase.State.LANDED) {
			list.add(createExteriorPortal(tardis));
		}

		list.add(interior);
		portals.put(tardis.getUuid(), list);
	}

	private static TardisPortal createExteriorPortal(Tardis tardis) {
		DirectedBlockPos doorPos = tardis.getDesktop().doorPos();
		DirectedGlobalPos.Cached exteriorPos = tardis.travel2().getState() == TravelHandlerBase.State.LANDED
				? tardis.travel2().position() : tardis.travel2().getProgress();

		Vec3d doorAdjust = adjustInteriorPos(tardis.getExterior().getVariant().door(), doorPos);
		Vec3d exteriorAdjust = adjustExteriorPos(tardis.getExterior().getVariant(), exteriorPos);

		TardisPortal portal = new TardisPortal(exteriorPos.getWorld(), tardis);

		portal.setOrientationAndSize(
				new Vec3d(1, 0, 0), // axisW
				new Vec3d(0, 1, 0), // axisH
				tardis.getExterior().getVariant().portalWidth(), // width
				tardis.getExterior().getVariant().portalHeight() // height
		);

		DQuaternion quat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), RotationPropertyHelper.toDegrees(exteriorPos.getRotation()));
		DQuaternion doorQuat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), RotationPropertyHelper.toDegrees(doorPos.getRotation()));

		PortalAPI.setPortalOrientationQuaternion(portal, quat);
		portal.setOtherSideOrientation(doorQuat);

		portal.setOriginPos(new Vec3d(exteriorAdjust.getX() + 0.5, exteriorAdjust.getY() + 1, exteriorAdjust.getZ() + 0.5));

		portal.setDestinationDimension(TardisUtil.getTardisDimension().getRegistryKey());
		portal.setDestination(new Vec3d(doorAdjust.getX() + 0.5, doorAdjust.getY() + 1, doorAdjust.getZ() + 0.5));

		portal.renderingMergable = true;
		portal.getWorld().spawnEntity(portal);

		return portal;
	}

	// todo allow for multiple interior doors
	private static TardisPortal createInteriorPortal(Tardis tardis) {
		DirectedBlockPos doorPos = tardis.getDesktop().doorPos();
		DirectedGlobalPos.Cached exteriorPos = tardis.travel2().getState() == TravelHandlerBase.State.LANDED
				? tardis.travel2().position() : tardis.travel2().getProgress();

		Vec3d doorAdjust = adjustInteriorPos(tardis.getExterior().getVariant().door(), doorPos);
		Vec3d exteriorAdjust = adjustExteriorPos(tardis.getExterior().getVariant(), exteriorPos);

		TardisPortal portal = new TardisPortal(TardisUtil.getTardisDimension(), tardis);

		portal.setOrientationAndSize(
				new Vec3d(1, 0, 0), // axisW
				new Vec3d(0, 1, 0), // axisH
				tardis.getExterior().getVariant().portalWidth(), // width
				tardis.getExterior().getVariant().portalHeight() // height
		);

		DQuaternion quat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), RotationPropertyHelper.toDegrees(doorPos.getRotation()));
		DQuaternion extQuat = DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), RotationPropertyHelper.toDegrees(exteriorPos.getRotation()));

		PortalAPI.setPortalOrientationQuaternion(portal, quat);
		portal.setOtherSideOrientation(extQuat);

		portal.setOriginPos(new Vec3d(doorAdjust.getX() + 0.5, doorAdjust.getY() + 1, doorAdjust.getZ() + 0.5));

		portal.setDestinationDimension(exteriorPos.getWorld().getRegistryKey());
		portal.setDestination(new Vec3d(exteriorAdjust.getX() + 0.5, exteriorAdjust.getY() + 1, exteriorAdjust.getZ() + 0.5));

		portal.renderingMergable = true;
		portal.getWorld().spawnEntity(portal);

		return portal;
	}

	private static Vec3d adjustExteriorPos(ExteriorVariantSchema exterior, DirectedGlobalPos.Cached directed) {
		BlockPos pos = directed.getPos();

		return exterior.adjustPortalPos(new Vec3d(pos.getX(), pos.getY(), pos.getZ()),
				RotationPropertyHelper.toDirection(directed.getRotation()).get()
		);
	}

	private static Vec3d adjustInteriorPos(DoorSchema door, DirectedBlockPos directed) {
		BlockPos pos = directed.getPos();
		return door.adjustPortalPos(new Vec3d(pos.getX(), pos.getY(), pos.getZ()),
				RotationPropertyHelper.toDirection(directed.getRotation()).get()
		);
	}
}
