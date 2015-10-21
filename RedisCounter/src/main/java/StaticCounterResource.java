import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by aman.gupta on 24/09/15.
 */
@Path("/")
public class StaticCounterResource{
    String script = "36bdfd2d29d95a210464f319143d35160eb7d839";
    String[] blobOrder = {"shipmentID","timestamp","vendor_id","merchant_code","status","latest_status"};
    public static Long[] counter ={0L,0L,0L,0L};
    public static ArrayList<ArrayList<Object>> counterWithList = new ArrayList<ArrayList<Object>>();
    int noOfRedisInstances = 6;
    int portBase = 7000;

    StaticCounterResource(){
        super();
    }
    @POST
    @Consumes("application/json")
    @Path("/insertCounter")
    public void insertCounter(String request) {
        System.out.print(request);
        Jedis jedis = new Jedis("localhost");
        StringBuffer listName = new StringBuffer();
        double timestamp;
        StringBuffer data = new StringBuffer();
        String[] listOfParams = {"merchant_code","source_type","vendor_id","request_type","new_status"};
        JSONObject rawData = new JSONObject(request);
        for(String key : listOfParams){
            listName.append(rawData.get(key));
        }
        String timestampString = rawData.get("created_at").toString();
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");

        try {
            Date date = sdf.parse(timestampString);
            timestamp =date.getTime();
            data.append(request);
            jedis.zadd(listName.toString(), timestamp, data.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        jedis.close();

    }

    @GET
    @Path("/test2")
    public void test(){
        long epoch = System.currentTimeMillis()/1000;
        Jedis jedis = new Jedis();
        String num = String.valueOf(ThreadLocalRandom.current().nextInt(100, 1000));

        num = num+epoch;
        System.out.print(num);
        jedis.zadd("ShipmentStatus",epoch,num);
        jedis.close();
    }

    @PUT
    @Path("/loadLuaScript")
    public void loadScript(String scriptName){
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(scriptName).getFile());
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append(" ");
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<noOfRedisInstances;i++){
            Jedis jedis = new Jedis("localhost",portBase+i);
            script = jedis.scriptLoad(result.toString());
            System.out.print(script);
            jedis.close();
        }
    }

    @GET
    @Produces("text/plain")
    @Path("/testQuery")
    public String testQuery(){
        Jedis jedis = new Jedis("localhost",7004);
        ArrayList<ArrayList<String>> result = (ArrayList<ArrayList<String>>) jedis.evalsha(script, 1, "ShipmentStatus", "1427006148", "1427086148", "46784460142707614812FKMPpickup_out_for_pickuppickup_out_for_pickup", "^31.1444......", "^61.1444......", "^81.1444......");
        //ArrayList<ArrayList<String>> result = (ArrayList<ArrayList<String>>) jedis.evalsha(script, 1, "test", "0", "1000", "^asd", "a", "^qe", "sd");
        ArrayList<String> counters = new ArrayList<String>();
        ArrayList<String> listOfShipments = new ArrayList<String>();
        System.out.print(result);
        Iterator<ArrayList<String>> it = result.iterator();
        while(it.hasNext()){
            ArrayList<String> row = it.next();
            Iterator<String> cell = row.iterator();
            counters.add(cell.next());
            while(cell.hasNext()){
                listOfShipments.add(cell.next());
            }
        }
        jedis.close();
        return counters.toString();
    }



    @POST
    @Produces("text/plain")
    @Path("/counterQuery")
    public String counterQuery(String request) throws InterruptedException {

        JSONObject json = new JSONObject(request);
        String json1 = json.getJSONObject("common").toString();
        String json2 = json.getJSONObject("diff1").toString();
        String json3 = json.getJSONObject("diff2").toString();
        String json4 = json.getJSONObject("diff3").toString();
        String json5 = json.getJSONObject("diff4").toString();
        long min = json.getLong("min");
        long max = json.getLong("max");
        counterWithList.clear();
        ArrayList<Object> temp = new ArrayList<Object>();
        ArrayList<Object> temp1 = new ArrayList<Object>();
        ArrayList<Object> temp2 = new ArrayList<Object>();
        ArrayList<Object> temp3 = new ArrayList<Object>();
        temp.add(0,0L);
        temp1.add(0,0L);
        temp2.add(0,0L);
        temp3.add(0,0L);
        counterWithList.add(temp);
        counterWithList.add(temp1);
        counterWithList.add(temp2);
        counterWithList.add(temp3);
        List<CounterQueryThread> thread = new ArrayList<CounterQueryThread>();
        for (int i =0;i<noOfRedisInstances;i++) {
            thread.add(new CounterQueryThread(script,json1,json2,json3,json4,json5,portBase+i,min,max));
        }
        Iterator<CounterQueryThread> i = thread.iterator();
        while (i.hasNext()) {
            CounterQueryThread th = i.next();
            //Start each thread
            th.start();
            th.join();
        }

        return counterWithList.toString();
    }

