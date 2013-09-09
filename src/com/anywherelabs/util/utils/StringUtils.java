package com.anywherelabs.util.utils;

public class StringUtils {

    // Constructors
    private StringUtils() {
        // Utils class
    }
    
    /**
     * Check whether the string is null or empty. It did not check if the string has
     * just white spaces.
     * @param string
     * @return true whether the string is null or empty
     */
    public static boolean isNullOrEmpty(String string) {
        boolean nullOrEmpty = true;
        if (string!=null) {
            if (string.length()>0) {
                nullOrEmpty = false;
            }
        }
        return nullOrEmpty;
    }
    
    /**
     * Check whether the string is null, empty or has just white spaces.
     * @param string
     * @return true whether the string is null, empty or has just white spaces.
     */
    public static boolean isNullOrTrimEmpty(String string) {
        boolean nullOrTrimEmpty = true;
        if (string!=null) {
            if (string.trim().length()>0) {
                nullOrTrimEmpty = false;
            }
        }
        return nullOrTrimEmpty;
    }
    
    
}
