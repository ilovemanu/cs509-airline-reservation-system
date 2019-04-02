package system;

import airplane.Airplane;
import airplane.Airplanes;
import dao.ServerInterface;
import flight.Flight;
import flight.Flights;

import java.util.HashMap;
import java.util.Map;


/**
 * This class holds logic needed to match flights.
 *
 * @author alex
 * @version 1 2019-04-01
 * @since 2019-04-01
 *
 */

public class FlightController {

    private static final String teamName = "GompeiSquad";
    private Map<String, Airplane> airplaneMap;

    public FlightController() {
        setAirplaneMap();
    }

    /**
     * Search non-stop flights given user inputs
     *
     * @param depAirport is the departure airport code
     * @param arrAirport is the arrival airport code
     * @param depTime is the departure time
     * @param seatClass is the string value for seat classes
     * @return
     */
    public Flights searchNonstop(String depAirport,
                              String depTime,
                              String arrAirport,
                              String seatClass) {

        Flights nonStopList = new Flights();
        Flights flights = ServerInterface.INSTANCE.getFlights(teamName,depAirport,depTime);
        for (Flight f: flights) {
//            System.out.println(f.toString());
            String arrCode = f.arrivalAirport();
            Airplane airplane = airplaneMap.get(f.airplane());

            // match arrival airport and check seat availability
            if (arrCode.equalsIgnoreCase(arrAirport)
            && isSeatAvailable(f, airplane, seatClass)) { nonStopList.add(f); }
        }
        return nonStopList;
    }


    /**
     * Check for available seats given a flight and the seat class
     *
     * @param flight is the flight/leg to check
     * @param airplane is the aircraft of the flight
     * @param seatClass is the string value for seat classes
     * @return true if # of seats left is positive
     */
    public boolean isSeatAvailable(Flight flight, Airplane airplane, String seatClass) {
        boolean ans = false;
        int totalSeats = 0;
        int reservedSeats = 0;


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
