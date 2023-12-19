package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.client.animation.ClassicAnimation;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.client.models.doors.*;
import mdteam.ait.client.models.exteriors.*;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.tardis.TardisTravel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public enum ExteriorEnum {
    CAPSULE() {
        @Override
        public ExteriorAnimation createAnimation(ExteriorBlockEntity entity) {
            return new ClassicAnimation(entity);
        }

        @Override
        public boolean isDoubleDoor() {
            return true;
        }

        @Override
        public boolean hasEmission() {
            return false;
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public ExteriorModel createModel() {
            return new CapsuleExteriorModel(CapsuleExteriorModel.getTexturedModelData().createModel());
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public DoorModel createDoorModel() {
            return new CapsuleDoorModel(CapsuleDoorModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ExteriorModel> getModelClass() {
            return CapsuleExteriorModel.class;
        }

        @Override
        public Class<? extends DoorModel> getDoorClass() {
            return CapsuleDoorModel.class;
        }

        @Override
        public SoundEvent getDoorCloseSound() {
            return SoundEvents.BLOCK_IRON_DOOR_CLOSE;
        }

        @Override
        public SoundEvent getDoorOpenSound() {
            return SoundEvents.BLOCK_IRON_DOOR_OPEN;
        }
    },
    POLICE_BOX() {
        @Override
        public ExteriorAnimation createAnimation(ExteriorBlockEntity entity) {
            return new PulsatingAnimation(entity);
        }

        @Override
        public boolean isDoubleDoor() {
            return true;
        }

        @Override
        public boolean hasEmission() {
            return true;
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public ExteriorModel createModel() {
            return new PoliceBoxModel(PoliceBoxModel.getTexturedModelData().createModel());
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public DoorModel createDoorModel() {
            return new PoliceBoxDoorModel(PoliceBoxDoorModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ExteriorModel> getModelClass() {
            return PoliceBoxModel.class;
        }

        @Override
        public Class<? extends DoorModel> getDoorClass() {
            return PoliceBoxDoorModel.class;
        }
    },
    CLASSIC() {
        @Override
        public ExteriorAnimation createAnimation(ExteriorBlockEntity entity) {
            return new ClassicAnimation(entity);
        }

        @Override
        public boolean isDoubleDoor() {
            return true;
        }

        @Override
        public boolean hasEmission() {
            return true;
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public ExteriorModel createModel() {
            return new ClassicExteriorModel(ClassicExteriorModel.getTexturedModelData().createModel());
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public DoorModel createDoorModel() {
            return new ClassicDoorModel(ClassicDoorModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ExteriorModel> getModelClass() {
            return ClassicExteriorModel.class;
        }

        @Override
        public Class<? extends DoorModel> getDoorClass() {
            return ClassicDoorModel.class;
        }
    },
    TARDIM() {
        @Override
        public ExteriorAnimation createAnimation(ExteriorBlockEntity entity) {
            return new PulsatingAnimation(entity);
        }

        @Override
        public boolean isDoubleDoor() {
            return true;
        }

        @Override
        public boolean hasEmission() {
            return true;
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public ExteriorModel createModel() {
            return new TardimExteriorModel(TardimExteriorModel.getTexturedModelData().createModel());
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public DoorModel createDoorModel() {
            return new TardimDoorModel(TardimDoorModel.getTexturedModelData().createModel());
        }

        @Override
        public Class<? extends ExteriorModel> getModelClass() {
            return TardimExteriorModel.class;
        }

        @Override
        public Class<? extends DoorModel> getDoorClass() {
            return TardimDoorModel.class;
        }
    },
    BOOTH {
        @Override
        public ExteriorAnimation createAnimation(ExteriorBlockEntity entity) {
            return new PulsatingAnimation(entity);
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public ExteriorModel createModel() {
            return new BoothExteriorModel(BoothExteriorModel.getTexturedModelData().createModel());
        }

        @Override
        @Environment(value = EnvType.CLIENT)
        public DoorModel createDoorModel() {
            return new BoothDoorModel(BoothDoorModel.getTexturedModelData().createModel());
        }

        @Override
        public boolean isDoubleDoor() {
            return false;
        }

        @Override
        public boolean hasEmission() {
            return true;
        }

        @Override
        public Class<? extends ExteriorModel> getModelClass() {
            return BoothExteriorModel.class;
        }

        @Override
        public Class<? extends DoorModel> getDoorClass() {
            return BoothDoorModel.class;
        }

        @Override
        public SoundEvent getDoorCloseSound() {
            return SoundEvents.BLOCK_IRON_DOOR_CLOSE;
        }

        @Override
        public SoundEvent getDoorOpenSound() {
            return SoundEvents.BLOCK_IRON_DOOR_OPEN;
        }
    },
    ;

    public abstract ExteriorAnimation createAnimation(ExteriorBlockEntity entity);

    @Environment(value = EnvType.CLIENT)
    public abstract ExteriorModel createModel();

    @Environment(value = EnvType.CLIENT)
    public abstract DoorModel createDoorModel();

    public abstract boolean isDoubleDoor();

    public abstract boolean hasEmission();

    public abstract Class<? extends ExteriorModel> getModelClass();

    public abstract Class<? extends DoorModel> getDoorClass();

    public SoundEvent getDoorCloseSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
    }

    public SoundEvent getDoorOpenSound() {
        return SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
    }

    public MatSound getSound(TardisTravel.State state) {
        return switch (state) {
            case LANDED, CRASH -> AITSounds.LANDED_ANIM;
            case FLIGHT -> AITSounds.FLIGHT_ANIM;
            case DEMAT -> AITSounds.DEMAT_ANIM;
            case MAT -> AITSounds.MAT_ANIM;
        };
    }
}
