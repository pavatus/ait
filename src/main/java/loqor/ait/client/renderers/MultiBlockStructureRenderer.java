package loqor.ait.client.renderers;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.BlockRenderView;

import loqor.ait.AITMod;
import loqor.ait.core.engine.block.multi.MultiBlockStructure;

public class MultiBlockStructureRenderer {
    private final MinecraftClient client;
    private final Profiler profiler;

    protected MultiBlockStructureRenderer(MinecraftClient client) {
        this.client = client;
        this.profiler = client.getProfiler();
    }
    private MultiBlockStructureRenderer() {
        this(MinecraftClient.getInstance());
    }

    public void render(MultiBlockStructure structure, BlockPos centre, BlockRenderView view, @Nullable MatrixStack stack) {
        profiler.push("multi_block_structure");
        profiler.push("iterate_offsets");
        structure.forEach(offset -> {
            BlockPos pos = centre.add(offset.offset());
            BlockState state = this.getBlock(offset.block(), client.getServer().getTicks()).getDefaultState();
            profiler.swap("render_block");
            renderBlock(state, pos, view, stack);
        });
    }
    public void renderBlock(BlockState state, BlockPos pos, BlockRenderView view, @Nullable MatrixStack stack) {
        if (stack == null) stack = new MatrixStack();

        BlockRenderManager manager = client.getBlockRenderManager();
        // BlockBufferBuilderStorage builders = client.getBufferBuilders().getBlockBufferBuilders();
        // BufferBuilder buffer = builders.get(RenderLayers.getBlockLayer(state));
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        if (!buffer.isBuilding()) buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);

        if (client.getServer().getTicks() % 20 == 0) {
            AITMod.LOGGER.info("RENDERING MULTI BLOCK STRUCTURE, state {} | pos {}", state, pos);
        }
        stack.push();

        manager.renderBlock(state, pos, view, stack, buffer, true, client.world.getRandom());

        stack.pop();

        Tessellator.getInstance().draw();
    }
    private Block getBlock(MultiBlockStructure.AllowedBlocks block, int ticks) {
        if (block.size() == 1) return block.get(0);

        // 20 ticks per block
        int index = (ticks / 20) % block.size();
        return block.get(index);
    }

    private static MultiBlockStructureRenderer INSTANCE;

    public static MultiBlockStructureRenderer instance() {
        if (INSTANCE == null) {
            INSTANCE = new MultiBlockStructureRenderer();
        }
        return INSTANCE;
    }
}
