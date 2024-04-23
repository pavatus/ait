package loqor.ait.core.item.part;

import loqor.ait.AITMod;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.util.AITModTags;
import loqor.ait.registry.MachineRecipeSchema;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;

public class MachineItem extends Item {

    public static final Identifier MACHINE_DISASSEMBLE = new Identifier(AITMod.MOD_ID, "machine_disassemble");

    public MachineItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT)
            return false;

        ItemStack machine = slot.getStack();

        // Should this be in SonicItem.Mode.INTERACTION?
        if (!stack.getRegistryEntry().isIn(AITModTags.Items.SONIC_ITEM))
            return false;

        if (SonicItem.findMode(stack) != SonicItem.Mode.INTERACTION)
            return false;

        MachineItem.disassemble(machine);
        return true;
    }

    @Environment(value = EnvType.CLIENT)
    public static void disassemble(ItemStack machine) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeItemStack(machine.copyWithCount(1));

        ClientPlayNetworking.send(MACHINE_DISASSEMBLE, data);
        machine.decrement(1);
    }

    @Environment(value = EnvType.SERVER)
    public static void disassemble(ServerPlayerEntity player, ItemStack machine, MachineRecipeSchema recipe) {
        machine.decrement(1);

        for (ItemStack input : recipe.input()) {
            player.dropItem(input, true);
        }
    }
}
