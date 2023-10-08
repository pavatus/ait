package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.security.InvalidParameterException;

import static mdteam.ait.AITMod.RADIONBT;
import static mdteam.ait.core.blocks.RadioBlock.*;
import static java.lang.Double.NaN;

public class AITRadioBlockEntity extends BlockEntity {

    public PlayerEntity player;
    public float tickRotT, tickRotV;

    private int timeInSeconds;

    public AITRadioBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.AIT_RADIO_BLOCK_ENTITY_TYPE, pos, state);
        setTickRot("tuner", getTickRot("tuner"));
        setTickRot("volume", getTickRot("volume"));
        toggleRadio(isRadioOn());
    }

    public static void tick(World world1, BlockPos pos, BlockState state, AITRadioBlockEntity be) {
        ++be.timeInSeconds;
        double d = (double) Util.getMeasuringTimeMs() / 1000.0;
        if(be.getTickRot("volume") > 0 && be.hasSecondPassed()) {
            be.timeInSeconds = 0;
            be.spawnNoteParticle(world1, pos);
        }
    }

    public boolean hasSecondPassed() {
        return this.timeInSeconds >= 20;
    }

    private void spawnNoteParticle(World world, BlockPos pos) {
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            Vec3d vec3d = Vec3d.ofBottomCenter(pos).add(0.0, 1.2f, 0.0);
            float f = (float)world.getRandom().nextInt(4) / 24.0f;
            serverWorld.spawnParticles(ParticleTypes.NOTE, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0, NaN, 0.0, 0.0, 1.0F);
            //System.out.println(Math.sin(Math.PI / 2 * Math.cos(Math.PI * 2 * d / f)) / 2.0 + 0.5);
        }
    }

    public void useOn(BlockHitResult hit, BlockState state, PlayerEntity player, World world, boolean isSneaking) {
        this.player = player;
        double mouseX = (hit.getPos().x * 16) - (hit.getBlockPos().getX() * 16);
        double mouseY = (hit.getPos().y * 16) - (hit.getBlockPos().getY() * 16);
        double mouseZ = (hit.getPos().z * 16) - (hit.getBlockPos().getZ() * 16);

        double[] xTuner = {0, 0, PZ_AXIS_TUNER.getMin(Direction.Axis.X), NZ_AXIS_TUNER.getMin(Direction.Axis.X), PX_AXIS_TUNER.getMin(Direction.Axis.X), NX_AXIS_TUNER.getMin(Direction.Axis.X)};
        double[] yTuner = {0, 0, PZ_AXIS_TUNER.getMin(Direction.Axis.Y), NZ_AXIS_TUNER.getMin(Direction.Axis.Y), PX_AXIS_TUNER.getMin(Direction.Axis.Y), NX_AXIS_TUNER.getMin(Direction.Axis.Y)};
        double[] zTuner = {0, 0, PZ_AXIS_TUNER.getMin(Direction.Axis.Z), NZ_AXIS_TUNER.getMin(Direction.Axis.Z), PX_AXIS_TUNER.getMin(Direction.Axis.Z), NX_AXIS_TUNER.getMin(Direction.Axis.Z)};
        double[] xVolume = {0, 0, PZ_AXIS_VOLUME.getMin(Direction.Axis.X), NZ_AXIS_VOLUME.getMin(Direction.Axis.X), PX_AXIS_VOLUME.getMin(Direction.Axis.X), NX_AXIS_VOLUME.getMin(Direction.Axis.X)};
        double[] yVolume = {0, 0, PZ_AXIS_VOLUME.getMin(Direction.Axis.Y), NZ_AXIS_VOLUME.getMin(Direction.Axis.Y), PX_AXIS_VOLUME.getMin(Direction.Axis.Y), NX_AXIS_VOLUME.getMin(Direction.Axis.Y)};
        double[] zVolume = {0, 0, PZ_AXIS_VOLUME.getMin(Direction.Axis.Z), NZ_AXIS_VOLUME.getMin(Direction.Axis.Z), PX_AXIS_VOLUME.getMin(Direction.Axis.Z), NX_AXIS_VOLUME.getMin(Direction.Axis.Z)};

        double width = 2;
        double height = 2;
        double length = 2;
        float multiVal = 36;//22.5;

        boolean tmx = mouseX >= (xTuner[state.get(FACING).ordinal()] * 16) && mouseY >= (yTuner[state.get(FACING).ordinal()] * 16) && mouseZ >= (zTuner[state.get(FACING).ordinal()] * 16)
                && mouseX <= ((xTuner[state.get(FACING).ordinal()] * 16) + width) && mouseY <= ((yTuner[state.get(FACING).ordinal()] * 16) + height) && mouseZ <= ((zTuner[state.get(FACING).ordinal()] * 16) + length);

        boolean vmx = mouseX >= (xVolume[state.get(FACING).ordinal()] * 16) && mouseY >= (yVolume[state.get(FACING).ordinal()] * 16) && mouseZ >= (zVolume[state.get(FACING).ordinal()] * 16)
                && mouseX <= ((xVolume[state.get(FACING).ordinal()] * 16) + width) && mouseY <= ((yVolume[state.get(FACING).ordinal()] * 16) + height) && mouseZ <= ((zVolume[state.get(FACING).ordinal()] * 16) + length);

        if(tmx && this.isRadioOn() && !isSneaking) {
            if (this.tickRotT < (360F - 22.5F) * ((float) Math.PI / 180f)) this.tickRotT = this.tickRotT + 22.5F * ((float) Math.PI / 180f); else if(this.tickRotT >= (360F - 22.5F) * ((float) Math.PI / 180f)) this.tickRotT = 0;
            System.out.println(this.tickRotT +  " ?=? " + 360F * ((float) Math.PI / 180f));
            if (player != null) player.sendMessage(Text.literal("Changing Frequency..."), true);
            world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 0.1F, 1.0F * (this.tickRotT * this.tickRotT));
        }
        if(vmx && this.isRadioOn() && !isSneaking) {
            if(this.tickRotV <= (360F - multiVal) * ((float) Math.PI / 180f)) this.tickRotV = this.tickRotV + multiVal * ((float) Math.PI / 180f); else if(this.tickRotV <= 360F/* - multiVal*/ * ((float) Math.PI / 180f)) this.tickRotV = 0;
            //System.out.println(this.tickRotV +  " ?=? " + 360 * ((float) Math.PI / 180f) + ": " + multiVal * ((float) Math.PI / 180f));
            //if(this.player != null) player.sendMessage(Text.literal("Changing Volume..."), true);
            world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 0.1F, 1.0F * (this.tickRotV * this.tickRotV));
        }

        if (!isSneaking && tmx && this.isRadioOn()) this.setTickRot("tuner", this.tickRotT);

        if (!isSneaking && vmx && this.isRadioOn()) this.setTickRot("volume", this.tickRotV);

        //@TODO Use SoundInstance and Minecraft.getInstance().getSoundManager() to play music. It allows for multiple values like looping and stopping the audio. It's perfect for our use case. - Loqor @Creativious
        if(((int) (Math.nextUp(this.getTickRot("volume") * (180 / Math.PI) * 11) / 360) + 1) == 10) {
            world.playSound(null, pos, AITSounds.SECRET_MUSIC, SoundCategory.MASTER, 1F, 1F);
        }

        if (this.isRadioOn()) {
            if(isSneaking && !vmx && !tmx) {
                this.toggleRadio(!this.isRadioOn());
                if (player != null) player.sendMessage(Text.literal("Radio Off"), true);
                world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_HIT, SoundCategory.MASTER, 0.2F, 1F);
            }
        } else {
            this.toggleRadio(!this.isRadioOn());
            if (player != null) player.sendMessage(Text.literal("Radio On"), true);
            world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_HIT, SoundCategory.MASTER, 0.2F, 2F);
        }


    }

    public double getTickRot(String name) {
        double value;
        switch (name) {
            case "tuner":
                value = RADIONBT.get(this).getTuner();
                return value;
            case "volume":
                value = RADIONBT.get(this).getVolume();
                return value;
            default:
                value = -1;
        }
        if(value < 0) throw new InvalidParameterException(AITMod.MOD_ID + ": Unexpected value in " + this.getClass().getCanonicalName());
        return value;
    }

    public boolean isRadioOn() {
        return RADIONBT.get(this).isOn();
    }

    public void setTickRot(String name, double value) {
        if(name.equals("tuner"))
            RADIONBT.get(this).setTuner(value);
        else if(name.equals("volume"))
            RADIONBT.get(this).setVolume(value);
    }

    public void toggleRadio(boolean bool) {
        RADIONBT.get(this).turnOn(bool);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }
}
