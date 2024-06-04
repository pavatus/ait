package loqor.ait.tardis.travel;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.TardisTravel2;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;

public class RematTravelState implements TravelState {

    @Override
    public void onEnable(TardisTravel2 travel) {
        if (!(travel.tardis() instanceof ServerTardis tardis))
            return;

        travel.destination().set(FlightUtil.getPositionFromPercentage(
                travel.position().get(), travel.destination().get(), tardis.flight().getDurationAsPercentage())
        );

        // Check if materialization is on cooldown and return if it is
        if (!ignoreChecks && FlightUtil.isMaterialiseOnCooldown(tardis)) {
            return;
        }

        // Check if the Tardis materialization is prevented by event listeners
        if (!ignoreChecks && TardisEvents.MAT.invoker().onMat(tardis)) {
            failToMaterialise();
            return;
        }

        // Lock the Tardis doors
        // DoorData.lockTardis(true, this.getTardis().get(), null, true);

        // Set the Tardis state to materialise
        this.setState(TardisTravel.State.MAT);

        SequenceHandler sequences = tardis.handler(TardisComponent.Id.SEQUENCE);

        if (sequences.hasActiveSequence()) {
            sequences.setActiveSequence(null, true);
        }

        // Get the server world of the destination
        ServerWorld destWorld = (ServerWorld) this.getDestination().getWorld();

        // Play materialize sound at the destination
        this.getDestination().getWorld().playSound(null, this.getDestination(), this.getSoundForCurrentState(), SoundCategory.BLOCKS, 1f, 1f);

        FlightUtil.playSoundAtConsole(tardis, this.getSoundForCurrentState(), SoundCategory.BLOCKS, 1f, 1f);

        // Set the destination block to the Tardis exterior block
        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.ROTATION, Math.abs(this.getDestination().getRotation())).with(ExteriorBlock.LEVEL_9, 0);
        destWorld.setBlockState(this.getDestination(), state);

        // Create and add the exterior block entity at the destination
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.getDestination(), state);
        destWorld.addBlockEntity(blockEntity);

        // Set the position of the Tardis to the destination
        this.setPosition(this.getDestination());

        // Run animations on the block entity
        this.runAnimations(blockEntity);

        this.onMaterialise(tardis);
    }

    @Override
    public TardisTravel2.State getNext() {
        return TardisTravel2.State.LANDED;
    }

    public record Context() {

    }
}
