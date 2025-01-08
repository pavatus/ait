package loqor.ait.core.tardis.handler;

import dev.drtheo.blockqueue.data.TimeUnit;
import dev.drtheo.scheduler.Scheduler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;

import loqor.ait.AITMod;
import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.api.TardisTickable;
import loqor.ait.core.AITSounds;
import loqor.ait.core.entities.FallingTardisEntity;
import loqor.ait.core.entities.FlightTardisEntity;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.properties.bool.BoolProperty;
import loqor.ait.data.properties.bool.BoolValue;

public class RealFlightHandler extends KeyedTardisComponent implements TardisTickable {

    private static final Identifier ENTER_FLIGHT = AITMod.id("enter_flight");
    private static final Identifier EXIT_FLIGHT = AITMod.id("exit_flight");

    private static final BoolProperty IS_FALLING = new BoolProperty("falling", false);
    private static final BoolProperty FLYING = new BoolProperty("flying", false);

    private final BoolValue falling = IS_FALLING.create(this);
    private final BoolValue flying = FLYING.create(this);

    static {
        ClientPlayNetworking.registerGlobalReceiver(EXIT_FLIGHT, (client, handler, buf, responseSender) -> {
            client.options.setPerspective(Perspective.FIRST_PERSON);
            client.options.hudHidden = false;
        });

        TardisEvents.DEMAT.register(tardis -> tardis.flight().falling().get() ? TardisEvents.Interaction.FAIL : TardisEvents.Interaction.PASS);
    }

    public RealFlightHandler() {
        super(Id.FLIGHT);
    }

    @Override
    public void onLoaded() {
        falling.of(this, IS_FALLING);
        flying.of(this, FLYING);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (this.falling.get())
            this.tardis.door().setLocked(true);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void tick(MinecraftClient client) {
        if (flying.get()) {
            client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            client.options.hudHidden = true;
        }
    }

    public void tickFlight(ServerPlayerEntity player) {
        tardis.travel().forcePosition(cached -> cached.pos(player.getBlockPos()));
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

    public void enterFlight(ServerPlayerEntity player) {
        FlightTardisEntity entity = FlightTardisEntity.createAndSpawn(
                player, this.tardis.asServer());

        TardisUtil.teleportOutside(tardis, player);
        Scheduler.get().runTaskLater(() -> {
            player.startRiding(entity);
            player.setInvisible(true);

            this.sendEnterFlightPacket(player);
        }, TimeUnit.TICKS, 2);

        tardis.travel().finishDemat();
    }

    private void sendEnterFlightPacket(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, ENTER_FLIGHT, PacketByteBufs.create());
    }

    public void exitFlight(ServerPlayerEntity player) {
        player.setInvisible(false);
        this.sendExitFlightPacket(player);

        tardis.travel().forcePosition(cached -> cached.rotation((byte) RotationPropertyHelper.fromYaw(player.getYaw())));
        tardis.travel().placeExterior(false);
    }

    private void sendExitFlightPacket(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, EXIT_FLIGHT, PacketByteBufs.create());
    }

    public BoolValue falling() {
        return falling;
    }
}
