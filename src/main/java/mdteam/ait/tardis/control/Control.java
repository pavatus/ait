package mdteam.ait.tardis.control;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

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
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick) {
        return runServer(tardis, player, world);
    }

    public SoundEvent getSound() {
        return SoundEvents.BLOCK_NOTE_BLOCK_BIT.value();
    }
    public boolean shouldFailOnNoPower() {return true;}
    public void runAnimation(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        return; // no animation
    }

    @Override
    public String toString() {
        return "Control{" +
                "id='" + id + '\'' +
                '}';
    }

    public long getDelayLength() {
        return 250L;
    }
    public boolean shouldHaveDelay() {
        return true;
    }
    public boolean ignoresSecurity() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Control control = (Control) o;

        return getId().equals(control.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
