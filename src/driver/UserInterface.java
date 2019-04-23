package driver;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import flight.*;
import system.FlightController;
import utils.Saps;

/**
 * This class holds user input for searching flight
 *
 * @author liz and alex
 * @version 1.2 2019-04-19
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
     * @param returnTime is the string value for return time
     * @param flightNumber is the int value for reserving flights
     * @param returnFlightNumber is the int value for reserving return flights
     * @param filterType is the int value for filters
     */
    String depAirport;
    String arrAirport;
    String depTime;
    String arrTime;
    String seatClass;
    String tripType;
    String sortParam;
    String returnTime;
    int flightNumber;
    int returnFlightNumber;
    int filterType;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    // flight seperate to res and view because when filter flight list, the flight reserve number (index) will change
    ArrayList<ArrayList<Flight>> resFlight;
    ArrayList<ArrayList<Flight>> resReturnFlight;
    ArrayList<ArrayList<Flight>> viewFlight;
    ArrayList<ArrayList<Flight>> viewReturnFlight;
    FlightController controller;
    ArrayList<ArrayList<Flight>> selectedFlight;
    ArrayList<ArrayList<Flight>> selectedRetFlight;

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
        returnTime = "";
        filterType = 3;
        resFlight = new ArrayList();
        resReturnFlight = new ArrayList();
        viewFlight = new ArrayList();
        viewReturnFlight = new ArrayList();
        selectedFlight = new ArrayList();
        selectedRetFlight = new ArrayList();

    }

    /**
     * List of parameters that required user to input
     */
    public void mainMenu() {
        System.out.println("1. Departure Airport: " + depAirport);
        System.out.println("2. Arrival Airport: " + arrAirport);
        System.out.println("3. Departure Date " + depTime);
        System.out.println("4. Arrival Date: " + arrTime);
        System.out.println("5. Seating Class (Coach, FirstClass): " + seatClass);
        System.out.println("6. Trip Type (one-way, round-trip): " + tripType);
        System.out.println("7. Return Date (Only for round-trip): " + returnTime);
        System.out.println("8. Sorting (depTime, arrTime, totalPrice, travelTime): " + sortParam);
        System.out.println("9. Filter (0. Non Stop, 1. One Stop, 2. Two Stops, 3. List All)");
        System.out.println("10, Select Flight (0. Select Departing Flight 1. Select Return Flight (Only for round-trip))");
        System.out.println("11. Search Flight");
        System.out.println("12. Reserve Flight");
        System.out.println("13. Show Flight List Again");

        // Use Scanner to read user input
        Scanner scan = new Scanner(System.in);
        if(scan.hasNext()) {
            try {
                int selectMenu = scan.nextInt();
                readUserInput(selectMenu);
                // handle user not typing integer when selecting
            } catch (Exception e){
                System.out.println("Please input integer only");
                mainMenu();
            }

        }
    }

    /**
     * read user input
     */
    public void readUserInput(int selectMenu) {
        Scanner scan = new Scanner(System.in);
        String param;

        // if user type 11, 12, 13 then assign empty string
        if(selectMenu<11 && scan.hasNext()) {
            param = scan.next();
        } else param = "";

        switch(selectMenu) {
            case 1:
                // convert string to upper case no matter user input lower or upper case
                depAirport = param.toUpperCase();
                System.out.println(depAirport);
                break;
            case 2:
                // convert string to upper case no matter user input lower or upper case
                arrAirport = param.toUpperCase();
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
                returnTime = param;
                LocalDate ret = LocalDate.parse(returnTime, formatter);
                if(!depTime.isEmpty()) {
                    LocalDate dep = LocalDate.parse(depTime, formatter);
                    if(dep.isAfter(ret) || dep.isEqual(ret)){
                        System.out.println("Inbound date must later than the outbound date.");
                        break;
                    }
                } else if(!arrTime.isEmpty()) {
                    LocalDate arr = LocalDate.parse(arrTime, formatter);
                    if(arr.isAfter(ret) || arr.isEqual(ret)){
                        System.out.println("Inbound date must later than the outbound date.");
                        break;
                    }
                }
                System.out.println(returnTime);
                break;
            case 8:
                sortParam = param;
                System.out.println(sortParam);
                break;
            case 9:
                filterType = Integer.valueOf(param);
                System.out.println(filterType);
                break;
            case 10:
                // depOrReturn is for user to input departing's or returning's flight reserve number
                int depOrReturn = Integer.valueOf(param);
                switch (depOrReturn){
                    case 0:
                        flightNumber = scan.nextInt();
//                        buildViewFlightList();
                        selectedFlight.clear();
                        ArrayList<Flight> depFlightList = resFlight.get(flightNumber);
                        selectedFlight.add(depFlightList);
                        System.out.println("Selected Departure Flight Number: " + flightNumber);
                        printSelectedList(selectedFlight, viewFlight, "");

                        // if user input departing flight reserve number and previously select round-trip
                        // then print return flight list for them to choose return flight
                        if (tripType.equalsIgnoreCase("round-trip")) {
                            printFlightList(resReturnFlight, viewReturnFlight, "Returning");
                            System.out.println("Please select return flight");
//                            readUserInput(10);
                            returnFlightNumber = scan.nextInt();
                            ArrayList<Flight> retFlightList = resReturnFlight.get(returnFlightNumber);
                            selectedRetFlight.clear();
                            selectedRetFlight.add(retFlightList);
                            System.out.println("Selected Departure Flight Number: " + flightNumber);
                            printSelectedList(selectedFlight, viewFlight, "");
                            System.out.println("Selected Return Flight Number: " + returnFlightNumber);
                            printSelectedList(selectedRetFlight, viewFlight, "");
                        }
                        break;
                    case 1:
                        returnFlightNumber = scan.nextInt();
                        ArrayList<Flight> retFlightList = resReturnFlight.get(returnFlightNumber);
                        selectedRetFlight.clear();
                        selectedRetFlight.add(retFlightList);
                        System.out.println("Selected Departure Flight Number: " + flightNumber);
                        printSelectedList(selectedFlight, viewFlight, "");
                        System.out.println("Selected Return Flight Number: " + returnFlightNumber);
                        printSelectedList(selectedRetFlight, viewFlight, "");
                        break;
                }
                if (selectedFlight.isEmpty()==false){
                    System.out.println("Please press 1 to book selected flights, other to discard");
                    if (scan.nextInt()==1){
                        readUserInput(12);
                    }
                }

//                    case 0:
//                        flightNumber = scan.nextInt();
//                        buildViewFlightList();
//                        // if user input departing flight reserve number and previously select round-trip
//                        // then print return flight list for them to choose return flight
//                        if (tripType.equalsIgnoreCase("round-trip")) {
//                            printFlightList(resReturnFlight, viewReturnFlight, "Returning");
//                        }
//                        break;
//                    case 1:
//                        returnFlightNumber = scan.nextInt();
//                        break;
//                }
                break;
            case 11:
                // default value
                // if user search again, reset flight number and return flight number
                flightNumber = -1;
                returnFlightNumber = -1;

                // check if departureAirport and arrivalAirport are empty
                if (depAirport.isEmpty() || arrAirport.isEmpty()) {
                    System.out.println("Please input departure airport and arrival airport");
                    break;
                    // check if returnTime is empty when user input round-trip
                } else if(tripType.equalsIgnoreCase("round-trip") && returnTime.isEmpty()){
                    System.out.println("Please input return date");
                    break;
                } else {
                    // if user search by departure time
                    if (!depTime.isEmpty()) {
                        resFlight = controller.searchDepTimeFlight(depAirport, depTime, arrAirport, seatClass, depTime);
                        String nextDay= LocalDate.parse(depTime, formatter).plusDays(1).format(formatter);
                        resFlight.addAll(controller.searchDepTimeFlight(depAirport, nextDay, arrAirport, seatClass, depTime));
                        // if user search by arrival time
                    } else {
                        resFlight = controller.searchArrTimeFlight(depAirport, arrTime, arrAirport, seatClass, arrTime);
                        String nextDay= LocalDate.parse(arrTime, formatter).plusDays(1).format(formatter);
                        resFlight.addAll(controller.searchArrTimeFlight(depAirport, nextDay, arrAirport, seatClass, arrTime));
                    }
                    controller.sortByParam(sortParam, resFlight, seatClass);
                    printFlightList(resFlight, viewFlight, "Departing");
                }

                // if trip type is round-trip, search return flight
                if (tripType.equalsIgnoreCase("round-trip")){
                    // if user search by departure time
                    if (!depTime.isEmpty()) {
                        resReturnFlight = controller.searchDepTimeFlight(arrAirport, returnTime, depAirport, seatClass, returnTime);
                        String nextDay= LocalDate.parse(returnTime, formatter).plusDays(1).format(formatter);
                        resFlight.addAll(controller.searchDepTimeFlight(depAirport, nextDay, arrAirport, seatClass, returnTime));
                        // if user search by arrival time
                    } else {
                        resReturnFlight = controller.searchArrTimeFlight(arrAirport, returnTime, depAirport, seatClass, returnTime);
                        String nextDay= LocalDate.parse(returnTime, formatter).plusDays(1).format(formatter);
                        resFlight.addAll(controller.searchArrTimeFlight(depAirport, nextDay, arrAirport, seatClass, returnTime));
                    }
                    controller.sortByParam(sortParam, resReturnFlight, seatClass);
                    System.out.println("Return Flight List");
                    printFlightList(resReturnFlight, viewReturnFlight, "Returning");
                }
                break;
            case 12:
                controller.reserveFlight(viewFlight.get(flightNumber), seatClass);
                if(tripType.equalsIgnoreCase("round-trip")){
                    controller.reserveFlight(viewReturnFlight.get(returnFlightNumber), seatClass);
                }
                break;
            case 13:
                // show flight list again
                printFlightList(resFlight, viewFlight, "Departing");
                if(resReturnFlight.size()>0){
                    System.out.println("Return Flight List:");
                    printFlightList(resReturnFlight, viewReturnFlight, "Returning");
                }

                break;
        }
        mainMenu();
    }

    /**
     * Print flight searching result
     */
    public void printFlightList(ArrayList<ArrayList<Flight>> flight, ArrayList<ArrayList<Flight>> viewFlight, String departOrReturn) {
        buildViewFlightList();
        if (flight.size() == 0) {
            System.out.println("No " + departOrReturn + " " + seatClass + " flights available.");
            return;
        } else {
            for (int i=0; i<viewFlight.size(); i++) {
                ArrayList<Flight> flightList = viewFlight.get(i);
                ArrayList<String> info = FlightController.getInfo(flightList, seatClass);
                System.out.println("Flight Reserve Number: " + i);
                System.out.println("Departure:"+info.get(0)+
                        ", Arrival:"+info.get(1)+
                        ", Duration:"+info.get(2)+"min"+
                        ", Price:"+"$"+info.get(3));
                for (Flight f : flightList) {
                    System.out.println(f.toLocalString());
//                    System.out.println("GMT: " + f.toString());
                }
                System.out.println();
            }
        }
    }

    /**
     * print selected flight
     */
    public void printSelectedList(ArrayList<ArrayList<Flight>> flight, ArrayList<ArrayList<Flight>> viewFlight, String departOrReturn) {
        buildViewFlightList();
        for (int i=0; i<flight.size(); i++) {
            ArrayList<Flight> flightList = flight.get(i);
            ArrayList<String> info = FlightController.getInfo(flightList, seatClass);
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
    /**
     * Build user view's flight
     */
    public void buildViewFlightList(){
        // clear the previous flight list because we have to show correct filter, valid return flight
        viewFlight.clear();
        viewReturnFlight.clear();
        if(resFlight.size()==0) {
            System.out.println("No resFlight has been built");
            return;
        }
        // if user select one-way
        for(int i=0;i<resFlight.size();i++){
            ArrayList<Flight> flightList = resFlight.get(i);
            int n = flightList.size();
            // if filter = non stop but flight list not having 1 element only then skip
            // if filter = one stop but flight list not having 2 elements only then skip
            // if filter = two stop but flight list not having 3 elements only then skip
            if((filterType==0 && n!=1) || (filterType==1 && n!=2) || (filterType==2 && n!=3)) continue;
            viewFlight.add(flightList);
        }
        if(!tripType.equalsIgnoreCase("round-trip") || resReturnFlight.size()==0) return;
        // if user select round-trip
        for(int i=0; i<resReturnFlight.size(); i++){
            ArrayList<Flight> flightList = resReturnFlight.get(i);
            int n = flightList.size();
            if((filterType==0 && n!=1) || (filterType==1 && n!=2) || (filterType==2 && n!=3)) continue;
            // if user select departing flight (default flightNumber is -1 which means not select)
            if(flightNumber!=-1){
                // viewFlight.get(flightNumber) means get the flight reserve number
                // .get(viewFlight.get(flightNumber).size()-1) means get the last element
                Flight departing = viewFlight.get(flightNumber).get(viewFlight.get(flightNumber).size()-1);
                Flight returning = flightList.get(0);
                // if departing flight + min layover time is already pass the returning flight time (which means we cannot catch up) then skip
                if(departing.arrivalTime().plusMinutes(Saps.MIN_LAYOVER_TIME).isAfter(returning.departureTime())) continue;
            }
            viewReturnFlight.add(flightList);
        }

    }

}