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

//    // zoneID getter
//    public ZoneId getZoneId() { return mZoneId; }

    /**
     * Convert time by zone id
     * @param serverTime is the time in xml on the server
     * @param zoneid is the airport time zone id
     * @return airport local time
     */
    public static LocalDateTime convertTimeByZoneId(LocalDateTime serverTime, ZoneId zoneid){
        ZonedDateTime zonedTime = ZonedDateTime.of(serverTime, ZoneId.of("GMT"));
        return zonedTime.withZoneSameInstant(zoneid).toLocalDateTime();
    }
}
