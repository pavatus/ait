package mdteam.ait.client.screens;

import com.google.common.collect.Lists;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.tardis.ClientTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.wrapper.client.ClientTardis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static mdteam.ait.client.renderers.exteriors.ExteriorEnum.*;
import static mdteam.ait.core.entities.control.impl.DimensionControl.convertWorldToReadable;

public class MonitorScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/borealis_monitor.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();

    int backgroundHeight = 86;
    int backgroundWidth = 176;
    UUID tardisId;

    public MonitorScreen(Text title, UUID tardis) {
        super(title);
        this.tardisId = tardis;
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
        this.buttons.clear();
        int i= (this.width - this.backgroundWidth / 2);
        int j = (this.height - this.backgroundHeight / 2);
        this.addButton(new ButtonWidget.Builder(Text.literal("<>"), button -> TardisUtil.changeExteriorWithScreen(this.tardisId)).dimensions(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20).build());
        this.buttons.forEach(buttons -> {
            buttons.active = true;
        });
    }

    private Tardis tardis() {
        return ClientTardisManager.getInstance().getLookup().get(tardisId);
    }

    private Tardis updatedTardis() {
        ClientTardisManager.getInstance().ask(this.tardisId);
        return tardis();
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = ((this.height) - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        //context.drawTexture(TEXTURE, i + 18, j + 67, 1, 87, 25, 8);
    }

    protected void drawTardisExterior(DrawContext context, int x, int y, float scale, float mouseX) {
        // testing @todo
        if (tardis() == null) return;

        ExteriorModel model = updatedTardis().getExterior().getType().createModel();

        MatrixStack stack = context.getMatrices();

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

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context, delta, mouseX, mouseY);
        int i = ((this.height - this.backgroundHeight) / 2);
        int j = ((this.width - this.backgroundWidth) / 2);
        /*float yClamping = MathHelper.clamp(mouseY, i + 60, i + 65);
        float xClamping = mouseX <= 196 && mouseX >= 156 ? MathHelper.clamp((mouseX + this.backgroundWidth / 2), j + 10, j + 50) : 176;
        System.out.println((mouseX + this.backgroundWidth / 2) +  " min: " + (j + 10) + ", max: " + (j + 50) + ": is this clamped right = " + xClamping);*/
        AbsoluteBlockPos.Directed abpd = this.tardis().getTravel().getDestination();
        context.getMatrices().push();
        context.getMatrices().scale(0.9f, 0.9f, 0.9f);
        String destinationText = abpd.getX() + ", " +
                abpd.getY() + ", " +
                abpd.getZ() + " | " +
                convertWorldToReadable(abpd.getWorld()) + " | " +
                abpd.getDirection().toString().toUpperCase();
        context.drawText(this.textRenderer, Text.literal(destinationText), ((width / 2 - destinationText.length()) - 10), (height / 2 - 20), 0x46BFE3, true);
        context.getMatrices().pop();
        this.drawTardisExterior(context, (width / 2 - 57), (height / 2 - 8), 15f, 176);
        super.render(context, mouseX, mouseY, delta);
    }
}