package mdteam.ait.core.managers;

import java.util.HashMap;

public class DeltaTimeManager {

    private static final HashMap<String, Long> nextUpdateTimeMap = new HashMap<>();

    /**
     *
     * @param string ID of the delta delay
     * @param timeDelay delay in ms
     */
    public static void createDelay(String string, Long timeDelay) {
        long nextUpdateTime = System.currentTimeMillis() + timeDelay;
        if (!nextUpdateTimeMap.containsKey(string)) {
            nextUpdateTimeMap.put(string, nextUpdateTime);
        } else {
            nextUpdateTimeMap.replace(string, nextUpdateTime);
        }
    }

    public static boolean isStillWaitingOnDelay(String string) {
        if (!nextUpdateTimeMap.containsKey(string)) return false;
        return nextUpdateTimeMap.get(string) > System.currentTimeMillis();
    }
}
