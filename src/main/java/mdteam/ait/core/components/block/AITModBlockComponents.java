package mdteam.ait.core.components.block;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import mdteam.ait.core.blockentities.AITRadioBlockEntity;
import mdteam.ait.core.components.block.radio.RadioNBTComponent;

import static mdteam.ait.AITMod.*;

public class AITModBlockComponents implements BlockComponentInitializer {

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(AITRadioBlockEntity.class, RADIONBT, RadioNBTComponent::new);
    }
}
