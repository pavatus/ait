package loqor.ait.compat.gravity;

import gravity_changer.EntityTags;
import gravity_changer.api.GravityChangerAPI;
import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisClientEvents;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.client.screens.interior.InteriorSettingsScreen;
import loqor.ait.client.screens.widget.DynamicPressableTextWidget;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.entities.BaseControlEntity;
import loqor.ait.registry.impl.TardisComponentRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;
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
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class GravityHandler extends KeyedTardisComponent implements TardisTickable {

    private static final Identifier SYNC = new Identifier(AITMod.MOD_ID, "sync_gravity");
    private static final Property<Direction> DIRECTION = new Property<>(Property.Type.DIRECTION, "direction", Direction.DOWN);

    private final Value<Direction> direction = DIRECTION.create(this);
    @Exclude private Direction tempDirection = Direction.DOWN;

    public GravityHandler() {
        super(ID);
    }

    @Override
    public void onLoaded() {
        direction.of(this, DIRECTION);
        this.tempDirection = Direction.DOWN;
    }

    @Override
    public void tick(MinecraftServer server) {
        if (server.getTicks() % 20 == 0)
            this.onTick();
    }

    private void onTick() {
        List<LivingEntity> list = TardisUtil.getEntitiesInsideInterior(this.tardis, entity ->
                !(entity instanceof BaseControlEntity) && EntityTags.canChangeGravity(entity));

        for (LivingEntity entity : list) {
            GravityChangerAPI.getGravityComponent(entity).setBaseGravityDirection(this.direction.get());
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
                Direction direction = buf.readEnumConstant(Direction.class);

                gravity.direction.set(direction);
            });
        });

        TardisComponentRegistry.getInstance().register(ID);

        TardisEvents.LEAVE_TARDIS.register((tardis, entity) -> GravityChangerAPI.getGravityComponent(entity)
                .setBaseGravityDirection(Direction.DOWN));
    }

    public static void clientInit() {
        TardisClientEvents.SETTINGS_SETUP.register(GravityHandler::setup);
    }

    private static void setup(InteriorSettingsScreen screen){
        screen.<DynamicPressableTextWidget>createAnyDynamicButton(button -> buttonText(screen, button),
                (x, y, width, height, text, onPress, textRenderer) -> new DynamicPressableTextWidget(
                        x, y, 80, height, text, onPress, textRenderer
                ), button -> onButton(screen, (DynamicPressableTextWidget) button));
    }

    private static Text buttonText(InteriorSettingsScreen screen, DynamicPressableTextWidget button) {
        GravityHandler gravity = screen.tardis().handler(ID);
        boolean isChanged = !button.isLeftClick() || gravity.tempDirection != gravity.direction.get();

        Direction direction = isChanged ? gravity.tempDirection : gravity.direction.get();
        Formatting formatting = isChanged ? Formatting.WHITE : Formatting.YELLOW;

        return Text.translatable("screen.ait.gravity",
                capitalize(direction.getName())).formatted(formatting);
    }

    private static void onButton(InteriorSettingsScreen screen, DynamicPressableTextWidget button) {
        Tardis tardis = screen.tardis();
        GravityHandler gravity = tardis.handler(GravityHandler.ID);

        if (button.isLeftClick()) {
            gravity.direction.set(gravity.tempDirection, false);
            button.refresh();

            syncToServer(tardis, gravity.tempDirection);
            return;
        }

        gravity.tempDirection = nextDirection(gravity.tempDirection);
        button.refresh();
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    static final IdLike ID = new AbstractId<>("GRAVITY", GravityHandler::new, GravityHandler.class);
}
