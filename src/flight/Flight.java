/**
 *
 */
package flight;

import airplane.Airplane;
import airport.Airport;

import java.util.Comparator;

/**
 * This class holds values pertaining to a single Flight. Class member attributes
 * are the same as defined by the CS509 server API and store values after conversion from
 * XML received from the server to Java primitives. Attributes are accessed via getter and
 * setter methods.
 *
 * @author alex
 * @version 1 2019-03-16
 * @since 2019-03-16
 *
 */

public class Flight implements Comparable<Flight>, Comparator<Flight> {

    /**
     * Flight attributes as defined by the CS509 server interface XML
     */

    // Airplane type of the flight
    private String mAirplane;
    // FlightTime in minutes
    private int mFlightTime;
    // Number of the flight
    private String mNumber;
    // Code for departure airport
    private String mDepartureAirport;
    // Code for arrival airport
    private String mArrivalAirport;
    /*
     * Departure and Arrival time of the flight.
     * Set to String for now.
     */
    private String mDepartureTime;
    private String mArrivalTime;
    /*
     * firstClass and coachPrice
     * Set to String for now.
     */
    private String mFirstClassPrice;
    private String mCoachPrice;

    // # of reserved first class seats
    private int mFirstClassReserved;
    // # of reserved coach seats
    private int mCoachReserved;

    /**
     * Default constructor
     * <p>
     * Constructor without params. Requires object fields to be explicitly
     * set using setter methods
     *
     * @pre None
     * @post member attributes are initialized to invalid default values
     */
    public Flight() {
        mAirplane = "";
        mFlightTime = Integer.MAX_VALUE;
        mNumber = "";
        mDepartureAirport = "";
        mArrivalAirport = "";
        mDepartureTime = "";
        mArrivalTime = "";
        mFirstClassPrice = "";
        mCoachPrice = "";
        mFirstClassReserved = Integer.MAX_VALUE;
        mCoachReserved = Integer.MAX_VALUE;
    }

    /**
     * Initializing constructor.
     * <p>
     * All attributes are initialized with specified input values following validation for reasonableness.
     * @throws IllegalArgumentException if any parameter is determined invalid
     * @pre String values are not empty; numeric values are valid
     * @post member attributes are initialized with input parameter values
     */
    public Flight(String airplane, int flightTime, String number, String departureAirport,
                  String arrivalAirport, String departureTime, String arrivalTime,
                  String firstClassPrice, String coachPrice,
                  int firstClassReserved, int coachReserved) {

        if (!isValidString(airplane))
            throw new IllegalArgumentException(airplane);
        if (!isValidInt(flightTime))
            throw new IllegalArgumentException(Integer.toString(flightTime));
        if (!isValidString(number))
            throw new IllegalArgumentException(number);
        if (!isValidCode(departureAirport))
            throw new IllegalArgumentException(departureAirport);
        if (!isValidCode(arrivalAirport))
            throw new IllegalArgumentException(arrivalAirport);
        if (!isValidString(departureTime))
            throw new IllegalArgumentException(departureTime);
        if (!isValidString(arrivalTime))
            throw new IllegalArgumentException(arrivalTime);
        if (!isValidString(firstClassPrice))
            throw new IllegalArgumentException(firstClassPrice);
        if (!isValidString(coachPrice))
            throw new IllegalArgumentException(coachPrice);
        if (!isValidSeats(firstClassReserved))
            throw new IllegalArgumentException(Double.toString(firstClassReserved));
        if (!isValidSeats(coachReserved))
            throw new IllegalArgumentException(Double.toString(coachReserved));


        mAirplane = airplane;
        mFlightTime = flightTime;
        mNumber = number;
        mDepartureAirport = departureAirport;
        mArrivalAirport = arrivalAirport;
        mDepartureTime = departureTime;
        mArrivalTime = arrivalTime;
        mCoachPrice = coachPrice;
        mFirstClassPrice = firstClassPrice;
        mCoachReserved = coachReserved;
        mFirstClassReserved = firstClassReserved;

    }

    /**
     * Convert object to printable string of format
     * "airplane, flightTime, number, departureAirport, departureTime,
     * arrivalAirport, arrivalTime, coachPrice, coachReserved, firstClassPrice, firstClassReserved"
     *
     * @return the object formatted as String to display
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(mAirplane).append(", ");
        sb.append(mFlightTime).append(", ");
        sb.append(mNumber).append(", ");
        sb.append(mDepartureAirport).append(", ");
        sb.append(mDepartureTime).append(", ");
        sb.append(mArrivalAirport).append(", ");
        sb.append(mArrivalTime).append(", ");
        sb.append(mCoachPrice).append(", ");
        sb.append(mCoachReserved).append(", ");
        sb.append(mFirstClassPrice).append(", ");
        sb.append(mFirstClassReserved);

        return sb.toString();
    }


    /**
     * Set and get the airplane info
     *
     * @param airplane The airplane of the flight
     * @throws IllegalArgumentException if airplane is invalid
     * @return airplane info
     */
    public void airplane(String airplane) {
        if (isValidString(airplane))
            mAirplane = airplane;
        else
            throw new IllegalArgumentException(airplane);
    }
    public String airplane() {
        return mAirplane;
    }

    /**
     * Set and get the flightTime
     *
     * @param flightTime The flightTime of the flight
     * @throws IllegalArgumentException if flightTime is invalid
     * @return flightTime
     */
    public void flightTime(int flightTime) {
        if (isValidInt(flightTime))
            mFlightTime = flightTime;
        else
            throw new IllegalArgumentException(Integer.toString(flightTime));
    }
    public int flightTime() {
        return mFlightTime;
    }

