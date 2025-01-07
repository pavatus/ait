package dev.drtheo.stp;

import java.util.*;

import dev.drtheo.stp.mixin.ClientPlayNetworkHandlerAccessor;
import dev.drtheo.stp.mixin.ClientWorldInvoker;
import loqor.ait.client.util.SkyboxUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.map.MapState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.LightData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.dimension.DimensionType;

@Environment(EnvType.CLIENT)
public class ClientSeamlessTp implements ClientModInitializer {

    private static final Map<Identifier, Set<CompleteChunkData>> MAP = new HashMap<>();

    record CompleteChunkData(int x, int z, ChunkData chunkData, LightData lightData) {

        public CompleteChunkData(ChunkDataS2CPacket packet) {
            this(packet.getX(), packet.getZ(), packet.getChunkData(), packet.getLightData());
        }

        @Override
        public int hashCode() {
            int i = 1664525 * x + 1013904223;
            int j = 1664525 * (z ^ 0xDEADBEEF) + 1013904223;
            return i ^ j;
        }
    }

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SeamlessTp.TP, (client, handler, buf, response) -> {
            RegistryKey<DimensionType> dimensionTypeKey = buf.readRegistryKey(RegistryKeys.DIMENSION_TYPE);
            RegistryKey<World> worldKey = buf.readRegistryKey(RegistryKeys.WORLD);

            long sha256Seed = buf.readLong();
            GameMode gameMode = GameMode.byId(buf.readUnsignedByte());
            GameMode previousGameMode = GameMode.getOrNull(buf.readByte());
            boolean debugWorld = buf.readBoolean();
            boolean flatWorld = buf.readBoolean();
            byte flag = buf.readByte();
            Optional<GlobalPos> lastDeathPos = buf.readOptional(PacketByteBuf::readGlobalPos);
            int portalCooldown = buf.readVarInt();

            client.executeSync(() -> {
                if (client.player == null || client.world == null)
                    return;

                if (!(client.getNetworkHandler() instanceof ClientPlayNetworkHandlerAccessor cpnha))
                    return;

                RegistryEntry.Reference<DimensionType> dimensionTypeEntry = cpnha.getCombinedDynamicRegistries()
                        .getCombinedRegistryManager().get(RegistryKeys.DIMENSION_TYPE).entryOf(dimensionTypeKey);

                tp(cpnha, MinecraftClient.getInstance(), client.player, dimensionTypeEntry,
                        worldKey, sha256Seed, gameMode, previousGameMode, debugWorld,
                        flatWorld, flag, lastDeathPos, portalCooldown);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SeamlessTp.PRELOAD, (client, handler, buf, responseSender) -> {
            Identifier id = buf.readIdentifier();
            ChunkDataS2CPacket packet = new ChunkDataS2CPacket(buf);

            MAP.computeIfAbsent(id, identifier -> new HashSet<>())
                    .add(new CompleteChunkData(packet));
        });

        ClientPlayNetworking.registerGlobalReceiver(SeamlessTp.UNLOAD, (client, handler, buf, responseSender) -> {
            Identifier id = buf.readIdentifier();
            Set<CompleteChunkData> set = MAP.remove(id);

            if (set != null)
                set.clear();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> MAP.clear());
    }

    private static void tryLoadCache(ClientWorld world) {
        Set<CompleteChunkData> set = MAP.remove(world.getRegistryKey().getValue());

        if (set == null)
            return;

        for (CompleteChunkData complete : set) {
            int x = complete.x();
            int z = complete.z();

            ChunkData chunkData = complete.chunkData();
            LightData lightData = complete.lightData();

            world.getChunkManager().loadChunkFromPacket(
                    x, z, chunkData.getSectionsDataBuf(),
                    chunkData.getHeightmap(), chunkData.getBlockEntities(x, z)
            );

            world.enqueueChunkUpdate(() -> {
                LightingProvider lightingProvider = world.getChunkManager().getLightingProvider();

                BitSet initedSky = lightData.getInitedSky();
                BitSet uninitedSky = lightData.getUninitedSky();

                Iterator<byte[]> iterator = lightData.getSkyNibbles().iterator();

                updateLighting(world, x, z, lightingProvider, LightType.SKY, initedSky, uninitedSky, iterator);

                BitSet initedBlock = lightData.getInitedBlock();
                BitSet uninitedBlock = lightData.getUninitedBlock();

                Iterator<byte[]> iterator2 = lightData.getBlockNibbles().iterator();

                updateLighting(world, x, z, lightingProvider, LightType.BLOCK, initedBlock, uninitedBlock, iterator2);
                lightingProvider.setColumnEnabled(new ChunkPos(x, z), true);

                WorldChunk worldChunk = world.getChunkManager().getWorldChunk(x, z, false);

                if (worldChunk != null) {
                    ChunkSection[] chunkSections = worldChunk.getSectionArray();
                    ChunkPos chunkPos = worldChunk.getPos();

                    for (int i = 0; i < chunkSections.length; ++i) {
                        ChunkSection chunkSection = chunkSections[i];
                        int coord = world.sectionIndexToCoord(i);

                        lightingProvider.setSectionStatus(ChunkSectionPos.from(chunkPos, coord), chunkSection.isEmpty());
                        world.scheduleBlockRenders(x, coord, z);
                    }
                }
            });

            // WorldRenderer#setupTerrain -> collectRenderableChunks
            // WorldRenderer#compileChunks
        }
    }

    private static void updateLighting(ClientWorld world, int chunkX, int chunkZ, LightingProvider provider, LightType type, BitSet inited, BitSet uninited, Iterator<byte[]> nibbles) {
        for (int i = 0; i < provider.getHeight(); ++i) {
            int j = provider.getBottomY() + i;

            boolean bl = inited.get(i);
            boolean bl2 = uninited.get(i);

            if (!bl && !bl2)
                continue;

            provider.enqueueSectionData(type, ChunkSectionPos.from(chunkX, j, chunkZ), bl ? new ChunkNibbleArray(nibbles.next().clone()) : new ChunkNibbleArray());
            world.scheduleBlockRenders(chunkX, j, chunkZ);
        }
    }

    private static void tp(ClientPlayNetworkHandlerAccessor accessor,
                           MinecraftClient client, ClientPlayerEntity player,
                           RegistryEntry.Reference<DimensionType> toDimensionEntry,
                           RegistryKey<World> toWorldKey, long sha256Seed, GameMode gameMode,
                           @Nullable GameMode previousGameMode, boolean debugWorld, boolean flatWorld,
                           byte flag, Optional<GlobalPos> lastDeathPos, int portalCooldown) {
        ClientWorld world = accessor.getWorld();
        int i = player.getId();

        if (toWorldKey != player.getWorld().getRegistryKey()) {
            ClientWorld.Properties oldProperties = accessor.getWorldProperties();
            ClientWorld.Properties newProperties = new ClientWorld.Properties(oldProperties.getDifficulty(), oldProperties.isHardcore(), flatWorld);

            Scoreboard scoreboard = world.getScoreboard();
            Map<String, MapState> map = ((ClientWorldInvoker) world).stp$getMapStates();

            accessor.setWorldProperties(newProperties);

            world = new ClientWorld(player.networkHandler, newProperties, toWorldKey, toDimensionEntry,
                    accessor.getChunkLoadDistance(), accessor.getSimulationDistance(), client::getProfiler,
                    client.worldRenderer, debugWorld, sha256Seed);

            accessor.setWorld(world);

            world.setScoreboard(scoreboard);
            ((ClientWorldInvoker) world).stp$putMapStates(map);

            tryLoadCache(world);
            ((STPMinecraftClient) client).stp$joinWorld(world);
        }

        String string = player.getServerBrand();
        client.cameraEntity = null;

        if (player.shouldCloseHandledScreenOnRespawn())
            player.closeHandledScreen();

        ClientPlayerEntity newPlayer = (flag & 2) != 0 ? client.interactionManager.createPlayer(world, player.getStatHandler(), player.getRecipeBook(), player.isSneaking(), player.isSprinting())
                : client.interactionManager.createPlayer(world, player.getStatHandler(), player.getRecipeBook());
        newPlayer.setId(i);

        client.player = newPlayer;

        if (toWorldKey != player.getWorld().getRegistryKey())
            client.getMusicTracker().stop();

        client.cameraEntity = newPlayer;

        List<DataTracker.SerializedEntry<?>> list = player.getDataTracker().getChangedEntries();

        if ((flag & 2) != 0 && list != null)
            newPlayer.getDataTracker().writeUpdatedEntries(list);

        if ((flag & 1) != 0)
            newPlayer.getAttributes().setFrom(player.getAttributes());

        newPlayer.init();
        newPlayer.setServerBrand(string);
        world.addPlayer(i, newPlayer);
        newPlayer.setYaw(-180.0f);
        newPlayer.input = new KeyboardInput(client.options);

        client.interactionManager.copyAbilities(newPlayer);

        newPlayer.setReducedDebugInfo(player.hasReducedDebugInfo());
        newPlayer.setShowsDeathScreen(player.showsDeathScreen());
        newPlayer.setLastDeathPos(lastDeathPos);
        newPlayer.setPortalCooldown(portalCooldown);

        newPlayer.nauseaIntensity = player.nauseaIntensity;
        newPlayer.prevNauseaIntensity = player.prevNauseaIntensity;

        if (client.currentScreen instanceof DeathScreen || client.currentScreen instanceof DeathScreen.TitleScreenConfirmScreen)
            client.setScreen(null);

        client.interactionManager.setGameModes(gameMode, previousGameMode);
        //tryLoadCache(world);

        ((STPWorldRenderer) client.worldRenderer).stp$setWorld(world);
    }
}
