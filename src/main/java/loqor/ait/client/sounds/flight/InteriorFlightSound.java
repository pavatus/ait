package loqor.ait.client.sounds.flight;

import java.util.Random;

import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

import loqor.ait.client.sounds.ClientSoundManager;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.sounds.flight.FlightSound;

public class InteriorFlightSound extends PositionedLoopingSound {
    private static final Random rnd = new Random();
    private FlightSound data;
    private int ticks = 0;
    private boolean dirty = true;

    public InteriorFlightSound(FlightSound data, SoundCategory soundCategory) {
        super(data.sound(), soundCategory, new BlockPos(0,0,0));
        this.data = data;
    }

    @Override
    public void tick() {
        super.tick();
        this.ticks++;

        if (this.ticks >= (this.getData().length() / this.pitch)) {
            this.pitch = getRandomPitch();
            this.setPosition(ClientTardisUtil.getNearestConsole());
            this.ticks = 0;
            this.dirty = true;
        }
    }
    public FlightSound getData() {
        if (this.data == null && ClientTardisUtil.getCurrentTardis() != null)
            this.data = ClientTardisUtil.getCurrentTardis().getExterior().getVariant().flight();

        return this.data;
    }
    public boolean isDirty() {
        return this.dirty;
    }
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    private static float getRandomPitch() {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (tardis == null)
            return 1f;

        int speed = tardis.travel().speed();

        if (ClientSoundManager.getFlight().hasThrottleAndHandbrakeDown(tardis)) {
            // todo i hate switch
            return switch (speed) {
                case 1 -> 0.5f;
                case 2 -> 0.55f;
                case 3 -> 0.6f;
                default -> 1.0f;
            };
        }

        return switch (speed) {
            case 1 -> rnd.nextFloat(0.9f, 0.95f);
            case 2 -> rnd.nextFloat(0.95f, 1.0f);
            case 3 -> rnd.nextFloat(1.0f, 1.25f);
            default -> 1.0f;
        };
    }
}
