package mdteam.ait.mixin.client;


import mdteam.ait.AITMod;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	protected TitleScreenMixin(Text title) {
		super(title);
	}

	private static final RotatingCubeMapRenderer NEWPANO = new RotatingCubeMapRenderer(new CubeMapRenderer(new Identifier(AITMod.MOD_ID, "textures/gui/title/background/panorama")));
	private static final Identifier AIT_CONFIG_TEX = new Identifier(AITMod.MOD_ID, "textures/gui/title/config.png");

	//This modifies the panorama in the background
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V", ordinal = 0))
	private void something(RotatingCubeMapRenderer instance, float delta, float alpha) {
		boolean isConfigEnabled = AITMod.AIT_CONFIG.CUSTOM_MENU();
		if (isConfigEnabled) NEWPANO.render(delta, alpha);
		else instance.render(delta, alpha);
	}

	//This adds buttons, currently just adds the config button I've made.
	//@Inject(at = @At("RETURN"), method = "initWidgetsNormal")
	//private void addCustomButton(int y, int spacingY, CallbackInfo ci) {
	//    int l = this.height / 4 + 48;
	//    this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, l + 72 + 12 - 36, 20, 20, 0, 0, 20,
	//            AIT_CONFIG_TEX, 32, 64, button ->
	//            //this.client.setScreen(new ConfigScreen(this, this.client.options)),
	//            Text.translatable("narrator.button.accessibility")));
	//}
}
