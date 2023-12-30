package mdteam.ait.tardis.control;

import mdteam.ait.client.animation.console.hartnell.HartnellAnimations;
import mdteam.ait.client.animation.controls.ControlAnimationState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.AnimationState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;

import static mdteam.ait.client.animation.console.hartnell.HartnellAnimations.listOfControlAnimations;

public class Control {

    /*public static HashMap<Integer, ControlAnimationState> animationStates = HartnellAnimations.animationStatePerControl(listOfControlAnimations());

// Start animation for control with ID 0
    public void start(int id, int age) {
        animationStates.get(id).start(age);
    }

    public AnimationState getAnimationState(int id) {
        return animationStates.get(id).getAnimationState();
    }

    public Animation getAnimation(int id) {
        return animationStates.get(id).getAnimation();
    }

    public void startIfNotRunning(int id, int age) {
        animationStates.get(id).startIfNotRunning(age);
    }

// Stop animation for control with ID 1
    public void stop(int id) {
        animationStates.get(id).stop();
    }*/


    public String id; // a name to represent the control

    public Control(String id) {
        this.setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        return false;
    }

    public SoundEvent getSound() {
        return SoundEvents.BLOCK_NOTE_BLOCK_BIT.value();
    }

    @Override
    public String toString() {
        return "Control{" +
                "id='" + id + '\'' +
                '}';
    }
}
