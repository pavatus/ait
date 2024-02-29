package mdteam.ait.core;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import mdteam.ait.AITMod;
import mdteam.ait.core.blocks.*;
import mdteam.ait.core.blocks.DoorBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;

public class AITBlocks implements BlockRegistryContainer {

    @NoBlockItem
    public static final Block EXTERIOR_BLOCK = new ExteriorBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing().luminance(7));
    public static final Block DOOR_BLOCK = new DoorBlock(FabricBlockSettings.create().nonOpaque().noCollision()
            .instrument(Instrument.BASEDRUM).requiresTool().strength(1.5F, 6.0F));
    public static final Block CONSOLE = new ConsoleBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing()
            .instrument(Instrument.COW_BELL));
    public static final Block CONSOLE_GENERATOR = new ConsoleGeneratorBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles()
            .instrument(Instrument.COW_BELL));
    public static final Block ARTRON_COLLECTOR_BLOCK = new ArtronCollectorBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().requiresTool()
            .instrument(Instrument.BANJO));
    public static final Block CORAL_PLANT = new CoralPlantBlock(FabricBlockSettings.create().ticksRandomly().nonOpaque().noCollision()
            .breakInstantly().sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block MONITOR_BLOCK = new MonitorBlock(FabricBlockSettings.create().nonOpaque().requiresTool().instrument(Instrument.COW_BELL).strength(1.5F, 6.0F));
    public static final Block PLAQUE_BLOCK = new PlaqueBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().instrument(Instrument.COW_BELL).strength(1.5F, 6.0F));
    public static final Block DETECTOR_BLOCK = new DetectorBlock(FabricBlockSettings.create().nonOpaque().instrument(Instrument.COW_BELL).strength(1.5F, 6.0F));
    public static final Block ZEITON_BLOCK = new AmethystBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).strength(1.5F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool());
    public static final Block BUDDING_ZEITON = new BuddingAmethystBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).ticksRandomly().strength(1.5F).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().pistonBehavior(PistonBehavior.DESTROY));
    public static final Block ZEITON_CLUSTER = new AmethystClusterBlock(7, 3, FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).solid().nonOpaque().ticksRandomly().sounds(BlockSoundGroup.AMETHYST_CLUSTER).strength(1.5F).luminance((state) -> 5).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block LARGE_ZEITON_BUD = new AmethystClusterBlock(5, 3, FabricBlockSettings.copy(ZEITON_CLUSTER).sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD).solid().luminance((state) -> 4).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block MEDIUM_ZEITON_BUD = new AmethystClusterBlock(4, 3, FabricBlockSettings.copy(ZEITON_CLUSTER).sounds(BlockSoundGroup.LARGE_AMETHYST_BUD).solid().luminance((state) -> 2).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block SMALL_ZEITON_BUD = new AmethystClusterBlock(3, 4, FabricBlockSettings.copy(ZEITON_CLUSTER).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).solid().luminance((state) -> 1).pistonBehavior(PistonBehavior.DESTROY));

    /*public static final Block CONSOLE_ROOM_PORT_BLOCK = new ConsoleRoomPortBlock(NeptuneBlockSettings.create().nonOpaque()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.IRON_XYLOPHONE).strength(1.5F, 6.0F));
    public static final Block ENGINE_ROOM_PORT_BLOCK = new EngineRoomPortBlock(NeptuneBlockSettings.create().nonOpaque()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.IRON_XYLOPHONE).strength(1.5F, 6.0F));
    public static final Block CABLE_BLOCK = new CableBlock(NeptuneBlockSettings.create().nonOpaque()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.GUITAR).strength(1.5F, 6.0F));*/

    public static List<Block> getBlocks() {
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
