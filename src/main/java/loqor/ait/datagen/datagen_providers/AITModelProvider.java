package loqor.ait.datagen.datagen_providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.pavatus.lib.datagen.model.SakitusModelProvider;
import dev.pavatus.module.ModuleRegistry;
import dev.pavatus.planet.core.PlanetItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import loqor.ait.AITMod;


public class AITModelProvider extends SakitusModelProvider {
    private final List<Block> directionalBlocksToRegister = new ArrayList<>();
    private final List<Block> simpleBlocksToRegister = new ArrayList<>();

    public AITModelProvider(FabricDataOutput output) {
        super(output);
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

    private static String getItemName(Item item) {
        return item.getTranslationKey().split("\\.")[2];
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
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

        ModuleRegistry.instance().iterator().forEachRemaining(module -> {
            module.getDataGenerator().ifPresent(data -> data.models(this, generator));
            module.getBlockRegistry().ifPresent(this::withBlocks);
        });

        super.generateBlockStateModels(generator);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(PlanetItems.MARTIAN_STONE_SWORD, Models.HANDHELD);
        generator.register(PlanetItems.MARTIAN_STONE_SHOVEL, Models.HANDHELD);
        generator.register(PlanetItems.MARTIAN_STONE_PICKAXE, Models.HANDHELD);
        generator.register(PlanetItems.MARTIAN_STONE_HOE, Models.HANDHELD);
        generator.register(PlanetItems.MARTIAN_STONE_AXE, Models.HANDHELD);

        generator.register(PlanetItems.ANORTHOSITE_SWORD, Models.HANDHELD);
        generator.register(PlanetItems.ANORTHOSITE_SHOVEL, Models.HANDHELD);
        generator.register(PlanetItems.ANORTHOSITE_PICKAXE, Models.HANDHELD);
        generator.register(PlanetItems.ANORTHOSITE_HOE, Models.HANDHELD);
        generator.register(PlanetItems.ANORTHOSITE_AXE, Models.HANDHELD);

        ModuleRegistry.instance().iterator().forEachRemaining(module -> {
            module.getItemRegistry().ifPresent(this::withItems);
            module.getBlockRegistry().ifPresent(this::withBlocks);
        });

        super.generateItemModels(generator);
    }

    public void registerDirectionalBlock(Block block) {
        directionalBlocksToRegister.add(block);
    }

    public void registerSimpleBlock(Block block) {
        simpleBlocksToRegister.add(block);
    }

    private void registerItem(ItemModelGenerator generator, Item item, String modid) {
        Model model = item(TextureKey.LAYER0);
        model.upload(ModelIds.getItemModelId(item), createTextureMap(item, modid), generator.writer);
    }

    private TextureMap createTextureMap(Item item, String modid) {
        Identifier texture = new Identifier(modid, "item/" + getItemName(item));
        if (!(doesTextureExist(texture))) {
            texture = AITMod.id("item/error");
        }

        return new TextureMap().put(TextureKey.LAYER0, texture);
    }

    public boolean doesTextureExist(Identifier texture) {
        return this.output.getModContainer().findPath("assets/" + texture.getNamespace() + "/textures/" + texture.getPath() + ".png").isPresent();
    }
}
