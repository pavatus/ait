package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.exterior.*;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.variant.exterior.booth.BoothDefaultVariant;
import mdteam.ait.tardis.variant.exterior.booth.BoothFireVariant;
import mdteam.ait.tardis.variant.exterior.booth.BoothSoulVariant;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxDefaultVariant;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxFireVariant;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxFuturisticVariant;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxSoulVariant;
import mdteam.ait.tardis.variant.exterior.capsule.CapsuleDefaultVariant;
import mdteam.ait.tardis.variant.exterior.capsule.CapsuleFireVariant;
import mdteam.ait.tardis.variant.exterior.capsule.CapsuleSoulVariant;
import mdteam.ait.tardis.variant.exterior.classic.ClassicBoxDefinitiveVariant;
import mdteam.ait.tardis.variant.exterior.classic.ClassicBoxPrimeVariant;
import mdteam.ait.tardis.variant.exterior.classic.ClassicBoxPtoredVariant;
import mdteam.ait.tardis.variant.exterior.classic.ClassicBoxYetiVariant;
import mdteam.ait.tardis.variant.exterior.cube.RedCoobVariant;
import mdteam.ait.tardis.variant.exterior.easter_head.EasterHeadDefaultVariant;
import mdteam.ait.tardis.variant.exterior.easter_head.EasterHeadFireVariant;
import mdteam.ait.tardis.variant.exterior.easter_head.EasterHeadSoulVariant;
import mdteam.ait.tardis.variant.exterior.tardim.TardimDefaultVariant;
import mdteam.ait.tardis.variant.exterior.tardim.TardimFireVariant;
import mdteam.ait.tardis.variant.exterior.tardim.TardimSoulVariant;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ExteriorVariantRegistry {
    public static final SimpleRegistry<ExteriorVariantSchema> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<ExteriorVariantSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "exterior_variant"))).buildAndRegister();
    public static ExteriorVariantSchema register(ExteriorVariantSchema schema) {
        return Registry.register(REGISTRY, schema.id(), schema);
    }

    public static Collection<ExteriorVariantSchema> withParent(ExteriorSchema parent) {
        List<ExteriorVariantSchema> list = new ArrayList<>();

        for (Iterator<ExteriorVariantSchema> it = REGISTRY.iterator(); it.hasNext(); ) {
            ExteriorVariantSchema schema = it.next();
            //AITExteriors.iterator().forEach((System.out::println));

            if (schema.parent().equals(parent)) list.add(schema);
        }

        return list;
    }

    public static ExteriorVariantSchema TARDIM_DEFAULT;
    public static ExteriorVariantSchema TARDIM_FIRE;
    public static ExteriorVariantSchema TARDIM_SOUL;
    public static ExteriorVariantSchema BOX_DEFAULT;
    public static ExteriorVariantSchema BOX_FIRE;
    public static ExteriorVariantSchema BOX_SOUL;
    public static ExteriorVariantSchema BOX_FUTURE;
    public static ExteriorVariantSchema PRIME;
    public static ExteriorVariantSchema YETI;
    public static ExteriorVariantSchema DEFINITIVE;
    public static ExteriorVariantSchema PTORED;
    public static ExteriorVariantSchema CAPSULE_DEFAULT;
    public static ExteriorVariantSchema CAPSULE_SOUL;
    public static ExteriorVariantSchema CAPSULE_FIRE;
    public static ExteriorVariantSchema BOOTH_DEFAULT;
    public static ExteriorVariantSchema BOOTH_FIRE;
    public static ExteriorVariantSchema BOOTH_SOUL;
    public static ExteriorVariantSchema COOB;
    public static ExteriorVariantSchema HEAD_DEFAULT;
    public static ExteriorVariantSchema HEAD_SOUL;
    public static ExteriorVariantSchema HEAD_FIRE;

    // AAAAAAAAAAAAAAAAAAAAAAAAAAA SO MANY VARIABLE
    public static void init() {
        // TARDIM
        TARDIM_DEFAULT = register(new TardimDefaultVariant());
        TARDIM_FIRE = register(new TardimFireVariant());
        TARDIM_SOUL = register(new TardimSoulVariant());

        // Police Box
        BOX_DEFAULT = register(new PoliceBoxDefaultVariant());
        BOX_SOUL = register(new PoliceBoxSoulVariant());
        BOX_FIRE = register(new PoliceBoxFireVariant());
        BOX_FUTURE = register(new PoliceBoxFuturisticVariant());

        // Classic Box
        PRIME = register(new ClassicBoxPrimeVariant());
        YETI = register(new ClassicBoxYetiVariant());
        DEFINITIVE = register(new ClassicBoxDefinitiveVariant());
        PTORED = register(new ClassicBoxPtoredVariant());

        // Capsule
        CAPSULE_DEFAULT = register(new CapsuleDefaultVariant());
        CAPSULE_SOUL = register(new CapsuleSoulVariant());
        CAPSULE_FIRE = register(new CapsuleFireVariant());

        // Booth
        BOOTH_DEFAULT = register(new BoothDefaultVariant());
        BOOTH_FIRE = register(new BoothFireVariant());
        BOOTH_SOUL = register(new BoothSoulVariant());

        // funny
        COOB = register(new RedCoobVariant());

        // Easter Head
        HEAD_DEFAULT = register(new EasterHeadDefaultVariant());
        HEAD_SOUL = register(new EasterHeadSoulVariant());
        HEAD_FIRE = register(new EasterHeadFireVariant());
    }
}
