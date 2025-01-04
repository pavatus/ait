package loqor.ait.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.util.UseAction;

import loqor.ait.api.AITUseActions;

@Mixin(UseAction.class)
public class UseActionMixin implements AITUseActions {

    private static final UseAction SONIC = init("SONIC", UseAction.values().length);

    @Invoker("<init>")
    private static UseAction init(String name, int ordinal) {
        throw new AssertionError();
    }

    @Override
    public UseAction ait$sonic() {
        return SONIC;
    }
}
