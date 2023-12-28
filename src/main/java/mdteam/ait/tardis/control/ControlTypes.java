package mdteam.ait.tardis.control;

import mdteam.ait.core.blocks.ConsoleBlock;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.console.ConsoleSchema;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.joml.Vector3f;

/**
 * Holds a control which will be ran when interacted with, an {@linkplain Vector3f offset} from the centre of the {@link ConsoleBlock} and a {@linkplain EntityDimensions scale} for the entity
 * <br><br>
 * A list of these is gotten by {@link ConsoleSchema#getControlTypes()} and used in {@link ConsoleControlEntity} to hold its information
 * @see ConsoleControlEntity
 * @author loqor
 */
public class ControlTypes {
    private Control control;
    private EntityDimensions scale;
    private Vector3f offset;

    public ControlTypes(Control control, EntityDimensions scaling, Vector3f offset) {
        this.control = control;
        this.scale = scaling;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "ControlTypes{" +
                "control=" + control +
                ", scale=" + scale +
                ", offset=" + offset +
                '}';
    }

    public Control getControl() {
        return this.control;
    }

    public void setControl(String id) {
        this.control = new Control(id) {
            @Override
            public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
                return true;
            }
        };
    }

    public EntityDimensions getScale() {
        return this.scale;
    }

    public void setScale(EntityDimensions scale) {
        this.scale = scale;
    }

    public Vector3f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }

}
