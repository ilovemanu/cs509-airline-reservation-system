package driver;
import java.util.*;
import flight.*;
import system.FlightController;

/**
 * This class holds user input for searching flight
 *
 * @author liz and alex
 * @version 1.0 2019-04-10
 * @since 2019-04-10
 */

public class UserInterface {
    /**
     * @param depAirport is the string value for departure airport
     * @param arrAirport is the string value for arrival airport
     * @param depTime is the string value for departure time
     * @param arrTime is the string value for arrival time
     * @param seatClass is the string value for seating class
     * @param tripType is the string value for trip type
     * @param sortParam is the string value for sorting
     * @param returnDate is the string value for return date
     * @param flightNumber is the int value for reserving flights
     * @param returnFlightNumber is the int value for reserving return flights
     */
    String depAirport;
    String arrAirport;
    String depTime;
    String arrTime;
    String seatClass;
    String tripType;
    String sortParam;
    String returnDate;
    int flightNumber;
    int returnFlightNumber;

    ArrayList<ArrayList<Flight>> flight;
    ArrayList<ArrayList<Flight>> returnFlight;
    FlightController controller;

    /**
     * Default constructor
     */
    public UserInterface() {
        controller = new FlightController();
        // default parameters
        depAirport = "";
        arrAirport = "";
        depTime = "";
        arrTime = "";
        seatClass = "Coach";
        tripType = "one-way";
        sortParam = "travelTime";
        returnDate = "";
    }

    /**
     * List of parameters that required user to input
     */
    public void mainMenu() {
        System.out.println("1. Departure Airport");
        System.out.println("2. Arrival Airport");
        System.out.println("3. Departure Date");
        System.out.println("4. Arrival Date");
        System.out.println("5. Seating Class");
        System.out.println("6. Trip Type");
        System.out.println("7. Return Date");
        System.out.println("8. Sort Parameter");
        System.out.println("9. Search Flight");
        System.out.println("10. Reserve Flight");

        // Use Scanner to read user input
        Scanner scan = new Scanner(System.in);
        if(scan.hasNext()) {
            int selectMenu = scan.nextInt();
            readParameter(selectMenu);
        }
    }

    /**
     * read user input
     */
    public void readParameter(int selectMenu) {
        Scanner scan = new Scanner(System.in);
        String param;

        if(selectMenu!=9 && selectMenu!=10 && scan.hasNext()) {
            param = scan.next();
        } else param="";

        switch(selectMenu) {
            case 1:
                depAirport = param;
                System.out.println(depAirport);
                break;
            case 2:
                arrAirport = param;
                System.out.println(arrAirport);
                break;
            case 3:
                // if user search by departure time, then set arrival time to empty string
                depTime = param;
                arrTime = "";
                System.out.println(depTime);
                break;
            case 4:
                // if user search by arrival time, then set departure time to empty string
                arrTime = param;
                depTime = "";
                System.out.println(arrTime);
                break;
            case 5:
                seatClass = param;
                System.out.println(seatClass);
                break;
            case 6:
                tripType = param;
                System.out.println(tripType);
                break;
            case 7:
                returnDate = param;
                System.out.println(returnDate);
                break;
            case 8:
                sortParam = param;
                System.out.println(sortParam);
                break;
            case 9:
                // if user search again, reset flight number and return flight number
                flightNumber = -1;
                returnFlightNumber = -1;
                // check if departureAirport and arrivalAirport are empty
                if (depAirport.isEmpty() || arrAirport.isEmpty()) {
                    System.out.println("Please input departure airport and arrival airport");
                    break;
                  // check if returnDate is empty when user input round-trip
                } else if(tripType.equalsIgnoreCase("round-trip") && returnDate.isEmpty()){
                    System.out.println("Please input return date");
                    break;
                } else {
                    // if user search by departure time
                    if (!depTime.isEmpty()) {
                        flight = controller.searchDepTimeFlight(depAirport, depTime, arrAirport, seatClass);
                    // if user search by arrival time
                    } else {
                        flight = controller.searchArrTimeFlight(depAirport, arrTime, arrAirport, seatClass);
                    }
                    controller.sortByParam(sortParam, flight, seatClass);
                    printFlightList(flight);
                }

                // if trip type is round-trip, search return flight
                if (tripType.equalsIgnoreCase("round-trip")){
                    // if user search by departure time
                    if (!depTime.isEmpty()) {
                        returnFlight = controller.searchDepTimeFlight(arrAirport, returnDate, depAirport, seatClass);
                    // if user search by arrival time
                    } else {
                        returnFlight = controller.searchArrTimeFlight(arrAirport, returnDate, depAirport, seatClass);
                    }
                    controller.sortByParam(sortParam, returnFlight, seatClass);
                    System.out.println("Return Flight List:");
                    printFlightList(returnFlight);
                }
                break;
//            TODO: Reserve flight
//            case 10:
//                break;
        }
        mainMenu();
    }

    /**
     * Print flight searching result
     */
    public void printFlightList(ArrayList<ArrayList<Flight>> flight) {
        if (flight.size() == 0) {
            System.out.println("No " + seatClass + " flights available.");
        } else {
            for (int i=0; i<flight.size(); i++) {
                ArrayList<Flight> flightList = flight.get(i);
                ArrayList<String> info = FlightController.getInfo(flightList, seatClass);
                System.out.println("Flight Reserve Number: " + i);
                System.out.println("Departure:"+info.get(0)+
                        ", Arrival:"+info.get(1)+
                        ", Duration:"+info.get(2)+
                        ", Price:"+"$"+info.get(3));
                for (Flight f : flightList) {
                    System.out.println(f.toString());
                }
                System.out.println();
            }
        }
    }

}