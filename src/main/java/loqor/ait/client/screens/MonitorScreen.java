package loqor.ait.client.screens;

import com.google.common.collect.Lists;
import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.data.FuelData;
import loqor.ait.tardis.exterior.category.BoothCategory;
import loqor.ait.tardis.exterior.category.ClassicCategory;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.tardis.exterior.category.PoliceBoxCategory;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.client.screens.interior.InteriorSettingsScreen;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.control.impl.DimensionControl;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.render.*;
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

import static loqor.ait.tardis.TardisTravel.State.FLIGHT;
import static loqor.ait.tardis.control.impl.DimensionControl.convertWorldValueToModified;

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
		return category == null ? tardis().getExterior().getCategory() : category;
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
			if (!tardis().getExterior().getCategory().equals(getCategory())) {
				setCurrentVariant(this.getCategory().getDefaultVariant());
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

	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		// just this whole thing is for the flight
		if (tardis() == null) return;

		int i = (this.width - this.backgroundWidth) / 2;
		int j = ((this.height) - this.backgroundHeight) / 2;
		context.drawTexture(TEXTURE, i - 8, j + 4, 0, 0, this.backgroundWidth, this.backgroundHeight);

		// apply button
		if (!this.buttons.get(0).isHovered()) context.drawTexture(TEXTURE, i + 22, j + 114, 0, 227, 57, 12);

		// around the battery
		context.drawTexture(TEXTURE, i + 1, j + 129, 0, tardis().getFuel() > 250 ? 150 : 165, 99, 15);

		// triangle buttons
		if (!this.buttons.get(1).isHovered()) context.drawTexture(TEXTURE, i + 3, j + 96, 0, 197, 15, 30);
		if (!this.buttons.get(2).isHovered()) context.drawTexture(TEXTURE, i + 83, j + 96, 30, 197, 15, 30);
		if (!this.buttons.get(3).isHovered()) context.drawTexture(TEXTURE, i + 3, j + 31, 45, 197, 15, 30);
		if (!this.buttons.get(4).isHovered()) context.drawTexture(TEXTURE, i + 83, j + 31, 15, 197, 15, 30);

		// fuel markers @TODO come back and actually do the rest of it with the halves and the red parts too
		for (int p = 0; p < Math.round(tardis().getFuel() / FuelData.TARDIS_MAX_FUEL * 12); ++p) {
			context.drawTexture(TEXTURE, i + 3 + (8 * p), j + 131, 99, 150, 7, 11);
		}

		int progress = tardis().getHandlers().getFlight().getDurationAsPercentage();

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

			context.drawTexture(TEXTURE, i + 101 + (index * 18), j + 78, tardis().travel().getState() == FLIGHT ? progress >= 100 ? 68 : uvOffset : UV_BASE, 180, 17, 17);
		}
	}

	protected void drawTardisExterior(DrawContext context, int x, int y, float scale) {
		MatrixStack stack = context.getMatrices();

		tickForSpin++;

		if (tardis() != null) {
			if (this.getCategory() == null || this.getCurrentVariant() == null)
				return;

			boolean isExtUnlocked = tardis().isUnlocked(this.getCurrentVariant().parent());

			stack.push();
			stack.translate(0, 0, 50f);
			context.drawCenteredTextWithShadow(
					this.textRenderer,
					convertCategoryNameToProper(this.getCategory().name()), (width / 2 - 54), (height / 2 + 41),
					5636095);

			List<ExteriorVariantSchema> list = ExteriorVariantRegistry.withParent(this.getCategory());
			context.drawCenteredTextWithShadow(
					this.textRenderer,
					(list.indexOf(this.getCurrentVariant().parent()) + 1) + "/" + ExteriorVariantRegistry.withParent(this.getCategory()).size(),
					(width / 2 - 29), (height / 2 + 26),
					0x00ffb3);
			stack.pop();

			ExteriorModel model = this.getCurrentVariant().model();

			stack.push();
			stack.translate(0, 0, -50f);
			stack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(((float) tickForSpin / 1400L) * 360.0f), x, y, 0);
			context.drawTexture(TEXTURE, x - 41, y - 41, 173, 173, 83, 83);
			stack.pop();

			stack.push();
			stack.translate(x, this.getCategory() == CategoryRegistry.getInstance().get(PoliceBoxCategory.REFERENCE) || this.getCategory() == CategoryRegistry.getInstance().get(ClassicCategory.REFERENCE) ? y + 11 : y, 0f);
			if (this.getCategory() == CategoryRegistry.getInstance().get(PoliceBoxCategory.REFERENCE) || this.getCategory() == CategoryRegistry.getInstance().get(ClassicCategory.REFERENCE)) {
				stack.scale(-12, 12, 12);
			} else if (this.getCategory() == CategoryRegistry.getInstance().get(BoothCategory.REFERENCE)) {
				stack.scale(-scale, scale, scale);
			} else {
				stack.scale(-scale, scale, scale);
			}
			stack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(((float) tickForSpin / 1200L) * 360.0f));
			DiffuseLighting.disableGuiDepthLighting();
			model.render(stack, context.getVertexConsumers().getBuffer(AITRenderLayers.getEntityTranslucentCull(getCurrentVariant().texture())), LightmapTextureManager.MAX_SKY_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, isExtUnlocked ? 1 : 0.1f, isExtUnlocked ? 1 : 0.1f, isExtUnlocked ? 1 : 0.1f, 1f);
			if (getCurrentVariant().emission() != null) model.render(stack, context.getVertexConsumers().getBuffer(AITRenderLayers.getEntityTranslucentCull(getCurrentVariant().emission())), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, isExtUnlocked ? 1 : 0.1f, isExtUnlocked ? 1: 0.1f, isExtUnlocked ? 1 : 0.1f, 1f);
			DiffuseLighting.enableGuiDepthLighting();
			stack.pop();

			stack.push();
			stack.translate(0, 0, 50f);
			context.drawCenteredTextWithShadow(
					this.textRenderer,
					(isExtUnlocked) ? "" : "\uD83D\uDD12",
					x, y,
					Color.WHITE.getRGB());
			stack.pop();
		}
	}

	@Override
	public void renderBackground(DrawContext context) {
		super.renderBackground(context);
	}

	protected void drawInformationText(DrawContext context) {
		if (this.tardis() == null)
			return;

		TardisTravel travel = this.tardis().travel();
		AbsoluteBlockPos.Directed abpd = travel.inFlight() ? FlightUtil.getPositionFromPercentage(travel.getPosition(), travel.getDestination(), this.tardis().getHandlers().getFlight().getDurationAsPercentage()) : travel.getPosition();
		AbsoluteBlockPos.Directed dabpd = travel.getDestination();

		if (abpd.getDimension() == null)
			return;

		String positionText = abpd.getX() + ", " + abpd.getY() + ", " + abpd.getZ();
		String dimensionText = convertWorldValueToModified(abpd.getDimension().getValue().toString());

		String directionText = DirectionControl.rotationToDirection(abpd.getRotation()).toUpperCase();
		String destinationText = dabpd.getX() + ", " + dabpd.getY() + ", " + dabpd.getZ();
		String dDimensionText = convertWorldValueToModified(dabpd.getDimension().getValue());
		String dDirectionText = DirectionControl.rotationToDirection(dabpd.getRotation()).toUpperCase();

		// position
		//context.drawText(this.textRenderer, Text.literal("Position"), (width / 2 - 64), (height / 2 - 46), 5636095, true);
		context.drawText(this.textRenderer, Text.literal(positionText), (width / 2 + 7), (height / 2 - 36), 0xFFFFFF, true);
		context.drawText(this.textRenderer, Text.literal(dimensionText), (width / 2 + 7), (height / 2 - 26), 0xFFFFFF, true);
		context.drawText(this.textRenderer, Text.literal(directionText), (width / 2 + 14), (height / 2 - 16), 0xFFFFFF, true);

		// destination
		context.drawText(this.textRenderer, Text.literal(destinationText), (width / 2 + 7), (height / 2 + 31), 0xFFFFFF, true);
		context.drawText(this.textRenderer, Text.literal(dDimensionText), (width / 2 + 7), (height / 2 + 41), 0xFFFFFF, true);
		context.drawText(this.textRenderer, Text.literal(dDirectionText), (width / 2 + 14), (height / 2 + 51), 0xFFFFFF, true);

		// fuel
		//context.drawText(this.textRenderer, Text.translatable("screen.ait.monitor.fuel"), (width / 2 - 102), (height / 2 + 28), 0xFFFFFF, true);
		//context.drawText(this.textRenderer, Text.literal(fuelText + "%"), (width / 2 - 108), (height / 2 + 38), 0xFFFFFF, true);
		// percentage of travel time to destination
		//context.drawText(this.textRenderer, Text.translatable("screen.ait.monitor.traveltime"), (width / 2 + 34), (height / 2 + 28), 0xFFFFFF, true);
		//context.drawText(this.textRenderer, Text.literal(flightTimeText + "%"), (width / 2 + 46), (height / 2 + 38), 0xFFFFFF, true);
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
		this.drawBackground(context, delta, mouseX, mouseY);
		// todo manually adjusting all these values are annoying me
		this.drawInformationText(context);
		super.render(context, mouseX, mouseY, delta);
	}

	@Deprecated(forRemoval = true)
	public String convertCategoryNameToProper(String string) {
		return switch (string) {
			case "easter_head" -> "Moyai";
			case "police_box" -> "Police";
			case "classic" -> "Classic";
			case "doom" -> "Doom";
			case "plinth" -> "Plinth";
			case "tardim" -> "TARDIM";
			case "booth" -> "K2 Booth";
			case "capsule" -> "Capsule";
			case "renegade" -> "Renegade";
			default -> DimensionControl.convertWorldValueToModified(string);
		};
	}
}