package system;

import airplane.Airplane;
import airplane.Airplanes;
import airport.Airport;
import dao.ServerInterface;
import flight.Flight;
import flight.Flights;
import utils.Saps;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * This class is the interface between user and server.
 * It holds logic needed to match flights using user inputs.
 * @author alex
 * @version 1.1 2019-04-03
 * @since 2019-04-01
 *
 */

public class FlightController {

    private static final String teamName = "GompeiSquad";
    private Map<String, Airplane> airplaneMap;

    public FlightController() {
        // initiate the [model, airplane] hashmap
        setAirplaneMap();
    }

//    /**
//     * Search non-stop flights given user inputs
//     *
//     * @param depAirport is the departure airport code
//     * @param arrAirport is the arrival airport code
//     * @param depTime is the departure time
//     * @param seatClass is the string value for seat classes
//     * @return
//     */
//    public Flights searchNonstop(String depAirport,
//                                 String depTime,
//                                 String arrAirport,
//                                 String seatClass) {
//
//        Flights nonStopList = new Flights();
//        Flights flights = ServerInterface.INSTANCE.getFlights(teamName,depAirport,depTime.toString());
//        for (Flight f: flights) {
////            System.out.println(f.toString());
//            String arrCode = f.arrivalAirport();
//            Airplane airplane = airplaneMap.get(f.airplane());
//
//            // match arrival airport and check seat availability
//            if (arrCode.equalsIgnoreCase(arrAirport)
//            && isSeatAvailable(f, airplane, seatClass)) { nonStopList.add(f); }
//        }
//        return nonStopList;
//    }

    /**
     * Search flights given user inputs (up to 2 layovers)
     *
     * @param depAirport is the departure airport code
     * @param arrAirport is the arrival airport code
     * @param depTime is the departure time
     * @param seatClass is the string value for seat classes
     * @return An list of list including all flight combinations
     */
    public ArrayList<ArrayList<String>> searchFlight(String depAirport,
                                                      String depTime,
                                                      String arrAirport,
                                                      String seatClass) {

        ArrayList<ArrayList<String>> nonStopList = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> oneStopList = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> twoStopList = new ArrayList<ArrayList<String>>();

        // leg1
        Flights flightsOne = ServerInterface.INSTANCE.getFlights(teamName,depAirport,depTime);
        for (Flight f: flightsOne) {

            // save leg1 arrival airport
            String arrCode = f.arrivalAirport();
            // check seat constraint
            if (isSeatAvailable(f, seatClass)) {
                // save non-stop flights if arrival airport matches
                if (arrCode.equalsIgnoreCase(arrAirport)) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(f.toString());
                    nonStopList.add(temp);
                }
                else {
                    // save leg1 arrival airport and time for matching leg2
                    LocalDateTime arrTime_1 = f.arrivalTime();
                    String arrCode_1 = f.arrivalAirport();
                    // create an list for leg1&2 combo
                    ArrayList<String> temp_1 = new ArrayList<>();
                    temp_1.add(f.toString());

                    // leg2
                    Flights flightsTwo = ServerInterface.INSTANCE.getFlights(teamName,arrCode_1,depTime);
                    // add layover time and seat constraint
                    for (Flight f2: flightsTwo) {
                        // save leg2 arrival airport and departure time
                        // TO DO: when converted to local time, need another constraint to limit arrival time
                        String arrCode2 = f2.arrivalAirport();
                        LocalDateTime depTime_2 = f2.departureTime();

                        // check seat and layover time constraints
                        if (isValid(f2, seatClass,
                                arrTime_1, depTime_2)) {
                            // save one-stop flights if arrival airport matches
                            if (arrCode2.equalsIgnoreCase(arrAirport)) {
                                temp_1.add(f2.toString());
                                oneStopList.add(temp_1);
                            }
                            else {
                                // save leg2 arrival time for matching leg3
                                LocalDateTime arrTime_2 = f2.arrivalTime();
                                // create an list for leg1&2&3 combo
                                ArrayList<String> temp_2 = new ArrayList<>();
                                temp_2.add(f.toString());
                                temp_2.add(f2.toString());

                                // leg3
                                Flights flightsThree = ServerInterface.INSTANCE.getFlights(teamName,arrCode2,depTime);
                                for (Flight f3: flightsThree) {
                                    // save leg3 arrival airport and departure time
                                    // TO DO: when converted to local time, need another constraint to limit arrival time
                                    String arrCode3 = f3.arrivalAirport();
                                    LocalDateTime depTime_3 = f3.departureTime();
                                    // save two-stop flights if arrival airport matches final arrival airport
                                    if (isValid(f3, seatClass, arrTime_2, depTime_3)
                                            && arrCode3.equalsIgnoreCase(arrAirport)) {
                                        temp_2.add(f3.toString());
                                        twoStopList.add(temp_2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        oneStopList.addAll(twoStopList);
        nonStopList.addAll(oneStopList);
        return nonStopList;
    }

    /**
     * Check for available seats given a flight and the seat class
     *
     * @param flight is the flight/leg to check
     * @param seatClass is the string value for seat classes
     * @return true if # of seats left is positive
     */
    public boolean isSeatAvailable(Flight flight, String seatClass) {
        boolean ans = false;
        int totalSeats = 0;
        int reservedSeats = 0;

        // get airplane model by flight info from hashmap
        Airplane airplane = airplaneMap.get(flight.airplane());

        if (seatClass.equalsIgnoreCase("Coach")) {
            totalSeats = airplane.coachSeats();
            reservedSeats = flight.coachReserved();
        } else if (seatClass.equalsIgnoreCase("FirstClass")) {
            totalSeats = airplane.firstClassSeats();
            reservedSeats = flight.firstClassReserved();
        }

        if (totalSeats-reservedSeats > 0) { ans = true; }

        return ans;
    }

    /**
     * Check the validity of a layover
     *
     * @param tArr is the arrival time of the previous leg
     * @param tDep is the departure time of the next leg
     * @return true if layover is between [30, 120] min
     */
    public boolean isValidLayover(LocalDateTime tArr, LocalDateTime tDep) {
        long layOver = Duration.between(tArr, tDep).toMinutes();
        return layOver >= Saps.MIN_LAYOVER_TIME && layOver <= Saps.MAX_LAYOVER_TIME;
    }

    /**
     * Check the validity of seat and layover constraints
     * combine isSeatAvailable and isValidLayover
     *
     * @param flight is the flight/leg to check
     * @param seatClass is the string value for seat classes
     * @param tArr is the arrival time of the previous leg
     * @param tDep is the departure time of the next leg
     * @return true if both are true
     */
    public boolean isValid(Flight flight, String seatClass,
                           LocalDateTime tArr, LocalDateTime tDep) {
        return isSeatAvailable(flight, seatClass)
                && isValidLayover(tArr, tDep);
    }

    /**
     * Find airplane by airplane model
     */
    public void setAirplaneMap() {
        Airplanes allPlanes = ServerInterface.INSTANCE.getAirplanes(teamName);
        airplaneMap = new HashMap<>();
        for (Airplane a: allPlanes) {
            String model = a.model();
            airplaneMap.put(model, a);
        }
    }


    /**
     * Convert gmt to airport local time
     *
     * @param flights is a list of flights to be converted
     * @return airport local times
     */
//    public void convertLocalTime(){}


}
