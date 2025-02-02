package loqor.ait.client.sonic;

import java.util.function.Function;

import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RawSonicModel extends Model {

    public RawSonicModel(Function<Identifier, RenderLayer> layerFactory) {
        super(layerFactory);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {

    }
}
