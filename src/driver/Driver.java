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
 * @author blake and alex
 * @since 2016-02-24
 * @version 1.1 2019-04-03
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
		// check invalid arguments
		if (args.length != 4) {
			System.err.println("Check arguments!");
			System.exit(-1);
			return;
		}

		String departureCode = args[0].toUpperCase();
		String departureTime = args[1];
		String arrivalCode = args[2].toUpperCase();
		String seatClass = args[3];

		// Try to get a list of all matching flights
		// with defined departure and arrival and seatClass
		// Test: bos to cle all coach on 2019_05_10 [bos 2019_05_10 cle coach]
		FlightController controller = new FlightController();
		ArrayList<ArrayList<String>> flights = controller.searchFlight(departureCode,departureTime,arrivalCode,seatClass);
//		Collections.sort(flights);
		if (flights.size() == 0) {
			System.out.println("No " + seatClass + " flights available.");
		} else {
			for (ArrayList flight : flights) {
				for (Object f : flight)
					System.out.println(f);
					System.out.println();
			}
		}


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
//		}

	}
}
