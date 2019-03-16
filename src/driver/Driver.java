/**
 * 
 */
package driver;

import java.util.Collections;

import airport.Airport;
import airport.Airports;
import airplane.Airplane;
import airplane.Airplanes;
import flight.Flight;
import flight.Flights;
import dao.ServerInterface;


/**
 * @author blake and alex
 * @since 2016-02-24
 * @version 1.0
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
		if (args.length > 2) {
			System.err.println("Check arguments!");
			System.exit(-1);
			return;
		}
		
		String teamName = args[0];
		String departureCode = args[1];

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

		// Try to get a list of flights with a given departure date(2019_05_10) and a query departureCode
		Flights flights = ServerInterface.INSTANCE.getFlights(teamName, departureCode);
		Collections.sort(flights);
		for (Flight flight : flights) {
			System.out.println(flight.toString());
		}

	}
}
