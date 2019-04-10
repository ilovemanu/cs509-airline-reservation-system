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
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * This class is the interface between user and server.
 * It holds logic needed to match flights using user inputs.
 *
 * @author alex and liz
 * @version 1.2 2019-04-09
 * @since 2019-04-01
 */

public class FlightController {

    private static final String teamName = "GompeiSquad";
    private Map<String, Airplane> airplaneMap;
    // format the time
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    public FlightController() {
        // initiate the [model, airplane] hashmap
        setAirplaneMap();
    }


    /**
     * Search flights given user inputs (up to 2 layovers)
     *
     * @param depAirport is the departure airport code
     * @param arrAirport is the arrival airport code
     * @param depTime    is the departure time
     * @param seatClass  is the string value for seat classes
     * @return An list of list including all flight combinations
     */
    public ArrayList<ArrayList<Flight>> searchFlight(String depAirport, String depTime, String arrAirport, String seatClass) {
        ArrayList<ArrayList<Flight>> allList = new ArrayList();
        searchFlightDFS(allList, new ArrayList<Flight>(), depAirport, depTime, arrAirport, seatClass);
        return allList;
    }

    public void searchFlightDFS(ArrayList<ArrayList<Flight>> res, ArrayList<Flight> subres,
                                String depAirport, String depTime, String arrAirport, String seatClass) {
        // skip the case when subres is empty at first
        // get the last element in subres and check if reach the destination
        if (!subres.isEmpty() && subres.get(subres.size() - 1).arrivalAirport().equalsIgnoreCase(arrAirport)) {
            res.add(new ArrayList(subres));
            return;
        }
        // if subres length equal to max layover + 1 (the length should be 3 because the departure airport + max 2 stops)
        if (subres.size() == Saps.MAX_LAYOVER + 1) {
            return;
        }
        Flights resFlights = ServerInterface.INSTANCE.getFlights(teamName, depAirport, depTime);
        for (Flight f : resFlights) {
            if (isSeatAvailable(f, seatClass)) {
                // if there's at least 1 element in subres, check the layover time, if layover time is invalid then skip
                if (subres.size() >= 1) {
                    if (!isValidLayover(subres.get(subres.size() - 1).arrivalTime(), f.departureTime())) continue;
                }
                // add element (flight) if is valid
                subres.add(f);
                // do recursion
                searchFlightDFS(res, subres, f.arrivalAirport(), f.arrivalTime().format(formatter), arrAirport, seatClass);
                // remove the last element we add and then continue to do the iteration
                subres.remove(subres.size() - 1);
            }
        }

    }


    /**
     * Check for available seats given a flight and the seat class
     *
     * @param flight    is the flight/leg to check
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

        if (totalSeats - reservedSeats > 0) {
            ans = true;
        }

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

//    /**
//     * Check the validity of seat and layover constraints
//     * combine isSeatAvailable and isValidLayover
//     *
//     * @param flight is the flight/leg to check
//     * @param seatClass is the string value for seat classes
//     * @param tArr is the arrival time of the previous leg
//     * @param tDep is the departure time of the next leg
//     * @return true if both are true
//     */
//    public boolean isValid(Flight flight, String seatClass,
//                           LocalDateTime tArr, LocalDateTime tDep) {
//        return isSeatAvailable(flight, seatClass)
//                && isValidLayover(tArr, tDep);
//    }


    /**
     * Find airplane by airplane model
     */
    public void setAirplaneMap() {
        Airplanes allPlanes = ServerInterface.INSTANCE.getAirplanes(teamName);
        airplaneMap = new HashMap<>();
        for (Airplane a : allPlanes) {
            String model = a.model();
            airplaneMap.put(model, a);
        }
    }


    /**
     * Get required flight(s) info from each search result
     *
     * @param flightList is one set of search result,
     *                   either one, two, or three legs included.
     * @return [depTime, arrTime, travelTime, totalPrice]
     */
    public static ArrayList<String> getInfo(ArrayList<Flight> flightList, String seatClass) {
        ArrayList<String> info = new ArrayList<>();
        LocalDateTime depTime = flightList.get(0).departureTime();
        LocalDateTime arrTime = flightList.get(flightList.size() - 1).arrivalTime();
        long travelTime = Duration.between(depTime, arrTime).toMinutes();
        double totalPrice = 0;

        for (Flight f : flightList) {
            if (seatClass.equalsIgnoreCase("coach")) {
                totalPrice += Double.valueOf(f.coachPrice().substring(1));
            } else {
                totalPrice += Double.valueOf(f.firstClassPrice().substring(1));
            }
        }

        info.add(depTime.toString());
        info.add(arrTime.toString());
        info.add(String.valueOf(travelTime));
        info.add(String.valueOf(totalPrice));
        return info;
    }


    /**
     * Sort search results [[Flight]] by user defined parameter
     *
     * @param param is the sort parameter defined by user
     * @param searchResult is the search result [[Flight]]
     * @param seatClass is the seatClass defined by user
     * @return sorted results
     */
    public ArrayList<ArrayList<Flight>> sortByParam(String param, ArrayList<ArrayList<Flight>> searchResult, String seatClass) {
        switch (param) {
            // depTime, arrTime, travelTime, totalPrice from front-end user input
            case "depTime":
                searchResult.sort(
                        (ArrayList<Flight> l1, ArrayList<Flight> l2) -> getInfo(l1, seatClass).get(0).compareTo(getInfo(l2, seatClass).get(0))
                );
                break;
            case "arrTime":
                searchResult.sort(
                        (ArrayList<Flight> l1, ArrayList<Flight> l2) -> getInfo(l1, seatClass).get(1).compareTo(getInfo(l2, seatClass).get(1))
                );
                break;
            case "totalPrice":
                searchResult.sort(
                        (ArrayList<Flight> l1, ArrayList<Flight> l2) -> Double.compare(Double.valueOf(getInfo(l1, seatClass).get(3)), Double.valueOf(getInfo(l2, seatClass).get(3)))
                );
                break;
            // default sort by travelTime
            default:
                searchResult.sort(
                        (ArrayList<Flight> l1, ArrayList<Flight> l2) -> Long.valueOf(getInfo(l1, seatClass).get(2)).compareTo(Long.valueOf(getInfo(l2, seatClass).get(2)))
                );
        }
        return searchResult;
    }


    /**
     * Convert gmt to airport local time
     *
     * @param flights is a list of flights to be converted
     * @return airport local times
     */
//    public void convertLocalTime(){}


}
