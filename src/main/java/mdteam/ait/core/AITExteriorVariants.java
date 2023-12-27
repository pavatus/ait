package mdteam.ait.core;

import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.variant.exterior.cube.RedCoobVariant;
import mdteam.ait.tardis.variant.exterior.booth.BoothDefaultVariant;
import mdteam.ait.tardis.variant.exterior.booth.BoothFireVariant;
import mdteam.ait.tardis.variant.exterior.booth.BoothSoulVariant;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxDefaultVariant;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxFireVariant;
import mdteam.ait.tardis.variant.exterior.box.PoliceBoxSoulVariant;
import mdteam.ait.tardis.variant.exterior.capsule.CapsuleDefaultVariant;
import mdteam.ait.tardis.variant.exterior.capsule.CapsuleFireVariant;
import mdteam.ait.tardis.variant.exterior.capsule.CapsuleSoulVariant;
import mdteam.ait.tardis.variant.exterior.classic.ClassicBoxPrimeVariant;
import mdteam.ait.tardis.variant.exterior.classic.ClassicBoxYetiVariant;
import mdteam.ait.tardis.variant.exterior.classic.ClassicBoxDefinitiveVariant;
import mdteam.ait.tardis.variant.exterior.easter_head.EasterHeadDefaultVariant;
import mdteam.ait.tardis.variant.exterior.easter_head.EasterHeadFireVariant;
import mdteam.ait.tardis.variant.exterior.easter_head.EasterHeadSoulVariant;
import mdteam.ait.tardis.variant.exterior.tardim.TardimDefaultVariant;
import mdteam.ait.tardis.variant.exterior.tardim.TardimFireVariant;
import mdteam.ait.tardis.variant.exterior.tardim.TardimSoulVariant;
import net.minecraft.util.Identifier;

import java.util.*;

// same comment as i had for consolevariants
public class AITExteriorVariants {
    private static final Map<Identifier, ExteriorVariantSchema> map = new HashMap<>();

    /**
     * Exterior Variants are registered here:
     */
    public static void init() {
        // TARDIM
        register(new TardimDefaultVariant());
        register(new TardimFireVariant());
        register(new TardimSoulVariant());

        // Police Box
        register(new PoliceBoxDefaultVariant());
        register(new PoliceBoxSoulVariant());
        register(new PoliceBoxFireVariant());

        // Classic Box
        register(new ClassicBoxPrimeVariant());
        register(new ClassicBoxYetiVariant());
        register(new ClassicBoxDefinitiveVariant());

        // Capsule
        register(new CapsuleDefaultVariant());
        register(new CapsuleSoulVariant());
        register(new CapsuleFireVariant());

        // Booth
        register(new BoothDefaultVariant());
        register(new BoothFireVariant());
        register(new BoothSoulVariant());

        // funny
        register(new RedCoobVariant());

        //Easter Head
        register(new EasterHeadDefaultVariant());
        register(new EasterHeadSoulVariant());
        register(new EasterHeadFireVariant());
    }

    public static void register(ExteriorVariantSchema variant) {
        map.put(variant.id(), variant);
    }

    public static ExteriorVariantSchema get(Identifier id) {
        return map.get(id);
    }

    public static Collection<ExteriorVariantSchema> iterator() {
        return map.values();
    }
    public static Collection<ExteriorVariantSchema> withParent(ExteriorSchema parent) {
        List<ExteriorVariantSchema> list = new ArrayList<>();

        for (ExteriorVariantSchema schema : iterator()) {
            //AITExteriors.iterator().forEach((System.out::println));

            if (schema.parent().equals(parent)) list.add(schema);
        }

        return list;
    }
}
