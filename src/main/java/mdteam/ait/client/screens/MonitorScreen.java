package mdteam.ait.client.screens;

import mdteam.ait.AITMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MonitorScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/borealis_monitor.png");

    int backgroundHeight = 176;
    int backgroundWidth = 88;

    public MonitorScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = ((this.height) - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context, delta, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
    }
}