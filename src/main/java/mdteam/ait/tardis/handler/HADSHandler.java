package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.UUID;

public class HADSHandler extends TardisLink {

    public HADSHandler(UUID tardisID) {
        super(tardisID);
    }

    public void setIsInDanger(boolean bool) {
        PropertiesHandler.setBool(tardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_ACTIVE_DANGER, bool);
    }

    public boolean isInDanger() {
        return PropertiesHandler.getBool(tardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_ACTIVE_DANGER);
    }

    @Override
    public void tick(MinecraftServer server) {
        tickingForDanger((ServerWorld) getExteriorPos().getWorld());
    }

    public void tickingForDanger(ServerWorld world) {
        /*System.out.println("hello");*/
        if (getExteriorPos() == null) return;
        List<Entity> listOfEntities = world.getOtherEntities(null,
                new Box(getExteriorPos()).expand(3f),
                EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);
        for(Entity entity : listOfEntities) {
            if(entity instanceof CreeperEntity creeperEntity) {
                if(creeperEntity.getFuseSpeed() > 0) {
                    setIsInDanger(true);
                    break;
                }
            } else if(entity instanceof TntEntity tnt) {
                setIsInDanger(true);
                break;
            }
            setIsInDanger(false);
        }
        dematerialiseWhenInDanger();
    }

    public void dematerialiseWhenInDanger() {
        // fixme is bug pls fix - idea enqueue a remat ( NEEDS_MAT var ? )
        if(isInDanger()) {
            if(tardis().getTravel().getState() == TardisTravel.State.LANDED) {
                tardis().getTravel().dematerialise(false);
            }
            tardis().getHandlers().getAlarms().enable();
        } else if (tardis().getHandlers().getAlarms().isEnabled()){
            if(tardis().getTravel().getState() == TardisTravel.State.FLIGHT) {
                tardis().getTravel().materialise();
            } else if (tardis().getTravel().getState() == TardisTravel.State.MAT) tardis().getHandlers().getAlarms().disable();
        }
    }

}
