package dev.amble.ait.core.item;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PsychpaperItem extends Item {
    private static final Map<String, String> NAME_MAP = new HashMap<>();

    public PsychpaperItem(Settings settings) {
        super(settings);
        registerEvents();
    }

    /** Register event listeners for chat and tick updates */
    private void registerEvents() {
        // Chat event to change name
        ServerMessageEvents.CHAT_MESSAGE.register(this::handleChatMessage);

        // Tick event to reset names if item is not held
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (!isHoldingPsychpaper(player)) {
                    resetName(player);
                }
            }
        });
    }

    /** Process chat messages and change name if the item is held */
    private void handleChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        // Only process if the message starts with "I'm " and if the player holds the item
        String content = message.getContent().getString();
        if (content == null || !content.toLowerCase().startsWith("i'm ")) {
            return;
        }

        String newName = content.substring(4).trim();
        if (newName.isEmpty()) return;  // Don't process if the name is empty

        // Debugging: Check if player is holding the item
        if (isHoldingPsychpaper(sender)) {
            System.out.println(sender.getName().getString() + " is holding the Psychpaper.");
            NAME_MAP.put(sender.getUuidAsString(), newName);  // Track the player's new name
            changeName(sender, newName);  // Update the display name
        } else {
            System.out.println(sender.getName().getString() + " is not holding the Psychpaper.");
        }
    }

    /** Check if the player is holding this item */
    private boolean isHoldingPsychpaper(ServerPlayerEntity player) {
        return player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof PsychpaperItem;
    }

    /** Change the player's display name */
    private void changeName(ServerPlayerEntity player, String newName) {
        player.setCustomName(Text.literal(newName));
        player.setCustomNameVisible(true);
        player.sendMessage(Text.literal("Your name is now " + newName + "!"), false);
        System.out.println("Changed " + player.getName().getString() + "'s name to " + newName);
    }

    /** Reset the player's name to default */
    private void resetName(ServerPlayerEntity player) {
        String uuid = player.getUuidAsString();
        if (NAME_MAP.containsKey(uuid)) {
            player.setCustomName(Text.literal(player.getEntityName()));
            player.setCustomNameVisible(false);
            NAME_MAP.remove(uuid);  // Reset to the default name
            System.out.println("Reset " + player.getName().getString() + "'s name.");
        }
    }

    /** Ensure no action occurs when right-clicking with the item */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    /** Apply "Hero of the Village" effect when item is selected */
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient() || !(entity instanceof PlayerEntity player) || !selected) return;

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.HERO_OF_THE_VILLAGE, 200, 0, true, false, false));
    }
}
