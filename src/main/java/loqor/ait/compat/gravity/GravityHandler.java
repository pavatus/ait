package loqor.ait.compat.gravity;

import gravity_changer.EntityTags;
import gravity_changer.api.GravityChangerAPI;
import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.registry.impl.TardisComponentRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class GravityHandler extends KeyedTardisComponent implements TardisTickable {

    private static final Identifier SYNC = new Identifier(AITMod.MOD_ID, "sync_gravity");
    private static final Property<Direction> DIRECTION = Property.of(Property.Type.DIRECTION, "direction", Direction.DOWN);

    private final Value<Direction> direction = DIRECTION.create(this);

    public GravityHandler() {
        super(ID);
    }

    @Override
    public void onLoaded() {
        direction.of(this, DIRECTION);
    }

    @Override
    public void tick(MinecraftServer server) {
        if (server.getTicks() % 20 == 0)
            this.onTick();
    }

    private void onTick() {
        List<LivingEntity> list = TardisUtil.getLivingEntitiesInsideInterior(this.tardis, EntityTags::canChangeGravity);

        for (LivingEntity entity : list) {
            GravityChangerAPI.getGravityComponent(entity).applyGravityDirectionEffect(
                    this.direction.get(), null, 10
            );
        }
    }

    private static void syncToServer(Tardis tardis, Direction direction) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(tardis.getUuid());
        buf.writeEnumConstant(direction);

        ClientPlayNetworking.send(SYNC, buf);
    }

    private static Direction nextDirection(Direction direction) {
        Direction[] values = Direction.values();
        int size = values.length;

        int nextIndex = direction.ordinal() + 1;

        if (nextIndex == size)
            nextIndex = 0;

        return values[nextIndex];
    }

    public static void init() {
        AITMod.LOGGER.info("AIT - Setting up interior gravity");

        ServerPlayNetworking.registerGlobalReceiver(SYNC, (server, player, handler, buf, responseSender) -> {
            ServerTardisManager.getInstance().getTardis(server, buf.readUuid(), tardis -> {
                if (tardis == null)
                    return;

                GravityHandler gravity = tardis.handler(ID);
                gravity.direction.set(buf.readEnumConstant(Direction.class));
            });
        });

        TardisEvents.SETTINGS_SETUP.register(screen -> screen.createTextButton(Text.translatable("screen.ait.gravity"), button -> {
            GravityHandler gravity = screen.tardis().handler(ID);
            syncToServer(screen.tardis(), nextDirection(gravity.direction.get()));
        }));

        TardisComponentRegistry.getInstance().register(ID);
    }

    static TardisComponent.IdLike ID = new AbstractId<>("GRAVITY", GravityHandler::new, GravityHandler.class);
}
