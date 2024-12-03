package loqor.ait.core.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.entities.StaserBoltEntity;

public class StaserBoltMagazine extends Item {
    public StaserBoltMagazine(Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createStaserbolt(World world, ItemStack stack, LivingEntity shooter) {
        StaserBoltEntity staserBoltEntity = new StaserBoltEntity(AITEntityTypes.STASER_BOLT_ENTITY_TYPE, world);
        return staserBoltEntity.createFromConstructor(world, shooter);
    }
}
