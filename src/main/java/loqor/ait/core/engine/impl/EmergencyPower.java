package loqor.ait.core.engine.impl;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

import loqor.ait.AITMod;
import loqor.ait.api.ArtronHolder;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;
import loqor.ait.core.item.RiftScannerItem;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.data.DirectedGlobalPos;

public class EmergencyPower extends SubSystem implements ArtronHolder, StructureHolder {
    private static final MultiBlockStructure STRUCTURE = MultiBlockStructure.from(new Identifier(AITMod.MOD_ID, "emergency_power"));
    private double fuel;

    static {
        TardisEvents.USE_BACKUP_POWER.register((tdis, power) -> {
            tdis.alarm().enabled().set(true);

            // if power is below 200, find the nearest rift and head there
            if (power > 200) return;
            TravelHandler travel = tdis.travel();
            RiftScannerItem.findNearestRift(travel.position().getWorld(), new ChunkPos(travel.position().getPos()), pos -> {
                travel.destination(DirectedGlobalPos.Cached.create(travel.position().getWorld(), pos.getCenterAtY(70), (byte) 0));
                travel.autopilot(true);
                travel.dematerialize();
            });
        });
    }

    public EmergencyPower() {
        super(Id.EMERGENCY_POWER);
    }

    @Override
    public double getCurrentFuel() {
        return fuel;
    }

    @Override
    public void setCurrentFuel(double var) {
        fuel = MathHelper.clamp(var, 0, getMaxFuel());
        this.sync();
    }

    @Override
    public double getMaxFuel() {
        return 1000;
    }
    public boolean hasBackupPower() {
        return this.getCurrentFuel() > 0 && this.isEnabled();
    }

    @Override
    public MultiBlockStructure getStructure() {
        return STRUCTURE;
    }
}
