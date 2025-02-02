package loqor.ait.core.item;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.*;
import net.minecraft.world.World;

import loqor.ait.api.AITUseActions;
import loqor.ait.api.ArtronHolderItem;
import loqor.ait.api.link.LinkableItem;
import loqor.ait.client.sounds.ClientSoundManager;
import loqor.ait.core.AITSounds;
import loqor.ait.core.item.sonic.SonicMode;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.data.schema.sonic.SonicSchema;
import loqor.ait.registry.impl.SonicRegistry;


public class SonicItem2 extends LinkableItem implements ArtronHolderItem {

    public static final double MAX_FUEL = 1000;
    public static final String MODE_KEY = "mode";
    public static final String SONIC_TYPE = "sonic_type";

    public SonicItem2(Settings settings) {
        super(settings, true);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt(MODE_KEY, 0);
        nbt.putDouble(FUEL_KEY, getMaxFuel(stack));
        if (SonicRegistry.DEFAULT != null)
            nbt.putString(SONIC_TYPE, SonicRegistry.DEFAULT.id().toString());

        return stack;
    }

    public static SonicSchema findSchema(NbtCompound nbt) {
        String rawId = nbt.getString(SONIC_TYPE);

        if (rawId == null)
            return SonicRegistry.DEFAULT;

        Identifier id = Identifier.tryParse(rawId);
        SonicSchema schema = SonicRegistry.getInstance().get(id);

        return schema == null ? SonicRegistry.DEFAULT : schema;
    }

    public static SonicSchema findSchema(ItemStack stack) {
        return findSchema(stack.getOrCreateNbt());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        SonicMode mode = mode(stack);

        if (mode == null)
            return TypedActionResult.fail(stack);

        if (user.isSneaking()) {
            mode = mode.next();
            setMode(stack, mode);

            world.playSound(user, user.getBlockPos(), AITSounds.SONIC_SWITCH, SoundCategory.PLAYERS, 1F, 1F);
            user.sendMessage(mode.text(), true);

            return TypedActionResult.consume(stack);
        }

        if (mode.startUsing(stack, world, user, hand)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.fail(stack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return ((AITUseActions) (Object) UseAction.NONE).ait$sonic();
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient())
            ClientSoundManager.getSonicSound().onUse((AbstractClientPlayerEntity) user);

        SonicMode mode = mode(stack);
        int ticks = mode.maxTime() - remainingUseTicks;

        if (ticks % 10 == 0)
            removeFuel(1, stack);

        mode.tick(stack, world, user, ticks, remainingUseTicks);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (world.isClient())
            ClientSoundManager.getSonicSound().onFinishUse((AbstractClientPlayerEntity) user);

        SonicMode mode = mode(stack);
        mode.stopUsing(stack, world, user, mode.maxTime() - remainingUseTicks, remainingUseTicks);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient())
            ClientSoundManager.getSonicSound().onFinishUse((AbstractClientPlayerEntity) user);

        mode(stack).finishUsing(stack, world, user);
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return mode(stack).maxTime();
    }

    public static SonicMode mode(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        return SonicMode.Modes.get(nbtCompound.getInt(MODE_KEY));
    }

    public static void setMode(ItemStack stack, SonicMode mode) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt(MODE_KEY, mode.index());
    }

    public static SonicSchema getSchema(ItemStack stack) {
        String rawId = stack.getOrCreateNbt().getString(SONIC_TYPE);

        if (rawId == null)
            return SonicRegistry.DEFAULT;

        Identifier id = Identifier.tryParse(rawId);
        SonicSchema schema = SonicRegistry.getInstance().get(id);

        return schema == null ? SonicRegistry.DEFAULT : schema;
    }

    public Tardis tardis(World world, ItemStack stack) {
        return LinkableItem.getTardisFromUuid(world, stack, "tardis");
    }

    @Override
    public double getMaxFuel(ItemStack stack) {
        return MAX_FUEL;
    }
}
