package mdteam.ait.client.sounds.flight;

import mdteam.ait.client.sounds.LoopingSound;
import mdteam.ait.client.sounds.PositionedLoopingSound;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.SoundHandler;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

// All this is CLIENT ONLY!!
// Loqor, if you dont understand DONT TOUCH or ask me! - doozoo
// todo create a ServerFlightHandler if necessary eg in future when we do more of the stuff on the trello to do with flight sounds.
// todo this is not positioned at the console anymore, as checking to see if an individual sound is playing at each console doesnt appear to be possible (?)
public class ClientFlightHandler extends SoundHandler {
    public static ConcurrentLinkedQueue<LoopingSound> FLIGHTS;
    protected ClientFlightHandler() {}

    public ConcurrentLinkedQueue<LoopingSound> getFlightLoops() {
        if (FLIGHTS == null) FLIGHTS = new ConcurrentLinkedQueue<>();
        if (FLIGHTS.isEmpty()) this.generate();

        return FLIGHTS;
    }
    private LoopingSound createFlightSound(BlockPos pos) {
        return new FlightSound(AITSounds.FLIGHT_LOOP, SoundCategory.BLOCKS, pos, 2.5f);
    }
    public static ClientFlightHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientFlightHandler handler = new ClientFlightHandler();
        handler.generate();
        return handler;
    }

    private void generate() {
        if (tardis() == null) return;

        if (FLIGHTS == null) FLIGHTS = new ConcurrentLinkedQueue<>();

        if (FLIGHTS.isEmpty()) {
            tardis().getDesktop().getConsoles().forEach(console -> {
                FLIGHTS.add(createFlightSound(console.position()));
            });
        }

        this.sounds = new ArrayList<>();
        this.sounds.addAll(
                FLIGHTS
        );
    }

    public boolean isPlayerInATardis() {
        if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);

        return found != null;
    }

    public Tardis tardis() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return null;
        return TardisUtil.findTardisByInterior(player.getBlockPos(), false);
    }

    private void playFlightSound() {
        this.getFlightLoops().forEach(this::startIfNotPlaying);
    }

    private boolean shouldPlaySounds() {
        return (ClientTardisUtil.distanceFromConsole() < 5) && (inFlight() || hasThrottleAndHandbrakeDown()) && tardis().hasPower();
    }

    private boolean inFlight() {
        return (isPlayerInATardis() && tardis() != null && tardis().getTravel().getState() == TardisTravel.State.FLIGHT);
    }
    public boolean hasThrottleAndHandbrakeDown() {
        return (isPlayerInATardis() && tardis() != null && tardis().getTravel().getSpeed() > 0 && PropertiesHandler.getBool(tardis().getHandlers().getProperties(), PropertiesHandler.HANDBRAKE));
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null) this.generate();

        if (shouldPlaySounds()) {
            this.playFlightSound();
        }
        else {
            this.stopSounds();
        }
    }
}
