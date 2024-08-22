package loqor.ait.tardis.data.distress;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import loqor.ait.core.item.DistressCallItem;
import loqor.ait.core.util.ServerLifecycleHooks;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.v2.TardisRef;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

public record DistressCall(TardisRef tardis, String message, int lifetime, int creationTime) {
    private static final int DEFAULT_LIFETIME = 60 * 20; // 5 minute default lifetime

    private static DistressCall create(UUID tardis, String message, MinecraftServer server, int lifetime) {
        return new DistressCall(TardisRef.createAs(server.getOverworld(), tardis), message, lifetime, server.getTicks());
    }
    private static DistressCall create(UUID tardis, String message, int lifetime) {
        return create(tardis, message, ServerLifecycleHooks.get(), lifetime);
    }
    private static DistressCall create(UUID tardis, String message) {
        return create(tardis, message, DEFAULT_LIFETIME);
    }
    public static DistressCall create(Tardis tardis, String message) {
        return create(tardis.getUuid(), message);
    }
    public static DistressCall create(NbtCompound data, int ticks, World world) {
        return new DistressCall(
                TardisRef.createAs(world, data.getUuid("Tardis")),
                data.getString("Message"),
                data.getInt("Lifetime"),
                Math.min(ticks, data.getInt("CreationTime")) // todo blehh
        );
    }

    public boolean isValid(int ticks) {
        return (this.creationTime + this.lifetime) > ticks;
    }
    private boolean isValid(MinecraftServer server) {
        return this.isValid(server.getTicks());
    }
    public boolean isValid() {
        return this.isValid(ServerLifecycleHooks.get());
    }
    public int getTimeLeft(int ticks) {
        int time = (this.creationTime + this.lifetime) - ticks;

        return Math.max(time, 0);
    }
    private int getTimeLeft(MinecraftServer server) {
        return this.getTimeLeft(server.getTicks());
    }
    public int getTimeLeft() {
        return this.getTimeLeft(ServerLifecycleHooks.get());
    }

    /**
     * sends this distress call to all tardises
     */
    public void send() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            ServerTardisManager.getInstance().forEach(this::send);

            return null;
        });
    }
    private void send(ServerTardis target) {
        if (this.isSource(target)) return; // dont send to self

        // spawn distress item at door
        ServerWorld world = (ServerWorld) TardisUtil.getTardisDimension();
        Vec3d pos = TardisUtil.offsetInteriorDoorPosition(target);

        ItemStack created = DistressCallItem.create(this);
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), created);

        world.spawnEntity(entity);
    }
    public boolean isSource(Tardis tardis) {
        return tardis.getUuid().equals(this.tardis().getId());
    }

    public NbtCompound toNbt() {
        NbtCompound data = new NbtCompound();

        data.putUuid("Tardis", this.tardis.getId());
        data.putString("Message", this.message);
        data.putInt("Lifetime", this.lifetime);
        data.putInt("CreationTime", this.creationTime);

        return data;
    }
}
