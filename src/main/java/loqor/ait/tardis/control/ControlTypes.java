package loqor.ait.tardis.control;

import loqor.ait.core.blocks.ConsoleBlock;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.core.entities.ConsoleControlEntity;
import net.minecraft.entity.EntityDimensions;
import org.joml.Vector3f;

/**
 * Holds a control which will be ran when interacted with, an {@linkplain Vector3f offset} from the centre of the {@link ConsoleBlock} and a {@linkplain EntityDimensions scale} for the entity
 * <br><br>
 * A list of these is gotten by {@link ConsoleTypeSchema#getControlTypes()} and used in {@link ConsoleControlEntity} to hold its information
 *
 * @author loqor
 * @see ConsoleControlEntity
 */
public class ControlTypes {
	private final Control control;
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
