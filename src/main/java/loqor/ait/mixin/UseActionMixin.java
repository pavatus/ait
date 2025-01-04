package loqor.ait.mixin;

import loqor.ait.api.AITUseActions;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(UseAction.class)
public class UseActionMixin implements AITUseActions {

    private static final UseAction SONIC = init();

    @Invoker("<init>")
    private static UseAction init() {
        throw new AssertionError();
    }

    @Override
    public UseAction ait$sonic() {
        return SONIC;
    }
}
