package loqor.ait.client.screens.interior;

import com.google.common.collect.Lists;
import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisClientEvents;
import loqor.ait.client.screens.ConsoleScreen;
import loqor.ait.client.screens.SonicSettingsScreen;
import loqor.ait.client.screens.TardisSecurityScreen;
import loqor.ait.client.sounds.ClientSoundManager;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.HumsRegistry;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.FuelData;
import loqor.ait.tardis.data.ServerHumHandler;
import loqor.ait.tardis.data.SonicHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.sound.HumSound;
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
import net.minecraft.util.math.RotationAxis;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static loqor.ait.tardis.data.InteriorChangingHandler.CHANGE_DESKTOP;

@Environment(EnvType.CLIENT)
public class InteriorSettingsScreen extends ConsoleScreen {
	private static final Identifier BACKGROUND = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/interior_settings.png");
	private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/monitor_gui.png");
	private static final Identifier MISSING_PREVIEW = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/desktop/missing_preview.png");
	private final List<ButtonWidget> buttons = Lists.newArrayList();
	int bgHeight = 166;
	int bgWidth = 256;
	int left, top;
	private int tickForSpin = 0;
	public int choicesCount = 0;
	private HumSound hum;
	private final Screen parent;
	private TardisDesktopSchema selectedDesktop;

	// loqor DONT rewrite with owo lib : (
	public InteriorSettingsScreen(UUID tardis, BlockPos console, Screen parent) {
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

		createTextButton(Text.translatable("screen.ait.interiorsettings.back"), (button -> backToExteriorChangeScreen()));
		createTextButton(Text.translatable("screen.ait.interiorsettings.cacheconsole").formatted(this.console != null ? Formatting.WHITE : Formatting.GRAY), button -> sendCachePacket());
		createTextButton(Text.translatable("screen.ait.security.button"), (button -> toSecurityScreen()));
		createTextButton(Text.translatable("screen.ait.sonic.button").formatted(tardis().sonic().hasSonic(SonicHandler.HAS_CONSOLE_SONIC) ? Formatting.WHITE : Formatting.GRAY),
				(button -> {
					if(tardis().sonic().hasSonic(SonicHandler.HAS_CONSOLE_SONIC)) {
						toSonicScreen();
					}
				}));

		this.createCompatButtons();
		TardisClientEvents.SETTINGS_SETUP.invoker().onSetup(this);

		this.addButton(
				new PressableTextWidget(
						(int) (left + (bgWidth * 0.59f)),
						(int) (top + (bgHeight * 0.885)),
						this.textRenderer.getWidth("<"),
						10,
						Text.literal(""),
						button -> previousHum(),
						this.textRenderer
				)
		);
		this.addButton(
				new PressableTextWidget(
						(int) (left + (bgWidth * 0.93f)),
						(int) (top + (bgHeight * 0.885f)),
						this.textRenderer.getWidth(">"),
						10,
						Text.literal(""),
						button -> nextHum(),
						this.textRenderer
				)
		);
		Text applyHumText = Text.literal("AP");
		this.addButton(
				new PressableTextWidget(
						(int) (left + (bgWidth * 0.928f)) - (this.textRenderer.getWidth(applyHumText) / 2),
						(int) (top + (bgHeight * 0.695f)),
						this.textRenderer.getWidth(applyHumText),
						10,
						Text.literal(""),
						button -> applyHum(),
						this.textRenderer
				)
		);
		this.addButton(new PressableTextWidget((left + 151), (top + 93),
				this.textRenderer.getWidth("<"), 10, Text.literal(""), button -> {
			previousDesktop();
		}, this.textRenderer));
		this.addButton(new PressableTextWidget((left + 238), (top + 93),
				this.textRenderer.getWidth(">"), 10, Text.literal(""), button -> {
			nextDesktop();
		}, this.textRenderer));
		Text applyInteriorText = Text.literal("Apply");
		this.addButton(
				new PressableTextWidget(
						(int) (left + (bgWidth * 0.77f)) - (this.textRenderer.getWidth(applyInteriorText) / 2),
						(int) (top + (bgHeight * 0.58f)),
						this.textRenderer.getWidth(applyInteriorText),
						10,
						Text.literal(""),
						button -> applyDesktop(),
						this.textRenderer
				)
		);
	}

	private void toSonicScreen() {
		MinecraftClient.getInstance().setScreen(new SonicSettingsScreen(tardis().getUuid(), this.console, this));
	}

	public <T extends ClickableWidget> void addButton(T button) {
		this.addDrawableChild(button);
		button.active = true; // this whole method is unnecessary bc it defaults to true ( ?? )
		this.buttons.add((ButtonWidget) button);
	}

