package mdteam.ait.core;

import mdteam.ait.AITMod;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import mdteam.ait.core.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class AITBlocks implements BlockRegistryContainer {

    @NoBlockItem
    public static final Block EXTERIOR_BLOCK = new ExteriorBlock(FabricBlockSettings.create().nonOpaque().ticksRandomly());
    public static final Block DOOR_BLOCK = new DoorBlock(FabricBlockSettings.create().nonOpaque().noCollision());
    public static final Block CONSOLE = new ConsoleBlock(FabricBlockSettings.create().nonOpaque());
    @NoBlockItem
    public static final Block RADIO = new RadioBlock(FabricBlockSettings.create().nonOpaque());


    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return new BlockItem(block, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    }
}
