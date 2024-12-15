package loqor.ait.core.tardis.dim;

import java.awt.*;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import dev.pavatus.multidim.api.MultiDimServer;
import dev.pavatus.multidim.api.VoidChunkGenerator;
import dev.pavatus.multidim.impl.AbstractWorldGenListener;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.UnmodifiableLevelProperties;

import loqor.ait.AITMod;
import loqor.ait.api.link.v2.Linkable;
import loqor.ait.api.link.v2.TardisRef;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.core.util.ServerLifecycleHooks;
import loqor.ait.core.util.WorldUtil;

// todo finish this eventually
public class TardisWorld extends ServerWorld implements Linkable {
    protected TardisRef ref;

    protected TardisWorld(MinecraftServer server, ServerTardis tardis) {
        super(server, Util.getMainWorkerExecutor(), ((MultiDimServer) server).multidim$getSession(),
                new UnmodifiableLevelProperties(server.getSaveProperties(), server.getSaveProperties().getMainWorldProperties()), RegistryKey.of(RegistryKeys.WORLD, AITMod.id(tardis.getUuid().toString())),
                buildOptions(server), new AbstractWorldGenListener(), false, BiomeAccess.hashSeed(WorldUtil.getOverworld().getSeed()),
                ImmutableList.of(), true, null);

        this.link(tardis);
    }
    protected TardisWorld(ServerTardis tardis) {
        this(ServerLifecycleHooks.get(), tardis);
    }
    private TardisWorld(RegistryKey<World> world) {
        this(ServerTardisManager.getInstance().demandTardis(ServerLifecycleHooks.get(), UUID.fromString(world.getValue().getPath())));
    }
    @Override
    public void link(Tardis tardis) {
        this.ref = TardisRef.createAs(this, tardis);
    }

    @Override
    public void link(UUID id) {
        this.ref = TardisRef.createAs(this, id);
    }

    @Override
    public TardisRef tardis() {
        return ref;
    }

    public boolean isLinked() {
        return this.ref != null && this.ref.isPresent();
    }

    protected static RegistryEntry<DimensionType> resolveType(MinecraftServer server, Identifier id) {
        SimpleRegistry<DimensionType> registry = (SimpleRegistry<DimensionType>) server.getRegistryManager().get(RegistryKeys.DIMENSION_TYPE);
        RegistryKey<DimensionType> key = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id);
        return registry.getEntry(key).orElse(null);
    }
    protected static DimensionOptions buildOptions(MinecraftServer server) {
        RegistryEntry<DimensionType> typeEntry = resolveType(server, new Identifier(AITMod.MOD_ID, "tardis_dimension_type"));

        if (typeEntry == null)
            throw new IllegalArgumentException("Dimension type is required to create tardis dimension options!");

        return new DimensionOptions(typeEntry, new VoidChunkGenerator(WorldUtil.getOverworld().getRegistryManager().get(RegistryKeys.BIOME), RegistryKey.of(RegistryKeys.BIOME, new Identifier(AITMod.MOD_ID, "tardis"))));
    }

}
