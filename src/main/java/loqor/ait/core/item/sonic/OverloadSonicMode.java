package loqor.ait.core.item.sonic;

import dev.pavatus.planet.core.PlanetBlocks;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITTags;
import loqor.ait.data.schema.sonic.SonicSchema;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class OverloadSonicMode extends SonicMode {

    private static final ItemStack TOOL = new ItemStack(Items.DIAMOND_PICKAXE);

    protected OverloadSonicMode(int index) {
        super(index);
    }

    @Override
    public void stopUsing(ItemStack stack, World world, LivingEntity user, int ticks, int ticksLeft) {
        if (!(world instanceof ServerWorld serverWorld))
            return;

        HitResult hitResult = SonicMode.getHitResult(user);

        if (hitResult instanceof BlockHitResult blockHit)
            this.overloadBlock(blockHit.getBlockPos(), stack, serverWorld, user, ticks);
    }

    private void overloadBlock(BlockPos pos, ItemStack stack, ServerWorld world, LivingEntity user, int ticks) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block.getDefaultState().isIn(ConventionalBlockTags.GLASS_PANES)) {
            world.breakBlock(pos, false);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            return;
        }

        if (block == Blocks.BRICKS || block == Blocks.BRICK_WALL)  {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.BRICK, 4)));
            world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == Blocks.NETHER_BRICKS || block == Blocks.RED_NETHER_BRICKS || block == Blocks.NETHER_BRICK_WALL || block == Blocks.RED_NETHER_BRICK_WALL)  {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.NETHER_BRICK, 4)));
            world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == Blocks.ICE || block == Blocks.BLUE_ICE || block == Blocks.FROSTED_ICE || block == Blocks.PACKED_ICE)  {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Blocks.AIR)));
            world.setBlockState(pos, Blocks.WATER.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }



        if (block == AITBlocks.CONSOLE)  {
            world.breakBlock(pos, true);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            return;
        }

        if (block.getDefaultState().isIn(ConventionalBlockTags.GLASS_BLOCKS)) {
            world.breakBlock(pos, false);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            return;
        }

        if (block instanceof LeavesBlock) {
            world.breakBlock(pos, false);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);

            return;
        }

        if (!world.getBlockState(pos).isIn(AITTags.Blocks.SONIC_INTERACTABLE))
            return;

        if (block instanceof TntBlock) {
            TntBlock.primeTnt(world, pos);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return;
        }


        if (block instanceof SandBlock) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.GLASS)));
            world.setBlockState(pos, Blocks.AIR.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

//God forgive me for what i did to this code

        if (block == Blocks.COAL_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.COAL)));
            world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == Blocks.DEEPSLATE_COAL_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.COAL)));
            world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == PlanetBlocks.MARTIAN_COAL_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.COAL)));
            world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == PlanetBlocks.ANORTHOSITE_COAL_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.COAL)));
            world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == Blocks.LAPIS_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.LAPIS_LAZULI)));
            world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == Blocks.DEEPSLATE_LAPIS_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.LAPIS_LAZULI)));
            world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == PlanetBlocks.MARTIAN_LAPIS_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.LAPIS_LAZULI)));
            world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == PlanetBlocks.ANORTHOSITE_LAPIS_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.LAPIS_LAZULI)));
            world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == Blocks.DIAMOND_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.DIAMOND)));
            world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == Blocks.DEEPSLATE_DIAMOND_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.DIAMOND)));
            world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == PlanetBlocks.MARTIAN_DIAMOND_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.DIAMOND)));
            world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == PlanetBlocks.ANORTHOSITE_DIAMOND_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.DIAMOND)));
            world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == Blocks.IRON_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_IRON)));
            world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == Blocks.DEEPSLATE_IRON_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_IRON)));
            world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == PlanetBlocks.MARTIAN_IRON_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_IRON)));
            world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == PlanetBlocks.ANORTHOSITE_IRON_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_IRON)));
            world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == Blocks.GOLD_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_GOLD)));
            world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == Blocks.DEEPSLATE_GOLD_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_GOLD)));
            world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == PlanetBlocks.MARTIAN_GOLD_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_GOLD)));
            world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == PlanetBlocks.ANORTHOSITE_GOLD_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_GOLD)));
            world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == Blocks.NETHER_GOLD_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_GOLD)));
            world.setBlockState(pos, Blocks.NETHERRACK.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == Blocks.COPPER_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_COPPER)));
            world.setBlockState(pos, Blocks.STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == Blocks.DEEPSLATE_COPPER_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_COPPER)));
            world.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (block == PlanetBlocks.MARTIAN_COPPER_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_COPPER)));
            world.setBlockState(pos, PlanetBlocks.MARTIAN_STONE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        if (block == PlanetBlocks.ANORTHOSITE_COPPER_ORE) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(Items.RAW_COPPER)));
            world.setBlockState(pos, PlanetBlocks.ANORTHOSITE.getDefaultState(),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);

        }

        if (state.isIn(ConventionalBlockTags.ORES)) {
            BlockState newState = this.guessOreBase(block);

            if (newState == null)
                return;

            world.setBlockState(pos, newState);

            LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(world)
                    .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                    .add(LootContextParameters.BLOCK_STATE, state)
                    .addOptional(LootContextParameters.BLOCK_ENTITY, world.getBlockEntity(pos))
                    .addOptional(LootContextParameters.THIS_ENTITY, user)
                    .add(LootContextParameters.TOOL, TOOL);

            for (ItemStack dropped : state.getDroppedStacks(builder)) {
                world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, dropped));
            }
        }

        if (block.getDefaultState().contains(RedstoneTorchBlock.LIT)) {
            world.playSound(user, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                    world.getRandom().nextFloat() * 0.4f + 0.8f);

            world.setBlockState(pos, state.cycle(RedstoneTorchBlock.LIT));
        }

        world.emitGameEvent(user, GameEvent.BLOCK_CHANGE, pos);
    }

    @Nullable
    private BlockState guessOreBase(Block block) {
        if (block == Blocks.NETHER_GOLD_ORE || block == Blocks.NETHER_QUARTZ_ORE)
            return Blocks.NETHERRACK.getDefaultState();

        // something_material_ore -> something
        // material_ore -> stone
        // martian_material_ore -> martian -> martian_stone

        Identifier id = Registries.BLOCK.getId(block);
        String ore = id.getPath();

        ore = ore.substring(0, ore.length() - 4);
        int i = ore.lastIndexOf('_');

        ore = i == -1 ? "stone" : ore.substring(0, i);
        id = id.withPath(ore);

        BlockState newState = Registries.BLOCK.get(id).getDefaultState();

        if (newState.isAir()) {
            id = id.withSuffixedPath("_stone");
            newState = Registries.BLOCK.get(id).getDefaultState();

            if (newState.isAir())
                return null;
        }

        return newState;
    }

    @Override
    public int maxTime() {
        return 5 * 60 * 20; // 5 minutes
    }

    @Override
    public Identifier model(SonicSchema.Models models) {
        return models.overload();
    }
}
