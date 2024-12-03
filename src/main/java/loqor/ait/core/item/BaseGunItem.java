package loqor.ait.core.item;

import java.util.List;
import java.util.function.Predicate;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;
import loqor.ait.core.AITStatusEffects;

public class BaseGunItem extends RangedWeaponItem {
    public static final Identifier SHOOT = new Identifier(AITMod.MOD_ID, "shoot_gun");
    public static final Predicate<ItemStack> GUN_PROJECTILES = itemStack -> itemStack.isOf(AITItems.STASER_BOLT_MAGAZINE);
    public static final double MAX_AMMO = 2000;
    public static final String AMMO_KEY = "ammo";

    public BaseGunItem(Settings settings) {
        super(settings);
    }

    static {
        ServerPlayNetworking.registerGlobalReceiver(SHOOT, (server, player, handler, buf, responseSender) -> {
        boolean shoot = buf.readBoolean();
        boolean isAds = buf.readBoolean();

        if (shoot) {
            if (player.getMainHandStack().getItem() instanceof BaseGunItem gun) {
                if (gun.getCurrentAmmo() <= 0) {
                    player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    return;
                }
                BaseGunItem.shoot(player.getWorld(), player, Hand.MAIN_HAND, player.getMainHandStack(), AITItems.STASER_BOLT_MAGAZINE.getDefaultStack(),
                        1.0f, false, 4.0f, player.hasStatusEffect(AITStatusEffects.ZEITON_HIGH) ? 20f : (isAds ? 0.2f : 1.42323f), 0.0f);
                NbtCompound compound = player.getMainHandStack().getOrCreateNbt();
                double current = compound.getDouble(AMMO_KEY);
                double removableAmmo = (isAds ? 15 : 10);
                if (current - removableAmmo <= 0) {
                    compound.putDouble(AMMO_KEY, 0);
                } else {
                    compound.putDouble(AMMO_KEY, current - removableAmmo <= 0 ? 0 : current - removableAmmo);
                }
            }
        }
        });
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putDouble(AMMO_KEY, 0);
        return stack;
    }

    public static void shootGun(boolean shoot, boolean isAds) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(shoot);
        buf.writeBoolean(isAds);
        ClientPlayNetworking.send(BaseGunItem.SHOOT, buf);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient() && entity instanceof PlayerEntity) {
            if (selected) {
                BaseGunItem.shootGun(MinecraftClient.getInstance().options.attackKey.isPressed(), MinecraftClient.getInstance().options.useKey.isPressed());
                MinecraftClient.getInstance().options.attackKey.setPressed(false);
            }
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (otherStack.getItem() instanceof StaserBoltMagazine magazine) {
            double magazineAmmo = magazine.getCurrentFuel(otherStack);
            if (stack.getItem() instanceof BaseGunItem gun) {
                double ammo = gun.getCurrentAmmo();
                if (clickType == ClickType.RIGHT && gun.getCurrentAmmo() < MAX_AMMO) {
                    double residual = (ammo + magazineAmmo) - MAX_AMMO;
                    gun.setCurrentAmmo(ammo + magazineAmmo, stack);
                    magazine.setCurrentFuel(residual, otherStack);
                    return true;
                }
            }
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return GUN_PROJECTILES;
    }

    public double getCurrentAmmo() {
        return new ItemStack(this).getOrCreateNbt().getDouble(AMMO_KEY);
    }

    public void setCurrentAmmo(double var, ItemStack stack) {
        stack.getOrCreateNbt().putDouble(AMMO_KEY, Math.min(var, MAX_AMMO));
    }

    public double getMaxAmmo() {
        return MAX_AMMO;
    }

    @Override
    public int getRange() {
        return 24;
    }

    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack gun, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        PersistentProjectileEntity projectileEntity;
        if (world.isClient) {
            return;
        }
        projectileEntity = BaseGunItem.createBolt(world, shooter, gun, projectile);
        if (creative || simulated != 0.0f) {
            projectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        }
        if (shooter instanceof CrossbowUser crossbowUser) {
            crossbowUser.shoot(crossbowUser.getTarget(), gun, projectileEntity, simulated);
        } else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0f);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis(simulated * ((float)Math.PI / 180), vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = shooter.getRotationVec(1.0f);
            Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
            projectileEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
        }
        gun.damage(3, shooter, e -> e.sendToolBreakStatus(hand));
        projectileEntity.setPos(shooter.getX(), shooter.getY() + 1.2f, shooter.getZ());
        world.spawnEntity(projectileEntity);
        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), AITSounds.STASER, SoundCategory.PLAYERS, 1.0f, soundPitch);
    }

    private static PersistentProjectileEntity createBolt(World world, LivingEntity entity, ItemStack gun, ItemStack bolt) {
        StaserBoltMagazine boltItem = (StaserBoltMagazine)(bolt.getItem() instanceof StaserBoltMagazine ? bolt.getItem() : AITItems.STASER_BOLT_MAGAZINE);
        PersistentProjectileEntity persistentProjectileEntity = boltItem.createStaserbolt(world, bolt, entity);
        if (entity instanceof PlayerEntity) {
            persistentProjectileEntity.setCritical(true);
        }
        persistentProjectileEntity.setSound(AITSounds.STASER);
        persistentProjectileEntity.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getLevel(Enchantments.PIERCING, gun);
        if (i > 0) {
            persistentProjectileEntity.setPierceLevel((byte)i);
        }
        return persistentProjectileEntity;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        double currentAmmo = this.getCurrentAmmo();
        Formatting ammoColor = currentAmmo > (MAX_AMMO / 4) ? Formatting.GREEN : Formatting.RED;

        tooltip.add(
                Text.translatable("message.ait.ammo", currentAmmo)
                        .formatted(ammoColor)
                        .append(Text.literal(" / ").formatted(Formatting.GRAY))
                        .append(Text.literal(String.valueOf(MAX_AMMO)).formatted(Formatting.GRAY))
        );
    }
}
