package mdteam.ait.client.renderers.consoles;

import mdteam.ait.client.animation.console.borealis.BorealisAnimations;
import mdteam.ait.client.models.consoles.BorealisConsoleModel;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.TempConsoleModel;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import the.mdteam.ait.TardisTravel;

public enum ConsoleEnum {
    TEMP() {
        @Override
        public ConsoleModel createModel() {
            return new TempConsoleModel(TempConsoleModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ConsoleModel> getModelClass() {
            return TempConsoleModel.class;
        }
    },
    BOREALIS() {

        @Override
        public ConsoleModel createModel() {
            return new BorealisConsoleModel(BorealisConsoleModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ConsoleModel> getModelClass() {
            return BorealisConsoleModel.class;
        }
    };
    public abstract ConsoleModel createModel();
    public abstract Class<? extends ConsoleModel> getModelClass();

    public MatSound getSound(TardisTravel.State state) {
        return switch(state) {
            case LANDED, FLIGHT -> AITSounds.LANDED_ANIM;
            case DEMAT -> AITSounds.DEMAT_ANIM;
            case MAT -> AITSounds.MAT_ANIM;
        };
    }
}
