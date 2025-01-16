package loqor.ait.core.item;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;

import loqor.ait.AITMod;
import loqor.ait.api.link.LinkableItem;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.core.world.TardisServerWorld;

public class HandlesItem extends LinkableItem {
    private static final Map<String, HandlesResponses> RESPONSE_MAP = new HashMap<>();
    public HandlesItem(Settings settings) {
        super(settings, true);
    }

    static {
        ServerMessageEvents.CHAT_MESSAGE.register(HandlesItem::onChatMessage);
    }

    private static void onChatMessage(SignedMessage signedMessage, ServerPlayerEntity player, MessageType.Parameters parameters) {
        ItemStack stack;

        String messageSignedContent = signedMessage.getSignedContent();

        boolean bl = messageSignedContent.toLowerCase().startsWith("handles");

        if (player.getWorld().isClient()) return;

        if (!bl) return;

        for (int i = 0; i < player.getInventory().size(); i++) {
            stack = player.getInventory().getStack(i);

            if (stack.getItem() instanceof HandlesItem item) {

                if (item.isLinked(stack)) {

                    Tardis tardis = HandlesItem.getTardis(player.getWorld(), stack);
                    HandlesResponses response = item.getHandlesResponses(messageSignedContent);

                    if (tardis.butler().getHandles() == null)
                        respond(tardis, player, response, stack);
                    break;
                }
            }
        }

        if (!TardisServerWorld.isTardisDimension(player.getWorld())) return;

        Tardis tardis = ((TardisServerWorld) player.getWorld()).getTardis();

        if (tardis.butler().getHandles() == null) return;

        if (!(tardis.butler().getHandles().getItem() instanceof HandlesItem item)) return;

        HandlesResponses response = item.getHandlesResponses(messageSignedContent);

        System.out.println(messageSignedContent.toLowerCase().replace(",", "").replace("handles ", ""));

        respond(tardis, player, response, tardis.butler().getHandles());
    }

    private static void respond(Tardis tardis, PlayerEntity player, HandlesResponses response, ItemStack stack) {
        response.run(tardis, (ServerWorld) player.getWorld(), player.getBlockPos(), player, stack);
        player.sendMessage(response.getResponseText(tardis, player), true);
    }

    static {
        RESPONSE_MAP.put("take off", HandlesResponses.TAKE_OFF);
        RESPONSE_MAP.put("takeoff", HandlesResponses.TAKE_OFF);
        RESPONSE_MAP.put("start flight", HandlesResponses.TAKE_OFF);
        RESPONSE_MAP.put("fly", HandlesResponses.TAKE_OFF);

        RESPONSE_MAP.put("land", HandlesResponses.LAND);
        RESPONSE_MAP.put("stop flight", HandlesResponses.LAND);
        RESPONSE_MAP.put("stop flying", HandlesResponses.LAND);

        RESPONSE_MAP.put("toggle shields", HandlesResponses.TOGGLE_SHIELDS);
        RESPONSE_MAP.put("shields", HandlesResponses.TOGGLE_SHIELDS);

        RESPONSE_MAP.put("toggle alarms", HandlesResponses.TOGGLE_ALARMS);
        RESPONSE_MAP.put("alarms", HandlesResponses.TOGGLE_ALARMS);

        RESPONSE_MAP.put("toggle antigravs", HandlesResponses.TOGGLE_ANTIGRAVS);
        RESPONSE_MAP.put("antigravs", HandlesResponses.TOGGLE_ANTIGRAVS);

        RESPONSE_MAP.put("toggle cloak", HandlesResponses.TOGGLE_CLOAK);
        RESPONSE_MAP.put("cloak", HandlesResponses.TOGGLE_CLOAK);
    }

    public HandlesResponses getHandlesResponses(String lastMessage) {
        return RESPONSE_MAP.getOrDefault(lastMessage.toLowerCase().replace(",", "")
                .replace("handles ", ""), HandlesResponses.DEFAULT);
    }

    public enum HandlesResponses implements StringIdentifiable {
        DEFAULT {
            @Override
            public Text getResponseText(Tardis tardis, PlayerEntity player) {
                return Text.translatable("message.ait.handles.default", player.getName());
            }

            @Override
            public void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null) return;
                failed(tardis, player, world);
            }

