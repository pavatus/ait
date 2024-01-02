package mdteam.ait.tardis.control.impl;

import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class HandBrakeControl extends Control {
    public HandBrakeControl() {
        super("handbrake");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        if(tardis.isInDanger())
            return false;

        PropertiesHandler.setBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE, !PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE));
        if(tardis.isRefueling())
            tardis.setRefueling(false);

        tardis.markDirty();

        messagePlayer(player, PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.HANDBRAKE));

        if (tardis.getTravel().getState() == TardisTravel.State.FLIGHT) {
            tardis.getTravel().crash();
        }

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean var) {
        Text on = Text.translatable("tardis.message.control.handbrake.on");
        Text off = Text.translatable("tardis.message.control.handbrake.off");
        player.sendMessage((var? on : off), true);
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.HANDBRAKE_LEVER_PULL;
    }

    @Override
    public boolean shouldFailOnNoPower() {
        return false;
    }
}
