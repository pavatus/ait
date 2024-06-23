package loqor.ait.core.data.base;

import net.minecraft.text.Text;

// TODO: change the String to Text
public interface Nameable {
    String name();

    default Text text() {
        return Text.literal(this.name());
    }
}
