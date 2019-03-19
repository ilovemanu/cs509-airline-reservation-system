/**
 * 
 */
package utils;

/**
 * @author blake
 * 
 * System Adaptive Parameters (SAPS)
 * 
 * Constants and values for limits, bounds and configuration of system
 *
 */
public class Saps {
	/**
	 * Constant values used for latitude and longitude range validation
	 */
	public static final double MAX_LATITUDE = 90.0;
	public static final double MIN_LATITUDE = -90.0;
	public static final double MAX_LONGITUDE = 180.0;
	public static final double MIN_LONGITUDE = -180.0;

	/**
	 * Constant values used for departure and arrival time
	 * Earliest date for flights in server database is 04 May 2019 at 0000 hrs
	 * Last date for flights in server database is 18 May 2019 at 2359 hrs
	 */
	public static final int DEFAULT_YEAR = 2019;
	public static final int DEFAULT_MONTH = 5;
	public static final int MIN_DATE = 4;
	public static final int MAX_DATE = 18;
	public static final int MIN_HOUR = 0;
	public static final int MAX_HOUR = 23;
	public static final int MIN_MINUTE = 0;
	public static final int MAX_MINUTE = 59;

}
