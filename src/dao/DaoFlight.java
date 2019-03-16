package dao;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import flight.Flight;
import flight.Flights;

/**
 * @author blake and alex
 * @version 1 2019-03-16
 * @since 2016-02-24
 *
 */

public class DaoFlight {

    /**
     * Builds a collection of flights from flights described in XML
     * <p>
     * Parses an XML string to read each of the flights and adds each valid flight
     * to the collection. The method uses Java DOM (Document Object Model) to convert
     * from XML to Java primitives.
     * <p>
     * Method iterates over the set of Flights nodes in the XML string and builds
     * a flight object from the XML node string and add the Flight object instance to
     * the Flights collection.
     *
     * @param xmlFlights XML string containing set of flights
     * @return [possibly empty] collection of Flights in the xml string
     * @throws NullPointerException included to keep signature consistent with other addAll methods
     * @pre the xmlFlights string adheres to the format specified by the server API
     * @post the [possibly empty] set of Flights in the XML string are added to collection
     */
    public static Flights addAll(String xmlFlights) throws NullPointerException {

        Flights flights = new Flights();

        // Load the XML string into a DOM tree for ease of processing
        // then iterate over all nodes adding each flight to our collection
        Document docFlights = buildDomDoc (xmlFlights);
        NodeList nodesFlights = docFlights.getElementsByTagName("Flight");

        for (int i = 0; i < nodesFlights.getLength(); i++) {
            Element elementFlight = (Element) nodesFlights.item(i);
            Flight flight = buildFlight (elementFlight);

            if (flight != null) {
                flights.add(flight);
            }
        }

        return flights;

    }

    /**
     * Creates an Flight object from a DOM node
     *
     * Processes a DOM Node that describes an Flight and creates an Flight object from the information
     * @param nodeFlight is a DOM Node describing an Flight
     * @return Flight object created from the DOM Node representation of the Flight
     *
     * @pre nodeFlight is of format specified by CS509 server API
     * @post flight object instantiated. Caller responsible for deallocating memory.
     */
    static private Flight buildFlight (Node nodeFlight) {

        String airplane;
        int flightTime;
        String number;
        String departureAirport;
        String arrivalAirport;
        String departureTime;
        String arrivalTime;
        String firsClassPrice;
        String coachPrice;
        int firstClassReserved;
        int coachReserved;

        // The flight element has attributes of airplane, flightTime, and number
        Element elementFlight = (Element) nodeFlight;
        airplane = elementFlight.getAttributeNode("Airplane").getValue();
        flightTime = Integer.valueOf(elementFlight.getAttributeNode("FlightTime").getValue());
        number = elementFlight.getAttribute("Number");

        // The rest are child elements
        // <departure></departure>
        Element elementDeparture = (Element)elementFlight.getElementsByTagName("Departure").item(0);
        Element elementDepartureCode = (Element)elementDeparture.getElementsByTagName("Code").item(0);
        departureAirport = getCharacterDataFromElement(elementDepartureCode);
        Element elementDepartureTime = (Element)elementDeparture.getElementsByTagName("Time").item(0);
        departureTime = getCharacterDataFromElement(elementDepartureTime);

        // <arrival></arrival>
        Element elementArrival = (Element)elementFlight.getElementsByTagName("Arrival").item(0);
        Element elementArrivalCode = (Element)elementArrival.getElementsByTagName("Code").item(0);
        arrivalAirport = getCharacterDataFromElement(elementArrivalCode);
        Element elementArrivalTime = (Element)elementArrival.getElementsByTagName("Time").item(0);
        arrivalTime = getCharacterDataFromElement(elementArrivalCode);

        // <seating></seating>
        Element elementSeating = (Element)elementFlight.getElementsByTagName("Seating").item(0);
        Element elementFirstClass = (Element)elementSeating.getElementsByTagName("FirstClass").item(0);
        firsClassPrice = elementFirstClass.getAttributeNode("Price").getValue();
        firstClassReserved = Integer.parseInt(getCharacterDataFromElement(elementFirstClass));
        Element elementCoach = (Element)elementSeating.getElementsByTagName("Coach").item(0);
        coachPrice = elementCoach.getAttributeNode("Price").getValue();
        coachReserved = Integer.parseInt(getCharacterDataFromElement(elementCoach));


        /**
         * Instantiate an empty Flight object and initialize with data from XML node
         */
        Flight flight = new Flight();

        flight.airplane(airplane);
        flight.flightTime(flightTime);
        flight.number(number);
        flight.departureAirport(departureAirport);
        flight.departureTime(departureTime);
        flight.arrivalAirport(arrivalAirport);
        flight.arrivalTime(arrivalTime);
        flight.coachPrice(coachPrice);
        flight.coachReserved(coachReserved);
        flight.firstClassPrice(firsClassPrice);
        flight.firstClassReserved(firstClassReserved);

        return flight;
    }

    /**
     * Builds a DOM tree from an XML string
     *
     * Parses the XML file and returns a DOM tree that can be processed
     *
     * @param xmlString XML String containing set of objects
     * @return DOM tree from parsed XML or null if exception is caught
     */
    static private Document buildDomDoc (String xmlString) {
        /**
         * load the xml string into a DOM document and return the Document
         */
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xmlString));

            return docBuilder.parse(inputSource);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieve character data from an element if it exists
     *
     * @param e is the DOM Element to retrieve character data from
     * @return the character data as String [possibly empty String]
     */
    private static String getCharacterDataFromElement (Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }
}
