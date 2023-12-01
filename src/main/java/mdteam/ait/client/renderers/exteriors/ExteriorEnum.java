package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.client.animation.ClassicAnimation;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.FalloutExteriorModel;
import mdteam.ait.client.models.exteriors.ToyotaExteriorModel;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import the.mdteam.ait.TardisTravel;

public enum ExteriorEnum {
    SHELTER() {
        @Override
        public ExteriorAnimation createAnimation(ExteriorBlockEntity entity) {
            return new ClassicAnimation(entity);
        }

        @Override
        public ExteriorModel createModel() {
            return new FalloutExteriorModel(FalloutExteriorModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ExteriorModel> getModelClass() {
            return FalloutExteriorModel.class;
        }
    },
    TOYOTA() {
        @Override
        public ExteriorAnimation createAnimation(ExteriorBlockEntity entity) {
            return new PulsatingAnimation(entity);
        }

        @Override
        public ExteriorModel createModel() {
            return new ToyotaExteriorModel(ToyotaExteriorModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ExteriorModel> getModelClass() {
            return ToyotaExteriorModel.class;
        }
    };

    public abstract ExteriorAnimation createAnimation(ExteriorBlockEntity entity);
    public abstract ExteriorModel createModel();
    public abstract Class<? extends ExteriorModel> getModelClass();

    public MatSound getSound(TardisTravel.State state) {
        return switch(state) {
            case LANDED, FLIGHT -> AITSounds.LANDED_ANIM;
            case DEMAT -> AITSounds.DEMAT_ANIM;
            case MAT -> AITSounds.MAT_ANIM;
        };
    }
}
