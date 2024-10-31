package loqor.ait.client.screens.interior;

import static loqor.ait.core.tardis.handler.InteriorChangingHandler.CHANGE_DESKTOP;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import loqor.ait.AITMod;
import loqor.ait.api.TardisClientEvents;
import loqor.ait.api.TardisComponent;
import loqor.ait.client.screens.ConsoleScreen;
import loqor.ait.client.screens.SonicSettingsScreen;
import loqor.ait.client.screens.TardisSecurityScreen;
import loqor.ait.client.sounds.ClientSoundManager;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.tardis.TardisDesktop;
import loqor.ait.core.tardis.handler.FuelHandler;
import loqor.ait.core.tardis.handler.ServerHumHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.data.HumSound;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.HumsRegistry;

@Environment(EnvType.CLIENT)
public class InteriorSettingsScreen extends ConsoleScreen {
    private static final Identifier BACKGROUND = new Identifier(AITMod.MOD_ID,
            "textures/gui/tardis/new_interior_settings.png");
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            "textures/gui/tardis/new_interior_settings.png");
    private static final Identifier MISSING_PREVIEW = new Identifier(AITMod.MOD_ID,
            "textures/gui/tardis/desktop/missing_preview.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    int bgHeight = 166;
    int bgWidth = 256;
    int left, top;
    private int tickForSpin = 0;
    public int choicesCount = 0;
    private HumSound hum;
    private final Screen parent;
    private TardisDesktopSchema selectedDesktop;

    public InteriorSettingsScreen(ClientTardis tardis, BlockPos console, Screen parent) {
        super(Text.translatable("screen.ait.interiorsettings.title"), tardis, console);

        this.parent = parent;
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
        if (this.console == null)
            return;

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(this.tardis().getUuid());
        buf.writeBlockPos(this.console);

        ClientPlayNetworking.send(TardisDesktop.CACHE_CONSOLE, buf);
        this.close();
    }

    private void createCompatButtons() {
    }

    private void createButtons() {
        choicesCount = 0;
        this.buttons.clear();

        createTextButton(Text.translatable("screen.ait.interiorsettings.back"),
                (button -> backToExteriorChangeScreen()));
        createTextButton(Text.translatable("screen.ait.interiorsettings.cacheconsole")
                .formatted(this.console != null ? Formatting.WHITE : Formatting.GRAY), button -> sendCachePacket());
        createTextButton(Text.translatable("screen.ait.security.button"), (button -> toSecurityScreen()));
        createTextButton(Text.translatable("screen.ait.sonic.button")
                .formatted(tardis().sonic().getConsoleSonic() != null ? Formatting.WHITE : Formatting.GRAY), button -> {
                    if (tardis().sonic().getConsoleSonic() != null)
                        toSonicScreen();
                });

        this.createCompatButtons();
        TardisClientEvents.SETTINGS_SETUP.invoker().onSetup(this);

        this.addButton(new PressableTextWidget((int) (left + (bgWidth * 0.59f)), (int) (top + (bgHeight * 0.885)),
                this.textRenderer.getWidth("<"), 10, Text.literal(""), button -> previousHum(), this.textRenderer));
        this.addButton(new PressableTextWidget((int) (left + (bgWidth * 0.93f)), (int) (top + (bgHeight * 0.885f)),
                this.textRenderer.getWidth(">"), 10, Text.literal(""), button -> nextHum(), this.textRenderer));
        Text applyHumText = Text.literal("AP");
        this.addButton(new PressableTextWidget(
                (int) (left + (bgWidth * 0.928f)) - (this.textRenderer.getWidth(applyHumText) / 2),
                (int) (top + (bgHeight * 0.695f)), this.textRenderer.getWidth(applyHumText), 10, Text.literal(""),
                button -> applyHum(), this.textRenderer));
        this.addButton(new PressableTextWidget((left + 151), (top + 93), this.textRenderer.getWidth("<"), 10,
                Text.literal(""), button -> {
                    previousDesktop();
                }, this.textRenderer));
        this.addButton(new PressableTextWidget((left + 238), (top + 93), this.textRenderer.getWidth(">"), 10,
                Text.literal(""), button -> {
                    nextDesktop();
                }, this.textRenderer));
        Text applyInteriorText = Text.translatable("screen.ait.monitor.apply");
        this.addButton(new PressableTextWidget(
                (int) (left + (bgWidth * 0.77f)) - (this.textRenderer.getWidth(applyInteriorText) / 2),
                (int) (top + (bgHeight * 0.108f)), this.textRenderer.getWidth(applyInteriorText), 5, Text.literal(""),
                button -> applyDesktop(), this.textRenderer));
    }

    private void toSonicScreen() {
        MinecraftClient.getInstance().setScreen(new SonicSettingsScreen(this.tardis(), this.console, this));
    }

    public <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        button.active = true; // this whole method is unnecessary bc it defaults to true ( ?? )
        this.buttons.add((ButtonWidget) button);
    }

    // this might be useful, so remember this exists and use it later on ( although
    // its giving NTM
    // vibes.. )
    public PressableTextWidget createTextButton(Text text, ButtonWidget.PressAction onPress) {
        return this.createAnyButton(text, PressableTextWidget::new, onPress);
    }

    public <T extends ButtonWidget> T initAnyButton(Text text, ButtonCreator<T> creator,
            ButtonWidget.PressAction onPress) {
        return creator.create((int) (left + (bgWidth * 0.06f)), (int) (top + (bgHeight * (0.1f * (choicesCount + 1)))),
                this.textRenderer.getWidth(text), 10, text, onPress, this.textRenderer);
    }

    public <T extends ButtonWidget> T initAnyDynamicButton(Function<T, Text> text, DynamicButtonCreator<T> creator,
            ButtonWidget.PressAction onPress) {
        return creator.create((int) (left + (bgWidth * 0.06f)), (int) (top + (bgHeight * (0.1f * (choicesCount + 1)))),
                this.textRenderer.getWidth(Text.empty()), 10, text, onPress, this.textRenderer);
    }

    public <T extends ButtonWidget> T createAnyButton(Text text, ButtonCreator<T> creator,
            ButtonWidget.PressAction onPress) {
        T result = this.initAnyButton(text, creator, onPress);

        this.addButton(result);
        choicesCount++;

        return result;
    }

    public <T extends ButtonWidget> T createAnyDynamicButton(Function<T, Text> text, DynamicButtonCreator<T> creator,
            ButtonWidget.PressAction onPress) {
        T result = this.initAnyDynamicButton(text, creator, onPress);

        this.addButton(result);
        choicesCount++;

        return result;
    }

    public void backToExteriorChangeScreen() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    public void toSecurityScreen() {
        MinecraftClient.getInstance().setScreen(new TardisSecurityScreen(tardis(), this.console, this));
    }

    final int UV_BASE = 159;
    final int UV_INCREMENT = 17;

    int calculateUvOffsetForRange(int progress) {
        int rangeProgress = progress % 20;
        return (rangeProgress / 5) * UV_INCREMENT;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderDesktop(context);
        this.drawBackground(context); // the grey backdrop
        context.getMatrices().push();
        int x = (left + 79);
        int y = (top + 59);
        int i = (this.width - this.bgWidth) / 2;
        int j = ((this.height) - this.bgHeight) / 2;
        context.drawTexture(BACKGROUND, x - 41, y - 41, 173, 173, 83, 83);
        context.getMatrices().pop();

        // FIXME @Loqor, this is dumb.
        int startIndex = DependencyChecker.hasGravity() ? 5 : 4;

        // Hum Selector
        if (!this.buttons.get(startIndex).isHovered())
            context.drawTexture(BACKGROUND, left + 151, top + 145, 93, 166, 20, 12);
        else
            context.drawTexture(TEXTURE, left + 151, top + 145, 93, 178, 20,
                    12);

        if (!this.buttons.get(startIndex + 2).isHovered())
            context.drawTexture(BACKGROUND, left + 226, top + 145, 113, 166, 20, 12);
        else
            context.drawTexture(TEXTURE, left + 226, top + 145, 133, 178, 11,
                    12);

        // Apply Button
        if (!this.buttons.get(startIndex + 1).isHovered())
            context.drawTexture(BACKGROUND, left + 172, top + 145, 133, 166, 53, 12);
        else
            context.drawTexture(TEXTURE, left + 172, top + 145, 133, 176, 53,
                    12);


        // Interior Selector
        if (!this.buttons.get(startIndex + 3).isHovered())
            context.drawTexture(TEXTURE, left + 151, top + 86, 0, 166, 20,
                    20);
        else
            context.drawTexture(TEXTURE, left + 151, top + 86, 0, 186, 20,
                    20);
        if (!this.buttons.get(startIndex + 4).isHovered())
            context.drawTexture(TEXTURE, left + 226, top + 86, 20, 166, 20,
                    20);
        else
            context.drawTexture(TEXTURE, left + 226, top + 86, 20, 186, 20,
                    20);

        // Interior Apply
        if (!this.buttons.get(startIndex + 5).isHovered())
            context.drawTexture(TEXTURE, left + 172, top + 86, 40, 166, 53,
                    20);
        else
            context.drawTexture(TEXTURE, left + 172, top + 86, 40, 186, 53,
                    20);

        if (tardis() == null)
            return;

        // Fuel
        context.drawTexture(TEXTURE, i + 30, j + 144, 0,
                this.tardis().getFuel() > (FuelHandler.TARDIS_MAX_FUEL / 4) ? 225 : 234,
                (int) (85 * this.tardis().getFuel() / FuelHandler.TARDIS_MAX_FUEL), 9);

        // Progress Bar
        int progress = this.tardis().travel().getDurationAsPercentage();

        for (int index = 0; index < 5; index++) {
            int rangeStart = index * 19;
            int rangeEnd = (index + 1) * 19;

            int uvOffset;
            if (progress >= rangeStart && progress <= rangeEnd) {
                uvOffset = calculateUvOffsetForRange(progress);
            } else if (progress >= rangeEnd) {
                uvOffset = 76;
            } else {
                uvOffset = UV_BASE;
            }

            context.drawTexture(TEXTURE, i + 25 + (index * 19), j + 113,
                    this.tardis().travel().getState() == TravelHandlerBase.State.FLIGHT
                            ? progress >= 100 ? 76 : uvOffset
                            : UV_BASE,
                    206, 19, 19);
        }

        this.renderHums(context);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawBackground(DrawContext context) {
        context.drawTexture(BACKGROUND, left, top, 0, 0, bgWidth, bgHeight);
    }

    private void renderDesktop(DrawContext context) {
        if (this.selectedDesktop == null)
            return;

        context.drawCenteredTextWithShadow(this.textRenderer, this.selectedDesktop.name(),
                (int) (left + (bgWidth * 0.77f)), (int) (top + (bgHeight * 0.58f)), 0xffffff);

        context.getMatrices().push();
        context.getMatrices().translate(0, 0, -50f);
        context.drawTexture(
                doesTextureExist(this.selectedDesktop.previewTexture().texture())
                        ? this.selectedDesktop.previewTexture().texture()
                        : MISSING_PREVIEW,
                left + 151, top + 14, 91, 91, 0, 0, this.selectedDesktop.previewTexture().width * 2,
                this.selectedDesktop.previewTexture().height * 2, this.selectedDesktop.previewTexture().width * 2,
                this.selectedDesktop.previewTexture().height * 2);

        context.getMatrices().pop();
    }

    private HumSound getHumSound() {
        return tardis().<ServerHumHandler>handler(TardisComponent.Id.HUM).getHum();
    }

    private void applyHum() {
        ClientSoundManager.getHum().setServersHum(this.tardis(), this.hum);
        MinecraftClient.getInstance().setScreen(null);
    }

    private void nextHum() {
        this.hum = nextHum(this.hum);
    }

    private static HumSound nextHum(HumSound current) {
        List<HumSound> list = HumsRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(current);
        if (idx < 0 || idx + 1 == list.size())
            return list.get(0);
        return list.get(idx + 1);
    }

    private void previousHum() {
        this.hum = previousHum(this.hum);
    }

    private static HumSound previousHum(HumSound current) {
        List<HumSound> list = HumsRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(current);
        if (idx <= 0)
            return list.get(list.size() - 1);
        return list.get(idx - 1);
    }

    private void renderHums(DrawContext context) {
        if (getHumSound() == null)
            return;
        Text humsText = Text.translatable("screen.ait.interior.settings.hum");
        context.drawText(this.textRenderer, humsText,
                (int) (left + (bgWidth * 0.65f)) - this.textRenderer.getWidth(humsText) / 2,
                (int) (top + (bgHeight * 0.7f)), 0xffffff, true);
        Text hum = Text.translatable("screen.ait.interior.settings." + this.hum.name());
        context.drawText(this.textRenderer, hum, (int) (left + (bgWidth * 0.76f)) - this.textRenderer.getWidth(hum) / 2,
                (int) (top + (bgHeight * 0.82f)), 0xffffff, true);
    }

    private void applyDesktop() {
        if (this.selectedDesktop == null)
            return;

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(tardis().getUuid());
        buf.writeIdentifier(this.selectedDesktop.id());

        ClientPlayNetworking.send(CHANGE_DESKTOP, buf);

        MinecraftClient.getInstance().setScreen(null);
    }

    private static TardisDesktopSchema nextDesktop(TardisDesktopSchema current) {
        List<TardisDesktopSchema> list = DesktopRegistry.getInstance().toList();

        int idx = list.indexOf(current);
        if (idx < 0 || idx + 1 == list.size())
            return list.get(0);
        return list.get(idx + 1);
    }

    private void nextDesktop() {
        this.selectedDesktop = nextDesktop(this.selectedDesktop);

        if (!isCurrentUnlocked())
            nextDesktop(); // ooo incursion crash
    }

    private static TardisDesktopSchema previousDesktop(TardisDesktopSchema current) {
        List<TardisDesktopSchema> list = DesktopRegistry.getInstance().toList();

        int idx = list.indexOf(current);
        if (idx <= 0)
            return list.get(list.size() - 1);
        return list.get(idx - 1);
    }

    private void previousDesktop() {
        this.selectedDesktop = previousDesktop(this.selectedDesktop);

        if (!isCurrentUnlocked())
            previousDesktop(); // ooo incursion crash
    }

    private boolean doesTextureExist(Identifier id) {
        return MinecraftClient.getInstance().getResourceManager().getResource(id).isPresent();
    }

    private boolean isCurrentUnlocked() {
        return this.tardis().isUnlocked(this.selectedDesktop);
    }

    @FunctionalInterface
    public interface ButtonCreator<T extends ButtonWidget> {
        T create(int x, int y, int width, int height, Text text, ButtonWidget.PressAction onPress,
                TextRenderer textRenderer);
    }

    @FunctionalInterface
    public interface DynamicButtonCreator<T extends ButtonWidget> {
        T create(int x, int y, int width, int height, Function<T, Text> text, ButtonWidget.PressAction onPress,
                TextRenderer textRenderer);
    }
}
