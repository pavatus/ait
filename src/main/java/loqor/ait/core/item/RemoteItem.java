package loqor.ait.core.item;

import static loqor.ait.core.tardis.handler.travel.TravelHandlerBase.State.LANDED;

import java.util.List;
import java.util.UUID;

import dev.pavatus.lib.data.CachedDirectedGlobalPos;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;

import loqor.ait.api.link.LinkableItem;
import loqor.ait.client.tardis.manager.ClientTardisManager;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.impl.DirectionControl;
import loqor.ait.core.tardis.handler.travel.TravelUtil;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.core.world.TardisServerWorld;

public class RemoteItem extends LinkableItem {

    public RemoteItem(Settings settings) {
        super(settings, true);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if (player == null)
            return ActionResult.PASS;

        if (!(world instanceof ServerWorld serverWorld))
            return ActionResult.PASS;

        NbtCompound nbt = itemStack.getOrCreateNbt();

        // Move tardis to the clicked pos
        if (!nbt.contains("tardis"))
            return ActionResult.FAIL;

        ServerTardisManager.getInstance().getTardis(context.getWorld().getServer(),
                getTardisIdFromUuid(itemStack, "tardis"), tardis -> {
                    if (tardis == null)
                        return;

                    if (tardis.getFuel() <= 0)
                        player.sendMessage(Text.translatable("message.ait.remoteitem.warning1"));

                    if (tardis.isRefueling())
                        player.sendMessage(Text.translatable("message.ait.remoteitem.cancel.refuel"));

                        //It was dematting before anyway so as a lazy fix its a feature now!!
                        //player.sendMessage(Text.translatable("message.ait.remoteitem.warning2"));

                    // Check if the Tardis is already present at this location before moving
                    // it there
                    CachedDirectedGlobalPos currentPosition = tardis.travel().position();

                    if (currentPosition.getPos().equals(pos))
                        return;

                    if (!TardisServerWorld.isTardisDimension((ServerWorld) world)) {
                        world.playSound(null, pos, AITSounds.REMOTE, SoundCategory.BLOCKS);

                        BlockPos temp = pos.up();

                        if (world.getBlockState(pos).isReplaceable())
                            temp = pos;

                        tardis.travel().speed(tardis.travel().maxSpeed().get());

                        TravelUtil.travelTo(tardis, CachedDirectedGlobalPos.create(serverWorld, temp, DirectionControl
                                .getGeneralizedRotation(RotationPropertyHelper.fromYaw(player.getBodyYaw()))));
                    } else {
                        world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F,
                                0.2F);
                        player.sendMessage(Text.translatable("message.ait.remoteitem.warning3"), true);
                    }
                });

        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        UUID id = getTardisIdFromUuid(stack, "tardis");
        if (id == null) return;

        Tardis tardis = ClientTardisManager.getInstance().demandTardis(id);

        if (tardis == null)
            return;

        if (tardis.travel().getState() != LANDED)
            tooltip.add(Text.literal("→ " + tardis.travel().getDurationAsPercentage() + "%")
                    .formatted(Formatting.GOLD));
    }
}
