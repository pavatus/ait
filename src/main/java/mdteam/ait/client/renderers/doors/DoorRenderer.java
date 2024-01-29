package mdteam.ait.client.renderers.doors;

import mdteam.ait.client.models.doors.DoomDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.registry.ClientDoorRegistry;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.door.ClientDoorSchema;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
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
        if (entity.getTardis().isEmpty())
            return;

        ClientExteriorVariantSchema exteriorVariant = ClientExteriorVariantRegistry.withParent(entity.getTardis().get().getExterior().getVariant());
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
            texture = entity.getTardis().get().getDoor().isOpen() ? DoomDoorModel.DOOM_DOOR_OPEN : DoomDoorModel.DOOM_DOOR;
            emission = null;
        }
        // if (entity.getTardis().getDoor().getDoorState() != DoorData.DoorStateEnum.CLOSED)
        //     light = LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE;
        int red = 1;
        int green = 1;
        int blue = 1;

        if (DependencyChecker.hasPortals() && entity.getTardis().get().getTravel().getState() == TardisTravel.State.LANDED && !PropertiesHandler.getBool(entity.getTardis().get().getHandlers().getProperties(), PropertiesHandler.IS_FALLING) && entity.getTardis().get().getDoor().getDoorState() != DoorData.DoorStateEnum.CLOSED) {
            BlockPos pos = entity.getTardis().get().getTravel().getPosition();
            World world = entity.getTardis().get().getTravel().getPosition().getWorld();
            if (world != null) {
                World doorWorld = entity.getWorld();
                BlockPos doorPos = entity.getPos();
                int lightConst = 524296; // 1 / maxLight;
                //light = WorldRenderer.getLightmapCoordinates(entity.getTardis().getHandlers().getExteriorPos().getWorld(), entity.getTardis().getHandlers().getExteriorPos());;
                int i = world.getLightLevel(LightType.SKY, pos);
                int j = world.getLightLevel(LightType.BLOCK, pos);
                int k = doorWorld.getLightLevel(LightType.BLOCK, doorPos);
                /*light = ((i + j >= 15 ? ((i + j) * 2) : i != 0 ? i * (world.isNight() ? 1 : 2) +
                        (world.getRegistryKey().equals(World.NETHER) ? j * 2 : j + 6) : j * 2) * lightConst);*/
                light = (i + j > 15 ? (15 * 2) + (j > 0 ? 0 : -5) : world.isNight() ? (i / 15) + j > 0 ? j + 13 : j : i + (world.getRegistryKey().equals(World.NETHER) ? j * 2 : j)) * lightConst;
                //System.out.println("Sky: " + i + " | Block: " + j + " | light: " + light);
            }
        }

        if (model != null) {
            if (!entity.getTardis().get().isSiegeMode()) {
                model.animateTile(entity);
                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1 /*0.5f*/, 1);
                if (entity.getTardis().get().getHandlers().getOvergrown().isOvergrown()) {
                    model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(entity.getTardis().get().getHandlers().getOvergrown().getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
                }
            }
            if (emission != null && entity.getTardis().get().hasPower()) {
                boolean alarms = PropertiesHandler.getBool(entity.getTardis().get().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);
                model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentEmissive(emission, true)), maxLight, overlay, 1, alarms ? 0.3f : 1 , alarms ? 0.3f : 1, 1);
            }
        }
        matrices.pop();
    }
}
