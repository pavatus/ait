package loqor.ait.client.renderers.doors;

import loqor.ait.client.models.doors.DoomDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.registry.impl.door.ClientDoorRegistry;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class DoorRenderer<T extends DoorBlockEntity> implements BlockEntityRenderer<T> {

	private DoorModel model;

	public DoorRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.findTardis().isEmpty())
			return;

		ClientExteriorVariantSchema exteriorVariant = ClientExteriorVariantRegistry.withParent(entity.findTardis().get().getExterior().getVariant());
		ClientDoorSchema variant = ClientDoorRegistry.withParent(exteriorVariant.parent().door());
		Class<? extends DoorModel> modelClass = variant.model().getClass();

		if (model != null && !(model.getClass().isInstance(modelClass)))
			model = null;

		if (model == null)
			this.model = variant.model();

		BlockState blockState = entity.getCachedState();
		float f = blockState.get(ExteriorBlock.FACING).asRotation();
		int maxLight = 0xF000F0;
		matrices.push();
		matrices.translate(0.5, 0, 0.5);
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
		Identifier texture = exteriorVariant.texture();

		Identifier emission = exteriorVariant.emission();

		if (exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM)) {
			texture = entity.findTardis().get().getDoor().isOpen() ? DoomDoorModel.DOOM_DOOR_OPEN : DoomDoorModel.DOOM_DOOR;
			emission = null;
		}

		if (DependencyChecker.hasPortals() && entity.findTardis().get().getTravel().getState() == TardisTravel.State.LANDED && !PropertiesHandler.getBool(entity.findTardis().get().getHandlers().getProperties(), PropertiesHandler.IS_FALLING) && entity.findTardis().get().getDoor().getDoorState() != DoorData.DoorStateEnum.CLOSED) {
			BlockPos pos = entity.findTardis().get().getTravel().getPosition();
			World world = entity.findTardis().get().getTravel().getPosition().getWorld();
			if (world != null) {
				World doorWorld = entity.getWorld();
				BlockPos doorPos = entity.getPos();
				int lightConst = 524296; // 1 / maxLight;
				int i = world.getLightLevel(LightType.SKY, pos);
				int j = world.getLightLevel(LightType.BLOCK, pos);
				int k = doorWorld.getLightLevel(LightType.BLOCK, doorPos);
				light = (i + j > 15 ? (15 * 2) + (j > 0 ? 0 : -5) : world.isNight() ? (i / 15) + j > 0 ? j + 13 : j : i + (world.getRegistryKey().equals(World.NETHER) ? j * 2 : j)) * lightConst;
			}
		}

		if (model != null) {
			if (!entity.findTardis().get().isSiegeMode()) {
				model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1 /*0.5f*/, 1);
				if (entity.findTardis().get().getHandlers().getOvergrown().isOvergrown()) {
					model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(entity.findTardis().get().getHandlers().getOvergrown().getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
				}
			}
			if (emission != null && entity.findTardis().get().hasPower()) {
				boolean alarms = PropertiesHandler.getBool(entity.findTardis().get().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);
				model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentEmissive(emission, true)), maxLight, overlay, 1, alarms ? 0.3f : 1, alarms ? 0.3f : 1, 1);
			}
		}
		matrices.pop();
	}
}