	// this might be useful, so remember this exists and use it later on ( although its giving NTM vibes.. )
	public PressableTextWidget createTextButton(Text text, ButtonWidget.PressAction onPress) {
		return this.createAnyButton(text, PressableTextWidget::new, onPress);
	}

	public <T extends ButtonWidget> T initAnyButton(Text text, ButtonCreator<T> creator, ButtonWidget.PressAction onPress) {
		return creator.create(
				(int) (left + (bgWidth * 0.06f)), (int) (top + (bgHeight * (0.1f * (choicesCount + 1)))),
				this.textRenderer.getWidth(text), 10, text, onPress, this.textRenderer
		);
	}

	public <T extends ButtonWidget> T initAnyDynamicButton(Function<T, Text> text, DynamicButtonCreator<T> creator, ButtonWidget.PressAction onPress) {
		return creator.create(
				(int) (left + (bgWidth * 0.06f)), (int) (top + (bgHeight * (0.1f * (choicesCount + 1)))),
				this.textRenderer.getWidth(Text.empty()), 10, text, onPress, this.textRenderer
		);
	}

	public <T extends ButtonWidget> T createAnyButton(Text text, ButtonCreator<T> creator, ButtonWidget.PressAction onPress) {
		T result = this.initAnyButton(text, creator, onPress);

		this.addButton(result);
		choicesCount++;

		return result;
	}

	public <T extends ButtonWidget> T createAnyDynamicButton(Function<T, Text> text, DynamicButtonCreator<T> creator, ButtonWidget.PressAction onPress) {
		T result = this.initAnyDynamicButton(text, creator, onPress);

		this.addButton(result);
		choicesCount++;

		return result;
	}

	public void backToExteriorChangeScreen() {
		MinecraftClient.getInstance().setScreen(this.parent);
	}

	public void toSecurityScreen() {
		MinecraftClient.getInstance().setScreen(new TardisSecurityScreen(tardis().getUuid(), this.console, this));
	}


	final int UV_BASE = 159;
	final int UV_INCREMENT = 17;

	int calculateUvOffsetForRange(int progress) {
		int rangeProgress = progress % 20;
		return (rangeProgress / 5) * UV_INCREMENT;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		tickForSpin++;
		this.renderDesktop(context);
		this.drawBackground(context); // the grey backdrop
		context.getMatrices().push();
		int x = (left + 79);
		int y = (top + 59);
		context.getMatrices().translate(0, 0, 0f);
		context.getMatrices().multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(((float) tickForSpin / 1400L) * 360.0f), x, y, 0);
		context.drawTexture(BACKGROUND, x - 41, y - 41, 173, 173, 83, 83);
		context.getMatrices().pop();

		// FIXME @Loqor, this is dumb.
		int startIndex = DependencyChecker.hasGravity() ? 5 : 4;

		if (!this.buttons.get(startIndex).isHovered()) context.drawTexture(BACKGROUND, left + 149, top + 142, 0, 166, 12, 12);
		if (!this.buttons.get(startIndex + 1).isHovered()) context.drawTexture(BACKGROUND, left + 232, top + 142, 12, 166, 12, 12);
		if (!this.buttons.get(startIndex + 2).isHovered()) context.drawTexture(BACKGROUND, left + 228, top + 111, 0, 178, 16, 16);

		// big triangles
		if (!this.buttons.get(startIndex + 3).isHovered()) context.drawTexture(TEXTURE, left + 149, top + 76, 0, 197, 15, 30);
		if (!this.buttons.get(startIndex + 4).isHovered()) context.drawTexture(TEXTURE, left + 229, top + 76, 30, 197, 15, 30);

		// big apply button
		if (!this.buttons.get(startIndex + 5).isHovered()) context.drawTexture(TEXTURE, left + 168, top + 94, 0, 227, 57, 12);

		if (tardis() == null)
			return;

		// battery
		context.drawTexture(TEXTURE, left + 27, top + 133, 0, tardis().getFuel() > 250 ? 150 : 165, 99, 15);

		// fuel markers @TODO come back and actually do the rest of it with the halves and the red parts too
		for (int p = 0; p < Math.round((tardis().getFuel() / FuelData.TARDIS_MAX_FUEL) * 12); ++p) {
			context.drawTexture(TEXTURE, left + 29 + (8 * p), top + 135, 99, 150, 7, 11);
		}

		int progress = tardis().travel2().getDurationAsPercentage();

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

