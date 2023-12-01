package mdteam.ait.core.entities;

import mdteam.ait.core.AITEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import the.mdteam.ait.Tardis;

public class ConsoleControlEntity extends BaseControlEntity {

    public ConsoleControlEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    protected ConsoleControlEntity(World world, Tardis tardis) {
        super(world, tardis);

        this.setTardis(tardis);
    }
}
