package loqor.ait.core;

import java.util.ArrayList;
import java.util.List;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;

import loqor.ait.AITMod;
import loqor.ait.core.blocks.*;
import loqor.ait.core.blocks.DoorBlock;
import loqor.ait.core.blocks.control.RedstoneControlBlock;
import loqor.ait.datagen.datagen_providers.util.NoBlockDrop;
import loqor.ait.datagen.datagen_providers.util.NoEnglish;
import loqor.ait.datagen.datagen_providers.util.PickaxeMineable;

public class AITBlocks implements BlockRegistryContainer {

    @NoBlockItem
    @NoBlockDrop
    @NoEnglish
    public static final Block EXTERIOR_BLOCK = new ExteriorBlock(
            FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing()
                    .pistonBehavior(PistonBehavior.IGNORE).luminance(ExteriorBlock.STATE_TO_LUMINANCE));

    @PickaxeMineable
    @NoEnglish
    public static final Block DOOR_BLOCK = new DoorBlock(FabricBlockSettings.create().nonOpaque().noCollision()
            .instrument(Instrument.BASEDRUM).requiresTool().strength(0.5F, 6.0F).pistonBehavior(PistonBehavior.IGNORE));
    @NoBlockDrop
    @NoEnglish
    public static final Block CONSOLE = new ConsoleBlock(
            FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing()
                    .instrument(Instrument.COW_BELL).pistonBehavior(PistonBehavior.IGNORE));

