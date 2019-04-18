package airport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;
import org.json.*;

/**
 * This class holds logic to retrieve the time Zones for given airport code.
 *
 * @author Priyanka
 * @version 1.3 2019-04-11
 * @since 2019-04-01
 */

public class AirportZone {
    /**
     * Get ZoneId by airport code
     * @param airport
     * @return zone ID
     */
    public static ZoneId getZoneByAirport(Airport airport) {
        double latitude = airport.latitude();
        double longitude = airport.longitude();

        String url = "https://maps.googleapis.com/maps/api/timezone/" + "json?location=" + latitude + ","
                + longitude + "&timestamp=1458000000&key=" + "AIzaSyC7Zae7E8sTj6tUsTmzcd4UP1XoLz4pvFU";

        try {
            JSONObject zoneObj = new JSONObject(getHTML(url));

            String timeZoneID = zoneObj.getString("timeZoneId");
            ZoneId retZone = ZoneId.of(timeZoneID);
            return retZone;
        } catch (InterruptedException | MalformedURLException | JSONException e) {
            return ZoneId.systemDefault();
        }
    }

    private static String getHTML(String urlToRead) throws MalformedURLException, InterruptedException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        for (int i = 0; i < 10; i++) {
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                break;
            } catch (IOException e) {
                TimeUnit.SECONDS.sleep(1);
            }
        }
        return result.toString();
    }

}