    /**
     * Set and get the number
     *
     * @param number The number of the flight
     * @throws IllegalArgumentException if number is invalid
     * @return number
     */
    public void number(String number) {
        if (isValidString(number))
            mNumber = number;
        else
            throw new IllegalArgumentException(number);
    }
    public String number() {
        return mNumber;
    }

    /**
     * Set and get the departureAirport
     *
     * @param departureAirport The code for departureAirport
     * @throws IllegalArgumentException if departureAirport is invalid
     * @return departureAirport
     */
    public void departureAirport(String departureAirport) {
        if (isValidCode(departureAirport))
            mDepartureAirport = departureAirport;
        else
            throw new IllegalArgumentException(departureAirport);
    }
    public String departureAirport() {
        return mDepartureAirport;
    }

    /**
     * Set and get the arrivalAirport
     *
     * @param arrivalAirport The code for arrivalAirport
     * @throws IllegalArgumentException if arrivalAirport is invalid
     * @return arrivalAirport
     */
    public void arrivalAirport(String arrivalAirport) {
        if (isValidCode(arrivalAirport))
            mArrivalAirport = arrivalAirport;
        else
            throw new IllegalArgumentException(arrivalAirport);
    }
    public String arrivalAirport() {
        return mArrivalAirport;
    }

    /**
     * Set and get the departureTime
     *
     * @param departureTime The departureTime of the flight
     * @throws IllegalArgumentException if departureTime is invalid
     * @return departureTime
     */
    public void departureTime(String departureTime) {
        if (isValidString(departureTime))
            mDepartureTime = departureTime;
        else
            throw new IllegalArgumentException(departureTime);
    }
    public String departureTime() {
        return mDepartureTime;
    }

    /**
     * Set and get the arrivalTime
     *
     * @param arrivalTime The arrivalTime of the flight
     * @throws IllegalArgumentException if arrivalTime is invalid
     * @return arrivalTime
     */
    public void arrivalTime(String arrivalTime) {
        if (isValidString(arrivalTime))
            mArrivalTime = arrivalTime;
        else
            throw new IllegalArgumentException(arrivalTime);
    }
    public String arrivalTime() {
        return mArrivalTime;
    }

    /**
     * Set and get the coachPrice
     *
     * @param coachPrice The coachPrice of the flight
     * @throws IllegalArgumentException if coachPrice is invalid
     * @return coachPrice
     */
    public void coachPrice(String coachPrice) {
        if (isValidString(coachPrice))
            mCoachPrice = coachPrice;
        else
            throw new IllegalArgumentException(coachPrice);
    }
    public String coachPrice() {
        return mCoachPrice;
    }

    /**
     * Set and get the firstClassPrice
     *
     * @param firstClassPrice The firstClassPrice of the flight
     * @throws IllegalArgumentException if firstClassPrice is invalid
     * @return firstClassPrice
     */
    public void firstClassPrice(String firstClassPrice) {
        if (isValidString(firstClassPrice))
            mFirstClassPrice = firstClassPrice;
        else
            throw new IllegalArgumentException(firstClassPrice);
    }
    public String firstClassPrice() {
        return mFirstClassPrice;
    }

    /**
     * Set and get the coachReserved
     *
     * @param coachReserved The reserved coach seats
     * @throws IllegalArgumentException if coachReserved is invalid
     * @return coachReserved
     */
    public void coachReserved(Integer coachReserved) {
        if (isValidSeats(coachReserved))
            mCoachReserved = coachReserved;
        else
            throw new IllegalArgumentException(Integer.toString(coachReserved));
    }
    public int coachReserved() {
        return mCoachReserved;
    }

    /**
     * Set and get the firstClassReserved
     *
     * @param firstClassReserved The reserved first class seats
     * @throws IllegalArgumentException if firstClassReserved is invalid
     * @return firstClassReserved
     */
    public void firstClassReserved(Integer firstClassReserved) {
        if (isValidSeats(firstClassReserved))
            mFirstClassReserved = firstClassReserved;
        else
            throw new IllegalArgumentException(Integer.toString(firstClassReserved));
    }
    public int firstClassReserved() {
        return mFirstClassReserved;
    }


    /**
     * Compare two flights
     *
     * This implementation delegates to the case insensitive version of string compareTo
     * @return results of String.compareToIgnoreCase
     */
    public int compareTo(Flight o) {
        //return this.mCode.compareToIgnoreCase(other.mCode);
        return 0;
    }

    /**
     * Compare two flights alphabetically for sorting, ordering
     *
     * @param o1 the first flight for comparison
     * @param o2 the second / other flight for comparison
     * @return -1 if flight1  less than flight2, +1 if flight1 greater than flight2, zero ==
     */
    public int compare(Flight o1, Flight o2) {
        //return o1.compareTo(o2);
        return 0;
    }



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
     * Check for invalid integers
     *
     * @param num is the integer value
     * @return false if negative
     */
    public boolean isValidInt(int num) {
        if(num < 0)
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

    /**
     * Check for invalid 3 character airport code
     *
     * @param code is the airport code to validate
     * @return false if null or not 3 characters in length, else assume valid and return true
     */
    public boolean isValidCode (String code) {
        // If we don't have a 3 character code it can't be valid valid
        if ((code == null) || (code.length() != 3))
            return false;
        return true;
    }


}
