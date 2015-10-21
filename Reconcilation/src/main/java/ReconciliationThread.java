import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by aman.gupta on 16/09/15.
 */
public class ReconciliationThread extends Thread {
    String shardId;

    public ReconciliationThread(String shardId) {
        this.shardId = shardId;
    }

    public void run() {
        //Create redis connection
        Jedis jedis = new Jedis("localhost");

        //Run loop till the missing ids are present
        while (jedis.zcard("missingFrom" + shardId) != 0) {
            //Pickup missing entries in a batch of 100
            Set<String> listOfIds = jedis.zrange("missingFrom" + shardId, 0, 100);
            StringBuffer response = new StringBuffer();
            //Get data corresponding to first 100 entries
            try {
                URL obj = new URL("http://localhost:8074/ShipmentStatusHistoryBatch");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                //con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(listOfIds.toString());
                //System.out.println(listOfIds.toString());
                wr.flush();
                wr.close();
                /*
                    if(con.getResponseCode()==404){
                    insertCheckpoint2(i);       //if entry not present, directly create its checkpoint
                    return true;
                }*/
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                //System.out.println(response);
                in.close();

            } catch (Exception e) {
                System.out.println("unable to connect");
                e.printStackTrace();
            }

            JSONObject jObject = new JSONObject(response.toString());
            Iterator<String> iterator = jObject.keys();
            Map<Boolean, List<String>> resultStatus = new HashMap<Boolean, List<String>>();
            //Create a list of successful and unsuccessful reconcilation
            List<String> trueList = new ArrayList<String>();
            List<String> falseList = new ArrayList<String>();
            while (iterator.hasNext()) {
                String id = iterator.next();
                try {
                    JSONObject result = (JSONObject) jObject.get(id);
                    try {
                        //Trigger marvin workflow with payload as data from primary DB
                        //System.out.print(result.toString());
                        URL obj = new URL("http://flo-marvin-backend.stage.ch.flipkart.com/workflows/aesop_marvin/start");
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                        con.setRequestProperty("Content-Type", "application/json");
                        con.setRequestProperty("Accept", "application/json");
                        con.setDoOutput(true);
                        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                        wr.writeBytes(result.toString());
                        wr.flush();
                        wr.close();
                        System.out.println(con.getResponseCode());
                        if (con.getResponseCode() >= 200 && con.getResponseCode() < 300) {
                            trueList.add(id);
                        } else {
                            falseList.add(id);
                        }
                    } catch (Exception e) {
                        System.out.println("failure");
                        falseList.add(id);
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    //If no entry present in primary database, mark as success
                    trueList.add(id);
                }
            }
            resultStatus.put(true, trueList);
            resultStatus.put(false, falseList);
            String[] remList = trueList.toArray(new String[trueList.size()]);
            //String remList = StringUtils.join(trueList.toArray(),' ');
            //System.out.print(remList[0]);
            //Remove the successfully reconciled list from missing id list
            try {
                jedis.zrem("missingFrom" + shardId, remList);
            } catch (JedisDataException E) {
                System.out.println("No problem");
            }
        }
    }
}