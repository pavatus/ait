package loqor.ait.core.data;

import loqor.ait.core.data.base.Identifiable;
import loqor.ait.core.data.base.Nameable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class BasicSchema implements Identifiable, Nameable {

    private final Text text;

    public BasicSchema() {
        Identifier id = this.id();
        String[] parts = id.getPath().split("/");

        this.text = Text.translatable(parts[0] + "." + id.getNamespace()
                + "." + join('.', 1, parts));
    }

    @Override
    public Text text() {
        return text;
    }

    @Override
    public String name() {
        return text.getString();
    }

    private static String join(char delim, int begin, String[] parts) {
        StringBuilder builder = new StringBuilder();

        for (int i = begin; i < parts.length; i++) {
            builder.append(parts[i]);

            if (i + 1 != parts.length)
                builder.append(delim);
        }

        return builder.toString();
    }
}
