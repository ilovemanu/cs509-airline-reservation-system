/**
 *
 */
package airplane;

import java.util.Comparator;


/**
 * This class holds values pertaining to a single Airplane. Class member attributes
 * are the same as defined by the CS509 server API and store values after conversion from
 * XML received from the server to Java primitives. Attributes are accessed via getter and
 * setter methods.
 *
 * @author alex
 * @version 1 2019-03-15
 * @since 2019-03-15
 *
 */

public class Airplane implements Comparable<Airplane>, Comparator<Airplane> {

    /**
     * Airplane attributes as defined by the CS509 server interface XML
     */

    /**
     * Seat classes of the airplane
     */
    public static String COACH = "Coach";
    public static String FIRSTCLASS = "FirstClass";

    /**
     * Manufacturer of the airplane
     */
    private String mManufacturer;

    /**
     * mModel of the airplane
     */
    private String mModel;

    /**
     * Total seats of the airplane
     */
    private int mCoachSeats;
    private int mFirstClassSeats;


    /**
     * Default constructor
     * <p>
     * Constructor without params. Requires object fields to be explicitly
     * set using setter methods
     *
     * @pre None
     * @post member attributes are initialized to invalid default values
     */
    public Airplane() {
        mManufacturer = "";
        mModel = "";
        mCoachSeats = Integer.MAX_VALUE;
        mFirstClassSeats = Integer.MAX_VALUE;

    }

    /**
     * Initializing constructor.
     * <p>
     * All attributes are initialized with specified input values following validation for reasonableness.
     *
     * @param manufacturer    The manufacturer of the airplane
     * @param model           The model of the airplane
     * @param firstClassSeats The number of first class seats
     * @param coachSeats      The number of coach seats
     * @throws IllegalArgumentException if any parameter is determined invalid
     * @pre manufacturer and model are not empty, seats are valid values
     * @post member attributes are initialized with input parameter values
     */
    public Airplane(String manufacturer, String model, int coachSeats, int firstClassSeats) {
        if (!isValidString(manufacturer))
            throw new IllegalArgumentException(manufacturer);
        if (!isValidString(model))
            throw new IllegalArgumentException(model);
        if (!isValidSeats(coachSeats))
            throw new IllegalArgumentException(Integer.toString(coachSeats));
        if (!isValidSeats(firstClassSeats))
            throw new IllegalArgumentException(Integer.toString(firstClassSeats));

        mManufacturer = manufacturer;
        mModel = model;
        mCoachSeats = coachSeats;
        mFirstClassSeats = firstClassSeats;
    }

//    /**
//     * Initializing constructor with all params as type String.
//     * @throws IllegalArgumentException is any parameter is invalid
//     */
//    public Airplane (String manufacturer, String model, String coachSeats, String firstClassSeats) {
//        int tmpCoach, tmpFirstClass;
//        tmpCoach = Integer.parseInt(coachSeats);
//        tmpFirstClass = Integer.parseInt(firstClassSeats);
//
//        // validate converted values
//        if (!isValidString(manufacturer))
//            throw new IllegalArgumentException(manufacturer);
//        if (!isValidString(model))
//            throw new IllegalArgumentException(model);
//        if (!isValidSeats(tmpCoach))
//            throw new IllegalArgumentException(coachSeats);
//        if (!isValidSeats(tmpFirstClass))
//            throw new IllegalArgumentException(firstClassSeats);
//
//        mManufacturer = manufacturer;
//        mModel = model;
//        mCoachSeats = tmpCoach;
//        mFirstClassSeats = tmpFirstClass;
//    }

    /**
     * Convert object to printable string of format "manu, model, coachSeats, firstClassSeats"
     *
     * @return the object formatted as String to display
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(mManufacturer).append(", ");
        sb.append(mModel).append(", ");
        sb.append(mCoachSeats).append(", ");
        sb.append(mFirstClassSeats);

        return sb.toString();
    }

    /**
     * Set the airplane manufacturer
     *
     * @param manufacturer The manufacturer of the airplane
     * @throws IllegalArgumentException if manufacturer is invalid
     */
    public void manufacturer(String manufacturer) {
        if (isValidString(manufacturer))
            mManufacturer = manufacturer;
        else
            throw new IllegalArgumentException(manufacturer);
    }

    /**
     * get the airplane manufacturer
     *
     * @return Airplane manufacturer
     */
    public String manufacturer() {
        return mManufacturer;
    }

    /**
     * set the airplane model
     *
     * @param model The model of the airplane
     * @throws IllegalArgumentException if model is invalid
     */
    public void model(String model) {
        if (isValidString(model))
            mModel = model;
        else
            throw new IllegalArgumentException(model);
    }

    /**
     * Get the airplane model
     *
     * @return The airplane model
     */
    public String model() {
        return mModel;
    }

    /**
     * Set the # of coach seats
     *
     * @param coachSeats The # of coach seats
     * @throws IllegalArgumentException if seats is invalid
     */
    public void coachSeats(int coachSeats) {
        if (isValidSeats(coachSeats))
            mCoachSeats = coachSeats;
        else
            throw new IllegalArgumentException(Integer.toString(coachSeats));
    }

    /**
     * Get the # of  coach seats
     *
     * @return The # of coach seats
     */
    public int coachSeats() {
        return mCoachSeats;
    }

    /**
     * Set the # of first class seats
     *
     * @param firstClassSeats The # of first class seats
     * @throws IllegalArgumentException if seats is invalid
     */
    public void firstClassSeats(int firstClassSeats) {
        if (isValidSeats(firstClassSeats))
            mFirstClassSeats = firstClassSeats;
        else
            throw new IllegalArgumentException(Integer.toString(firstClassSeats));
    }

    /**
     * get the # of first class seats
     *
     * @return The # of first class seats
     */
    public int firstClassSeats() {
        return mFirstClassSeats;
    }


    /**
     * Compare two airplanes
     * <p>
     * This implementation delegates to the case insensitive version of string compareTo
     *
     * @return equal == 0 //results of String.compareToIgnoreCase
     */
    public int compareTo(Airplane o) {
        //return this.mCode.compareToIgnoreCase(other.mCode);
        return 0;
    }

    /**
     * Compare two airplanes alphabetically for sorting, ordering
     * <p>
     * Delegates to airport1.compareTo for ordering by 3 character code
     *
     * @param o1 the first airplane for comparison
     * @param o2 the second / other airplane for comparison
     * @return -1 if o1 less than o2, +1 if o1 greater than o2, zero ==
     */
    public int compare(Airplane o1, Airplane o2) {
        return o1.compareTo(o2);
    }


//    /**
//     * Determine if object instance has valid attribute data
//     * <p>
//     * Verifies the manufacturer and model are not null and not an empty strings.
//     * Verifies the seats are valid integers.
//     *
//     * @return true if object passes above validation checks
//     */
//    public boolean isValid() {
//
//        return isValidString(mManufacturer)
//                && isValidString(mModel)
//                && isValidSeats(mFirstClassSeats)
//                && isValidSeats(mCoachSeats);
//    }

    /**
     * Check for invalid strings
     *
     * @param str is the string value
     * @return false if null or empty, else assume valid and return true
     */
    public boolean isValidString(String str) {
        if (str == null || str.length() == 0)
            return false;
        return true;
    }

    /**
     * Check for invalid seats
     *
     * @param seats is the integer value for seats
     * @return false if negative or off limits , else assume valid and return true
     */
    public boolean isValidSeats(int seats) {
        if (seats < 0 || seats > Integer.MAX_VALUE)
            return false;
        return true;
    }

}