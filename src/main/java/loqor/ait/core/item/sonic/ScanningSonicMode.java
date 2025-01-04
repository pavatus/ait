package loqor.ait.core.item.sonic;

import loqor.ait.data.schema.sonic.SonicSchema;
import net.minecraft.util.Identifier;

public class ScanningSonicMode extends SonicMode {

    protected ScanningSonicMode(int index) {
        super(index);
    }

    @Override
    public int maxTime() {
        return 2 * 20;
    }

    @Override
    public Identifier model(SonicSchema.Models models) {
        return models.scanning();
    }
}
