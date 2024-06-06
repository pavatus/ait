package loqor.ait.tardis.data.properties.v2;

import loqor.ait.tardis.util.Disposable;

import java.util.HashMap;

public class PropertyMap extends HashMap<String, Value<?>> implements Disposable {

    @Override
    public void dispose() {
        for (Value<?> value : this.values()) {
            value.dispose();
        }
    }
}
