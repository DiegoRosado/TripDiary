package com.anywherelabs.util.time;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Class for Time Utils static methods.
 * 
 * @author drosado
 *
 */

public class TimeUtils {
    
    public static final String FILENAME_DATEFORMATTER_PATTERN = "yyyy_dd_MM-HH_mm_ss_SSS";

    
    /**
     * Return the current Timestamp. 
     * @return the current Timestamp.
     */
    public static Timestamp getNow() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * This method convert from timestamp string to a Timestamp object.
     * 
     * @param the timestamp in string format
     * @return the Timestamp
     * @throws IllegalArgumentException if the string does not fit with
     * the expected pattern which is yyyy-mm-dd hh:mm:ss.nnnnnnnnn
     */
    public static Timestamp fromStringToTimestamp(String string) {
        return Timestamp.valueOf(string);        
    }
    
    public static String toFileNameFormat(Timestamp timestamp) {
        String filenameFormat = new SimpleDateFormat(FILENAME_DATEFORMATTER_PATTERN).format(timestamp);
        return filenameFormat;
    }

}
