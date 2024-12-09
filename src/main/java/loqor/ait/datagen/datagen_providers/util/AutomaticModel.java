package loqor.ait.datagen.datagen_providers.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to indicate that the datagen should automatically create a block state model and item model for this block
 * todo - move blocks over to this annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutomaticModel {
    boolean justItem() default false;
}