            @Override
            public void failed(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITMod.RANDOM.nextBoolean() ? AITSounds.HANDLES_PARDON : AITSounds.HANDLES_PLEASE_ASK_AGAIN, SoundCategory.PLAYERS, 1f, 1f);
                });
            }

            @Override
            public void success(Tardis tardis, PlayerEntity player, ServerWorld world) {
            }
        },
        TAKE_OFF {
            @Override
            public Text getResponseText(Tardis tardis, PlayerEntity player) {
                return Text.translatable("message.ait.handles.take_off", player.getName());
            }
            @Override
            public void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null) return;

                if (!tardis.travel().isLanded()) {
                    failed(tardis, player, world);
                    return;
                }

                boolean doors = tardis.door().isOpen();
                boolean handbrake = tardis.travel().handbrake();
                boolean speed = tardis.travel().speed() <= 0;

                if (handbrake) tardis.travel().handbrake(false);
                if (doors) tardis.door().closeDoors();
                if (speed) tardis.travel().increaseSpeed();
                success(tardis, player, world);
            }

            @Override
            public void failed(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_DENIED, SoundCategory.PLAYERS, 1f, 1f);
                });
            }

            @Override
            public void success(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_AFFIRMATIVE, SoundCategory.PLAYERS, 1f, 1f);
                });
            }
        },
        LAND {
            @Override
            public Text getResponseText(Tardis tardis, PlayerEntity player) {
                return Text.translatable("message.ait.handles.land", player.getName());
            }
            @Override
            public void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null) return;

                if (tardis.travel().getState() == TravelHandlerBase.State.DEMAT) {
                    tardis.travel().handbrake(true);
                    success(tardis, player, world);
                    return;
                }

                if (!tardis.travel().inFlight() || tardis.flight().isFlying()) {
                    failed(tardis, player, world);
                    return;
                }

                boolean speed = tardis.travel().speed() > 0;
                boolean doors = tardis.door().isOpen();

                if (doors) tardis.door().closeDoors();
                if (speed) tardis.travel().speed(0);
                tardis.travel().handbrake(true);
                success(tardis, player, world);
            }
            @Override
            public void failed(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_DENIED, SoundCategory.PLAYERS, 1f, 1f);
                });
            }

            @Override
            public void success(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_AFFIRMATIVE, SoundCategory.PLAYERS, 1f, 1f);
                });
            }
        },
        TOGGLE_SHIELDS {
            @Override
            public Text getResponseText(Tardis tardis, PlayerEntity player) {
                return Text.translatable("message.ait.handles.toggle_shields", tardis.shields().shielded());
            }
            @Override
            public void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null) return;

                tardis.shields().toggle();
                success(tardis, player, world);
            }
            @Override
            public void failed(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_DENIED, SoundCategory.PLAYERS, 1f, 1f);
                });
            }

            @Override
            public void success(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_AFFIRMATIVE, SoundCategory.PLAYERS, 1f, 1f);
                });
            }
        },
        TOGGLE_ANTIGRAVS {
            @Override
            public Text getResponseText(Tardis tardis, PlayerEntity player) {
                return Text.translatable("message.ait.handles.toggle_antigravs", tardis.travel().antigravs());
            }
            @Override
            public void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null) return;

                tardis.travel().antigravs().toggle();
                success(tardis, player, world);
            }
            @Override
            public void failed(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_DENIED, SoundCategory.PLAYERS, 1f, 1f);
                });
            }

            @Override
            public void success(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_AFFIRMATIVE, SoundCategory.PLAYERS, 1f, 1f);
                });
            }
        },
        TOGGLE_CLOAK {
            @Override
            public Text getResponseText(Tardis tardis, PlayerEntity player) {
                return Text.translatable("message.ait.handles.toggle_cloaked", tardis.cloak().cloaked().get());
            }
            @Override
            public void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null) return;

                tardis.cloak().cloaked().set(!tardis.cloak().cloaked().get());
                success(tardis, player, world);
            }
            @Override
            public void failed(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_DENIED, SoundCategory.PLAYERS, 1f, 1f);
                });
            }

            @Override
            public void success(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_AFFIRMATIVE, SoundCategory.PLAYERS, 1f, 1f);
                });
            }
        },
        TOGGLE_ALARMS {
            @Override
            public Text getResponseText(Tardis tardis, PlayerEntity player) {
                return Text.translatable("message.ait.handles.toggle_alarms", tardis.alarm().enabled());
            }
            @Override
            public void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
                if (tardis == null) return;

                tardis.alarm().toggle();
                success(tardis, player, world);
            }
            @Override
            public void failed(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_DENIED, SoundCategory.PLAYERS, 1f, 1f);
                });
            }

            @Override
            public void success(Tardis tardis, PlayerEntity player, ServerWorld world) {
                tardis.getDesktop().getConsolePos().forEach(pos -> {
                    player.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            AITSounds.HANDLES_AFFIRMATIVE, SoundCategory.PLAYERS, 1f, 1f);
                });
            }
        };

        public abstract void run(@Nullable Tardis tardis, ServerWorld world, BlockPos pos, PlayerEntity player,
                                 ItemStack stack);

        public abstract Text getResponseText(Tardis tardis, PlayerEntity player);
        public abstract void failed(Tardis tardis, PlayerEntity player, ServerWorld world);

        public abstract void success(Tardis tardis, PlayerEntity player, ServerWorld world);

        @Override
        public String asString() {
            return StringUtils.capitalize(this.toString().replace("_", " "));
        }
    }
}
