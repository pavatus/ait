package loqor.ait.tardis.data.properties;

import java.util.HashMap;

import loqor.ait.tardis.util.Disposable;

public class PropertyMap extends HashMap<String, Value<?>> implements Disposable {

    @SuppressWarnings("unchecked")
    public <T> Value<T> getExact(String key) {
        return (Value<T>) this.get(key);
    }

    @Override
    public void dispose() {
        for (Value<?> value : this.values()) {
            value.dispose();
        }

        this.clear();
    }
}
