import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

/**
 * Created by aman.gupta on 18/09/15.
 */
public class SyncTimeThread extends Thread {
    String shardId;
    Timestamp checkpointTime;
    Timestamp latest_time;
    Timestamp now;
    //Create new redis connection
    Jedis jedis = new Jedis("localhost");

    public SyncTimeThread(String key, String value) {
        this.shardId = key;
        this.checkpointTime = new Timestamp(Long.parseLong(value));
    }

    public void run() {
        //Get latest update time for given shard and the current time at which query was made
        try {
            URL obj = new URL("http://localhost:8074/getLatestTime/" + shardId);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject json = new JSONObject(response.toString());
            System.out.print(json.toString());
            latest_time = Timestamp.valueOf(json.get("latest_time").toString());
            now = Timestamp.valueOf(json.get("now").toString());

        } catch (Exception e) {
            System.out.println("unable to connect");
            e.printStackTrace();
        }

        //If the latest checkpoint time is same as latest update time, current time is the latest checkpoint time
        if (checkpointTime.equals(latest_time)) {
            jedis.hset("shardData", shardId, String.valueOf(now));
        }
    }
}
