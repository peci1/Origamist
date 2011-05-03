/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

/**
 * Array utilities.
 * 
 * @author Martin Pecka
 */
public class Arrays
{
    private Arrays()
    {
    }

    /**
     * Check if the given array contains the given value.
     * 
     * @param haystack The array to search in.
     * @param needle The value to search.
     * @return True if haystack contains needle. If haystack is null, false is returned always.
     */
    public static boolean contains(int[] haystack, int needle)
    {
        if (haystack == null)
            return false;
        for (int trial : haystack) {
            if (trial == needle)
                return true;
        }
        return false;
    }

    /**
     * Check if the given array contains the given value.
     * 
     * @param haystack The array to search in.
     * @param needle The value to search.
     * @return True if haystack contains needle. If haystack is null, false is returned always.
     */
    public static boolean contains(byte[] haystack, byte needle)
    {
        if (haystack == null)
            return false;
        for (byte trial : haystack) {
            if (trial == needle)
                return true;
        }
        return false;
    }

    /**
     * Check if the given array contains the given value.
     * 
     * @param haystack The array to search in.
     * @param needle The value to search.
     * @return True if haystack contains needle. If haystack is null, false is returned always.
     */
    public static boolean contains(float[] haystack, float needle)
    {
        if (haystack == null)
            return false;
        for (float trial : haystack) {
            if (trial == needle)
                return true;
        }
        return false;
    }

    /**
     * Check if the given array contains the given value.
     * 
     * @param haystack The array to search in.
     * @param needle The value to search.
     * @return True if haystack contains needle. If haystack is null, false is returned always.
     */
    public static boolean contains(double[] haystack, double needle)
    {
        if (haystack == null)
            return false;
        for (double trial : haystack) {
            if (trial == needle)
                return true;
        }
        return false;
    }

    /**
     * Check if the given array contains the given value.
     * 
     * @param haystack The array to search in.
     * @param needle The value to search.
     * @return True if haystack contains needle. If haystack is null, false is returned always.
     */
    public static boolean contains(boolean[] haystack, boolean needle)
    {
        if (haystack == null)
            return false;
        for (boolean trial : haystack) {
            if (trial == needle)
                return true;
        }
        return false;
    }

    /**
     * Check if the given array contains the given value.
     * 
     * @param haystack The array to search in.
     * @param needle The value to search.
     * @return True if haystack contains needle. If haystack is null, false is returned always.
     */
    public static boolean contains(long[] haystack, long needle)
    {
        if (haystack == null)
            return false;
        for (long trial : haystack) {
            if (trial == needle)
                return true;
        }
        return false;
    }

    /**
     * Check if the given array contains the given value.
     * 
     * @param haystack The array to search in.
     * @param needle The value to search.
     * @return True if haystack contains needle. If haystack is null, false is returned always.
     */
    public static <T> boolean contains(T[] haystack, T needle)
    {
        if (haystack == null)
            return false;
        if (needle == null) {
            for (T trial : haystack) {
                if (trial == null)
                    return true;
            }
        } else {
            for (T trial : haystack) {
                if (needle.equals(trial))
                    return true;
            }
        }
        return false;
    }

    /**
     * Check if the given array contains the given value.
     * 
     * @param haystack The array to search in.
     * @param needle The value to search.
     * @param cmpInstances If true, use <code>==</code> instead of <code>equals()</code> for object comparison.
     * @return True if haystack contains needle. If haystack is null, false is returned always.
     */
    public static <T> boolean contains(T[] haystack, T needle, boolean cmpInstances)
    {
        if (!cmpInstances)
            return contains(haystack, needle);

        if (haystack == null)
            return false;

        for (T trial : haystack) {
            if (needle == trial)
                return true;
        }
        return false;
    }
}
