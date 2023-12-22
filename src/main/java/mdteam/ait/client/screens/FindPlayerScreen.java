package mdteam.ait.client.screens;

import com.google.common.collect.Lists;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static mdteam.ait.client.renderers.exteriors.ExteriorEnum.*;
import static mdteam.ait.tardis.control.impl.DimensionControl.convertWorldValueToModified;

public class FindPlayerScreen extends TardisScreen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/exterior_changer.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    int backgroundHeight = 133;
    int backgroundWidth = 236;

    public FindPlayerScreen(UUID tardis) {
        super(Text.translatable("screen." + AITMod.MOD_ID + ".find_player"), tardis);
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

    private void createButtons() {
        int i = (this.width - this.backgroundWidth / 2);
        int j = (this.height - this.backgroundHeight / 2);
        this.buttons.clear();
        this.addButton(new PressableTextWidget((width / 2 - 103), (height / 2 + 12),
                this.textRenderer.getWidth("Apply"), 10, Text.literal("Apply"), button -> {
            onPress();
        }, this.textRenderer));
        this.buttons.forEach(buttons -> {
            // buttons.visible = false;
            buttons.active = true;
        });
    }

    public void onPress() {
        if (tardis() != null) {
            // todo;
        }
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = ((this.height) - this.backgroundHeight) / 2;
        context.push();
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        for (int l = 0; l < 7; l++) {
            context.drawTexture(TEXTURE, (i + 11) + (l * 9), j + 5, 20 + (l * 6), 133, 6, 6);
        }
        context.drawTexture(TEXTURE, i + 215, j + 21, 20, 145, 12, 15);
        for (int k = 0; k < 8; k++) {
            context.drawTexture(TEXTURE, (i + 51) + (k * 20), j + 21, 0, 133, 20, 40);
            context.drawTexture(TEXTURE, (i + 51) + (k * 20), j + 61, 0, 133, 20, 40);
        }
        context.pop();
    }

    @Override
    public void renderBackground(DrawContext context) {
        super.renderBackground(context);
    }

    protected void drawPlayerText(DrawContext context) {
        int i = ((this.height - this.backgroundHeight) / 2);
        int j = ((this.width - this.backgroundWidth) / 2);
        if (tardis() == null) {
            return;
        }
        if(MinecraftClient.getInstance().player == null) return;
        List<PlayerListEntry> list = this.collectPlayerEntries();
        int h = 0;
        for (PlayerListEntry playerListEntry : list) {
            h++;
            context.drawText(this.textRenderer, this.getPlayerName(playerListEntry), (width / 2 - 67), (height / 2 + 27) + (9 * h), 0xFFFFFF, true);
        }
    }

    private List<PlayerListEntry> collectPlayerEntries() {
        return MinecraftClient.getInstance().getNetworkHandler().getListedPlayerListEntries().stream().toList();
    }

    public Text getPlayerName(PlayerListEntry entry) {
        if (entry.getDisplayName() != null) {
            return entry.getDisplayName().copy();
        }
        return Text.literal(entry.getProfile().getName());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = ((this.height - this.backgroundHeight) / 2);
        int j = ((this.width - this.backgroundWidth) / 2);
        this.drawBackground(context, delta, mouseX, mouseY);
        this.drawPlayerText(context);
        super.render(context, mouseX, mouseY, delta);
    }
}