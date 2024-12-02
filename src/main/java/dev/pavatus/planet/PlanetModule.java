package dev.pavatus.planet;

import java.util.Optional;
import java.util.function.Consumer;

import dev.pavatus.module.Module;
import dev.pavatus.module.ModuleRegistry;
import dev.pavatus.planet.client.SpaceSuitOverlay;
import dev.pavatus.planet.core.PlanetBlocks;
import dev.pavatus.planet.core.PlanetItems;
import dev.pavatus.planet.core.planet.PlanetRegistry;
import dev.pavatus.register.api.RegistryEvents;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.advancement.TardisCriterions;
import loqor.ait.datagen.datagen_providers.AITBlockTagProvider;
import loqor.ait.datagen.datagen_providers.AITItemTagProvider;
import loqor.ait.datagen.datagen_providers.AITLanguageProvider;
import loqor.ait.datagen.datagen_providers.AITRecipeProvider;


public class PlanetModule extends Module {
    public static final OwoItemGroup ITEM_GROUP = OwoItemGroup
            .builder(new Identifier(AITMod.MOD_ID, "planets_item_group"), () -> Icon.of(PlanetBlocks.MARTIAN_STONE))
            .disableDynamicTitle().build();
    private static final PlanetModule INSTANCE = new PlanetModule();

    public static final Identifier ID = AITMod.id("planet");

