package mdteam.ait.client.screens;

import com.google.common.collect.Lists;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.client.screens.interior.InteriorSelectScreen;
import mdteam.ait.client.screens.interior.InteriorSettingsScreen;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.exterior.BoothExterior;
import mdteam.ait.tardis.exterior.ClassicExterior;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.exterior.PoliceBoxExterior;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static mdteam.ait.tardis.control.impl.DimensionControl.convertWorldValueToModified;
import static mdteam.ait.tardis.handler.FuelHandler.TARDIS_MAX_FUEL;

public class MonitorScreen extends TardisScreen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/exterior_changer.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    private ExteriorSchema currentModel;
    private ClientExteriorVariantSchema currentVariant;
    private float scrollPosition;
    private boolean scrollbarClicked;
    private int visibleTopRow;
    int backgroundHeight = 133;
    int backgroundWidth = 236;

    public MonitorScreen(UUID tardis) {
        super(Text.translatable("screen." + AITMod.MOD_ID + ".monitor"), tardis);
        updateTardis();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add((ButtonWidget) button);
    }

    @Override
    protected void init() {
        super.init();
        this.createButtons();
    }

    public ExteriorSchema getCurrentModel() {
        // if (currentModel == ExteriorRegistry.CORAL_GROWTH) nextExterior();

        return currentModel == null ? tardis().getExterior().getType() : currentModel;
    }

    public void setCurrentModel(ExteriorSchema currentModel) {
        this.currentModel = currentModel;

        if (currentVariant == null) return;
        if (this.currentVariant.parent().parent() != currentModel) {
            currentVariant = null;
        }
    }

    public ClientExteriorVariantSchema getCurrentVariant() {
        if (Objects.equals(currentVariant, ClientExteriorVariantRegistry.CORAL_GROWTH)) whichDirectionExterior(true);

        if (currentVariant == null)
            if(tardis().getExterior().getType() != getCurrentModel()) {
                setCurrentVariant(TardisItemBuilder.findRandomVariant(getCurrentModel()));
            } else {
                setCurrentVariant(tardis().getExterior().getVariant());
            }

        return currentVariant;
    }

    public void setCurrentVariant(ExteriorVariantSchema var) {
        setCurrentVariant(ClientExteriorVariantRegistry.withParent(var));
    }

    public void setCurrentVariant(ClientExteriorVariantSchema currentVariant) {
        this.currentVariant = currentVariant;
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
            whichDirectionExterior(false);
        }, this.textRenderer));
        this.addButton(new PressableTextWidget((width / 2 - 76), (height / 2 - 24),
                this.textRenderer.getWidth(">"), 10, Text.literal(">"), button -> {
            whichDirectionExterior(true);
        }, this.textRenderer));
        this.addButton(new PressableTextWidget((width / 2 - 110), (height / 2 - 14),
                this.textRenderer.getWidth("<"), 10, Text.literal("<").formatted(Formatting.LIGHT_PURPLE), button -> {
            whichDirectionVariant(false);
        }, this.textRenderer));
        this.addButton(new PressableTextWidget((width / 2 - 76), (height / 2 - 14),
                this.textRenderer.getWidth(">"), 10, Text.literal(">").formatted(Formatting.LIGHT_PURPLE), button -> {
            whichDirectionVariant(true);
        }, this.textRenderer));
        this.addButton(new PressableTextWidget((width / 2 + 12), (height / 2 + 38),
                this.textRenderer.getWidth("Desktop Settings"), 10, Text.literal("Desktop Settings").formatted(Formatting.AQUA), button -> toInteriorSettingsScreen(), this.textRenderer));
        this.buttons.forEach(buttons -> {
            // buttons.visible = false;
            buttons.active = true;
        });
    }

    public void sendExteriorPacket() {
        if (tardis() != null) {
            /*TardisUtil.changeExteriorWithScreen(this.tardisId, this.getCurrentModel() != tardis().getExterior().getType() ?
                    this.getCurrentModel().ordinal() : tardis().getExterior().getType().ordinal(), this.getCurrentVariant().ordinal(),
                    this.getCurrentVariant() != tardis().getExterior().getVariant());*/
            if (this.getCurrentModel() != tardis().getExterior().getType() || this.getCurrentVariant().parent() != tardis().getExterior().getVariant()) {
                ClientTardisUtil.changeExteriorWithScreen(this.tardisId,
                        this.getCurrentModel().id().toString(), this.getCurrentVariant().id().toString(),
                        this.getCurrentVariant().parent() != tardis().getExterior().getVariant());
            }
        }
    }

    public void toInteriorSettingsScreen() {
        MinecraftClient.getInstance().setScreenAndRender(new InteriorSettingsScreen(this.tardisId, this));
    }

    public void whichDirectionExterior(boolean direction) {
        if (direction) setCurrentModel(nextExterior());
        else setCurrentModel(previousExterior());

        if (this.currentModel == ExteriorRegistry.CORAL_GROWTH) {
            whichDirectionExterior(direction);
        }
    }
    public ExteriorSchema nextExterior() {
        List<ExteriorSchema> list = ExteriorRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(getCurrentModel());
        if (idx < 0 || idx+1 == list.size()) return list.get(0);
        return list.get(idx + 1);
    }
    public ExteriorSchema previousExterior() {
        List<ExteriorSchema> list = ExteriorRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(getCurrentModel());
        if (idx <= 0) return list.get(list.size() - 1);
        return list.get(idx - 1);
    }


    public void whichDirectionVariant(boolean direction) {
        if (direction) setCurrentVariant(nextVariant());
        else setCurrentVariant(previousVariant());
    }

    public ExteriorVariantSchema nextVariant() {
        List<ExteriorVariantSchema> list = ExteriorVariantRegistry.withParent(getCurrentVariant().parent().parent()).stream().toList();

        int idx = list.indexOf(getCurrentVariant().parent());
        if (idx < 0 || idx+1 == list.size()) return list.get(0);
        return list.get(idx + 1);
    }

    public ExteriorVariantSchema previousVariant() {
        List<ExteriorVariantSchema> list = ExteriorVariantRegistry.withParent(getCurrentVariant().parent().parent()).stream().toList();

        int idx = list.indexOf(getCurrentVariant().parent());
        if (idx <= 0) return list.get(list.size() - 1);
        return list.get(idx - 1);
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
        for (int k = 0; k < 8; k++) {
            context.drawTexture(TEXTURE, (i + 51) + (k * 20), j + 21, 0, 133, 20, 40);
            context.drawTexture(TEXTURE, (i + 51) + (k * 20), j + 61, 0, 133, 20, 40);
        }
        context.pop();
        //context.drawTexture(TEXTURE, i + 18, j + 67, 1, 87, 25, 8);
    }

    protected void drawTardisExterior(DrawContext context, int x, int y, float scale, float mouseX) {
        // testing @todo
        if (tardis() != null) {
            if (this.getCurrentModel() == null || this.getCurrentVariant() == null) return;
            ExteriorModel model = this.getCurrentVariant().model();
            MatrixStack stack = context.getMatrices();
            // fixme is bad
            stack.push();
            stack.translate(x, this.getCurrentModel() == ExteriorRegistry.REGISTRY.get(PoliceBoxExterior.REFERENCE) || this.getCurrentModel() == ExteriorRegistry.REGISTRY.get(ClassicExterior.REFERENCE) ? y + 8 : y, 100f);
            if (this.getCurrentModel() == ExteriorRegistry.REGISTRY.get(PoliceBoxExterior.REFERENCE) || this.getCurrentModel() == ExteriorRegistry.REGISTRY.get(ClassicExterior.REFERENCE)) stack.scale(-10, 10, 10);
            else if (this.getCurrentModel() == ExteriorRegistry.REGISTRY.get(BoothExterior.REFERENCE)) stack.scale(-scale, scale, scale);
            else stack.scale(-scale, scale, scale);
            //stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-180f));
            stack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(mouseX));
            DiffuseLighting.disableGuiDepthLighting();
            model.render(stack, context.getVertexConsumers().getBuffer(AITRenderLayers.getEntityTranslucentCull(getCurrentVariant().texture())), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
            DiffuseLighting.enableGuiDepthLighting();
            stack.pop();
        }
    }

    @Override
    public void renderBackground(DrawContext context) {
        super.renderBackground(context);
    }

    protected void drawInformationText(DrawContext context) {
        int i = ((this.height - this.backgroundHeight) / 2); // loqor make sure to use these so it stays consistent on different sized screens (kind of ??)
        int j = ((this.width - this.backgroundWidth) / 2);
        if (tardis() == null) return;
        AbsoluteBlockPos.Directed abpd = tardis().getTravel().getPosition();
        if(abpd == null) return;
        if(abpd.getDimension() == null) return;
        String destinationText = "> " + abpd.getX() + ", " + abpd.getY() + ", " + abpd.getZ();
        String dimensionText = "> " + convertWorldValueToModified(abpd.getDimension().getValue());
        String directionText = "> " + abpd.getDirection().toString().toUpperCase();
        String fuelText = "> " + Math.round((tardis().getFuel() / TARDIS_MAX_FUEL) * 100);
        context.drawText(this.textRenderer, Text.literal(destinationText), (width / 2 - 67), (height / 2 + 38), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(dimensionText), (width / 2 - 19), (height / 2 + 48), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(directionText), (width / 2 - 67), (height / 2 + 48), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal("Fuel:"), (width / 2 - 102), (height / 2 + 28), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(fuelText + "%"), (width / 2 - 108), (height / 2 + 38), 0xFFFFFF, true);
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
        this.drawInformationText(context);
        super.render(context, mouseX, mouseY, delta);
    }
}