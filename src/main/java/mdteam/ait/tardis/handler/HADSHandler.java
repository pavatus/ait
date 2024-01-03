package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class HADSHandler extends TardisLink {

    public HADSHandler(UUID tardisID) {
        super(tardisID);
    }

    public boolean isHADSActive() {
        return PropertiesHandler.getBool(getTardis().getHandlers().getProperties(), PropertiesHandler.HADS_ENABLED);
    }

    public void setIsInDanger(boolean bool) {
        PropertiesHandler.setBool(getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_ACTIVE_DANGER, bool);
    }

    public boolean isInDanger() {
        return PropertiesHandler.getBool(getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_ACTIVE_DANGER);
    }

    @Override
    public void tick(MinecraftServer server) {
        tickingForDanger(getExteriorPos().getWorld());
    }


    // @TODO Fix hads idk why its broken. duzo did something to the demat idk what happened lol
    public void tickingForDanger(World world) {
        /*System.out.println("hello");*/
        if (getExteriorPos() == null) return;
        List<Entity> listOfEntities = world.getOtherEntities(null,
                new Box(getExteriorPos()).expand(3f),
                EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
        /*if(isHADSActive()) {*/
            for (Entity entity : listOfEntities) {
                if (entity instanceof CreeperEntity creeperEntity) {
                    if (creeperEntity.getFuseSpeed() > 0) {
                        setIsInDanger(true);
                        break;
                    }
                } else if (entity instanceof TntEntity) {
                    setIsInDanger(true);
                    break;
                }
                setIsInDanger(false);
            }
            dematerialiseWhenInDanger();
        /*} else {
            for (Entity entity : listOfEntities) {
                if (entity instanceof CreeperEntity creeperEntity) {
                    if (creeperEntity.getFuseSpeed() > 0) {
                        tardis().removeFuel(Explosion.getExposure(getExteriorPos().toCenterPos(), creeperEntity));
                        break;
                    }
                } else if (entity instanceof TntEntity tnt) {
                    tardis().removeFuel(Explosion.getExposure(getExteriorPos().toCenterPos(), tnt));
                    break;
                }
            }
        }*/
    }

    public void dematerialiseWhenInDanger() {
        // fixme is bug pls fix - idea enqueue a remat ( NEEDS_MAT var ? )
        if(isInDanger()) {
            if(getTardis().getTravel().getState() == TardisTravel.State.LANDED) {
                getTardis().getTravel().dematerialise(false);
            }
            getTardis().getHandlers().getAlarms().enable();
        } else if (getTardis().getHandlers().getAlarms().isEnabled()){
            if(getTardis().getTravel().getState() == TardisTravel.State.FLIGHT) {
                getTardis().getTravel().materialise();
            } else if (getTardis().getTravel().getState() == TardisTravel.State.MAT) getTardis().getHandlers().getAlarms().disable();
        }
    }

}
