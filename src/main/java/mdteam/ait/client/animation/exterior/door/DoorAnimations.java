package mdteam.ait.client.animation.exterior.door;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class DoorAnimations {

    public static final Animation EXTERIOR_FIRST_OPEN_ANIMATION = Animation.Builder.create(0.875f)
            .addBoneAnimation("left_door",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, 10f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, 60f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5834334f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(0f, 75f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation EXTERIOR_FIRST_CLOSE_ANIMATION = Animation.Builder.create(0.875f)
            .addBoneAnimation("left_door",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, 70f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, 20f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5834334f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(0f, 5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation EXTERIOR_BOTH_CLOSE_ANIMATION = Animation.Builder.create(0.875f)
            .addBoneAnimation("right_door",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, -80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, -70f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, -20f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5834334f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(0f, -5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_door",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, 70f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, 20f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5834334f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(0f, 5f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation EXTERIOR_BOTH_OPEN_ANIMATION = Animation.Builder.create(0.875f)
            .addBoneAnimation("right_door",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, -10f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(0f, -80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, -60f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5834334f, AnimationHelper.createRotationalVector(0f, -80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(0f, -75f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, -80f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_door",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, 10f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, 60f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5834334f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(0f, 75f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation EXTERIOR_SECOND_OPEN_ANIMATION = Animation.Builder.create(0.875f)
            .addBoneAnimation("right_door",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(0f, -10f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(0f, -80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(0f, -60f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5834334f, AnimationHelper.createRotationalVector(0f, -80f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7083434f, AnimationHelper.createRotationalVector(0f, -75f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(0f, -80f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_door",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 80f, 0f),
                                    Transformation.Interpolations.CUBIC))).build();

}
