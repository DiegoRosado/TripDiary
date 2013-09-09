package com.anywherelabs.util.utils;

public class BooleanUtils {

    // Constants
    public static final int INT_TRUE_VALUE = 1;
    public static final int INT_FALSE_VALUE = 0;
    
    // Constructors
    private BooleanUtils() {
        // Util class
    }

    // Methods
    /**
     * Create a int that match with the boolean value
     * @param bool
     * @return the int value
     */
    public static int fromBoolToInt(boolean bool) {
        return bool ? INT_TRUE_VALUE: INT_FALSE_VALUE;
    }
    
    /**
     * Create a boolean value which match with the int value 
     * @param intValue
     * @return the boolean value
     */
    public static boolean fromIntToBool(int intValue) {
        return (intValue==INT_FALSE_VALUE) ? false : true;
    }
    
    
}
