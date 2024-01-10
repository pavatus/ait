package mdteam.ait.client.screens.interior;

import mdteam.ait.AITMod;
import mdteam.ait.client.AITModClient;
import mdteam.ait.client.screens.TardisScreen;
import mdteam.ait.client.sounds.ClientSoundManager;
import mdteam.ait.client.sounds.hum.ClientHumHandler;
import mdteam.ait.core.AITMessages;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.registry.HumsRegistry;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.sound.HumSound;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

import static mdteam.ait.tardis.handler.InteriorChangingHandler.CHANGE_DESKTOP;

public class InteriorSettingsScreen extends TardisScreen {
    private static final Identifier BACKGROUND = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/interior_settings.png");
    int bgHeight = 166;
    int bgWidth = 256;
    int left, top;
    int choicesCount = 0;
    private HumSound hum;
    private final Screen parent;

    private TardisDesktopSchema selectedDesktop;

    // loqor DONT rewrite with owo lib : (
    public InteriorSettingsScreen(UUID tardis, Screen parent) {
        super(Text.translatable("screen.ait.interiorsettings.title"), tardis);
        this.parent = parent;
        updateTardis();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        this.selectedDesktop = tardis().getDesktop().getSchema();
        this.hum = getHumSound();
        this.top = (this.height - this.bgHeight) / 2; // this means everythings centered and scaling, same for below
        this.left = (this.width - this.bgWidth) / 2;
        this.createButtons();

        super.init();
    }

    private void sendCachePacket() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(this.tardis().getUuid());
        ClientPlayNetworking.send(TardisDesktop.CACHE_CONSOLE, buf);
        this.close();
    }

    private void createButtons() {
        choicesCount = 0;

        createTextButton(Text.translatable("screen.ait.interiorsettings.back"),(button -> backToExteriorChangeScreen()));
        createTextButton(Text.translatable("screen.ait.interiorsettings.changeinterior"), (button -> toSelectInteriorScreen()));
        createTextButton(Text.translatable("screen.ait.interiorsettings.cacheconsole"), (button -> sendCachePacket()));

        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.09f)),
                        (int) (top + (bgHeight * 0.6)),
                        this.textRenderer.getWidth("<"),
                        10,
                        Text.literal("<"),
                        button -> previousHum(),
                        this.textRenderer
                )
        );
        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.3f)),
                        (int) (top + (bgHeight * 0.6f)),
                        this.textRenderer.getWidth(">"),
                        10,
                        Text.literal(">"),
                        button -> nextHum(),
                        this.textRenderer
                )
        );

        Text applyText = Text.translatable("screen.ait.monitor.apply");
        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.2f)) - (this.textRenderer.getWidth(applyText) / 2),
                        (int) (top + (bgHeight * 0.7f)),
                        this.textRenderer.getWidth(applyText),
                        10,
                        applyText,
                        button -> applyHum(),
                        this.textRenderer
                )
        );
    }

    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        button.active = true; // this whole method is unnecessary bc it defaults to true ( ?? )
    }

    // this might be useful, so remember this exists and use it later on ( although its giving NTM vibes.. )
    private void createTextButton(Text text, ButtonWidget.PressAction onPress) {
        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.03f)),
                        (int) (top + (bgHeight * (0.06f * (choicesCount + 1)))),
                        this.textRenderer.getWidth(text),
                        10,
                        text,
                        onPress,
                        this.textRenderer
                )
        );

        choicesCount++;
    }

    public void backToExteriorChangeScreen() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }
    public void toSelectInteriorScreen() {
        MinecraftClient.getInstance().setScreen(new OwOInteriorSelectScreen(tardis().getUuid(), this));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context); // the grey backdrop
        this.renderDesktop(context);
        this.renderHums(context);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawBackground(DrawContext context) {
        context.drawTexture(BACKGROUND, left, top, 0, 0, bgWidth, bgHeight);
    }

    private void renderDesktop(DrawContext context) {
        // context.drawCenteredTextWithShadow(
        //         this.textRenderer,
        //         this.selectedDesktop.name(),
        //         (int) (left + (bgWidth * 0.5f)),
        //         (int) (top + (bgHeight * 0.85f)),
        //         0xadcaf7
        // );

        if(this.selectedDesktop == null) return;

        context.drawTexture(this.selectedDesktop.previewTexture().texture(), left + 119, top + 18, 128, 128, 0, 0, this.selectedDesktop.previewTexture().width * 2, this.selectedDesktop.previewTexture().height * 2, this.selectedDesktop.previewTexture().width * 2, this.selectedDesktop.previewTexture().height * 2);
    }

    private HumSound getHumSound() {
        return tardis().getHandlers().getHum().getHum();
    }

    private void applyHum() {
        ClientSoundManager.getHum().setServersHum(this.hum);
        MinecraftClient.getInstance().setScreen(null);
    }
    private void nextHum() {
        this.hum = nextHum(this.hum);
    }

    private static HumSound nextHum(HumSound current) {
        List<HumSound> list = HumsRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(current);
        if (idx < 0 || idx + 1 == list.size()) return list.get(0);
        return list.get(idx + 1);
    }

    private void previousHum() {
        this.hum = previousHum(this.hum);
    }
    private static HumSound previousHum(HumSound current) {
        List<HumSound> list = HumsRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(current);
        if (idx <= 0) return list.get(list.size() - 1);
        return list.get(idx - 1);
    }

    private void renderHums(DrawContext context) {
        if (getHumSound() == null) return;
        Text humsText = Text.translatable("screen.ait.interior.settings.hum");
        context.drawText(
                this.textRenderer,
                humsText,
                (int) (left + (bgWidth * 0.2f)) - this.textRenderer.getWidth(humsText) / 2,
                (int) (top + (bgHeight * 0.5f)),
                0xbad7ff,
                true
        );
        Text hum = Text.translatable("screen.ait.interior.settings." + this.hum.name());
        //System.out.println(this.hum.name());
        context.drawText(
                this.textRenderer,
                hum,
                (int) (left + (bgWidth * 0.2f)) - this.textRenderer.getWidth(hum) / 2,
                (int) (top + (bgHeight * 0.6f)),
                0xadcaf7,
                true
        );
    }
}