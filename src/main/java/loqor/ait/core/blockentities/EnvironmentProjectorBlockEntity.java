package loqor.ait.core.blockentities;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITDimensions;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.LinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Optional;

import static loqor.ait.tardis.util.TardisUtil.findTardisByInterior;

public class EnvironmentProjectorBlockEntity extends LinkableBlockEntity {

    private static final RegistryKey<World> DEFAULT = AITDimensions.TARDIS_DIM_WORLD;
    private RegistryKey<World> current = DEFAULT;

    public EnvironmentProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.ENVIRONMENT_PROJECTOR_BLOCK_ENTITY_TYPE, pos, state);
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

    public void switchSkybox(Tardis tardis, PlayerEntity player) {
        RegistryKey<World> next = findNext(world.getServer(), this.current);

        player.sendMessage(Text.translatable("message.ait.environment_projector", next.getValue()));
        AITMod.LOGGER.debug("Last: {}, next: {}", this.current, next);

        this.current = next;
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
        tardis.stats().skybox().set(DEFAULT);
    }

    @Override
    public Optional<Tardis> findTardis() {
        if (this.tardisId == null && this.world != null) {
            Tardis found = findTardisByInterior(pos, !this.world.isClient());

            if (found != null)
                this.setTardis(found);
        }

        return super.findTardis();
    }

    private static RegistryKey<World> findNext(MinecraftServer server, RegistryKey<World> last) {
        Iterator<ServerWorld> iter = server.getWorlds().iterator();

        ServerWorld first = iter.next();
        ServerWorld found = first;

        while (iter.hasNext()) {
            if (found.getRegistryKey().getValue().equals(last.getValue())) {
                if (!iter.hasNext())
                    break;

                return iter.next().getRegistryKey();
            }

            found = iter.next();
        }

        return first.getRegistryKey();
    }
}
