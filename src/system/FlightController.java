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
 * @author alex, liz and kathy
 * @version 1.4 2019-04-13
 * @since 2019-04-01
 */

public class FlightController {

    private static final String teamName = "GompeiSquad";
    private Map<String, Airplane> airplaneMap;
    private Map<String, Flights> flightsMap;
    // format the time
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    public FlightController() {
        // initiate the [model, airplane] hashmap
        setAirplaneMap();
        flightsMap = new HashMap();
    }


    /**
     * Search flight by departure date
     *
     * @param depAirport is the departure airport code
     * @param arrAirport is the arrival airport code
     * @param depTime    is the departure time
     * @param seatClass  is the string value for seat classes
     * @return An list of list including all flight combinations
     */
    public ArrayList<ArrayList<Flight>> searchDepTimeFlight(String depAirport, String depTime, String arrAirport, String seatClass) {
        ArrayList<ArrayList<Flight>> allList = new ArrayList();
        depTimeFlightDFS(allList, new ArrayList<Flight>(), depAirport, depTime, arrAirport, seatClass);
        return allList;
    }

    /**
     * Use DFS algorithm to implement search flight by departure date
     */
    public void depTimeFlightDFS(ArrayList<ArrayList<Flight>> res, ArrayList<Flight> subres,
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
        // pass departing to server to get the list of flights departure at an airport
        Flights resFlights = getFromMapOrServer(depAirport, depTime, "departing");
        // if there's at least 1 element in subres
        if (subres.size() >= 1) {
            // get last flight to calculate layover time
            Flight lastFlight = subres.get(subres.size() - 1);
            // check if next day is still in valid layover time range
            // max layover time is 2hr so 22:00 to next day is still in layover time range
            LocalDateTime arrTime = lastFlight.arrivalTime();
            int hr = arrTime.getHour();
            String nextDay = arrTime.plusDays(1).format(formatter);
            if (hr >= 22) {
                Flights nextDayFlights = getFromMapOrServer(depAirport, nextDay, "departing");
                resFlights.addAll(nextDayFlights);
            }
        }

        for (Flight f : resFlights) {
            if (isSeatAvailable(f, seatClass)) {
                // if there's at least 1 element in subres, check if layover time is invalid and then skip
                if (subres.size() >= 1) {
                    if (!isValidLayover(subres.get(subres.size() - 1).arrivalTime(), f.departureTime())) continue;
                }
                // add element (flight) if it's valid
                if(!isValidTime(f)) continue;
                subres.add(f);
                // do recursion
                depTimeFlightDFS(res, subres, f.arrivalAirport(), f.arrivalTime().format(formatter), arrAirport, seatClass);
                // remove the last element we add and then continue to do the iteration
                subres.remove(subres.size()-1);
            }
        }
    }

    /**
     * Search flight by arrival date
     *
     * @param depAirport is the departure airport code
     * @param arrAirport is the arrival airport code
     * @param arrTime    is the arrival time
     * @param seatClass  is the string value for seat classes
     * @return An list of list including all flight combinations
     */
    public ArrayList<ArrayList<Flight>> searchArrTimeFlight(String depAirport, String arrTime, String arrAirport, String seatClass) {
        ArrayList<ArrayList<Flight>> allList = new ArrayList();
        arrTimeFlightDFS(allList, new ArrayList<Flight>(), depAirport, arrTime, arrAirport, seatClass);
        // reverse list because tracing back to do arrival time
        for (ArrayList<Flight> list:allList) {
            Collections.reverse(list);
        }
        return allList;
    }

    /**
     * Use DFS algorithm to implement search flight by arrival date
     */
    public void arrTimeFlightDFS(ArrayList<ArrayList<Flight>> res, ArrayList<Flight> subres,
                                 String depAirport, String arrTime, String arrAirport, String seatClass) {
        // skip the case when subres is empty at first
        // get the last element in subres and check if reach the destination
        if (!subres.isEmpty() && subres.get(subres.size() - 1).departureAirport().equalsIgnoreCase(depAirport)) {
            res.add(new ArrayList(subres));
            return;
        }
        // if subres length equal to max layover + 1 (the length should be 3 because the departure airport + max 2 stops)
        if (subres.size() == Saps.MAX_LAYOVER+1) {
            return;
        }
        // pass arriving to server to get the list of flights arriving at an airport
        Flights resFlights = getFromMapOrServer(arrAirport, arrTime, "arriving");
        // if there's at least 1 element in subres
        if (subres.size() >= 1) {
            // get last flight to calculate layover time
            Flight lastFlight = subres.get(subres.size() - 1);
            // check if previous day is still in valid layover time range
            // max layover time is 2hr so previous day to 02:00 is still in the range
            LocalDateTime depTime = lastFlight.departureTime();
            int hr = depTime.getHour();
            String preDay=depTime.minusDays(1).format(formatter);
            if(hr <= 2) {
                Flights preDayFlights = getFromMapOrServer(lastFlight.departureAirport(), preDay, "arriving");
                resFlights.addAll(preDayFlights);
            }
        }

        for (Flight f : resFlights) {
            if (isSeatAvailable(f, seatClass)) {
                // if there's at least 1 element in subres, check if layover time is invalid and then skip
                if (subres.size() >= 1) {
                    if (!isValidLayover(f.arrivalTime(), subres.get(subres.size() - 1).departureTime())) continue;
                }
                // add element (flight) if it's valid
                if(!isValidTime(f)) continue;
                subres.add(f);
                // do recursion
                arrTimeFlightDFS(res, subres, depAirport, f.departureTime().format(formatter), f.departureAirport(), seatClass);
                // remove the last element we add and then continue to do the iteration
                subres.remove(subres.size()-1);
            }
        }
    }

