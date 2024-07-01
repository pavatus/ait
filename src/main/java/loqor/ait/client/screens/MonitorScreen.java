package loqor.ait.client.screens;

import com.google.common.collect.Lists;
import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.client.screens.interior.InteriorSettingsScreen;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.data.FuelData;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.exterior.category.ClassicCategory;
import loqor.ait.tardis.exterior.category.PoliceBoxCategory;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class MonitorScreen extends ConsoleScreen {
	private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/monitor_gui.png");
	private final List<ButtonWidget> buttons = Lists.newArrayList();
	private ExteriorCategorySchema category;
	private ClientExteriorVariantSchema currentVariant;
	int backgroundHeight = 150;
	int backgroundWidth = 208;
	private int tickForSpin = 0;

	public MonitorScreen(ClientTardis tardis, BlockPos console) {
		super(Text.translatable("screen." + AITMod.MOD_ID + ".monitor"), tardis, console);
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

	public ExteriorCategorySchema getCategory() {
		return category == null ? this.tardis().getExterior().getCategory() : category;
	}

	public void setCategory(ExteriorCategorySchema category) {
		this.category = category;

		if (currentVariant == null)
            return;

		if (this.currentVariant.parent().category() != category)
			currentVariant = null;
	}

	public ClientExteriorVariantSchema getCurrentVariant() {
		if (Objects.equals(currentVariant, ClientExteriorVariantRegistry.CORAL_GROWTH))
			changeCategory(true);

		if (currentVariant == null)
			if (!this.tardis().getExterior().getCategory().equals(getCategory())) {
				setCurrentVariant(this.getCategory().getDefaultVariant());
			} else {
				setCurrentVariant(this.tardis().getExterior().getVariant());
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
		this.buttons.clear();
		// exterior change text button
		Text applyText = Text.literal("Apply");
		this.addButton(new PressableTextWidget((width / 2 - 67), (height / 2 + 41),
				this.textRenderer.getWidth(applyText), 10, Text.literal(""), button -> {
			sendExteriorPacket(this.tardis(), this.getCategory(), this.getCurrentVariant());
		}, this.textRenderer));
		this.addButton(new PressableTextWidget((width / 2 - 99), (height / 2 + 37),
				this.textRenderer.getWidth("<"), 10, Text.literal(""), button -> {
			changeCategory(false);
		}, this.textRenderer));
		this.addButton(new PressableTextWidget((width / 2 - 12), (height / 2 + 37),
				this.textRenderer.getWidth(">"), 10, Text.literal(""), button -> {
			changeCategory(true);
		}, this.textRenderer));
		this.addButton(new PressableTextWidget((width / 2 - 99), (height / 2 - 37),
				this.textRenderer.getWidth("<"), 10, Text.literal("").formatted(Formatting.LIGHT_PURPLE), button -> {
			whichDirectionVariant(false);
		}, this.textRenderer));
		this.addButton(new PressableTextWidget((width / 2 - 12), (height / 2 - 37),
				this.textRenderer.getWidth(">"), 10, Text.literal("").formatted(Formatting.LIGHT_PURPLE), button -> {
			whichDirectionVariant(true);
		}, this.textRenderer));
		Text desktopSettingsText = Text.literal("Settings");
		this.addButton(new PressableTextWidget((width / 2 - 105), (height / 2 - 66),
				this.textRenderer.getWidth(desktopSettingsText), 10, Text.literal("Settings").formatted(Formatting.WHITE), button -> toInteriorSettingsScreen(), this.textRenderer));
		this.buttons.forEach(buttons -> {
			// buttons.visible = false;
			buttons.active = true;
		});
	}

    public static void sendExteriorPacket(ClientTardis tardis, ExteriorCategorySchema category, ClientExteriorVariantSchema variant) {
        if (category != tardis.getExterior().getCategory() || variant.parent() != tardis.getExterior().getVariant()) {
            ClientTardisUtil.changeExteriorWithScreen(tardis,
                    category.id().toString(), variant.id().toString(),
                    variant.parent() != tardis.getExterior().getVariant());
        }
    }

	public void toInteriorSettingsScreen() {
		if (tardis() == null || tardis().isGrowth())
			return;

		MinecraftClient.getInstance().setScreenAndRender(new InteriorSettingsScreen(this.tardis(), this.console, this));
	}

	public void changeCategory(boolean direction) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
            return;

		if (direction) setCategory(nextCategory());
		else setCategory(previousCategory());

		if (CategoryRegistry.CORAL_GROWTH.equals(this.category) || (!("ad504e7c-22a0-4b3f-94e3-5b6ad5514cb6".equalsIgnoreCase(player.getUuidAsString())) && CategoryRegistry.DOOM.equals(this.category))) {
			changeCategory(direction);
		}
	}

	public ExteriorCategorySchema nextCategory() {
		List<ExteriorCategorySchema> list = CategoryRegistry.getInstance().toList();

		int idx = list.indexOf(getCategory());
		if (idx < 0 || idx + 1 == list.size()) return list.get(0);
		return list.get(idx + 1);
	}

	public ExteriorCategorySchema previousCategory() {
		List<ExteriorCategorySchema> list = CategoryRegistry.getInstance().toList();

		int idx = list.indexOf(getCategory());
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
		if (idx < 0 || idx + 1 == list.size()) return list.get(0);
		return list.get(idx + 1);
	}

	public ExteriorVariantSchema previousVariant() {
		List<ExteriorVariantSchema> list = ExteriorVariantRegistry.withParent(getCurrentVariant().parent().category()).stream().toList();

		int idx = list.indexOf(getCurrentVariant().parent());
		if (idx <= 0) return list.get(list.size() - 1);
		return list.get(idx - 1);
	}

	final int UV_BASE = 159;
	final int UV_INCREMENT = 17;

	int calculateUvOffsetForRange(int progress) {
		int rangeProgress = progress % 20;
		return (rangeProgress / 5) * UV_INCREMENT;
	}

	protected void drawBackground(DrawContext context) {
        // just this whole thing is for the flight
        if (this.tardis() == null)
            return;

        int i = (this.width - this.backgroundWidth) / 2;
		int j = ((this.height) - this.backgroundHeight) / 2;
		context.drawTexture(TEXTURE, i - 8, j + 4, 0, 0, this.backgroundWidth, this.backgroundHeight);

		// apply button
		if (!this.buttons.get(0).isHovered())
            context.drawTexture(TEXTURE, i + 22, j + 114, 0, 227, 57, 12);

		// around the battery
		context.drawTexture(TEXTURE, i + 1, j + 129, 0, this.tardis().getFuel() > 250 ? 150 : 165, 99, 15);

		// triangle buttons
		if (!this.buttons.get(1).isHovered()) context.drawTexture(TEXTURE, i + 3, j + 96, 0, 197, 15, 30);
		if (!this.buttons.get(2).isHovered()) context.drawTexture(TEXTURE, i + 83, j + 96, 30, 197, 15, 30);
		if (!this.buttons.get(3).isHovered()) context.drawTexture(TEXTURE, i + 3, j + 31, 45, 197, 15, 30);
		if (!this.buttons.get(4).isHovered()) context.drawTexture(TEXTURE, i + 83, j + 31, 15, 197, 15, 30);

		// fuel markers @TODO come back and actually do the rest of it with the halves and the red parts too
		for (int p = 0; p < Math.round((this.tardis().getFuel() / FuelData.TARDIS_MAX_FUEL) * 12); ++p) {
			context.drawTexture(TEXTURE, i + 3 + (8 * p), j + 131, 99, 150, 7, 11);
		}

        int progress = this.tardis().travel().getDurationAsPercentage();

		for (int index = 0; index < 5; index++) {
			int rangeStart = index * 20;
			int rangeEnd = (index + 1) * 20;

			int uvOffset;
			if (progress >= rangeStart && progress <= rangeEnd) {
				uvOffset = calculateUvOffsetForRange(progress);
			} else if (progress >= rangeEnd) {
				uvOffset = 68;
			} else {
				uvOffset = UV_BASE;
			}

            context.drawTexture(TEXTURE, i + 101 + index * 18, j + 78,
                    this.tardis().travel().getState() == TravelHandlerBase.State.FLIGHT
                            ? progress >= 100 ? 68 : uvOffset : UV_BASE, 180, 17, 17
            );
        }
	}

    protected void drawTardisExterior(DrawContext context, int x, int y, float scale) {
        this.tickForSpin++;
        Tardis tardis = this.tardis();

        if (tardis == null)
            return;

        MatrixStack stack = context.getMatrices();

        int centerWidth = width / 2;
        int centerHeight = height / 2;

        ExteriorCategorySchema category = this.getCategory();
        ClientExteriorVariantSchema variant = this.getCurrentVariant();

        if (category == null || variant == null)
            return;

        boolean isPoliceBox = category.equals(CategoryRegistry.getInstance().get(PoliceBoxCategory.REFERENCE))
                || category.equals(CategoryRegistry.getInstance().get(ClassicCategory.REFERENCE));

        boolean isExtUnlocked = tardis.isUnlocked(variant.parent());
        boolean hasPower = tardis.engine().hasPower();
        boolean alarms = tardis.alarm().isEnabled();

        stack.push();
        stack.translate(0, 0, 50f);

        context.drawCenteredTextWithShadow(
                this.textRenderer, category.text(), (centerWidth - 54),
                (centerHeight + 41), 5636095
        );

        List<ExteriorVariantSchema> list = ExteriorVariantRegistry.withParent(category);

        context.drawCenteredTextWithShadow(
                this.textRenderer, (list.indexOf(variant.parent()) + 1)
                        + "/" + list.size(),
                (centerWidth - 29), (centerHeight + 26), 0x00ffb3
        );

        stack.pop();
        ExteriorModel model = variant.model();

        stack.push();
        stack.translate(0, 0, -50f);
        stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(((float) tickForSpin / 1400L) * 360.0f), x, y, 0);
        context.drawTexture(TEXTURE, x - 41, y - 41, 173, 173, 83, 83);
        stack.pop();

        stack.push();
        stack.translate(x, isPoliceBox ? y + 11 : y, 0f);

        if (isPoliceBox) {
            stack.scale(-12, 12, 12);
        } else {
            stack.scale(-scale, scale, scale);
        }

        stack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(((float) tickForSpin / 1200L) * 360.0f));

        Identifier texture = variant.texture();
        Identifier emissive = variant.emission();

        float base = isExtUnlocked ? 1f : 0.1f;
        float tinted = alarms && isExtUnlocked ? 0.3f : base;

        model.render(stack, context.getVertexConsumers().getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)),
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, base, base, base, 1f
        );

        if (hasPower && emissive != null) {
            ClientLightUtil.renderEmissive(ClientLightUtil.Renderable.create(model::render), emissive, null,
                    model.getPart(), stack, context.getVertexConsumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE,
                    OverlayTexture.DEFAULT_UV, base, tinted, tinted, 1f
            );
        }

        stack.pop();

        stack.push();
        stack.translate(0, 0, 50f);

        context.drawCenteredTextWithShadow(
                this.textRenderer, isExtUnlocked
                        ? "" : "\uD83D\uDD12",
                x, y, Color.WHITE.getRGB()
        );

        stack.pop();
    }

	@Override
	public void renderBackground(DrawContext context) {
		super.renderBackground(context);
	}

	protected void drawInformationText(DrawContext context) {
        if (this.tardis() == null)
            return;

        TravelHandler travel = this.tardis().travel();
        DirectedGlobalPos abpd = travel.getState() == TravelHandlerBase.State.FLIGHT ? travel.getProgress() : travel.position();
        DirectedGlobalPos.Cached dabpd = travel.destination();

        if (abpd.getDimension() == null)
            return;

        BlockPos abpdPos = abpd.getPos();

        String positionText = abpdPos.getX() + ", " + abpdPos.getY() + ", " + abpdPos.getZ();
        Text dimensionText = WorldUtil.worldText(abpd.getDimension());

        BlockPos dabpdPos = dabpd.getPos();

        String directionText = DirectionControl.rotationToDirection(abpd.getRotation()).toUpperCase();
        String destinationText = dabpdPos.getX() + ", " + dabpdPos.getY() + ", " + dabpdPos.getZ();
		Text dDimensionText = WorldUtil.worldText(dabpd.getDimension());
        String dDirectionText = DirectionControl.rotationToDirection(dabpd.getRotation()).toUpperCase();

        // position
		context.drawText(this.textRenderer, Text.literal(positionText), (width / 2 + 7), (height / 2 - 36), 0xFFFFFF, true);
		context.drawText(this.textRenderer, dimensionText, (width / 2 + 7), (height / 2 - 26), 0xFFFFFF, true);
		context.drawText(this.textRenderer, Text.literal(directionText), (width / 2 + 14), (height / 2 - 16), 0xFFFFFF, true);

		// destination
		context.drawText(this.textRenderer, Text.literal(destinationText), (width / 2 + 7), (height / 2 + 31), 0xFFFFFF, true);
		context.drawText(this.textRenderer, dDimensionText, (width / 2 + 7), (height / 2 + 41), 0xFFFFFF, true);
		context.drawText(this.textRenderer, Text.literal(dDirectionText), (width / 2 + 14), (height / 2 + 51), 0xFFFFFF, true);
    }

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		int i = ((this.height - this.backgroundHeight) / 2); // loqor make sure to use these so it stays consistent on different sized screens (kind of ??)
		int j = ((this.width - this.backgroundWidth) / 2);
		// background behind the tardis and gallifreyan text
		MatrixStack stack = context.getMatrices();
		stack.push();
		stack.translate(0, 0, -100f);
		context.drawTexture(TEXTURE, j + 4, i + 32, 80, 180, 93, 76);
		stack.pop();
		this.drawTardisExterior(context, (width / 2 - 54), (height / 2 - 4), 19f);
		this.drawBackground(context);
		// todo manually adjusting all these values are annoying me
		this.drawInformationText(context);
		super.render(context, mouseX, mouseY, delta);
	}
}