import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by aman.gupta on 04/09/15.
 */
@Path("/")
public class ReconciliationModuleResource {

    public ReconciliationModuleResource() {
        super();
    }

    @POST
    @Consumes("application/json")
    @Path("/insert")
    public void insertCheckpoint(String request) {

        JSONObject obj = new JSONObject(request);
        long checkpoint = Long.parseLong(obj.get("id").toString());
        String shardId = obj.get("shardId").toString();
        //Create new redis connection
        Jedis jedis = new Jedis("localhost");
        //Add checkpoint entry to corresponding shard table
        jedis.zadd(shardId, checkpoint, String.valueOf(checkpoint));
        jedis.close();
    }

    @POST
    @Path("/findMissingEntries")
    public void findMissingEntries() {

        //Connect to Redis via Jedis Java Client
        Jedis jedis = new Jedis("localhost");

        List<FindMissingIdThread> thread = new ArrayList<FindMissingIdThread>();
        //Get latest checkpoint for each shard
        Map<String, String> latest_checkpoints = jedis.hgetAll("latest_checkpoints");
        for (Map.Entry<String, String> entry : latest_checkpoints.entrySet()) {
            String shardId = entry.getKey();
            String checkpoint = entry.getValue();
            //Initialize separate thread for each shard
            thread.add(new FindMissingIdThread(shardId, checkpoint));
        }
        Iterator<FindMissingIdThread> i = thread.iterator();
        while (i.hasNext()) {
            FindMissingIdThread th = i.next();
            //start each thread
            th.start();
        }
        jedis.close();
    }

    @POST
    @Path("/reconcile")
    public void reconcile() {
        //Create a redis connection
        Jedis jedis = new Jedis("localhost");

        //Find number of shards in primary data store
        long noOfShards = jedis.hlen("shardData");
        //Get list of shard IDs in primary data store
        Set<String> shardIds = jedis.hkeys("shardData");
        Iterator<String> i = shardIds.iterator();
        List<ReconciliationThread> thread = new ArrayList<ReconciliationThread>();
        while (i.hasNext()) {
            String shardId = i.next();
            // System.out.println(shardId);
            //Create new thread for reconciling each shard
            thread.add(new ReconciliationThread(shardId));
        }
        Iterator<ReconciliationThread> i2 = thread.iterator();
        while (i2.hasNext()) {
            ReconciliationThread th = i2.next();
            //Start each thread
            th.start();
        }
        jedis.close();
    }

    @POST
    @Path("/syncTime")
    public void syncTime() {
        //Create a redis connection
        Jedis jedis = new Jedis("localhost");
        //Get last checkpointed time for each shard
        Map<String, String> shardData = jedis.hgetAll("shardData");
        List<SyncTimeThread> thread = new ArrayList<SyncTimeThread>();
        for (Map.Entry<String, String> entry : shardData.entrySet()) {
            //Create separate thread for each shard
            thread.add(new SyncTimeThread(entry.getKey(), entry.getValue()));
        }
        Iterator<SyncTimeThread> i = thread.iterator();
        while (i.hasNext()) {
            SyncTimeThread th = i.next();
            //Start each thread
            th.start();
        }
        jedis.close();
    }

    @POST
    @Path("/purge")
    public void purgeCheckpointList() {
        //Create a redis connection
        Jedis jedis = new Jedis("localhost");
        //Get latest checkpoint for each shard
        Map<String, String> latest_checkpoints = jedis.hgetAll("latest_checkpoints");
        for (Map.Entry<String, String> entry : latest_checkpoints.entrySet()) {
            //Delete entries till checkpoint for each shard
            jedis.zremrangeByScore(entry.getKey(), 0, Integer.parseInt(entry.getValue()) - 1);
        }
        jedis.close();
    }

