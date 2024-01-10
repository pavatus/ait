package mdteam.ait.core;

import com.neptunedevelopmentteam.neptunelib.core.blocksettings.NeptuneBlockSettings;
import com.neptunedevelopmentteam.neptunelib.core.init_handlers.CustomName;
import com.neptunedevelopmentteam.neptunelib.core.init_handlers.NeptuneBlockInit;
import com.neptunedevelopmentteam.neptunelib.core.itemsettings.NeptuneItemSettings;
import mdteam.ait.AITMod;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import mdteam.ait.core.blockentities.*;
import mdteam.ait.core.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AITBlocks implements NeptuneBlockInit {

    public static final Block EXTERIOR_BLOCK = new ExteriorBlock(NeptuneBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing().luminance(7));
    public static final Block DOOR_BLOCK = new DoorBlock(NeptuneBlockSettings.create().nonOpaque().noCollision().noBlockBreakParticles()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)).strength(3F, 12000F));
    public static final Block CONSOLE = new ConsoleBlock(NeptuneBlockSettings.create().nonOpaque().noBlockBreakParticles().strength(-1.0f, 3600000.0f).dropsNothing()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)));
    public static final Block CONSOLE_GENERATOR = new ConsoleGeneratorBlock(NeptuneBlockSettings.create().nonOpaque().noBlockBreakParticles()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)));
    public static final Block CORAL_PLANT = new CoralPlantBlock(NeptuneBlockSettings.create().ticksRandomly().nonOpaque().noCollision()
            .addItemSettings(new NeptuneItemSettings().group(() -> AITMod.AIT_ITEM_GROUP)));
    public static final Block RADIO = new RadioBlock(NeptuneBlockSettings.create().nonOpaque());

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
