package mdteam.ait.client.screens.interior;

import mdteam.ait.AITMod;
import mdteam.ait.client.screens.TardisScreen;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

import static mdteam.ait.tardis.handler.InteriorChangingHandler.CHANGE_DESKTOP;

public class InteriorSelectScreen extends TardisScreen {
    private static final Identifier BACKGROUND = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/interior_select.png");
    int bgHeight = 166;
    int bgWidth = 256;
    int left, top;
    private final Screen parent;

    private TardisDesktopSchema selectedDesktop;

    // loqor DONT rewrite with owo lib : (
    public InteriorSelectScreen(UUID tardis, Screen parent) {
        super(Text.translatable("screen." + AITMod.MOD_ID + ".interior_select"), tardis);
        this.parent = parent;
        updateTardis();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        this.selectedDesktop = tardis().getDesktop().getSchema();
        this.top = (this.height - this.bgHeight) / 2; // this means everythings centered and scaling, same for below
        this.left = (this.width - this.bgWidth) / 2;
        this.createButtons();

        super.init();
    }

    private static TardisDesktopSchema findFirstSchema() {
        return DesktopRegistry.REGISTRY.get(0);
    }

    private void createButtons() {
        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.3)) - (textRenderer.getWidth("<") / 2),
                        (int) (top + (bgHeight * 0.85f)),
                        this.textRenderer.getWidth("<"),
                        10,
                        Text.literal("<"),
                        button -> previousDesktop(),
                        this.textRenderer
                )
        );
        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.7f)) - (textRenderer.getWidth(">") / 2),
                        (int) (top + (bgHeight * 0.85f)),
                        this.textRenderer.getWidth(">"),
                        10,
                        Text.literal(">"),
                        button -> nextDesktop(),
                        this.textRenderer
                )
        );
        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.5f)) - (textRenderer.getWidth("apply") / 2),
                        (int) (top + (bgHeight * 0.91f)),
                        this.textRenderer.getWidth("apply"),
                        10,
                        Text.literal("apply"),
                        button -> applyDesktop(),
                        this.textRenderer
                )
        );
        this.addButton(
                new PressableTextWidget(
                        (int) (left + (bgWidth * 0.03f)) - (textRenderer.getWidth("<") / 2),
                        (int) (top + (bgHeight * 0.03f)),
                        this.textRenderer.getWidth("<"),
                        10,
                        Text.literal("<").formatted(Formatting.RED),
                        button -> backToExteriorChangeScreen(),
                        this.textRenderer
                )
        );
    }

    private <T extends ClickableWidget> void addButton(T button) {
        this.addDrawableChild(button);
        button.active = true; // this whole method is unnecessary bc it defaults to true ( ?? )
    }

    private void applyDesktop() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(tardis().getUuid());
        buf.writeIdentifier(this.selectedDesktop.id());

        ClientPlayNetworking.send(CHANGE_DESKTOP, buf);

        MinecraftClient.getInstance().setScreen(null);
    }

    public void backToExteriorChangeScreen() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }


    //@TODO make it use the current interior as the starting point - Loqor
    private static TardisDesktopSchema nextDesktop(TardisDesktopSchema current) {
        List<TardisDesktopSchema> list = DesktopRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(current);
        if (idx < 0 || idx + 1 == list.size()) return list.get(0);
        return list.get(idx + 1);
    }

    private void nextDesktop() {
        this.selectedDesktop = nextDesktop(this.selectedDesktop);

        if (!isCurrentUnlocked()) nextDesktop(); // ooo incursion crash
    }

    private static TardisDesktopSchema previousDesktop(TardisDesktopSchema current) {
        List<TardisDesktopSchema> list = DesktopRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(current);
        if (idx <= 0) return list.get(list.size() - 1);
        return list.get(idx - 1);
    }

    private void previousDesktop() {
        this.selectedDesktop = previousDesktop(this.selectedDesktop);

        if (!isCurrentUnlocked()) previousDesktop(); // ooo incursion crash
    }

    private boolean isCurrentUnlocked() {
        return tardis().isDesktopUnlocked(this.selectedDesktop);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context); // the grey backdrop
        this.renderDesktop(context);
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawBackground(DrawContext context) {
        context.drawTexture(BACKGROUND, left, top, 0, 0, bgWidth, bgHeight);
    }
    private void renderDesktop(DrawContext context) {
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            this.selectedDesktop.name(),
            (int) (left + (bgWidth * 0.5f)),
            (int) (top + (bgHeight * 0.85f)),
            0xadcaf7
        );

        context.drawTexture(this.selectedDesktop.previewTexture().texture(), left + 63, top + 9,128,128,0,0, this.selectedDesktop.previewTexture().width * 2, this.selectedDesktop.previewTexture().height * 2,this.selectedDesktop.previewTexture().width * 2, this.selectedDesktop.previewTexture().height * 2);
    }
}
