package mdteam.ait.core;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import mdteam.ait.AITMod;
import mdteam.ait.core.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;

public class AITBlocks implements BlockRegistryContainer {

    public static final Block EXTERIOR_BLOCK = new ExteriorBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing().luminance(7));
    public static final Block DOOR_BLOCK = new DoorBlock(FabricBlockSettings.create().nonOpaque().noCollision().noBlockBreakParticles()
            .instrument(Instrument.BASEDRUM).requiresTool().strength(1.5F, 6.0F));
    public static final Block CONSOLE = new ConsoleBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing()
            .instrument(Instrument.COW_BELL));
    public static final Block CONSOLE_GENERATOR = new ConsoleGeneratorBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles()
            .instrument(Instrument.COW_BELL));
    public static final Block ARTRON_COLLECTOR_BLOCK = new ArtronCollectorBlock(FabricBlockSettings.create().nonOpaque().noBlockBreakParticles()
            .instrument(Instrument.BANJO));
    public static final Block CORAL_PLANT = new CoralPlantBlock(FabricBlockSettings.create().ticksRandomly().nonOpaque().noCollision()
            .breakInstantly().sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.DESTROY));
    @NoBlockItem
    public static final Block RADIO = new RadioBlock(FabricBlockSettings.create().nonOpaque().instrument(Instrument.BASEDRUM).requiresTool().strength(1.5F, 6.0F).dropsNothing());
    public static final Block MONITOR_BLOCK = new MonitorBlock(FabricBlockSettings.create().nonOpaque().instrument(Instrument.COW_BELL).strength(1.5F, 6.0F));
    public static final Block DETECTOR_BLOCK = new DetectorBlock(FabricBlockSettings.create().nonOpaque().instrument(Instrument.COW_BELL).strength(1.5F, 6.0F));
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
