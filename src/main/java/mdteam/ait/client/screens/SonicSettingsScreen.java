package mdteam.ait.client.screens;

import com.google.common.collect.Lists;
import mdteam.ait.AITMod;
import mdteam.ait.core.item.SonicItem;
import mdteam.ait.tardis.data.SonicHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.List;
import java.util.UUID;

public class SonicSettingsScreen extends ConsoleScreen {
    private static final Identifier BACKGROUND = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/sonic_selection.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    int bgHeight = 126;
    int bgWidth = 201;
    int left, top;
    int choicesCount = 0;
    private final Screen parent;
    private float selectedSonic;
    private int tickForSpin = 0;

    public SonicSettingsScreen(UUID tardis, UUID console, Screen parent) {
        super(Text.translatable("screen.ait.sonicsettings.title"), tardis, console);
        this.parent = parent;
        updateTardis();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        //this.selectedSonic = tardis().getHandlers().getSonic().get(SonicHandler.HAS_CONSOLE_SONIC).getOrCreateNbt().getFloat(SonicItem.SONIC_TYPE);
        this.top = (this.height - this.bgHeight) / 2; // this means everythings centered and scaling, same for below
        this.left = (this.width - this.bgWidth) / 2;
        this.createButtons();

        super.init();
    }

    private void createButtons() {
        choicesCount = 0;
        this.buttons.clear();

        Text back = Text.translatable("screen.ait.interiorsettings.back").formatted(tardis().getHandlers().getSonic().hasSonic(SonicHandler.HAS_CONSOLE_SONIC) ? Formatting.WHITE : Formatting.GRAY);
        this.addButton(new PressableTextWidget((width / 2 - 105), (height / 2 - 66),
                this.textRenderer.getWidth(back), 10, back, button -> backToInteriorSettings(), this.textRenderer));
    }

    public void backToInteriorSettings() {
        if(!tardis().getHandlers().getSonic().hasSonic(SonicHandler.HAS_CONSOLE_SONIC)) return;
        MinecraftClient.getInstance().setScreen(this.parent);
    }


    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        button.active = true; // this whole method is unnecessary bc it defaults to true ( ?? ) - does it though?
        this.buttons.add((ButtonWidget) button);
    }

    private void createTextButton(Text text, ButtonWidget.PressAction onPress) {
        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.06f)),
                        (int) (top + (bgHeight * (0.1f * (choicesCount + 1)))),
                        this.textRenderer.getWidth(text),
                        10,
                        text,
                        onPress,
                        this.textRenderer
                )
        );

        choicesCount++;
    }

    @Override
    public void renderBackground(DrawContext context) {
        super.renderBackground(context);
    }

    protected void drawTardisExterior(DrawContext context, int x, int y, float scale, float mouseX, float delta) {
        tickForSpin++;
        if(!tardis().getHandlers().getSonic().hasSonic(SonicHandler.HAS_CONSOLE_SONIC)) {
            return;
        }
        ItemStack sonic = tardis().getHandlers().getSonic().get(SonicHandler.HAS_CONSOLE_SONIC);
        NbtCompound nbt = sonic.getOrCreateNbt();
        if(!nbt.contains(SonicItem.SONIC_TYPE)) {
            return;
        }
        if (getFromUUID(tardisId) != null) {
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 50f);
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    nbt.getString(SonicItem.SONIC_TYPE), (width / 2 - 54), (height / 2 + 41),
                    5636095);
            context.drawCenteredTextWithShadow(
                    this.textRenderer,
                    (nbt.getString(SonicItem.SONIC_TYPE)),
                    (width / 2 - 29), (height / 2 + 26),
                    0x00ffb3);
            context.getMatrices().pop();
            MatrixStack stack = context.getMatrices();
            stack.push();
            stack.translate(x, y, 0f);
            stack.scale(-scale, scale, scale);
            stack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(((float) tickForSpin / 1200L) * 360.0f));
            DiffuseLighting.disableGuiDepthLighting();
            MinecraftClient.getInstance().getItemRenderer().renderItem(sonic, ModelTransformationMode.GROUND, 0, OverlayTexture.DEFAULT_UV,
                    context.getMatrices(), context.getVertexConsumers(), MinecraftClient.getInstance().world, 0);
            DiffuseLighting.enableGuiDepthLighting();
            stack.pop();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context);
    }

    private void drawBackground(DrawContext context) {
        context.drawTexture(BACKGROUND, left, top, 0, 0, bgWidth, bgHeight);
    }
}
