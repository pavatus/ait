package mdteam.ait.client.screens;

import com.google.common.collect.Lists;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.client.screens.interior.InteriorSettingsScreen;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.registry.CategoryRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.exterior.category.BoothCategory;
import mdteam.ait.tardis.exterior.category.ClassicCategory;
import mdteam.ait.tardis.exterior.category.ExteriorCategory;
import mdteam.ait.tardis.exterior.category.PoliceBoxCategory;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
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
import static mdteam.ait.tardis.data.FuelData.TARDIS_MAX_FUEL;

public class MonitorScreen extends TardisScreen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/exterior_changer.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    private ExteriorCategory currentModel;
    private ClientExteriorVariantSchema currentVariant;
    int backgroundHeight = 121;//101;
    int backgroundWidth = 220;//200;
    public MonitorScreen(UUID tardis) {
        super(Text.translatable("screen." + AITMod.MOD_ID + ".monitor"), tardis);
        this.tardisId = tardis;
        //AITMod.LOGGER.debug("@#!@@!: " + tardis + " | " + ClientTardisManager.getInstance().getLookup());
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

    public ExteriorCategory getCurrentModel() {
        return currentModel == null ? getFromUUID(tardisId).getExterior().getCategory() : currentModel;
    }

    public void setCurrentModel(ExteriorCategory currentModel) {
        this.currentModel = currentModel;

        if (currentVariant == null) return;
        if (this.currentVariant.parent().category() != currentModel) {
            currentVariant = null;
        }
    }

    public ClientExteriorVariantSchema getCurrentVariant() {
        if (Objects.equals(currentVariant, ClientExteriorVariantRegistry.CORAL_GROWTH)) whichDirectionExterior(true);

        if (currentVariant == null)
            if(getFromUUID(tardisId).getExterior().getCategory() != getCurrentModel()) {
                setCurrentVariant(ExteriorVariantRegistry.withParentToList(getCurrentModel()).get(0));
            } else {
                setCurrentVariant(getFromUUID(tardisId).getExterior().getVariant());
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
        Text applyText = Text.translatable("screen.ait.monitor.apply");
        this.addButton(new PressableTextWidget((width / 2 - 103), (height / 2 + 12),
                this.textRenderer.getWidth(applyText), 10, applyText, button -> {
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
        Text desktopSettingsText = Text.literal("⚙");
        this.addButton(new PressableTextWidget((width / 2 + 84), (height / 2 - 47),
                this.textRenderer.getWidth(desktopSettingsText), 10, Text.literal("⚙").formatted(Formatting.WHITE), button -> toInteriorSettingsScreen(), this.textRenderer));
        this.buttons.forEach(buttons -> {
            // buttons.visible = false;
            buttons.active = true;
        });
    }

    public void sendExteriorPacket() {
        if (getFromUUID(tardisId) != null) {
            if (this.getCurrentModel() != getFromUUID(tardisId).getExterior().getCategory() || this.getCurrentVariant().parent() != getFromUUID(tardisId).getExterior().getVariant()) {
                ClientTardisUtil.changeExteriorWithScreen(this.tardisId,
                        this.getCurrentModel().id().toString(), this.getCurrentVariant().id().toString(),
                        this.getCurrentVariant().parent() != getFromUUID(tardisId).getExterior().getVariant());
            }
        }
    }

    public void toInteriorSettingsScreen() {
        if (tardis() == null || tardis().isGrowth()) return;
        MinecraftClient.getInstance().setScreenAndRender(new InteriorSettingsScreen(this.tardisId, this));
    }

    public void whichDirectionExterior(boolean direction) {

        if(MinecraftClient.getInstance().player == null) return;

        if (direction) setCurrentModel(nextExterior());
        else setCurrentModel(previousExterior());

        if (this.currentModel == CategoryRegistry.CORAL_GROWTH || (!("Loqor".equalsIgnoreCase(MinecraftClient.getInstance().player.getName().getString())) && this.currentModel == CategoryRegistry.DOOM)) {
            whichDirectionExterior(direction);
        }
    }
    public ExteriorCategory nextExterior() {
        List<ExteriorCategory> list = CategoryRegistry.getInstance().toList();

        int idx = list.indexOf(getCurrentModel());
        if (idx < 0 || idx+1 == list.size()) return list.get(0);
        return list.get(idx + 1);
    }
    public ExteriorCategory previousExterior() {
        List<ExteriorCategory> list = CategoryRegistry.getInstance().toList();

        int idx = list.indexOf(getCurrentModel());
        if (idx <= 0) return list.get(list.size() - 1);
        return list.get(idx - 1);
    }


    public void whichDirectionVariant(boolean direction) {
        if (direction) setCurrentVariant(nextVariant());
        else setCurrentVariant(previousVariant());
    }

    public ExteriorVariantSchema nextVariant() {
        List<ExteriorVariantSchema> list = ExteriorVariantRegistry.withParent(getCurrentVariant().parent().category()).stream().toList();

        int idx = list.indexOf(getCurrentVariant().parent());
        if (idx < 0 || idx+1 == list.size()) return list.get(0);
        return list.get(idx + 1);
    }

    public ExteriorVariantSchema previousVariant() {
        List<ExteriorVariantSchema> list = ExteriorVariantRegistry.withParent(getCurrentVariant().parent().category()).stream().toList();

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
        context.drawTexture(TEXTURE, i - 8, j + 4, 0, 12, this.backgroundWidth, this.backgroundHeight);

        // @TODO ive gotta move this to a OwO screen and fix the rendering for the new monitor stuff with the gallifreyan - Loqor

        /*context.push();
        context.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(delta * 10), i + 41.5f, j + 41.5f, 0);
        context.drawTexture(TEXTURE, i, j, 0, 101, 83, 83);
        context.pop();*/
        //context.drawTexture(TEXTURE, i + 18, j + 67, 1, 87, 25, 8);
    }

    protected void drawTardisExterior(DrawContext context, int x, int y, float scale, float mouseX) {
        // testing @todo
        if (getFromUUID(tardisId) != null) {
            if (this.getCurrentModel() == null || this.getCurrentVariant() == null) return;
            ExteriorModel model = this.getCurrentVariant().model();
            MatrixStack stack = context.getMatrices();
            // fixme is bad
            stack.push();
            stack.translate(x, this.getCurrentModel() == CategoryRegistry.getInstance().get(PoliceBoxCategory.REFERENCE) || this.getCurrentModel() == CategoryRegistry.getInstance().get(ClassicCategory.REFERENCE) ? y + 8 : y, 100f);
            if (this.getCurrentModel() == CategoryRegistry.getInstance().get(PoliceBoxCategory.REFERENCE) || this.getCurrentModel() == CategoryRegistry.getInstance().get(ClassicCategory.REFERENCE)) stack.scale(-10, 10, 10);
            else if (this.getCurrentModel() == CategoryRegistry.getInstance().get(BoothCategory.REFERENCE)) stack.scale(-scale, scale, scale);
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
        if (getFromUUID(tardisId) == null) return;
        AbsoluteBlockPos.Directed abpd = getFromUUID(tardisId).getTravel().getPosition();
        AbsoluteBlockPos.Directed dabpd = getFromUUID(tardisId).getTravel().getDestination();
        if(abpd == null) return;
        if(abpd.getDimension() == null) return;
        String positionText = "> " + abpd.getX() + ", " + abpd.getY() + ", " + abpd.getZ();
        String dimensionText = "> " + convertWorldValueToModified(abpd.getDimension().getValue());
        String directionText = "> " + abpd.getDirection().toString().toUpperCase();
        String destinationText = "> " + dabpd.getX() + ", " + dabpd.getY() + ", " + dabpd.getZ();
        String dDimensionText = "> " + convertWorldValueToModified(dabpd.getDimension().getValue());
        String dDirectionText = "> " + dabpd.getDirection().toString().toUpperCase();
        String fuelText = "> " + Math.round((getFromUUID(tardisId).getFuel() / TARDIS_MAX_FUEL) * 100);
        String flightTimeText = "> " + (tardis().getTravel().getState() == TardisTravel.State.LANDED ? "0" : tardis().getHandlers().getFlight().getDurationAsPercentage());
        // position
        context.drawText(this.textRenderer, Text.literal("Position"), (width / 2 - 64), (height / 2 - 46), 5636095, true);
        context.drawText(this.textRenderer, Text.literal(positionText), (width / 2 - 64), (height / 2 - 36), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(dimensionText), (width / 2 - 64), (height / 2 - 26), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(directionText), (width / 2 - 64), (height / 2 - 16), 0xFFFFFF, true);

        // destination
        context.drawText(this.textRenderer, Text.literal("Destination"), (width / 2 - 64), (height / 2 + 14), 5636095, true);
        context.drawText(this.textRenderer, Text.literal(destinationText), (width / 2 - 64), (height / 2 + 24), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(dDimensionText), (width / 2 - 64), (height / 2 + 34), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(dDirectionText), (width / 2 - 64), (height / 2 + 44), 0xFFFFFF, true);

        // fuel
        context.drawText(this.textRenderer, Text.translatable("screen.ait.monitor.fuel"), (width / 2 - 102), (height / 2 + 28), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(fuelText + "%"), (width / 2 - 108), (height / 2 + 38), 0xFFFFFF, true);
        // percentage of travel time to destination
        context.drawText(this.textRenderer, Text.translatable("screen.ait.monitor.traveltime"), (width / 2 + 34), (height / 2 + 28), 0xFFFFFF, true);
        context.drawText(this.textRenderer, Text.literal(flightTimeText + "%"), (width / 2 + 46), (height / 2 + 38), 0xFFFFFF, true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = ((this.height - this.backgroundHeight) / 2); // loqor make sure to use these so it stays consistent on different sized screens (kind of ??)
        int j = ((this.width - this.backgroundWidth) / 2);
        this.drawBackground(context, delta, mouseX, mouseY);
        /*float yClamping = MathHelper.clamp(mouseY, i + 60, i + 65);
        float xClamping = mouseX <= 196 && mouseX >= 156 ? MathHelper.clamp((mouseX + this.backgroundWidth / 2), j + 10, j + 50) : 176;
        AITMod.LOGGER.debug((mouseX + this.backgroundWidth / 2) +  " min: " + (j + 10) + ", max: " + (j + 50) + ": is this clamped right = " + xClamping);*/
        // todo manually adjusting all these values are annoying me
        this.drawTardisExterior(context, (width / 2 - 91), (height / 2 - 19), 15f, 176);
        this.drawInformationText(context);
        super.render(context, mouseX, mouseY, delta);
    }
}