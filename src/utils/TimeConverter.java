package utils;

import airport.Airport;
import airport.AirportZone;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * This class holds logic to convert GMT to airport local time.
 *
 * @author alex and liz
 * @version 1.1 2019-04-19
 * @since 2019-04-17
 */


public class TimeConverter {
    // id of the timezone
    private final ZoneId mZoneId;

    public TimeConverter(ZoneId zoneId) {
        mZoneId = zoneId;
    }

    // zoneID getter
    public ZoneId getZoneId() { return mZoneId; }

    /**
     * Get ZoneId by airport code
     * @param serverTime is the time in xml on the server
     * @param theAirport is the airport to which the time conversion happens
     * @return airport local time
     */
    public static LocalDateTime convertTimeByAirport(LocalDateTime serverTime, Airport theAirport){
        // get timezone by airport coordinates
        ZoneId theZone = AirportZone.getZoneByAirport(theAirport);
        // match server time with gmt timezone
        ZonedDateTime zonedTime = ZonedDateTime.of(serverTime, ZoneId.of("GMT"));

        return zonedTime.withZoneSameInstant(theZone).toLocalDateTime();
    }

    public static LocalDateTime convertTimeByZoneId(LocalDateTime serverTime, ZoneId zoneid){
        ZonedDateTime zonedTime = ZonedDateTime.of(serverTime, ZoneId.of("GMT"));
        return zonedTime.withZoneSameInstant(zoneid).toLocalDateTime();
    }
}
