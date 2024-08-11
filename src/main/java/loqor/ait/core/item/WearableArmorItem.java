package loqor.ait.core.item;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import loqor.ait.core.util.AITArmorMaterial;
import loqor.ait.core.util.AITArmorMaterials;

public class WearableArmorItem extends Item implements Equipment {
    private static final EnumMap MODIFIERS = Util.make(new EnumMap(Type.class), (uuidMap) -> {
        uuidMap.put(Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        uuidMap.put(Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        uuidMap.put(Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        uuidMap.put(Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });
    private final boolean hasCustomRenderer;

    public static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            return ArmorItem.dispenseArmor(pointer, stack) ? stack : super.dispenseSilently(pointer, stack);
        }
    };
    protected final Type type;
    private final int protection;
    private final float toughness;
    protected final float knockbackResistance;
    protected final AITArmorMaterial material;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public static boolean dispenseArmor(BlockPointer pointer, ItemStack armor) {
        BlockPos blockPos = pointer.getPos().offset((Direction) pointer.getBlockState().get(DispenserBlock.FACING));
        List<LivingEntity> list = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(blockPos),
                EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.Equipable(armor)));
        if (list.isEmpty()) {
            return false;
        } else {
            LivingEntity livingEntity = (LivingEntity) list.get(0);
            EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(armor);
            ItemStack itemStack = armor.split(1);
            livingEntity.equipStack(equipmentSlot, itemStack);
            if (livingEntity instanceof MobEntity) {
                ((MobEntity) livingEntity).setEquipmentDropChance(equipmentSlot, 2.0F);
                ((MobEntity) livingEntity).setPersistent();
            }

            return true;
        }
    }

    public boolean hasCustomRenderer() {
        return this.hasCustomRenderer;
    }

    public WearableArmorItem(AITArmorMaterial material, Type type, Item.Settings settings, boolean hasCustomRenderer) {
        super(settings.maxDamageIfAbsent(material.getDurability(type)));
        this.material = material;
        this.type = type;
        this.hasCustomRenderer = hasCustomRenderer;
        this.protection = material.getProtection(type);
        this.toughness = material.getToughness();
        this.knockbackResistance = material.getKnockbackResistance();
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uUID = (UUID) MODIFIERS.get(type);
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uUID, "Armor modifier", this.protection,
                EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uUID, "Armor toughness",
                this.toughness, EntityAttributeModifier.Operation.ADDITION));
        if (material == AITArmorMaterials.NETHERITE) {
            builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                    new EntityAttributeModifier(uUID, "Armor knockback resistance", this.knockbackResistance,
                            EntityAttributeModifier.Operation.ADDITION));
        }

        this.attributeModifiers = builder.build();
    }

    public Type getType() {
        return this.type;
    }

    public int getEnchantability() {
        return this.material.getEnchantability();
    }

    public AITArmorMaterial getMaterial() {
        return this.material;
    }

    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.equipAndSwap(this, world, user, hand);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == this.type.getEquipmentSlot() ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }

    public int getProtection() {
        return this.protection;
    }

    public float getToughness() {
        return this.toughness;
    }

    public EquipmentSlot getSlotType() {
        return this.type.getEquipmentSlot();
    }

    public SoundEvent getEquipSound() {
        return this.getMaterial().getEquipSound();
    }

    public enum Type {
        HELMET(EquipmentSlot.HEAD, "helmet"), CHESTPLATE(EquipmentSlot.CHEST,
                "chestplate"), LEGGINGS(EquipmentSlot.LEGS, "leggings"), BOOTS(EquipmentSlot.FEET, "boots");

        private final EquipmentSlot equipmentSlot;
        private final String name;

        Type(EquipmentSlot equipmentSlot, String name) {
            this.equipmentSlot = equipmentSlot;
            this.name = name;
        }

        public EquipmentSlot getEquipmentSlot() {
            return this.equipmentSlot;
        }

        public String getName() {
            return this.name;
        }
    }
}