    @NoBlockDrop
    public static final Block WAYPOINT_BANK = new WaypointBankBlock(
            FabricBlockSettings.create().nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F)
                    .pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));

    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    @NoEnglish
    public static final Block LANDING_PAD = new LandingPadBlock(FabricBlockSettings.create().nonOpaque().requiresTool()
            .instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F).pistonBehavior(PistonBehavior.IGNORE));

    @NoEnglish
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    public static final Block ENGINE_BLOCK = new EngineBlock(FabricBlockSettings.create().requiresTool()
            .instrument(Instrument.BASEDRUM).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.IGNORE));
    @NoEnglish
    public static final Block ENGINE_CORE_BLOCK = new EngineCoreBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.DIAMOND_BLUE).solid().instrument(Instrument.HAT)
                    .strength(3.0F).luminance((state) -> 15).nonOpaque());
    @PickaxeMineable
    public static final Block CONSOLE_GENERATOR = new ConsoleGeneratorBlock(
            FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().requiresTool().strength(1.5F)
                    .instrument(Instrument.COW_BELL).pistonBehavior(PistonBehavior.DESTROY));
    @PickaxeMineable
    @NoEnglish
    public static final Block ARTRON_COLLECTOR_BLOCK = new ArtronCollectorBlock(
            FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().requiresTool().strength(1.5F)
                    .instrument(Instrument.BANJO).pistonBehavior(PistonBehavior.IGNORE));
    @NoEnglish
    public static final Block CORAL_PLANT = new CoralPlantBlock(FabricBlockSettings.create().ticksRandomly().nonOpaque()
            .noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.DESTROY));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    @NoEnglish
    public static final Block MONITOR_BLOCK = new MonitorBlock(FabricBlockSettings.create().nonOpaque().requiresTool()
            .instrument(Instrument.COW_BELL).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY));
    @NoEnglish
    public static final Block PLAQUE_BLOCK = new PlaqueBlock(
            FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().instrument(Instrument.COW_BELL)
                    .strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY));
    @NoEnglish
    public static final Block WALL_MONITOR_BLOCK = new WallMonitorBlock(
            FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().instrument(Instrument.COW_BELL)
                    .strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY));
    @NoEnglish
    public static final Block DETECTOR_BLOCK = new DetectorBlock(FabricBlockSettings.create().nonOpaque()
            .instrument(Instrument.COW_BELL).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.NORMAL));
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    @NoEnglish
    public static final Block ZEITON_BLOCK = new AmethystBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA)
            .strength(1.5F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool());
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    public static final Block BUDDING_ZEITON = new BuddingZeitonBlock(
            FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).ticksRandomly().strength(1.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().pistonBehavior(PistonBehavior.DESTROY));
    @NoBlockDrop
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    public static final Block ZEITON_CLUSTER = new AmethystClusterBlock(7, 3,
            FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).solid().nonOpaque().ticksRandomly()
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(1.5F).luminance((state) -> 5)
                    .pistonBehavior(PistonBehavior.DESTROY));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    public static final Block LARGE_ZEITON_BUD = new AmethystClusterBlock(5, 3,
            FabricBlockSettings.copyOf(ZEITON_CLUSTER).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).solid()
                    .luminance((state) -> 4).pistonBehavior(PistonBehavior.DESTROY));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    public static final Block MEDIUM_ZEITON_BUD = new AmethystClusterBlock(4, 3,
            FabricBlockSettings.copyOf(ZEITON_CLUSTER).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).solid()
                    .luminance((state) -> 2).pistonBehavior(PistonBehavior.DESTROY));
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    public static final Block SMALL_ZEITON_BUD = new AmethystClusterBlock(3, 4,
            FabricBlockSettings.copyOf(ZEITON_CLUSTER).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).solid()
                    .luminance((state) -> 1).pistonBehavior(PistonBehavior.DESTROY));
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    @NoEnglish
    public static final Block ZEITON_CAGE = new ZeitonCageBlock(FabricBlockSettings.create().nonOpaque().requiresTool()
            .instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F).pistonBehavior(PistonBehavior.IGNORE)
            .luminance(light -> 15));
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    public static final Block POWER_CONVERTER = new PowerConverterBlock(FabricBlockSettings.create().nonOpaque()
            .requiresTool().instrument(Instrument.COW_BELL).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY));
    @PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
    public static final Block DEMAT_CIRCUIT = new DematCircuitBlock(FabricBlockSettings.create().nonOpaque()
            .requiresTool().instrument(Instrument.COW_BELL).strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY));


    @NoBlockItem
    public static final Block PLUGBOARD = new PlugBoardBlock(
            FabricBlockSettings.create().solid().noCollision().strength(1.0f));

    @NoBlockItem
    public static final Block RADIO = new RadioBlock(FabricBlockSettings.create().nonOpaque());

    // Machines
    @NoBlockItem
    public static final Block MACHINE_CASING = new MachineCasingBlock(FabricBlockSettings.create().nonOpaque()
            .requiresTool().instrument(Instrument.COW_BELL).strength(1.5F, 6.0F));

    @NoBlockItem
    public static final Block FABRICATOR = new FabricatorBlock(FabricBlockSettings.create().nonOpaque().requiresTool()
            .instrument(Instrument.COW_BELL).strength(1.5F, 6.0F));

    // Control Blocks
    @NoBlockItem
    @NoEnglish
    public static final Block REDSTONE_CONTROL_BLOCK = new RedstoneControlBlock(
            FabricBlockSettings.create().nonOpaque().strength(1.5F, 6.0F).pistonBehavior(PistonBehavior.DESTROY));

    public static final Block ENVIRONMENT_PROJECTOR = new EnvironmentProjectorBlock(FabricBlockSettings.create());

    // IF I SEE PEANUT ONE MORE FUCKING TIME I'M GONNA OBLITERATE THE ENTIRETY OF AUSTRIA AND RUSSIA
    /*
     * public static final Block CONSOLE_ROOM_PORT_BLOCK = new
     * ConsoleRoomPortBlock(NeptuneBlockSettings.create().nonOpaque()
     * .addItemSettings(new NeptuneItemSettings().group(() ->
     * AITMod.AIT_ITEM_GROUP)).instrument(Instrument.IRON_XYLOPHONE).strength(1.5F,
     * 6.0F)); public static final Block ENGINE_ROOM_PORT_BLOCK = new
     * EngineRoomPortBlock(NeptuneBlockSettings.create().nonOpaque()
     * .addItemSettings(new NeptuneItemSettings().group(() ->
     * AITMod.AIT_ITEM_GROUP)).instrument(Instrument.IRON_XYLOPHONE).strength(1.5F,
     * 6.0F));
     */

    @NoEnglish
    public static final Block CABLE_BLOCK = new CableBlock(
            FabricBlockSettings.create().nonOpaque().instrument(Instrument.GUITAR).strength(1.5F, 6.0F));

    public static List<Block> get() {
        List<Block> list = new ArrayList<>();

        for (Block block : Registries.BLOCK) {
            if (Registries.BLOCK.getId(block).getNamespace().equalsIgnoreCase(AITMod.MOD_ID)) {
                list.add(block);
            }
        }

        return list;
    }

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return new BlockItem(block, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    }
}
