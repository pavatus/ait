package mdteam.ait.datagen.datagen_providers;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class AITModelProvider extends FabricModelProvider {
    private final List<Block> directionalBlocksToRegister = new ArrayList<>();
    private final List<Block> simpleBlocksToRegister = new ArrayList<>();
    public AITModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (Block block : directionalBlocksToRegister) {
//            Identifier identifier = new Identifier(block.getTranslationKey().split("\\.")[1]);
            blockStateModelGenerator.blockStateCollector.accept(
                    MultipartBlockStateSupplier.create(block)
                            .with(When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                                    BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R0))
            );
        }
        for (Block block : simpleBlocksToRegister) {
            blockStateModelGenerator.registerSimpleCubeAll(block);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }

    public void registerDirectionalBlock(Block block) {
        directionalBlocksToRegister.add(block);
    }

    public void registerSimpleBlock(Block block) {
        simpleBlocksToRegister.add(block);
    }
}
