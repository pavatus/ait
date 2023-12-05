package mdteam.ait.core.entities.control.impl;

import mdteam.ait.core.entities.control.Control;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.Iterator;

public class LandTypeControl extends Control {
    public LandTypeControl() {
        super("land_type");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return false;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        // fixme for this, i was originally gonna do the searching like you would if it was landing, then i changed it to just do it automatically and have this
        // fixme configure the actual landing position on materialise by adding a new method called checkPositionAndMaterialise() that runs the loop and lands if possible. - Loqor

        //world.getChunk(tardis.getTravel().getDestination()).areSectionsEmptyBetween(-64, 256); perhaps a future use thing, doesn't return a list of air blocks though

        //TardisTravel travel = tardis.getTravel();
        //BlockPos.Mutable mutable = new BlockPos.Mutable(travel.getDestination().getX(), travel.getDestination().getY(), travel.getDestination().getZ());
        //for(int i = travel.getDestination().getY(); i < world.getTopY(); i++) {
        //    BlockState state = world.getBlockState(mutable.setY(i));
        //    if(state.getBlock() instanceof AirBlock) {
        //        AbsoluteBlockPos.Directed abpd = new AbsoluteBlockPos.Directed(mutable.setY(i),
        //                travel.getDestination().getWorld(), travel.getDestination().getDirection());
        //        travel.setDestination(abpd, false);
        //        break;
        //    }
        //}
        return false;
    }
}
