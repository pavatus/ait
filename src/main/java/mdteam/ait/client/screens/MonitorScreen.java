package mdteam.ait.client.screens;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.tardis.ClientTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import org.joml.Vector3f;

import java.util.UUID;

public class MonitorScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/borealis_monitor.png");

    int backgroundHeight = 176;
    int backgroundWidth = 88 * 2;
    UUID tardisId;

    public MonitorScreen(Text title, UUID tardis) {
        super(title);
        this.tardisId = tardis;
    }

    @Override
    protected void init() {
        super.init();
    }

    private Tardis tardis() {
        return ClientTardisManager.getInstance().getLookup().get(tardisId);
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = ((this.height) - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    protected void drawTardisExterior(DrawContext context, int x, int y, float scale) {
        // testing @todo
        if (tardis() == null) return;

        ExteriorModel model = tardis().getExterior().getType().createModel();

        MatrixStack stack = context.getMatrices();

        stack.push();
        stack.translate(x,y,100f);
        stack.scale(-scale, scale, scale);
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-180f));

        model.render(stack,context.getVertexConsumers().getBuffer(AITRenderLayers.getEntityTranslucentCull(model.getTexture())), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1,1,1,1);

        stack.pop();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context, delta, mouseX, mouseY);
        this.drawTardisExterior(context, (width / 2 - 57), (height / 2 - 52), 15f);
        super.render(context, mouseX, mouseY, delta);
    }
}