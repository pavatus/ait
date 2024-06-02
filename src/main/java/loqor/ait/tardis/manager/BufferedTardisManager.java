package loqor.ait.tardis.manager;

import loqor.ait.AITMod;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BufferedTardisManager<T extends ServerTardis, P extends PlayerEntity, C> extends TardisManager<T, C> {

    private final ConcurrentHashMap<UUID, List<UUID>> subscribers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, List<UUID>> buffers = new ConcurrentHashMap<>();

    protected BufferedTardisManager() {

    }

    public void remove(UUID uuid) {
        this.buffers.remove(uuid);
        this.lookup.remove(uuid);
        this.subscribers.remove(uuid);
    }

    protected abstract T loadTardis(C c, UUID uuid);

    protected abstract void updateTardisProperty(@NotNull P player, T tardis, TardisComponent.Id id, String key, String type, String value);

    protected abstract void updateTardisProperty(@NotNull P player, T tardis, TardisComponent.Id id, Value<?> property);

    protected abstract void updateTardis(@NotNull P player, T tardis, TardisComponent.Id id, String json);

    public void updateTardisProperty(@NotNull P player, T tardis, TardisComponent component, String key, String type, String value) {
        this.updateTardisProperty(player, tardis, component.getId(), key, type, value);
        this.checkForceSync(player, tardis);
    }

    public void updateTardisPropertyV2(@NotNull P player, T tardis, Value<?> property) {
        this.updateTardisProperty(player, tardis, property.getHolder().getId(), property);
        this.checkForceSync(player, tardis);
    }

    public void updateTardis(@NotNull P player, T tardis, TardisComponent component) {
        this.updateTardis(player, tardis, component.getId(), this.networkGson.toJson(component));
        this.checkForceSync(player, tardis);
    }

    @Override
    public void loadTardis(C c, UUID uuid, @Nullable Consumer<T> consumer) {
        T tardis = this.loadTardis(c, uuid);

        if (consumer != null)
            consumer.accept(tardis);
    }

    @Override
    @Deprecated
    public @Nullable T demandTardis(C c, UUID uuid) {
        if (uuid == null)
            return null; // ugh

        T result = this.lookup.get(uuid);

        if (result == null)
            result = this.loadTardis(c, uuid);

        return result;
    }

    protected void sendAndSubscribe(P player, T tardis) {
        this.sendTardis(player, tardis);
        if (tardis != null)
            this.addSubscriberToTardis(player, tardis.getUuid());
    }

    protected void sendTardis(@NotNull P player, Tardis tardis) {
        if (tardis == null || this.networkGson == null)
            return;
        
        if (this.isInBuffer(player, tardis.getUuid()))
            return;

        if (this.isAskOnDelay(player)) {
            this.addToBuffer(player, tardis.getUuid());
            return;
        }

        this.sendTardis(player, tardis.getUuid(), this.networkGson.toJson(tardis, ServerTardis.class));

        this.createAskDelay(player);
        this.createForceSyncDelay(player);
    }

    protected abstract void sendTardis(@NotNull P player, UUID uuid, String json);

    /**
     * Adds a subscriber to the Tardis
     *
     * @param P PLAYER
     * @param tardisUUID         TARDIS UUID
     */
    private void addSubscriberToTardis(P P, UUID tardisUUID) {
        if (this.subscribers.containsKey(tardisUUID)) {
            this.subscribers.get(tardisUUID).add(P.getUuid());
        } else {
            List<UUID> subscriber_list = new CopyOnWriteArrayList<>();
            subscriber_list.add(P.getUuid());
            this.subscribers.put(tardisUUID, subscriber_list);
        }
    }

    private boolean isAskOnDelay(P player) {
        return DeltaTimeManager.isStillWaitingOnDelay(player.getUuidAsString() + "-ask-delay");
    }

    protected void tickBuffer(Function<UUID, P> players) {
        if (this.buffers.isEmpty())
            return;

        for (Iterator<UUID> it = this.buffers.keys().asIterator(); it.hasNext(); ) {
            UUID playerId = it.next();
            P player = players.apply(playerId);

            if (player == null
                    || !this.buffers.containsKey(playerId)
                    || isAskOnDelay(player))
                continue;

            List<UUID> tardisIds = this.buffers.get(playerId);

            if (tardisIds == null || tardisIds.isEmpty())
                continue;

            List<UUID> copyOfTardisIds = new CopyOnWriteArrayList<>(tardisIds);

            for (UUID tardisId : copyOfTardisIds) {
                tardisIds.remove(tardisId);
                this.sendTardis(player, this.lookup.get(tardisId));
            }

            if (tardisIds.isEmpty())
                this.buffers.remove(playerId);
        }
    }

    private boolean isInBuffer(P player, UUID tardis) {
        if (!this.buffers.containsKey(player.getUuid()))
            return false;

        return this.buffers.get(player.getUuid()).contains(tardis);
    }

    private void addToBuffer(P player, UUID tardis) {
        if (this.buffers.containsKey(player.getUuid())) {
            this.buffers.get(player.getUuid()).add(tardis);
            return;
        }

        this.buffers.put(player.getUuid(), new ArrayList<>(Collections.singletonList(tardis)));
    }

    /**
     * A delay to stop the client getting overloaded with tons of tardises all at once, splitting it up over a few seconds to save server performance.
     */
    private void createAskDelay(P player) {
        DeltaTimeManager.createDelay(player.getUuidAsString() + "-ask-delay", (long) ((AITMod.AIT_CONFIG.ASK_DELAY()) * 1000L)); // A delay between asking for tardises to be synced
    }

    /**
     * A delay to force resync the server when its been a while since theyve seen a tardis to fix sync issues
     */
    private void createForceSyncDelay(P player) {
        DeltaTimeManager.createDelay(player.getUuidAsString() + "-force-sync-delay", (long) ((AITMod.AIT_CONFIG.force_sync_delay()) * 1000L)); // A delay between asking for tardises to be synced
    }

    private boolean isForceSyncOnDelay(P player) {
        return DeltaTimeManager.isStillWaitingOnDelay(player.getUuidAsString() + "-force-sync-delay");
    }

    private void checkForceSync(P player, T tardis) {
        if (!isForceSyncOnDelay(player))
            this.sendTardis(player, tardis);

        this.createForceSyncDelay(player);
    }

    @Override
    public void reset() {
        this.subscribers.clear();
        this.buffers.clear();
        super.reset();
    }
}
