package loqor.ait.core;

import java.util.ArrayList;
import java.util.List;

import io.wispforest.owo.itemgroup.OwoItemSettingsExtension;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.blocks.*;
import loqor.ait.core.blocks.DoorBlock;
import loqor.ait.core.blocks.control.RedstoneControlBlock;
import loqor.ait.core.engine.block.generic.GenericSubSystemBlock;
import loqor.ait.datagen.datagen_providers.util.AutomaticModel;
import loqor.ait.datagen.datagen_providers.util.NoBlockDrop;
import loqor.ait.datagen.datagen_providers.util.NoEnglish;
import loqor.ait.datagen.datagen_providers.util.PickaxeMineable;

public class AITBlocks {

    @NoBlockItem
    @NoBlockDrop
    @NoEnglish
    public static final Block EXTERIOR_BLOCK = registerBlock("pink_garnet_fence", new ExteriorBlock(
            AbstractBlock.Settings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing()
                    .pistonBehavior(PistonBehavior.IGNORE).luminance(ExteriorBlock.STATE_TO_LUMINANCE)));

    @PickaxeMineable
    @NoEnglish
    public static final Block DOOR_BLOCK = registerBlock("door_block", new DoorBlock(AbstractBlock.Settings.create().nonOpaque().noCollision()
            .instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(0.5F, 6.0F).pistonBehavior(PistonBehavior.IGNORE)));
    @NoBlockDrop
    @NoEnglish
    public static final Block CONSOLE = registerBlock("console", new ConsoleBlock(
            AbstractBlock.Settings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing()
                    .instrument(NoteBlockInstrument.COW_BELL).pistonBehavior(PistonBehavior.IGNORE)));

    @NoBlockDrop
    public static final Block WAYPOINT_BANK = registerBlock("waypoint_bank", new WaypointBankBlock(
            AbstractBlock.Settings.create().nonOpaque().requiresTool().instrument(NoteBlockInstrument.BASEDRUM).strength(0.5F, 6.0F)
                    .pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3)));

