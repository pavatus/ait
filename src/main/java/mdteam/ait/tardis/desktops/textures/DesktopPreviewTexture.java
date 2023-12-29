package mdteam.ait.tardis.desktops.textures;

import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

public class DesktopPreviewTexture {
    private final Identifier path;
    public final int width;
    public final int height;

    public DesktopPreviewTexture(Identifier path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }
    public DesktopPreviewTexture(TardisDesktopSchema schema, int width, int height) {
        this(pathFromDesktopId(schema.id()), width, height);
    }
    public DesktopPreviewTexture(Identifier path) {
        this(path, 128, 128);
    }
    public DesktopPreviewTexture(TardisDesktopSchema schema) {
        this(schema.id());
    }

    public Identifier texture() { return this.path; }

    public static Identifier pathFromDesktopId(Identifier desktopId) {
        return new Identifier(desktopId.getNamespace(), "textures/desktop/" + desktopId.getPath() + ".png");
    }
}
