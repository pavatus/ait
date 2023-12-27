package mdteam.ait.tardis.variant.exterior.cube;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.client.models.exteriors.CoobExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.core.AITDoors;
import mdteam.ait.core.AITExteriors;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.exterior.CubeExterior;
import mdteam.ait.tardis.variant.door.CapsuleDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class RedCoobVariant extends CubeVariant {
    public RedCoobVariant() {
        super("red");
    }
}
