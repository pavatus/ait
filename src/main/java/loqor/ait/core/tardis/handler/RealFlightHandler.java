package loqor.ait.core.tardis.handler;

import loqor.ait.api.TardisComponent;
import loqor.ait.core.AITSounds;
import loqor.ait.core.entities.FallingTardisEntity;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RealFlightHandler extends KeyedTardisComponent implements TardisTickable {

    private static final BoolProperty IS_FALLING = new BoolProperty("falling", false);
    private final BoolValue falling = IS_FALLING.create(this);

    static {
        TardisEvents.DEMAT.register(tardis -> tardis.flight().falling().get() ? TardisEvents.Interaction.FAIL : TardisEvents.Interaction.PASS);
    }

    public RealFlightHandler() {
        super(Id.FLIGHT);
    }

    @Override
    public void onLoaded() {
        falling.of(this, IS_FALLING);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (this.falling.get())
            this.tardis.door().setLocked(true);
    }

    public void onLanding(ServerWorld world, BlockPos pos) {
        this.tardis.travel().forcePosition(cached -> cached.world(world.getRegistryKey()).pos(pos));

        this.falling.set(false);
        this.tardis.door().setDeadlocked(false);

        world.playSound(null, pos, AITSounds.LAND_THUD, SoundCategory.BLOCKS);

        tardis.getDesktop().playSoundAtEveryConsole(AITSounds.LAND_THUD, SoundCategory.BLOCKS);
        TardisEvents.LANDED.invoker().onLanded(tardis);
    }

    public void onStartFalling(ServerWorld world, BlockState state, BlockPos pos) {
        this.falling.set(true);
        TardisEvents.START_FALLING.invoker().onStartFall(tardis);

        FallingTardisEntity.spawnFromBlock(world, pos, state);
    }

    public BoolValue falling() {
        return falling;
    }
}
