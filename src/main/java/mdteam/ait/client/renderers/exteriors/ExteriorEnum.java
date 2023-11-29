package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;

public enum ExteriorEnum {
    SHELTER() {
        @Override
        public ExteriorAnimation createAnimation(ExteriorBlockEntity entity) {
            return new PulsatingAnimation(entity);
        }
    };

    public abstract ExteriorAnimation createAnimation(ExteriorBlockEntity entity);
}
