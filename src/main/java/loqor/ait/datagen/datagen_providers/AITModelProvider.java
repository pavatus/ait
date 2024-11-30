package loqor.ait.datagen.datagen_providers;

import static loqor.ait.datagen.datagen_providers.loot.AITBlockLootTables.filterBlocksWithAnnotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.pavatus.module.ModuleRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlocks;
import loqor.ait.datagen.datagen_providers.loot.AITBlockLootTables;
import loqor.ait.datagen.datagen_providers.util.AutomaticModel;


public class AITModelProvider extends FabricModelProvider {
    private final FabricDataOutput output;
    private final List<Block> directionalBlocksToRegister = new ArrayList<>();
    private final List<Block> simpleBlocksToRegister = new ArrayList<>();

    public AITModelProvider(FabricDataOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        filterBlocksWithAnnotation(AITBlocks.get(), AutomaticModel.class, false).forEach(generator::registerSimpleCubeAll);

        for (Block block : directionalBlocksToRegister) {
            // Identifier identifier = new
            // Identifier(block.getTranslationKey().split("\\.")[1]);
            generator.blockStateCollector.accept(MultipartBlockStateSupplier.create(block).with(
                    When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                    BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R0)));
        }
        for (Block block : simpleBlocksToRegister) {
            generator.registerSimpleCubeAll(block);
        }

        ModuleRegistry.instance().iterator().forEachRemaining(module -> module.getDataGenerator().ifPresent(data -> data.models(generator)));
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        for (Map.Entry<Block, Annotation> entry : AITBlockLootTables.getAnnotatedBlocks(AutomaticModel.class)) {
            AutomaticModel annotation = (AutomaticModel) entry.getValue();
            if (annotation.justItem()) {
                registerItem(generator, entry.getKey().asItem(), AITMod.MOD_ID);
            }
        }
        for (Map.Entry<Item, Annotation> entry : AITBlockLootTables.getAnnotatedItems(AutomaticModel.class)) {
            registerItem(generator, entry.getKey(), AITMod.MOD_ID);
        }
    }

    public void registerDirectionalBlock(Block block) {
        directionalBlocksToRegister.add(block);
    }

    public void registerSimpleBlock(Block block) {
        simpleBlocksToRegister.add(block);
    }

    private static Model item(String modid, String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(new Identifier(modid, "item/" + parent)), Optional.empty(), requiredTextureKeys);
    }
    private static Model item(String parent, TextureKey... requiredTextureKeys) {
        return item(AITMod.MOD_ID, parent, requiredTextureKeys);
    }
    private static Model item(TextureKey... requiredTextureKeys) {
        return item("minecraft", "generated", requiredTextureKeys);
    }
    private static Model item(String name) {
        return item(name, TextureKey.LAYER0);
    }
    private void registerItem(ItemModelGenerator generator, Item item, String modid) {
        Model model = item(TextureKey.LAYER0);
        model.upload(ModelIds.getItemModelId(item), createTextureMap(item, modid), generator.writer);
    }
    private TextureMap createTextureMap(Item item, String modid) {
        Identifier texture = new Identifier(modid, "item/" + getItemName(item));
        if (!(doesTextureExist(texture))) {
            texture = new Identifier(AITMod.MOD_ID, "item/error");
        }

        return new TextureMap().put(TextureKey.LAYER0, texture);
    }
    private static String getItemName(Item item) {
        return item.getTranslationKey().split("\\.")[2];
    }

    public boolean doesTextureExist(Identifier texture) {
        return this.output.getModContainer().findPath("assets/" + texture.getNamespace() + "/textures/" + texture.getPath() + ".png").isPresent();
    }
}
