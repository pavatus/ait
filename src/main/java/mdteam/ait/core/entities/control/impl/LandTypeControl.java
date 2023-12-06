package mdteam.ait.core.entities.control.impl;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blocks.ConsoleBlock;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.core.entities.control.Control;
import mdteam.ait.core.helper.TardisUtil;
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

import static mdteam.ait.client.renderers.consoles.ConsoleEnum.BOREALIS;

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

        TardisTravel travel = tardis.getTravel();

        // fixme ?? im not entirely sure how to do this but it's really just my brain being fried - Loqor

        return false;
    }
}
