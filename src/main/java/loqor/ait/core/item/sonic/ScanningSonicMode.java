package loqor.ait.core.item.sonic;

import net.minecraft.util.Identifier;

import loqor.ait.data.schema.sonic.SonicSchema;

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
