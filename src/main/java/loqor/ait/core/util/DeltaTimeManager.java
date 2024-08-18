package loqor.ait.core.util;

import java.util.HashMap;

@Deprecated
public class DeltaTimeManager {
    private static final HashMap<String, Long> nextUpdateTimeMap = new HashMap<>();

    /**
     * Creates a delay for the given string with the specified time delay.
     *
     * @param string
     *            the string for which the delay is created
     * @param timeDelay
     *            the time delay in milliseconds
     */
    public static void createDelay(String string, Long timeDelay) {
        long nextUpdateTime = System.currentTimeMillis() + timeDelay;
        if (!nextUpdateTimeMap.containsKey(string)) {
            nextUpdateTimeMap.put(string, nextUpdateTime);
        } else {
            nextUpdateTimeMap.replace(string, nextUpdateTime);
        }
    }

    /**
     * Checks if the given string is still waiting on delay.
     *
     * @param string
     *            the string to check
     * @return true if the string is still waiting on delay, false otherwise
     */
    public static boolean isStillWaitingOnDelay(String string) {
        if (!nextUpdateTimeMap.containsKey(string))
            return false;
        return nextUpdateTimeMap.get(string) > System.currentTimeMillis();
    }

    /**
     * Calculates the time left based on the provided string.
     *
     * @param string
     *            the string for which the time left is calculated
     * @return the time left in milliseconds
     */
    public static int timeLeft(String string) {
        if (!nextUpdateTimeMap.containsKey(string))
            return 0;
        return Math.round(nextUpdateTimeMap.get(string) - System.currentTimeMillis());
    }
}
