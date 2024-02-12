package mdteam.ait.core;

import com.neptunedevelopmentteam.neptunelib.core.blocksettings.NeptuneBlockSettings;
import com.neptunedevelopmentteam.neptunelib.core.init_handlers.NeptuneBlockInit;
import com.neptunedevelopmentteam.neptunelib.core.itemsettings.NeptuneItemSettings;
import mdteam.ait.AITMod;
import mdteam.ait.core.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;

public class AITBlocks implements NeptuneBlockInit {

    public static final Block EXTERIOR_BLOCK = new ExteriorBlock(NeptuneBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing().luminance(7));
    public static final Block DOOR_BLOCK = new DoorBlock(NeptuneBlockSettings.create().nonOpaque().noCollision().noBlockBreakParticles()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.BASEDRUM).requiresTool().strength(1.5F, 6.0F));
    public static final Block CONSOLE = new ConsoleBlock(NeptuneBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.COW_BELL));
    public static final Block CONSOLE_GENERATOR = new ConsoleGeneratorBlock(NeptuneBlockSettings.create().nonOpaque().noBlockBreakParticles()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.COW_BELL));
    public static final Block ARTRON_COLLECTOR_BLOCK = new ArtronCollectorBlock(NeptuneBlockSettings.create().nonOpaque().noBlockBreakParticles()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.BANJO));
    public static final Block CORAL_PLANT = new CoralPlantBlock(NeptuneBlockSettings.create().ticksRandomly().nonOpaque().noCollision()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).breakInstantly().sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block RADIO = new RadioBlock(NeptuneBlockSettings.create().nonOpaque().instrument(Instrument.BASEDRUM).requiresTool().strength(1.5F, 6.0F).dropsNothing());
    public static final Block MONITOR_BLOCK = new MonitorBlock(NeptuneBlockSettings.create().nonOpaque()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.COW_BELL).strength(1.5F, 6.0F));
    public static final Block CONSOLE_ROOM_PORT_BLOCK = new ConsoleRoomPortBlock(NeptuneBlockSettings.create().nonOpaque()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.IRON_XYLOPHONE).strength(1.5F, 6.0F));
    public static final Block ENGINE_ROOM_PORT_BLOCK = new EngineRoomPortBlock(NeptuneBlockSettings.create().nonOpaque()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.IRON_XYLOPHONE).strength(1.5F, 6.0F));
    public static final Block CABLE_BLOCK = new CableBlock(NeptuneBlockSettings.create().nonOpaque()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).instrument(Instrument.GUITAR).strength(1.5F, 6.0F));

    public static List<Block> getBlocks() {
        List<Block> list = new ArrayList<>();

        for (Block block : Registries.BLOCK) {
            if (Registries.BLOCK.getId(block).getNamespace().equalsIgnoreCase(AITMod.MOD_ID)) {
                list.add(block);
            }
        }

        return list;
    }
}
