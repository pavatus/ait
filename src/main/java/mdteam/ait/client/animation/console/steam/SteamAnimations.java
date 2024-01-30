package mdteam.ait.client.animation.console.steam;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class SteamAnimations {


    public static final Animation CONSOLE_STEAM_FLIGHT = Animation.Builder.create(4.32f).looping()
            .addBoneAnimation("base7",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.72f, AnimationHelper.createTranslationalVector(0f, 3f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.44f, AnimationHelper.createTranslationalVector(0f, 3f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2.16f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(2.88f, AnimationHelper.createTranslationalVector(0f, -2f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(3.6f, AnimationHelper.createTranslationalVector(0f, -2f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(4.32f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("spin",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.72f, AnimationHelper.createRotationalVector(-90f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.44f, AnimationHelper.createRotationalVector(-270f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2.16f, AnimationHelper.createRotationalVector(-360f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(3.6f, AnimationHelper.createRotationalVector(-358.75f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(4.32f, AnimationHelper.createRotationalVector(-360f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

}
