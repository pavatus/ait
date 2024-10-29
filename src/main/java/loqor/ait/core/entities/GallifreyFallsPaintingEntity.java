package loqor.ait.core.entities;


import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.world.World;

import loqor.ait.core.AITEntityTypes;

public class GallifreyFallsPaintingEntity extends AbstractDecorationEntity {
    public GallifreyFallsPaintingEntity(EntityType<? extends GallifreyFallsPaintingEntity> entityType, World world) {
        super(AITEntityTypes.GALLIFREY_FALLS_PAINTING_TYPE, world);
    }

    @Override
    public int getWidthPixels() {
        return 48;
    }

    @Override
    public int getHeightPixels() {
        return 32;
    }

    @Override
    public void onBreak(@Nullable Entity entity) {

    }

    @Override
    public void onPlace() {

    }
}