    /**
     * Storing airport, date and search type to reduce searching time
     */
    public Flights getFromMapOrServer(String airport, String date, String searchType) {
        String key = airport + date + searchType;
        Flights res = new Flights();
        if (flightsMap.containsKey(key)) {
            res.addAll(flightsMap.get(key));
            return res;
        }
        Flights flights = ServerInterface.INSTANCE.getFlights(teamName, airport, date, searchType);
        flightsMap.put(key, flights);
        res.addAll(flights);
        return res;
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

    /**
     * Hard code validation time to handle edge case:
     * Search arrival time on 2019_05_04, and search departure time on 2019_05_18
     */
    public boolean isValidTime(Flight flight){
        return flight.departureTime().isAfter(LocalDateTime.of(Saps.DEFAULT_YEAR, Saps.DEFAULT_MONTH, Saps.MIN_DATE,Saps.MIN_HOUR, Saps.MIN_MINUTE)) &&
               flight.departureTime().isBefore(LocalDateTime.of(Saps.DEFAULT_YEAR, Saps.DEFAULT_MONTH, Saps.MAX_DATE, Saps.MAX_HOUR, Saps.MAX_MINUTE)) &&
               flight.arrivalTime().isAfter(LocalDateTime.of(Saps.DEFAULT_YEAR, Saps.DEFAULT_MONTH, Saps.MIN_DATE,Saps.MIN_HOUR, Saps.MIN_MINUTE)) &&
               flight.arrivalTime().isBefore(LocalDateTime.of(Saps.DEFAULT_YEAR, Saps.DEFAULT_MONTH, Saps.MAX_DATE, Saps.MAX_HOUR, Saps.MAX_MINUTE));
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
                // "$1,000.8"
                totalPrice += Double.valueOf(f.coachPrice().substring(1).replaceAll(",", ""));
            } else {
                totalPrice += Double.valueOf(f.firstClassPrice().substring(1).replaceAll(",",""));
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

    public void reserveFlight(ArrayList<Flight> flightList, String seatClass) {
        boolean isLocked = false;
        boolean isReserved = false;
        boolean isUnlocked = true;
        String xmlFlights;

        // lock server
        isLocked = ServerInterface.INSTANCE.lock(teamName);

        // if server locked
        // update server
        if (isLocked) {
            isUnlocked = false;
            xmlFlights = getXML(flightList, seatClass);
            isReserved = ServerInterface.INSTANCE.reserveSeat(teamName, xmlFlights);

            // if reservation is successful
            // unlock server
            if (isReserved) {
                isUnlocked = ServerInterface.INSTANCE.unlock(teamName);
            }
            // TODO: if isLocked is not successful
            // TODO: if isReserved is not successful
        }
    }

    public String getXML(ArrayList<Flight> flightList, String seatClass) {

//      <Flights>
//          <Flight number=DDDDD seating=SEAT_TYPE/>
//	        <Flight number=DDDDD seating=SEAT_TYPE/>
//      </Flights>
        String head = "<Flights>";
        String end = "</Flights>";
        String middle = "";
        String SEAT_TYPE = "";

        if(seatClass.equalsIgnoreCase("coach")) {
            SEAT_TYPE = "Coach";
        } else if (seatClass.equalsIgnoreCase("firstClass")) {
            SEAT_TYPE = "FirstClass";
        }

        for(Flight f:flightList){
            String number = f.number();
            middle += "<Flight number=\"" + number + "\" seating=\"" + SEAT_TYPE + "\"/>";
        }
        String res = head + middle + end;
        return res;
    }



    /**
     * Convert gmt to airport local time
     *
     * @param flights is a list of flights to be converted
     * @return airport local times
     */
//    public void convertLocalTime(){}


}