    @POST
    @Produces("text/plain")
    @Path("/counterQueryBackup")
    public String counterQueryBackup(String request) throws InterruptedException {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss.SSSS a");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);


        JSONObject json = new JSONObject(request);
        long min = json.getLong("min");
        long max = json.getLong("max");
        counterWithList.clear();
        ArrayList<Object> temp = new ArrayList<Object>();
        ArrayList<Object> temp1 = new ArrayList<Object>();
        ArrayList<Object> temp2 = new ArrayList<Object>();
        ArrayList<Object> temp3 = new ArrayList<Object>();
        temp.add(0,0L);
        temp1.add(0,0L);
        temp2.add(0,0L);
        temp3.add(0,0L);
        counterWithList.add(temp);
        counterWithList.add(temp1);
        counterWithList.add(temp2);
        counterWithList.add(temp3);
        List<CounterQueryThread> thread = new ArrayList<CounterQueryThread>();
        String regex1 = "10000";
        String regex2 = "46784123142707754612FKMPpickup_reattemptpickup_reattempt";
        //String regex3 = "....................FKMPpickup_reattemptpickup_reattempt";
        String regex3 = "46784113142707611812FKMPpickup_out_for_pickuppickup_out_for_pickup";
        String regex4 = "46784113142707611812FKMPpickup_out_for_pickuppickup_out_for_pickup";
        //String regex4 = "..................12FKMPpickup_out_for_pickuppickup_out_for_pickup";
        for (int i =0;i<noOfRedisInstances;i++) {
       //     thread.add(new CounterQueryThread(portBase+i,script,regex1,regex2,regex3,regex4,min,max));
        }
        Iterator<CounterQueryThread> i = thread.iterator();
        while (i.hasNext()) {
            CounterQueryThread th = i.next();
            //Start each thread
            th.start();
            th.join();
        }


        Date date2 = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy h:mm:ss.SSSS a");
        String formattedDate2 = sdf2.format(date);
        System.out.println(formattedDate2);

        return counterWithList.toString();
    }


    @GET
    @Produces("text/plain")
    @Path("/testQuery2")
    public String testQuery2(){
        Jedis jedis = new Jedis("localhost");
        String response = jedis.zrangeByScore("ShipmentStatus",0,1444205139).toString();
        jedis.close();
        return response;
    }

    @POST
    @Path("/insertShipmentJSON")
    @Consumes(MediaType.APPLICATION_JSON)
    public void insertShipmentJSON(RequestPOJO request){
        JSONObject json = new JSONObject(request);
        int port = portBase + hash(request.getTimestamp());
        System.out.println(port);
        //System.out.print(json.toString());
        Jedis jedis = new Jedis("localhost",port);
        jedis.zadd("ShipmentJSON",request.getTimestamp(),json.toString());
        jedis.close();
    }

    @POST
    @Path("/insertShipmentBlob")
    @Consumes(MediaType.APPLICATION_JSON)
    public void insertShipment(RequestPOJO request){
        JSONObject json = new JSONObject(request);
        int port = portBase + hash(request.getTimestamp());
        System.out.println(port);
        //System.out.print(json.toString());
        Jedis jedis = new Jedis("localhost",port);
        Map<String,String> mapping = jedis.hgetAll("Mapping");
        StringBuilder blob = new StringBuilder();
        for(int i = 0;i<blobOrder.length;i++){
            String key = json.get(blobOrder[i]).toString();
            String mappedKey = mapping.get(key);
            if(mappedKey==null)
                blob.append(key);
            else
                blob.append(mappedKey);
        }
        jedis.zadd("ShipmentBlob",request.getTimestamp(),blob.toString());
        System.out.println(blob);
        jedis.close();
    }

    public int hash(long num){
        return (int)(num % noOfRedisInstances);

    }

    @POST
    @Path("/createMapping")
    public void createMapping(String request){
        Map<String,String> mapping = new HashMap<String, String>();
        JSONObject json = new JSONObject(request);
        Iterator<String> it = json.keys();
        while(it.hasNext()){
            String key = it.next();
            mapping.put(key,json.getString(key));
        }
        Jedis jedis = new Jedis("localhost");
        jedis.hmset("Mapping",mapping);
        jedis.close();
    }
}
