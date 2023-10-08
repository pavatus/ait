package mdteam.ait.core.components.item;

import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import mdteam.ait.AITMod;

public class AITModItemComponents implements ItemComponentInitializer {
    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        //registry.register(item -> item instanceof PipboyItem, AITMod.PIPBOYNBT, PipNBTComponent::new);
    }
}
