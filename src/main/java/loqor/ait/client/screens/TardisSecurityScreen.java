package loqor.ait.client.screens;

import java.awt.*;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.core.tardis.handler.loyalty.Loyalty;
import loqor.ait.core.tardis.handler.permissions.PermissionHandler;
import loqor.ait.core.tardis.util.TardisUtil;

public class TardisSecurityScreen extends ConsoleScreen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            "textures/gui/tardis/consoles/monitors/security_menu.png");
    int bgHeight = 117;
    int bgWidth = 191;
    int left, top;
    int choicesCount = 0;
    private final Screen parent;
    private TextFieldWidget landingCodeInput;

    public TardisSecurityScreen(ClientTardis tardis, BlockPos console, Screen parent) {
        super(Text.translatable("screen.ait.security.title"), tardis, console);
        this.parent = parent;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        this.top = (this.height - this.bgHeight) / 2; // this means everythings centered and scaling, same for below
        this.left = (this.width - this.bgWidth) / 2;
        this.createButtons();

        super.init();
    }

    private void createButtons() {
        choicesCount = 0;

        createTextButton(Text.translatable("screen.ait.interiorsettings.back"),
                (button -> backToExteriorChangeScreen()));
        createTextButton(Text.translatable("screen.ait.security.leave_behind"), (button -> toggleLeaveBehind()));
        createTextButton(Text.translatable("screen.ait.security.hostile_alarms"), (button -> toggleHostileAlarms()));
        createTextButton(Text.translatable("screen.ait.security.minimum_loyalty"), (button -> changeMinimumLoyalty()));

        this.landingCodeInput = new TextFieldWidget(this.textRenderer, (int) (left + (bgWidth * 0.06f)), this.top + 60, 120, this.textRenderer.fontHeight + 4,
                Text.literal("Landing Code..."));
        this.addButton(new PressableTextWidget((width / 2 + 40), (height / 2 + 2),
                this.textRenderer.getWidth("✓"), 20, Text.literal("✓").formatted(Formatting.BOLD), button -> {
            updateLandingCode();
        }, this.textRenderer));

        this.landingCodeInput.setMaxLength(50);
        this.landingCodeInput.setDrawsBackground(true);
        this.landingCodeInput.setVisible(true);

        if(this.tardis().landingPad().code().get().isBlank())
            this.landingCodeInput.setPlaceholder(Text.literal("Enter landing code..."));
        else
            this.landingCodeInput.setText(this.tardis().landingPad().code().get());

        this.addSelectableChild(this.landingCodeInput);
    }

    private void toggleLeaveBehind() {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeUuid(tardis().getUuid());
        buf.writeBoolean(!tardis().travel().leaveBehind().get());

        ClientPlayNetworking.send(TardisUtil.LEAVEBEHIND, buf);
    }

    private void changeMinimumLoyalty() {
        ClientTardis tardis = this.tardis();
        PermissionHandler.p19Loyalty(tardis, getMinimumLoyalty(tardis).next());
    }

    private static Loyalty.Type getMinimumLoyalty(ClientTardis tardis) {
        return tardis.<PermissionHandler>handler(TardisComponent.Id.PERMISSIONS).p19Loyalty().get();
    }

    private void toggleHostileAlarms() {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeUuid(tardis().getUuid());
        buf.writeBoolean(!tardis().alarm().hostilePresence().get());

        ClientPlayNetworking.send(TardisUtil.HOSTILEALARMS, buf);
    }

    private void updateLandingCode() {
        String input = this.landingCodeInput.getText();

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeUuid(this.tardis().getUuid());
        buf.writeString(input);

        ClientPlayNetworking.send(TardisUtil.LANDING_CODE, buf);
    }

    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        button.active = true; // this whole method is unnecessary bc it defaults to true ( ?? )
    }

    // this might be useful, so remember this exists and use it later on
    private void createTextButton(Text text, ButtonWidget.PressAction onPress) {
        this.addButton(new PressableTextWidget((int) (left + (bgWidth * 0.06f)),
                (int) (top + (bgHeight * (0.1f * (choicesCount + 1)))), this.textRenderer.getWidth(text), 10, text,
                onPress, this.textRenderer));

        choicesCount++;
    }

    public void backToExteriorChangeScreen() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context);

        ClientTardis tardis = this.tardis();

        context.drawText(this.textRenderer, Text.literal(": " + (tardis.travel().leaveBehind().get() ? "ON" : "OFF")),
                (int) (left + (bgWidth * 0.46f)), (int) (top + (bgHeight * (0.1f * 2))), Color.ORANGE.getRGB(), false);
        context.drawText(this.textRenderer,
                Text.literal(": " + (tardis.alarm().hostilePresence().get() ? "ON" : "OFF")),
                (int) (left + (bgWidth * 0.48f)), (int) (top + (bgHeight * (0.1f * 3))), Color.ORANGE.getRGB(), false);

        context.drawText(this.textRenderer, Text.literal(": " + getMinimumLoyalty(tardis)),
                (int) (left + (bgWidth * 0.48f)), (int) (top + (bgHeight * (0.1f * 4))), Color.ORANGE.getRGB(), false);

        context.drawText(this.textRenderer, Text.literal("Date created:"), (int) (left + (bgWidth * 0.06f)),
                (int) (top + (bgHeight * (0.1f * 7))), 0xadcaf7, false);
        context.drawText(this.textRenderer, Text.literal(tardis.stats().getCreationString()),
                (int) (left + (bgWidth * 0.06f)), (int) (top + (bgHeight * (0.1f * 8))), 0xadcaf7, false);
        this.landingCodeInput.render(context, mouseX, mouseY, delta);
        this.landingCodeInput.setEditableColor(this.landingCodeInput.isSelected() || !this.landingCodeInput.getText().isBlank() ? 0xffffff: 0x545454);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawBackground(DrawContext context) {
        context.drawTexture(TEXTURE, left, top, 0, 0, bgWidth, bgHeight);
    }
}
