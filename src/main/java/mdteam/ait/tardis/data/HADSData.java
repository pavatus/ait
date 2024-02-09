package mdteam.ait.tardis.data;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class HADSData extends TardisLink {

    public HADSData(Tardis tardis) {
        super(tardis, "hads");
    }

    public boolean isHADSActive() {
        if(findTardis().isEmpty()) return false;
        return PropertiesHandler.getBool(findTardis().get().getHandlers().getProperties(), PropertiesHandler.HADS_ENABLED);
    }

    public void setIsInDanger(boolean bool) {
        if(findTardis().isEmpty()) return;
        PropertiesHandler.set(findTardis().get(), PropertiesHandler.IS_IN_ACTIVE_DANGER, bool);
    }

    public boolean isInDanger() {
        if(findTardis().isEmpty()) return false;
        return PropertiesHandler.getBool(findTardis().get().getHandlers().getProperties(), PropertiesHandler.IS_IN_ACTIVE_DANGER);
    }

    @Override
    public void tick(MinecraftServer server) {
        if(isHADSActive())
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
        if(findTardis().isEmpty()) return;
        if(isInDanger()) {
            if(findTardis().get().getTravel().getState() == TardisTravel.State.LANDED) {
                findTardis().get().getTravel().dematerialise(false);
            }
            findTardis().get().getHandlers().getAlarms().enable();
        } else if (findTardis().get().getHandlers().getAlarms().isEnabled()){
            if(findTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT) {
                findTardis().get().getTravel().materialise();
            } else if (findTardis().get().getTravel().getState() == TardisTravel.State.MAT) findTardis().get().getHandlers().getAlarms().disable();
        }
    }

}
