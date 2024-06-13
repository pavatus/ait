package loqor.ait.core.blockentities;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.blocks.EnvironmentProjectorBlock;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.link.InteriorLinkableBlockEntity;
import loqor.ait.tardis.link.TardisRef;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

import static loqor.ait.core.blocks.EnvironmentProjectorBlock.*;

public class EnvironmentProjectorBlockEntity extends InteriorLinkableBlockEntity {

    private static final RegistryKey<World> DEFAULT = AITDimensions.TARDIS_DIM_WORLD;
    private RegistryKey<World> current = DEFAULT;

    public EnvironmentProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.ENVIRONMENT_PROJECTOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);

        if (powered != state.get(POWERED)) {
            if (state.get(ENABLED) != powered) {
                state = state.with(ENABLED, powered);

                Tardis tardis = TardisUtil.findTardisByInterior(pos, true);

                if (tardis == null)
                    return;

                EnvironmentProjectorBlock.toggle(tardis, null, world, pos, state, powered);
            }

            state = state.with(POWERED, powered);
        }

        world.setBlockState(pos, state.with(SILENT, world.getBlockState(
                pos.down()).isIn(BlockTags.WOOL)
        ), Block.NOTIFY_LISTENERS);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        TardisRef ref = this.tardis();

        if (ref.isEmpty())
            return ActionResult.PASS;

        Tardis tardis = ref.get();

        if (player.isSneaking()) {
            this.switchSkybox(tardis, state, player);
            return ActionResult.SUCCESS;
        }

        state = state.cycle(ENABLED);
        world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);

        EnvironmentProjectorBlock.toggle(tardis, player, world, pos, state, state.get(ENABLED));
        return ActionResult.SUCCESS;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.current = RegistryKey.of(RegistryKeys.WORLD, new Identifier(nbt.getString("dimension")));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putString("dimension", this.current.getValue().toString());
    }

    public void switchSkybox(Tardis tardis, BlockState state, PlayerEntity player) {
        RegistryKey<World> next = findNext(world.getServer(), this.current);

        player.sendMessage(Text.translatable("message.ait.environment_projector", next.getValue().toString()));
        AITMod.LOGGER.debug("Last: {}, next: {}", this.current, next);

        this.current = next;

        if (state.get(EnvironmentProjectorBlock.ENABLED))
            this.apply(tardis);
    }

    public void toggle(Tardis tardis, boolean active) {
        if (active) {
            this.apply(tardis);
        } else {
            this.disable(tardis);
        }
    }

    public void apply(Tardis tardis) {
        tardis.stats().skybox().set(this.current);
    }

    public void disable(Tardis tardis) {
        Value<RegistryKey<World>> value = tardis.stats().skybox();

        if (same(this.current, value.get()))
            value.set(DEFAULT);
    }

    private static RegistryKey<World> findNext(MinecraftServer server, RegistryKey<World> last) {
        Iterator<ServerWorld> iter = server.getWorlds().iterator();

        ServerWorld first = iter.next();
        ServerWorld found = first;

        while (iter.hasNext()) {
            if (same(found.getRegistryKey(), last)) {
                if (!iter.hasNext())
                    break;

                return iter.next().getRegistryKey();
            }

            found = iter.next();
        }

        return first.getRegistryKey();
    }

    private static boolean same(RegistryKey<World> a, RegistryKey<World> b) {
        return a == b || a.getValue().equals(b.getValue());
    }
}
