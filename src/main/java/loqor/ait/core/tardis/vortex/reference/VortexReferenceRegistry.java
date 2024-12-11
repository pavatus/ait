package loqor.ait.core.tardis.vortex.reference;

import dev.pavatus.register.datapack.SimpleDatapackRegistry;

import loqor.ait.AITMod;

public class VortexReferenceRegistry extends SimpleDatapackRegistry<VortexReference> {
    private static final VortexReferenceRegistry instance = new VortexReferenceRegistry();

    public VortexReferenceRegistry() {
        super(VortexReference::fromInputStream, VortexReference.CODEC, "fx/vortex", true);
    }

    public static VortexReference SPACE;

    @Override
    protected void defaults() {
        SPACE = register(new VortexReference(AITMod.id("space"), AITMod.id("textures/vortex/space.png")));
    }

    @Override
    public VortexReference fallback() {
        return SPACE;
    }

    public static VortexReferenceRegistry getInstance() {
        return instance;
    }
}