			context.drawTexture(TEXTURE, left + 32 + (index * 18), top + 114, tardis().travel2().getState() == TravelHandlerBase.State.FLIGHT ? progress >= 100 ? 68 : uvOffset : UV_BASE, 180, 17, 17);
		}

		this.renderHums(context);
		super.render(context, mouseX, mouseY, delta);
	}

	private void drawBackground(DrawContext context) {
		context.drawTexture(BACKGROUND, left, top, 0, 0, bgWidth, bgHeight);
	}

	private void renderDesktop(DrawContext context) {
		context.drawCenteredTextWithShadow(
				this.textRenderer,
				this.selectedDesktop.name(),
				(int) (left + (bgWidth * 0.77f)),
				(int) (top + (bgHeight * 0.58f)),
				0xffffff
		);

		if (this.selectedDesktop == null) return;

		context.getMatrices().push();
		context.getMatrices().translate(0, 0, -50f);
		context.drawTexture(doesTextureExist(this.selectedDesktop.previewTexture().texture()) ?
						this.selectedDesktop.previewTexture().texture() : MISSING_PREVIEW,
				left + 151, top + 14, 91, 91, 0, 0,
				this.selectedDesktop.previewTexture().width * 2,
				this.selectedDesktop.previewTexture().height * 2,
				this.selectedDesktop.previewTexture().width * 2, this.selectedDesktop.previewTexture().height * 2);
		//context.drawTexture(this.selectedDesktop.previewTexture().texture(), left + 151, top + 14, 91, 91, 0, 0, this.selectedDesktop.previewTexture().width * 2, this.selectedDesktop.previewTexture().height * 2, this.selectedDesktop.previewTexture().width * 2, this.selectedDesktop.previewTexture().height * 2);
		context.getMatrices().pop();
	}

	private HumSound getHumSound() {
		return tardis().<ServerHumHandler>handler(TardisComponent.Id.HUM).getHum();
	}

	private void applyHum() {
		ClientSoundManager.getHum().setServersHum(this.hum);
		MinecraftClient.getInstance().setScreen(null);
	}

	private void nextHum() {
		this.hum = nextHum(this.hum);
	}

	private static HumSound nextHum(HumSound current) {
		List<HumSound> list = HumsRegistry.REGISTRY.stream().toList();

		int idx = list.indexOf(current);
		if (idx < 0 || idx + 1 == list.size()) return list.get(0);
		return list.get(idx + 1);
	}

	private void previousHum() {
		this.hum = previousHum(this.hum);
	}

	private static HumSound previousHum(HumSound current) {
		List<HumSound> list = HumsRegistry.REGISTRY.stream().toList();

		int idx = list.indexOf(current);
		if (idx <= 0) return list.get(list.size() - 1);
		return list.get(idx - 1);
	}

	private void renderHums(DrawContext context) {
		if (getHumSound() == null) return;
		Text humsText = Text.translatable("screen.ait.interior.settings.hum");
		context.drawText(
				this.textRenderer,
				humsText,
				(int) (left + (bgWidth * 0.65f)) - this.textRenderer.getWidth(humsText) / 2,
				(int) (top + (bgHeight * 0.7f)),
				0xffffff,
				true
		);
		Text hum = Text.translatable("screen.ait.interior.settings." + this.hum.name());
		context.drawText(
				this.textRenderer,
				hum,
				(int) (left + (bgWidth * 0.76f)) - this.textRenderer.getWidth(hum) / 2,
				(int) (top + (bgHeight * 0.82f)),
				0xffffff,
				true
		);
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
		if (idx < 0 || idx + 1 == list.size()) return list.get(0);
		return list.get(idx + 1);
	}

	private void nextDesktop() {
		this.selectedDesktop = nextDesktop(this.selectedDesktop);

		if (!isCurrentUnlocked()) nextDesktop(); // ooo incursion crash
	}

	private static TardisDesktopSchema previousDesktop(TardisDesktopSchema current) {
		List<TardisDesktopSchema> list = DesktopRegistry.getInstance().toList();

		int idx = list.indexOf(current);
		if (idx <= 0) return list.get(list.size() - 1);
		return list.get(idx - 1);
	}

	private void previousDesktop() {
		this.selectedDesktop = previousDesktop(this.selectedDesktop);

		if (!isCurrentUnlocked()) previousDesktop(); // ooo incursion crash
	}

	private boolean doesTextureExist(Identifier id) {
		return MinecraftClient.getInstance().getResourceManager().getResource(id).isPresent();
	}

	private boolean isCurrentUnlocked() {
		return this.tardis().isUnlocked(this.selectedDesktop);
	}

	@FunctionalInterface
	public interface ButtonCreator<T extends ButtonWidget> {
		T create(int x, int y, int width, int height, Text text, ButtonWidget.PressAction onPress, TextRenderer textRenderer);
	}

	@FunctionalInterface
	public interface DynamicButtonCreator<T extends ButtonWidget> {
		T create(int x, int y, int width, int height, Function<T, Text> text, ButtonWidget.PressAction onPress, TextRenderer textRenderer);
	}
}