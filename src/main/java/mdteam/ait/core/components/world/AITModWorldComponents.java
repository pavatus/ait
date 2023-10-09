package mdteam.ait.core.components.world;

import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import mdteam.ait.core.components.world.tardis.TARDISComponent;

import static mdteam.ait.AITMod.TARDISNBT;

public class AITModWorldComponents implements WorldComponentInitializer {
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(TARDISNBT, TARDISComponent::new);
    }
}