//    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
//    @NoEnglish
//    public static final Block LANDING_PAD = registerBlock("landing_padd", new LandingPadBlock(AbstractBlock.Settings.create().nonOpaque().requiresTool()
//            .instrument(NoteBlockInstrument.BASEDRUM).strength(0.5F, 6.0F).pistonBehavior(PistonBehavior.IGNORE)));

    @NoEnglish
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    public static final Block ENGINE_BLOCK = registerBlock("engine_block", new EngineBlock(AbstractBlock.Settings.create().requiresTool()
            .instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.IGNORE)));
    @NoEnglish
    public static final Block ENGINE_CORE_BLOCK = registerBlock("engine_core_block", new EngineCoreBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.DIAMOND_BLUE).solid().instrument(NoteBlockInstrument.HAT)
                    .strength(3.0F).luminance((state) -> 15).nonOpaque()));
    @PickaxeMineable
    public static final Block CONSOLE_GENERATOR = registerBlock("console_generator", new ConsoleGeneratorBlock(
            AbstractBlock.Settings.create().nonOpaque().noBlockBreakParticles().requiresTool().strength(1.5F)
                    .instrument(NoteBlockInstrument.COW_BELL).pistonBehavior(PistonBehavior.DESTROY)));
    @PickaxeMineable
    @NoEnglish
    public static final Block ARTRON_COLLECTOR_BLOCK = registerBlock("artron_collector_block", new ArtronCollectorBlock(
            AbstractBlock.Settings.create().nonOpaque().noBlockBreakParticles().requiresTool().strength(1.5F)
                    .instrument(NoteBlockInstrument.BANJO).pistonBehavior(PistonBehavior.IGNORE)));
    @NoEnglish
    public static final Block CORAL_PLANT = registerBlock("coral_plant", new CoralPlantBlock(AbstractBlock.Settings.create().ticksRandomly().nonOpaque()
            .noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.DESTROY)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    @NoEnglish
    public static final Block MONITOR_BLOCK = registerBlock("monitor_block", new MonitorBlock(AbstractBlock.Settings.create().nonOpaque().requiresTool()
            .instrument(NoteBlockInstrument.COW_BELL).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY)));
    @NoEnglish
    public static final Block PLAQUE_BLOCK = registerBlock("plaque_block", new PlaqueBlock(
            AbstractBlock.Settings.create().nonOpaque().noBlockBreakParticles().instrument(NoteBlockInstrument.COW_BELL)
                    .strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY)));
    @NoEnglish
    public static final Block WALL_MONITOR_BLOCK = registerBlock("wall_monitor_block", new WallMonitorBlock(
            AbstractBlock.Settings.create().nonOpaque().noBlockBreakParticles().instrument(NoteBlockInstrument.COW_BELL)
                    .strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY)));
    @NoEnglish
    public static final Block DETECTOR_BLOCK = registerBlock("detector_block", new DetectorBlock(AbstractBlock.Settings.create().nonOpaque()
            .instrument(NoteBlockInstrument.COW_BELL).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.NORMAL)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    @NoEnglish
    public static final Block ZEITON_BLOCK = registerBlock("zeiton_block", new AmethystBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_AQUA)
            .strength(1.5F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool()));
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    public static final Block BUDDING_ZEITON = registerBlock("budding_zeiton", new BuddingZeitonBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.DARK_AQUA).ticksRandomly().strength(1.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().pistonBehavior(PistonBehavior.DESTROY)));
    @NoBlockDrop
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    public static final Block ZEITON_CLUSTER = registerBlock("zeiton_cluster", new AmethystClusterBlock(7, 3,
            AbstractBlock.Settings.create().mapColor(MapColor.DARK_AQUA).solid().nonOpaque().ticksRandomly()
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(1.5F).luminance((state) -> 5)
                    .pistonBehavior(PistonBehavior.DESTROY)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    public static final Block LARGE_ZEITON_BUD = registerBlock("large_zeiton_bud", new AmethystClusterBlock(5, 3,
            AbstractBlock.Settings.copy(ZEITON_CLUSTER).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).solid()
                    .luminance((state) -> 4).pistonBehavior(PistonBehavior.DESTROY)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    public static final Block MEDIUM_ZEITON_BUD = registerBlock("medium_zeiton_bud", new AmethystClusterBlock(4, 3,
            AbstractBlock.Settings.copy(ZEITON_CLUSTER).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).solid()
                    .luminance((state) -> 2).pistonBehavior(PistonBehavior.DESTROY)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    public static final Block SMALL_ZEITON_BUD = registerBlock("small_zeiton_bud", new AmethystClusterBlock(3, 4,
            AbstractBlock.Settings.copy(ZEITON_CLUSTER).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).solid()
                    .luminance((state) -> 1).pistonBehavior(PistonBehavior.DESTROY)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    @AutomaticModel
    public static final Block COMPACT_ZEITON = registerBlock("compact_zeiton", new Block(AbstractBlock.Settings.copy(ZEITON_BLOCK)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    @AutomaticModel
    public static final Block ZEITON_COBBLE = registerBlock("zeiton_cobble", new Block(AbstractBlock.Settings.copy(ZEITON_BLOCK)));

    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    @NoEnglish
    public static final Block ZEITON_CAGE = registerBlock("zeiton_cage", new ZeitonCageBlock(AbstractBlock.Settings.create().nonOpaque().requiresTool()
            .instrument(NoteBlockInstrument.BASEDRUM).strength(0.5F, 6.0F).pistonBehavior(PistonBehavior.IGNORE)
            .luminance(light -> 15)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    @AutomaticModel(justItem = true)
    public static final Block POWER_CONVERTER = registerBlock("power_converter", new PowerConverterBlock(AbstractBlock.Settings.create().nonOpaque()
            .requiresTool().instrument(NoteBlockInstrument.COW_BELL).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY)));
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    @NoEnglish
    public static final Block GENERIC_SUBSYSTEM = registerBlock("generic_subsystem", new GenericSubSystemBlock(AbstractBlock.Settings.create().nonOpaque()
            .requiresTool().instrument(NoteBlockInstrument.COW_BELL).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY)));


    @NoBlockItem
    public static final Block RADIO = registerBlock("radio", new RadioBlock(AbstractBlock.Settings.create().nonOpaque()));

    // Machines
    @NoBlockItem
    public static final Block MACHINE_CASING = registerBlock("machine_casing", new MachineCasingBlock(AbstractBlock.Settings.create().nonOpaque()
            .requiresTool().instrument(NoteBlockInstrument.COW_BELL).strength(1.5F, 6.0F)));

    @NoBlockItem
    public static final Block FABRICATOR = registerBlock("fabricator", new FabricatorBlock(AbstractBlock.Settings.create().nonOpaque().requiresTool()
            .instrument(NoteBlockInstrument.COW_BELL).strength(1.5F, 6.0F)));

    // Control Blocks
    @NoBlockItem
    @NoEnglish
    public static final Block REDSTONE_CONTROL_BLOCK = registerBlock("redstone_control_block", new RedstoneControlBlock(
            AbstractBlock.Settings.create().nonOpaque().strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block ENVIRONMENT_PROJECTOR = registerBlock("environment_projector", new EnvironmentProjectorBlock(AbstractBlock.Settings.create()));

    @NoEnglish
    public static final Block CABLE_BLOCK = registerBlock("cable_block", new CableBlock(
            AbstractBlock.Settings.create().nonOpaque().instrument(NoteBlockInstrument.GUITAR).strength(1.5F, 6.0F)));

    public static List<Block> get() {
        List<Block> list = new ArrayList<>();

        for (Block block : Registries.BLOCK) {
            if (Registries.BLOCK.getId(block).getNamespace().equalsIgnoreCase(AITMod.MOD_ID)) {
                list.add(block);
            }
        }

        return list;
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(AITMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(AITMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, name))).useBlockPrefixedTranslationKey()));
    }

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return new BlockItem(block, new OwoItemSettingsExtension().group(AITMod.AIT_ITEM_GROUP));
    }
}
