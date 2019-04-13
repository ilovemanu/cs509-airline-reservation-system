/**
 * 
 */
package driver;

import java.util.ArrayList;
import java.util.Collections;

import airport.Airport;
import airport.Airports;
import airplane.Airplane;
import airplane.Airplanes;
import flight.Flight;
import flight.Flights;
import dao.ServerInterface;
import system.FlightController;

/**
 * @author blake, alex and liz
 * @since 2016-02-24
 * @version 1.2 2019-04-09
 *
 */
public class Driver {

	/**
	 * Entry point for CS509 sample code driver
	 *
	 * This driver will retrieve the list of airports/airplanes from the CS509 server and print the sorted list
	 * to the console
	 *
	 * @param args is the arguments passed to java vm in format of "CS509.sample teamName" where teamName is a valid team
	 */
	public static void main(String[] args) {

		// use scan ui if no args are given
		if (args.length == 0) {
			UserInterface userinterface = new UserInterface();
			userinterface.mainMenu();

		// else use args to run
		} else {

			String departureCode = args[0].toUpperCase();
			String departureTime = args[1];
			String arrivalCode = args[2].toUpperCase();
			String seatClass = args[3];
			String sortParam = ""; // default sort by travelTime
			if (args.length == 5) { sortParam = args[4]; }// options: depTime, arrTime, travelTime, totalPrice

			// Try to get a list of all matching flights
			// with defined departure and arrival and seatClass and sort parameter
			// Test: bos to cle all coach on 2019_05_10 [bos 2019_05_10 cle coach totalPrice]
			FlightController controller = new FlightController();
			ArrayList<ArrayList<Flight>> flights = controller.searchDepTimeFlight(departureCode,departureTime,arrivalCode,seatClass);

			// apply sorter
			controller.sortByParam(sortParam, flights, seatClass);

		if (flights.size() == 0) {
			System.out.println("No " + seatClass + " flights available.");
		} else {
			for (ArrayList<Flight> flightList : flights) {
				ArrayList<String> info = FlightController.getInfo(flightList,seatClass);
				System.out.println("Departure:"+info.get(0)+
						           ", Arrival:"+info.get(1)+
						           ", Duration:"+info.get(2)+"min"+
						           ", Price:"+"$"+info.get(3));
				for (Flight f : flightList) {
					System.out.println(f.toString());
				}
				System.out.println();
			}
		}

//			 test reserve: reserve the first flight/flights on the returned list.
//			ArrayList<Flight> selected = flights.get(2);
//			controller.reserveFlight(selected, seatClass);
			// TODO: implement a confirm function for reservation summary
		}

//		// check invalid arguments
//		if (args.length <4) {
//			System.err.println("Check arguments!");
//			System.exit(-1);
//			return;
//		}


//		// Try to get a list of airports
//		Airports airports = ServerInterface.INSTANCE.getAirports(teamName);
//		Collections.sort(airports);
//		for (Airport airport : airports) {
//			System.out.println(airport.toString());
//		}
//		// Try to get a list of airplanes
//		Airplanes airplanes = ServerInterface.INSTANCE.getAirplanes(teamName);
//		Collections.sort(airplanes);
//		for (Airplane airplane : airplanes) {
//			System.out.println(airplane.toString());
//		}

//		// Try to get a list of flights
//		// with a query departureCode(case-insensitive)
//		// and a query departure date(yyyy_mm_dd).
//		Flights flights = ServerInterface.INSTANCE.getFlights(teamName, departureCode.toUpperCase(), departureTime);
//		Collections.sort(flights);
//		for (Flight flight : flights) {
//			System.out.println(flight.toString());
//		}z

	}
}