    @Override
    public void init() {
        RegistryEvents.SUBSCRIBE.register((registries, env) -> {
            env.init(PlanetRegistry.getInstance());
        });

        FieldRegistrationHandler.register(PlanetItems.class, AITMod.MOD_ID, false);
        FieldRegistrationHandler.register(PlanetBlocks.class, AITMod.MOD_ID, false);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void initClient() {
        HudRenderCallback.EVENT.register(new SpaceSuitOverlay());
    }

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public BlockItem createBlockItem(Block block, String id) {
        return new BlockItem(block, new OwoItemSettings().group(ITEM_GROUP));
    }

    @Override
    public Optional<Class<?>> getBlockRegistry() {
        return Optional.of(PlanetBlocks.class);
    }

    @Override
    public Optional<Class<?>> getItemRegistry() {
        return Optional.of(PlanetItems.class);
    }

    @Override
    public Optional<DataGenerator> getDataGenerator() {
        return Optional.of(new DataGenerator() {
            @Override
            public void lang(AITLanguageProvider provider) {
                provider.addTranslation("itemGroup.ait.planets_item_group", "AIT: Planetary Exploration");
                provider.addTranslation("message.ait.oxygen", "Stored Oxygen: %s");
                provider.addTranslation("achievements.ait.title.planet_root", "Planetary Exploration");
                provider.addTranslation("achievements.ait.description.planet_root", "Explore the planets of the universe");
                provider.addTranslation("achievements.ait.title.enter_mars", "You were not the first");
                provider.addTranslation("achievements.ait.description.enter_mars", "Landed on Mars for the first time");
                provider.addTranslation("achievements.ait.title.enter_moon", "One small step for Time Lords");
                provider.addTranslation("achievements.ait.description.enter_moon", "Landed on the Moon for the first time");
            }

            @Override
            public void recipes(AITRecipeProvider provider) {

                // Martian
                provider.addStonecutting(PlanetBlocks.MARTIAN_STONE, PlanetBlocks.MARTIAN_BRICKS);
                provider.addStonecutting(PlanetBlocks.MARTIAN_STONE, PlanetBlocks.MARTIAN_BRICK_WALL);
                provider.addStonecutting(PlanetBlocks.MARTIAN_STONE, PlanetBlocks.MARTIAN_BRICK_STAIRS);
                provider.addStonecutting(PlanetBlocks.MARTIAN_STONE, PlanetBlocks.MARTIAN_BRICK_SLAB);
                provider.addStonecutting(PlanetBlocks.MARTIAN_STONE, PlanetBlocks.CHISELED_MARTIAN_STONE);
                provider.addStonecutting(PlanetBlocks.MARTIAN_STONE, PlanetBlocks.MARTIAN_STONE_SLAB);
                provider.addStonecutting(PlanetBlocks.MARTIAN_STONE, PlanetBlocks.MARTIAN_STONE_STAIRS);

                provider.addStonecutting(PlanetBlocks.MARTIAN_COBBLESTONE, PlanetBlocks.MARTIAN_COBBLESTONE_SLAB);
                provider.addStonecutting(PlanetBlocks.MARTIAN_COBBLESTONE, PlanetBlocks.MARTIAN_COBBLESTONE_STAIRS);
                provider.addStonecutting(PlanetBlocks.MARTIAN_COBBLESTONE, PlanetBlocks.MARTIAN_COBBLESTONE_WALL);

                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE, PlanetBlocks.CHISELED_MARTIAN_SANDSTONE);
                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE, PlanetBlocks.MARTIAN_SANDSTONE_PILLAR);
                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE, PlanetBlocks.MARTIAN_SANDSTONE_BRICKS);
                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE, PlanetBlocks.MARTIAN_SANDSTONE_SLAB);
                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE, PlanetBlocks.MARTIAN_SANDSTONE_STAIRS);
                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE, PlanetBlocks.MARTIAN_SANDSTONE_WALL);

                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE_BRICKS, PlanetBlocks.CHISELED_MARTIAN_SANDSTONE);
                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE_BRICKS, PlanetBlocks.MARTIAN_SANDSTONE_BRICK_SLAB);
                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE_BRICKS, PlanetBlocks.MARTIAN_SANDSTONE_BRICK_STAIRS);
                provider.addStonecutting(PlanetBlocks.MARTIAN_SANDSTONE_BRICKS, PlanetBlocks.MARTIAN_SANDSTONE_BRICK_WALL);

                provider.addStonecutting(PlanetBlocks.MARTIAN_BRICKS, PlanetBlocks.MARTIAN_BRICK_WALL);
                provider.addStonecutting(PlanetBlocks.MARTIAN_BRICKS, PlanetBlocks.MARTIAN_BRICK_STAIRS);
                provider.addStonecutting(PlanetBlocks.MARTIAN_BRICKS, PlanetBlocks.MARTIAN_BRICK_SLAB);

                provider.addStonecutting(PlanetBlocks.SMOOTH_MARTIAN_STONE, PlanetBlocks.SMOOTH_MARTIAN_STONE_SLAB);

                provider.addStonecutting(PlanetBlocks.POLISHED_MARTIAN_STONE, PlanetBlocks.POLISHED_MARTIAN_STONE_SLAB);
                provider.addStonecutting(PlanetBlocks.POLISHED_MARTIAN_STONE, PlanetBlocks.POLISHED_MARTIAN_STONE_STAIRS);

                provider.addStonecutting(PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE, PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE_SLAB);
                provider.addStonecutting(PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE, PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE_STAIRS);

                // Anorthosite

                provider.addStonecutting(PlanetBlocks.ANORTHOSITE, PlanetBlocks.ANORTHOSITE_BRICKS);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE, PlanetBlocks.ANORTHOSITE_BRICK_WALL);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE, PlanetBlocks.ANORTHOSITE_BRICK_STAIRS);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE, PlanetBlocks.ANORTHOSITE_BRICK_SLAB);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE, PlanetBlocks.CHISELED_ANORTHOSITE);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE, PlanetBlocks.ANORTHOSITE_SLAB);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE, PlanetBlocks.ANORTHOSITE_STAIRS);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE, PlanetBlocks.ANORTHOSITE_WALL);


                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE, PlanetBlocks.CHISELED_MOON_SANDSTONE);
                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE, PlanetBlocks.MOON_SANDSTONE_PILLAR);
                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE, PlanetBlocks.MOON_SANDSTONE_BRICKS);
                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE, PlanetBlocks.MOON_SANDSTONE_SLAB);
                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE, PlanetBlocks.MOON_SANDSTONE_STAIRS);
                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE, PlanetBlocks.MOON_SANDSTONE_WALL);

                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE_BRICKS, PlanetBlocks.CHISELED_MOON_SANDSTONE);
                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE_BRICKS, PlanetBlocks.MOON_SANDSTONE_BRICK_SLAB);
                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE_BRICKS, PlanetBlocks.MOON_SANDSTONE_BRICK_STAIRS);
                provider.addStonecutting(PlanetBlocks.MOON_SANDSTONE_BRICKS, PlanetBlocks.MOON_SANDSTONE_BRICK_WALL);

                provider.addStonecutting(PlanetBlocks.ANORTHOSITE_BRICKS, PlanetBlocks.MARTIAN_BRICK_WALL);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE_BRICKS, PlanetBlocks.MARTIAN_BRICK_STAIRS);
                provider.addStonecutting(PlanetBlocks.ANORTHOSITE_BRICKS, PlanetBlocks.MARTIAN_BRICK_SLAB);

                provider.addStonecutting(PlanetBlocks.SMOOTH_ANORTHOSITE, PlanetBlocks.SMOOTH_ANORTHOSITE_SLAB);

                provider.addStonecutting(PlanetBlocks.POLISHED_ANORTHOSITE, PlanetBlocks.POLISHED_ANORTHOSITE_SLAB);
                provider.addStonecutting(PlanetBlocks.POLISHED_ANORTHOSITE, PlanetBlocks.POLISHED_ANORTHOSITE_STAIRS);

            }


            @Override
            public void blockTags(AITBlockTagProvider provider) {

                // Martian Blocks
                provider.getOrCreateTagBuilder(BlockTags.WALLS)
                        .add(PlanetBlocks.MARTIAN_BRICK_WALL).add(PlanetBlocks.MARTIAN_COBBLESTONE_WALL).add(PlanetBlocks.MARTIAN_SANDSTONE_WALL).add(PlanetBlocks.MARTIAN_STONE_WALL).add(PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE_WALL).add(PlanetBlocks.MARTIAN_BRICK_WALL).add(PlanetBlocks.MARTIAN_SANDSTONE_BRICK_WALL);

                // Anorthosite Blocks
                provider.getOrCreateTagBuilder(BlockTags.WALLS)
                        .add(PlanetBlocks.ANORTHOSITE_BRICK_WALL).add(PlanetBlocks.ANORTHOSITE_WALL).add(PlanetBlocks.MOON_SANDSTONE_BRICK_WALL).add(PlanetBlocks.MOON_SANDSTONE_WALL);

            }

            @Override
            public void itemTags(AITItemTagProvider provider) {

            }


            @Override
            public void generateItemModels(ItemModelGenerator generator) {
                generator.registerArmor((ArmorItem) PlanetItems.SPACESUIT_BOOTS);
                generator.registerArmor((ArmorItem) PlanetItems.SPACESUIT_CHESTPLATE);
                generator.registerArmor((ArmorItem) PlanetItems.SPACESUIT_LEGGINGS);
                generator.registerArmor((ArmorItem) PlanetItems.SPACESUIT_HELMET);

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

            }

            @Override
            public void models(BlockStateModelGenerator generator) {
                //Martian (Slabs, Walls, etc.)
                BlockStateModelGenerator.BlockTexturePool martian_stone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.MARTIAN_STONE);
                martian_stone_pool.stairs(PlanetBlocks.MARTIAN_STONE_STAIRS);
                martian_stone_pool.wall(PlanetBlocks.MARTIAN_STONE_WALL);
                martian_stone_pool.slab(PlanetBlocks.MARTIAN_STONE_SLAB);
                martian_stone_pool.button(PlanetBlocks.MARTIAN_STONE_BUTTON);
                martian_stone_pool.pressurePlate(PlanetBlocks.MARTIAN_STONE_PRESSURE_PLATE);

                BlockStateModelGenerator.BlockTexturePool martian_bricks_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.MARTIAN_BRICKS);
                martian_bricks_pool.stairs(PlanetBlocks.MARTIAN_BRICK_STAIRS);
                martian_bricks_pool.wall(PlanetBlocks.MARTIAN_BRICK_WALL);
                martian_bricks_pool.slab(PlanetBlocks.MARTIAN_BRICK_SLAB);

                BlockStateModelGenerator.BlockTexturePool martian_cobblestone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.MARTIAN_COBBLESTONE);
                martian_cobblestone_pool.stairs(PlanetBlocks.MARTIAN_COBBLESTONE_STAIRS);
                martian_cobblestone_pool.wall(PlanetBlocks.MARTIAN_COBBLESTONE_WALL);
                martian_cobblestone_pool.slab(PlanetBlocks.MARTIAN_COBBLESTONE_SLAB);


                BlockStateModelGenerator.BlockTexturePool mossy_martian_cobblestone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE);
                mossy_martian_cobblestone_pool.stairs(PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE_STAIRS);
                mossy_martian_cobblestone_pool.wall(PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE_WALL);
                mossy_martian_cobblestone_pool.slab(PlanetBlocks.MOSSY_MARTIAN_COBBLESTONE_SLAB);

                BlockStateModelGenerator.BlockTexturePool martian_sandstone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.MARTIAN_SANDSTONE);
                martian_sandstone_pool.stairs(PlanetBlocks.MARTIAN_SANDSTONE_STAIRS);
                martian_sandstone_pool.wall(PlanetBlocks.MARTIAN_SANDSTONE_WALL);
                martian_sandstone_pool.slab(PlanetBlocks.MARTIAN_SANDSTONE_SLAB);

                BlockStateModelGenerator.BlockTexturePool martian_sandstone_bricks_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.MARTIAN_SANDSTONE_BRICKS);
                martian_sandstone_bricks_pool.stairs(PlanetBlocks.MARTIAN_SANDSTONE_BRICK_STAIRS);
                martian_sandstone_bricks_pool.wall(PlanetBlocks.MARTIAN_SANDSTONE_BRICK_WALL);
                martian_sandstone_bricks_pool.slab(PlanetBlocks.MARTIAN_SANDSTONE_BRICK_SLAB);

                BlockStateModelGenerator.BlockTexturePool smooth_martian_stone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.SMOOTH_MARTIAN_STONE);
                smooth_martian_stone_pool.slab(PlanetBlocks.SMOOTH_MARTIAN_STONE_SLAB);

                BlockStateModelGenerator.BlockTexturePool polished_martian_stone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.POLISHED_MARTIAN_STONE);
                polished_martian_stone_pool.stairs(PlanetBlocks.POLISHED_MARTIAN_STONE_STAIRS);
                polished_martian_stone_pool.slab(PlanetBlocks.POLISHED_MARTIAN_STONE_SLAB);


                //Anorthosite (Slabs, Walls, etc.)

                BlockStateModelGenerator.BlockTexturePool anorthosite_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.ANORTHOSITE);
                anorthosite_pool.stairs(PlanetBlocks.ANORTHOSITE_STAIRS);
                anorthosite_pool.wall(PlanetBlocks.ANORTHOSITE_WALL);
                anorthosite_pool.slab(PlanetBlocks.ANORTHOSITE_SLAB);

                BlockStateModelGenerator.BlockTexturePool anorthosite_bricks_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.ANORTHOSITE_BRICKS);
                anorthosite_bricks_pool.stairs(PlanetBlocks.ANORTHOSITE_BRICK_STAIRS);
                anorthosite_bricks_pool.wall(PlanetBlocks.ANORTHOSITE_BRICK_WALL);
                anorthosite_bricks_pool.slab(PlanetBlocks.ANORTHOSITE_BRICK_SLAB);

                BlockStateModelGenerator.BlockTexturePool smooth_anorthosite_stone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.SMOOTH_ANORTHOSITE);
                smooth_anorthosite_stone_pool.slab(PlanetBlocks.SMOOTH_ANORTHOSITE_SLAB);

                BlockStateModelGenerator.BlockTexturePool polished_anorthosite_stone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.POLISHED_ANORTHOSITE);
                polished_anorthosite_stone_pool.stairs(PlanetBlocks.POLISHED_ANORTHOSITE_STAIRS);
                polished_anorthosite_stone_pool.slab(PlanetBlocks.POLISHED_ANORTHOSITE_SLAB);

                BlockStateModelGenerator.BlockTexturePool moon_sandstone_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.MOON_SANDSTONE);
                moon_sandstone_pool.stairs(PlanetBlocks.MOON_SANDSTONE_STAIRS);
                moon_sandstone_pool.wall(PlanetBlocks.MOON_SANDSTONE_WALL);
                moon_sandstone_pool.slab(PlanetBlocks.MOON_SANDSTONE_SLAB);

                BlockStateModelGenerator.BlockTexturePool moon_sandstone_bricks_pool = generator.registerCubeAllModelTexturePool(PlanetBlocks.MOON_SANDSTONE_BRICKS);
                moon_sandstone_bricks_pool.stairs(PlanetBlocks.MOON_SANDSTONE_BRICK_STAIRS);
                moon_sandstone_bricks_pool.wall(PlanetBlocks.MOON_SANDSTONE_BRICK_WALL);
                moon_sandstone_bricks_pool.slab(PlanetBlocks.MOON_SANDSTONE_BRICK_SLAB);
            }

            @Override
            public void advancements(Consumer<Advancement> consumer) {
                Advancement root = Advancement.Builder.create()
                        .display(
                                PlanetItems.SPACESUIT_HELMET,
                                Text.translatable("achievements.ait.title.planet_root"),
                                Text.translatable("achievements.ait.description.planet_root"),
                                new Identifier(AITMod.MOD_ID, "textures/block/martian_stone.png"),
                                AdvancementFrame.TASK,
                                true,
                                true,
                                true
                        )
                        .criterion("enter_tardis", TardisCriterions.ENTER_TARDIS.conditions())
                        .build(consumer, AITMod.MOD_ID + "/planet_root");
                Advancement landOnMars = Advancement.Builder.create()
                        .parent(root)
                        .display(
                                PlanetBlocks.MARTIAN_STONE,
                                Text.translatable("achievements.ait.title.enter_mars"),
                                Text.translatable("achievements.ait.description.enter_marse"),
                                null,
                                AdvancementFrame.TASK,
                                true,
                                true,
                                true
                        )
                        .criterion(
                                "enter_mars",
                                ChangedDimensionCriterion.Conditions.to(
                                        RegistryKey.of(
                                                RegistryKeys.WORLD,
                                                new Identifier(AITMod.MOD_ID, "mars")
                                        )
                                )
                        )
                        .build(consumer, AITMod.MOD_ID + "/enter_mars");
                Advancement landOnMoon = Advancement.Builder.create()
                        .parent(root)
                        .display(
                                PlanetBlocks.ANORTHOSITE,
                                Text.translatable("achievements.ait.title.enter_moon"),
                                Text.translatable("achievements.ait.description.enter_moon"),
                                null,
                                AdvancementFrame.TASK,
                                true,
                                true,
                                true
                        )
                        .criterion(
                                "enter_moon",
                                ChangedDimensionCriterion.Conditions.to(
                                        RegistryKey.of(
                                                RegistryKeys.WORLD,
                                                new Identifier(AITMod.MOD_ID, "moon")
                                        )
                                )
                        )
                        .build(consumer, AITMod.MOD_ID + "/enter_moon");
            }
        });
    }

    public static PlanetModule instance() {
        return INSTANCE;
    }
    public static boolean isLoaded() {
        return ModuleRegistry.instance().get(ID) != null;
    }
}
