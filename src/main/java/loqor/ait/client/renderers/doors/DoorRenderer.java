package loqor.ait.client.renderers.doors;

import loqor.ait.client.models.doors.DoomDoorModel;
import loqor.ait.client.models.doors.DoorModel;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.blocks.DoorBlock;
import loqor.ait.registry.impl.door.ClientDoorRegistry;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.core.data.schema.door.ClientDoorSchema;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.OvergrownData;
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

import java.util.Optional;

public class DoorRenderer<T extends DoorBlockEntity> implements BlockEntityRenderer<T> {

	private DoorModel model;

	public DoorRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Optional<Tardis> optionalTardis = entity.findTardis();

		if (optionalTardis.isEmpty())
			return;

		Tardis tardis = optionalTardis.get();

		ClientExteriorVariantSchema exteriorVariant = ClientExteriorVariantRegistry.withParent(tardis.getExterior().getVariant());
		ClientDoorSchema variant = ClientDoorRegistry.withParent(exteriorVariant.parent().door());
		Class<? extends DoorModel> modelClass = variant.model().getClass();

		if (model != null && !(model.getClass().isInstance(modelClass)))
			model = null;

		if (model == null)
			this.model = variant.model();

		BlockState blockState = entity.getCachedState();
		float k = blockState.get(DoorBlock.FACING).asRotation();

        matrices.push();
		matrices.translate(0.5, 0, 0.5);
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

		Identifier texture = exteriorVariant.texture();

		if (exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM)) {
			texture = tardis.getDoor().isOpen() ? DoomDoorModel.DOOM_DOOR_OPEN : DoomDoorModel.DOOM_DOOR;
		}

		if (DependencyChecker.hasPortals() && tardis.travel().getState() == TardisTravel.State.LANDED
				&& !PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.IS_FALLING)
				&& tardis.getDoor().getDoorState() != DoorData.DoorStateEnum.CLOSED
		) {
			BlockPos pos = tardis.travel().getPosition();
			World world = tardis.travel().getPosition().getWorld();
			if (world != null) {
                int lightConst = 524296; // 1 / maxLight;
				int i = world.getLightLevel(LightType.SKY, pos);
				int j = world.getLightLevel(LightType.BLOCK, pos);
                light = (i + j > 15 ? (15 * 2) + (j > 0 ? 0 : -5) : world.isNight()
						? (i / 15) + j > 0 ? j + 13 : j
						: i + (world.getRegistryKey().equals(World.NETHER) ? j * 2 : j)) * lightConst;
			}
		}

		if (model != null) {
			if (!tardis.siege().isActive()) {
				model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1 /*0.5f*/, 1);
				if (tardis.<OvergrownData>handler(TardisComponent.Id.OVERGROWN).isOvergrown()) {
					model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(tardis.<OvergrownData>handler(TardisComponent.Id.OVERGROWN).getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
				}
			}

			ClientLightUtil.renderEmissivable(
					tardis.engine().hasPower(), model::renderWithAnimations, exteriorVariant.emission(), entity,
					this.model.getPart(), matrices, vertexConsumers, light, overlay, 1, 1, 1, 1
			);

			if (tardis.<BiomeHandler>handler(TardisComponent.Id.BIOME).getBiomeKey() != null && !exteriorVariant.equals(ClientExteriorVariantRegistry.CORAL_GROWTH)) {
				Identifier biomeTexture = exteriorVariant.getBiomeTexture(BiomeHandler.getBiomeTypeFromKey(tardis.<BiomeHandler>handler(TardisComponent.Id.BIOME).getBiomeKey()));
				if (biomeTexture != null && !texture.equals(biomeTexture)) {
					model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(biomeTexture)), light, overlay, 1, 1, 1, 1);
				}
			}
		}
		matrices.pop();
	}
}
