package loqor.ait.tardis.util.desktop.structures;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public abstract class DesktopStructure {
    private final String structureName;
    protected Identifier location;

    public DesktopStructure(String structureName) {
        this(new Identifier(AITMod.MOD_ID, structureName), structureName);
    }

    public DesktopStructure(Identifier location, String structureName) {
        this.location = location;
        this.structureName = structureName;
    }

    public String getStructureName() {
        return this.structureName;
    }

    public Identifier getLocation() {
        return this.location;
    }
}