    @POST
    @Path("/updateCheckpoint")
    public void updateCheckpoint() {
        //Initialize a redis session
        Jedis jedis = new Jedis("localhost");

        //Get latest checkpoints for each shard
        Map<String, String> latest_checkpoints = jedis.hgetAll("latest_checkpoints");
        for (Map.Entry<String, String> entry : latest_checkpoints.entrySet()) {
            String shardId = entry.getKey();
            long checkpoint = Long.parseLong(entry.getValue());
            //Get list of inserted entries for each shard
            Set<String> list = jedis.zrangeByScore(shardId, checkpoint, Long.MAX_VALUE);
            String[] arr = list.toArray(new String[0]);
            //Storing the missing entries in an array
            int length = list.size();
            int low = 0;
            int high = length - 1;
            //calculate new checkpoint value
            String newCheckpoint = findCheckpoint(low, high, length, arr);
            latest_checkpoints.put(shardId, newCheckpoint);
            System.out.println(shardId + " " + newCheckpoint);
            checkpoint = Long.parseLong(newCheckpoint);
        }
        //Update latest checkpoint values for each shard in Redis
        jedis.hmset("latest_checkpoints", latest_checkpoints);
        String request = latest_checkpoints.toString();

        //Get timestamp corresponding to each shard's latest checkpoint
        try {
            //Trigger marvin workflow with payload as data from primary DB
            //System.out.print(result.toString());
            URL obj = new URL("http://localhost:8074/getTimeFromCheckpointBatch");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            //con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(request);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //System.out.println(response);

            JSONObject checkpointTime = new JSONObject(response.toString());
            //System.out.print(checkpointTime.toString());
            Iterator<String> keys = checkpointTime.keys();
            Map<String, String> checkpointTimeMap = new HashMap<String, String>();
            Map<String, String> currentCheckpointTimeMap = jedis.hgetAll("shardData");
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) checkpointTime.get(key);
                Timestamp latestTime = new Timestamp(Long.parseLong(currentCheckpointTimeMap.get(key)));
                Timestamp newTime = new Timestamp(Long.parseLong(value));
                //If the new checkpoint time is greater than current checkpoint time, update the time, else keep as it is.
                if(newTime.after(latestTime)){
                    checkpointTimeMap.put(key, value);
                }
                else{
                    checkpointTimeMap.put(key, currentCheckpointTimeMap.get(key));
                }
            }

            //Update shard Data i.e. latest checkpoint time of each shard
            jedis.hmset("shardData", checkpointTimeMap);
            Timestamp finalCheckpointTime = null;

            //Find minimum timestamp out of all shard checkpoint timestamps
            for (Map.Entry<String, String> entry : checkpointTimeMap.entrySet()) {
                String shardId = entry.getKey();
                Timestamp timestamp = new Timestamp(Long.parseLong(entry.getValue()));
                if (finalCheckpointTime == null || finalCheckpointTime.after(timestamp)) {
                    finalCheckpointTime = timestamp;
                }
            }
            //Set final checkpoint time to be displayed to user
            jedis.set("checkpointTime", String.valueOf(finalCheckpointTime));

        } catch (Exception e) {
            System.out.println("failure");
            e.printStackTrace();
        }
        jedis.close();
    }

    private String findCheckpoint(int low, int high, int length, String[] arr) {
        if (high == low) {
            return arr[low];
        }
        //if difference of indices equals difference in values, the values are consecutive
        if ((high - low) == (Long.parseLong(arr[high]) - Long.parseLong(arr[low]))) {
            return arr[high];
        } else {
            int mid = (low + high) / 2;
            if (mid == low) {
                return arr[low];
            }
            //If first half of array has consecutive elements, check in 2nd half, else repeat the whole process for first half
            if ((mid - low) == (Long.parseLong(arr[mid]) - Long.parseLong(arr[low]))) {
                //System.out.print("2nd"+low+"b"+high);
                return findCheckpoint(mid, high, (length + 1) / 2, arr);
            } else {
                //System.out.print("3rd"+low+"a"+high);
                return findCheckpoint(low, mid, (length + 1) / 2, arr);
            }
        }
    }


    @GET
    @Path("/test")
    public void test() {
        String arr[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "12", "14", "16", "18", "20"};
        System.out.print(findCheckpoint(0, 10, 11, arr));

    }

}