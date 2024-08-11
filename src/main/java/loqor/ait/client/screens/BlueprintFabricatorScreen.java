package loqor.ait.client.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class BlueprintFabricatorScreen extends Screen {

    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/blueprinting_deck.png");
    int backgroundHeight = 166;
    int backgroundWidth = 176;

    public BlueprintFabricatorScreen(Text title) {
        super(title);
    }

    protected void drawBackground(DrawContext context) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = ((this.height) - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        drawBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}
