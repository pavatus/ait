package mdteam.ait.client.screens;

import com.google.common.collect.Lists;
import io.wispforest.owo.ui.component.SliderComponent;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.JigsawBlockScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static mdteam.ait.client.renderers.exteriors.ExteriorEnum.*;
import static mdteam.ait.tardis.control.impl.DimensionControl.convertWorldToReadable;

public class MonitorScreen extends TardisScreen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/exterior_changer.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    private ExteriorEnum currentModel;
    private float scrollPosition;
    private boolean scrollbarClicked;
    private int visibleTopRow;
    int backgroundHeight = 133;
    int backgroundWidth = 236;
    public MonitorScreen(UUID tardis) {
        super(Text.translatable("screen." + AITMod.MOD_ID + ".monitor"), tardis);
        if(tardis() != null)
            this.currentModel = tardis().getExterior().getType();
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add((ButtonWidget)button);
    }
    @Override
    protected void init() {
        super.init();
        this.createButtons();
    }
    public ExteriorEnum getCurrentModel() {
        return currentModel;
    }
    public void setCurrentModel(ExteriorEnum currentModel) {
        this.currentModel = currentModel;
    }
    private void createButtons() {
        int i = (this.width - this.backgroundWidth / 2);
        int j = (this.height - this.backgroundHeight / 2);
        this.buttons.clear();
        // exterior change text button
        // fixme i think we're overloading with packets because the client side of all the code kinda desperately needs a redo / if you spam the button everything slows OR you get kicked
        this.addButton(new PressableTextWidget((width / 2 - 103), (height / 2 + 12),
                this.textRenderer.getWidth("Apply"), 10, Text.literal("Apply"), button -> {
            sendExteriorPacket();
            }, this.textRenderer));
        this.addButton(new PressableTextWidget((width / 2 - 110), (height / 2 - 24),
                this.textRenderer.getWidth("<"), 10, Text.literal("<"), button -> {
            exteriorBack();
        }, this.textRenderer));
        this.addButton(new PressableTextWidget((width / 2 - 76), (height / 2 - 24),
                this.textRenderer.getWidth(">"), 10, Text.literal(">"), button -> {
            exteriorForward();
        }, this.textRenderer));
        this.buttons.forEach(buttons -> {
            // buttons.visible = false;
            buttons.active = true;
        });
    }
    public void sendExteriorPacket() {
        if(this.getCurrentModel() != tardis().getExterior().getType())
            TardisUtil.changeExteriorWithScreen(this.tardisId, this.getCurrentModel().ordinal());
    }
    public void exteriorBack() {
        ExteriorEnum setBack = ExteriorEnum.values()[Math.abs(this.getCurrentModel().ordinal() - 1) % values().length];
        this.setCurrentModel(setBack);
    }
    public void exteriorForward() {
        ExteriorEnum setForward = ExteriorEnum.values()[Math.abs(this.getCurrentModel().ordinal() + 1) % values().length];
        this.setCurrentModel(setForward);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        /*if (this.scrollbarClicked) {
            int j = ((this.height - this.backgroundHeight) / 2) + 13;
            int k = j + 56;
            this.scrollPosition = ((float)mouseY - (float)j - 7.5f) / ((float)(k - j) - 15.0f);
            this.scrollPosition = MathHelper.clamp(this.scrollPosition, 0.0f, 1.0f);
            this.visibleTopRow = Math.max((int)((double)(this.scrollPosition * (float)2) + 0.5), 0);
            return true;
        }*/
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        /*float f = (float)amount / (float)2;
        this.scrollPosition = MathHelper.clamp(this.scrollPosition - f, 0.0f, 1.0f);
        this.visibleTopRow = Math.max((int)(this.scrollPosition * (float)2 + 0.5f), 0);
        return true;*/
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    /*@Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        return mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
    }*/
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = ((this.height) - this.backgroundHeight) / 2;
        context.push();
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        // Jewels
        for (int l = 0; l < 7; l++) {
            context.drawTexture(TEXTURE, (i + 11) + (l * 9), j + 5, 20 + (l * 6), 133, 6, 6);
        }
        // Slider
        context.drawTexture(TEXTURE, i + 215, j + 21, 20, 145, 12, 15);
        // Panes
        for(int k = 0; k < 8; k++) {
            context.drawTexture(TEXTURE, (i + 51) + (k * 20), j + 21, 0, 133, 20, 40);
            context.drawTexture(TEXTURE, (i + 51) + (k * 20), j + 61, 0, 133, 20, 40);
        }
        context.pop();
        //context.drawTexture(TEXTURE, i + 18, j + 67, 1, 87, 25, 8);
    }
    protected void drawTardisExterior(DrawContext context, int x, int y, float scale, float mouseX) {
        // testing @todo
        ExteriorModel model = this.getCurrentModel().createModel();
        MatrixStack stack = context.getMatrices();
        // fixme is bad
        stack.push();
        stack.translate(x,this.getCurrentModel() != SHELTER ? this.getCurrentModel() == TOYOTA ? y + 8 : y : y + 23,100f);
        if(this.getCurrentModel() == TOYOTA) stack.scale(-10, 10, 10);
        else if(this.getCurrentModel() == BOOTH) stack.scale(-scale, scale, scale);
        else stack.scale(-scale, scale, scale);
        //stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-180f));
        stack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(mouseX));
        DiffuseLighting.disableGuiDepthLighting();
        model.render(stack,context.getVertexConsumers().getBuffer(AITRenderLayers.getEntityTranslucentCull(model.getTexture())), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1,1,1,1);
        DiffuseLighting.enableGuiDepthLighting();
        stack.pop();
    }
    @Override
    public void renderBackground(DrawContext context) {
        super.renderBackground(context);
    }
    protected void drawDestinationText(DrawContext context) {
        int i = ((this.height - this.backgroundHeight) / 2); // loqor make sure to use these so it stays consistent on different sized screens (kind of ??)
        int j = ((this.width - this.backgroundWidth) / 2);
        if (this.tardis() == null) return;
        AbsoluteBlockPos.Directed abpd = this.updateTardis().getTravel().getDestination();
        String destinationText = "> " + abpd.getX() + ", " + abpd.getY() + ", " + abpd.getZ();
        String dimensionText = "> " + convertWorldToReadable(abpd.getWorld());
        String directionText = "> " + abpd.getDirection().toString().toUpperCase();
        context.drawText(this.textRenderer, Text.literal(destinationText), (width / 2 - 67), (height / 2 + 38), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(dimensionText), (width / 2 - 19), (height / 2 + 48), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(directionText), (width / 2 - 67), (height / 2 + 48), 0xFFFFFF, true);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = ((this.height - this.backgroundHeight) / 2); // loqor make sure to use these so it stays consistent on different sized screens (kind of ??)
        int j = ((this.width - this.backgroundWidth) / 2);
        this.drawBackground(context, delta, mouseX, mouseY);
        /*float yClamping = MathHelper.clamp(mouseY, i + 60, i + 65);
        float xClamping = mouseX <= 196 && mouseX >= 156 ? MathHelper.clamp((mouseX + this.backgroundWidth / 2), j + 10, j + 50) : 176;
        System.out.println((mouseX + this.backgroundWidth / 2) +  " min: " + (j + 10) + ", max: " + (j + 50) + ": is this clamped right = " + xClamping);*/
        // todo manually adjusting all these values are annoying me
        this.drawTardisExterior(context, (width / 2 - 91), (height / 2 - 19), 15f, 176);
        this.drawDestinationText(context);
        super.render(context, mouseX, mouseY, delta);
    }
}