package mdteam.ait.client.screens;

import com.google.common.collect.Lists;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.tardis.ClientTardisManager;
import mdteam.ait.tardis.Tardis;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TextReorderingProcessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.List;
import java.util.UUID;

import static mdteam.ait.client.renderers.exteriors.ExteriorEnum.*;
import static mdteam.ait.core.entities.control.impl.DimensionControl.convertWorldToReadable;

public class MonitorScreen extends TardisScreen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/borealis_monitor.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();

    int backgroundHeight = 128;
    int backgroundWidth = 256;
    public MonitorScreen(UUID tardis) {
        super(Text.translatable("screen." + AITMod.MOD_ID + ".monitor"), tardis);
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


    private void createButtons() {
        int i= (this.width - this.backgroundWidth / 2);
        int j = (this.height - this.backgroundHeight / 2);

        this.buttons.clear();

        // exterior change text button
        // fixme i think we're overloading with packets because the client side of all the code kinda desperately needs a redo / if you spam the button everything slows OR you get kicked
        this.addButton(new PressableTextWidget((width / 2 - 105), (height / 2 + 28), this.textRenderer.getWidth("exterior"), 10, Text.literal("exterior"), button -> TardisUtil.changeExteriorWithScreen(this.tardisId), this.textRenderer));

        this.buttons.forEach(buttons -> {
            // buttons.visible = false;
            buttons.active = true;
        });
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.push();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = ((this.height) - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        context.pop();
        //context.drawTexture(TEXTURE, i + 18, j + 67, 1, 87, 25, 8);
    }

    protected void drawTardisExterior(DrawContext context, int x, int y, float scale, float mouseX) {
        // testing @todo
        if (tardis() == null) return;

        ExteriorModel model = updateTardis().getExterior().getType().createModel();

        MatrixStack stack = context.getMatrices();

        // fixme is bad
        stack.push();
        stack.translate(x,tardis().getExterior().getType() != SHELTER ? tardis().getExterior().getType() == TOYOTA ? y + 12 : tardis().getExterior().getType() == BOOTH ? y + 4 : y : y + 24,100f);
        if(tardis().getExterior().getType() == TOYOTA) stack.scale(-9, 9, 9);
        else if(tardis().getExterior().getType() == BOOTH) stack.scale(-14f, 14f, 14f);
        else stack.scale(-scale, scale, scale);
        //stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-180f));
        stack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(mouseX));

        model.render(stack,context.getVertexConsumers().getBuffer(AITRenderLayers.getEntityTranslucentCull(model.getTexture())), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1,1,1,1);

        stack.pop();
    }

    protected void drawDestinationText(DrawContext context) {
        int i = ((this.height - this.backgroundHeight) / 2);
        int j = ((this.width - this.backgroundWidth) / 2);

        if (this.tardis() == null) return;

        AbsoluteBlockPos.Directed abpd = this.updateTardis().getTravel().getDestination();
        context.getMatrices().push();
        // context.getMatrices().scale(0.9f, 0.9f, 0.9f); // dont scale text it causes issues
        String destinationText = abpd.getX() + ", " +
                abpd.getY() + ", " +
                abpd.getZ() + " | " +
                convertWorldToReadable(abpd.getWorld()) + " | " +
                abpd.getDirection().toString().toUpperCase();
        context.drawText(this.textRenderer, Text.literal(destinationText), (width / 2 - 42), (height / 2 - 40), 0x46BFE3, true);
        context.getMatrices().pop();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context, delta, mouseX, mouseY);
        int i = ((this.height - this.backgroundHeight) / 2); // loqor make sure to use these so it stays consistent on different sized screens (kind of ??)
        int j = ((this.width - this.backgroundWidth) / 2);

        /*float yClamping = MathHelper.clamp(mouseY, i + 60, i + 65);
        float xClamping = mouseX <= 196 && mouseX >= 156 ? MathHelper.clamp((mouseX + this.backgroundWidth / 2), j + 10, j + 50) : 176;
        System.out.println((mouseX + this.backgroundWidth / 2) +  " min: " + (j + 10) + ", max: " + (j + 50) + ": is this clamped right = " + xClamping);*/

        // todo manually adjusting all these values are annoying me
        this.drawDestinationText(context);
        this.drawTardisExterior(context, (width / 2 - 85), (height / 2 - 8), 15f, 176);
        super.render(context, mouseX, mouseY, delta);
    }
}