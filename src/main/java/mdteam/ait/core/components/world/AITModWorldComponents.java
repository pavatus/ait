package mdteam.ait.core.components.world;

import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import mdteam.ait.AITMod;
import mdteam.ait.core.components.world.tardis.TardisComponent;

import static mdteam.ait.AITMod.TARDISCLASSNBT;

public class AITModWorldComponents implements WorldComponentInitializer {
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        // registry.register(AITMod.TARDISNBT, it -> new TARDISListComponent());
        registry.register(TARDISCLASSNBT, TardisComponent::new);
    }
}
