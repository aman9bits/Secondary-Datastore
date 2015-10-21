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
    //Stores Hash code for lua script
    String script;

    //Stores order of parameters in redis blob
    String[] blobOrder = {"shipmentID","timestamp","vendor_id","merchant_code","status","latest_status"};

    //Stores Final counters to be Displayed on Dashboard
    public static Long[] counter ={0L,0L,0L,0L};

    //Stores Final Counters to be displayed on dashboard, along with shipment IDs
    public static ArrayList<ArrayList<Object>> counterWithList = new ArrayList<ArrayList<Object>>();

    //Assuming port numbers of redis instances to be continuous
    //Stores number of Redis Instances running. Used in deciding the port ID.
    int noOfRedisInstances = 6;
    //Stores the base ID of Redis Instances
    int portBase = 7000;

    StaticCounterResource(){
        super();
    }

    /* Inserts counter in redis list corresponding to different parameters.
     * Not to be used now.*/

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

    /*
     * Script for generating random entries in Redis.
     * Not to be used now.
     */
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

    /*
        Load the Lua Script in redis memory.
        Provide the file name of lua script as input. The file must be present in the resources folder.
        The script is loaded in all the redis instances automatically (not the default port).
     */
    @PUT
    @Path("/loadLuaScript")
    public void loadScript(String scriptName){
        StringBuilder result = new StringBuilder("");

        //Get the lua script from resources folder.
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(scriptName).getFile());

        //Read the lua script and replace nextLine characters by spaces to create a string
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

        //Load the script in each redis instance
        for(int i = 0; i < noOfRedisInstances; i++){
            Jedis jedis = new Jedis("localhost",portBase+i);
            script = jedis.scriptLoad(result.toString());
            System.out.print(script);
            jedis.close();
        }
    }

    /*
        Testing the working of Lua Script.
        Not to be used now.
     */
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

    /*
        Get dashboard counters corresponding to given parameters.
        Takes json input with the following keys:
            1. common: json containing common parameters for all counters e.g. vendors, merchant, facility
            2. diff1: json containing parameters particular to first counter
            3. diff2: json containing parameters particular to second counter
            4. diff3: json containing parameters particular to third counter
            5. diff4: json containing parameters particular to fourth counter
            6. min: lower bound of timestamp range
            7. max: upper bound of timestamp range
         Produces a toString() of arrayList, each element of which is an arraylist with first element as counter and
         remaining as ShipmentIDs
     */
    @POST
    @Produces("text/plain")
    @Path("/counterQuery")
    public String counterQuery(String request) throws InterruptedException {
        //Convert the input to json
        JSONObject json = new JSONObject(request);

        //Extract different parameters as mentioned in description
        String json1 = json.getJSONObject("common").toString();
        String json2 = json.getJSONObject("diff1").toString();
        String json3 = json.getJSONObject("diff2").toString();
        String json4 = json.getJSONObject("diff3").toString();
        String json5 = json.getJSONObject("diff4").toString();
        long min = json.getLong("min");
        long max = json.getLong("max");

        //Clear the list of counters
        counterWithList.clear();

        //Add 4 arraylists to the counterWithList, with their first elements (counters) initialized to zero.
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

        //Create a thread for each redis instance and initialize it with the query parameters.
        List<CounterQueryThread> thread = new ArrayList<CounterQueryThread>();
        for (int i =0;i<noOfRedisInstances;i++) {
            thread.add(new CounterQueryThread(script,json1,json2,json3,json4,json5,portBase+i,min,max));
        }

        //Start thread corresponding to each redis instance and join the current thread to them.
        Iterator<CounterQueryThread> i = thread.iterator();
        while (i.hasNext()) {
            CounterQueryThread th = i.next();
            //Start each thread
            th.start();
            th.join();
        }
        //Return the counterWithList that contains counters + list of ShipmentIDs
        return counterWithList.toString();
    }

    /*
        Get counters when Inserts are as a blob.
        Not to be used now.
     */
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

    /*
        Testing redis zrange.
        Not to be used now.
     */
    @GET
    @Produces("text/plain")
    @Path("/testQuery2")
    public String testQuery2(){
        Jedis jedis = new Jedis("localhost");
        String response = jedis.zrangeByScore("ShipmentStatus",0,1444205139).toString();
        jedis.close();
        return response;
    }

    /*
        Insert Shipment information as a json in redis sorted set.
        Takes the json to be inserted as the input.
        Adds it to the redis instance decided by hash function
     */
    @POST
    @Path("/insertShipmentJSON")
    @Consumes(MediaType.APPLICATION_JSON)
    public void insertShipmentJSON(RequestPOJO request){
        //Convert input to json
        JSONObject json = new JSONObject(request);
        //Find port number using the hash function based on timestamp
        int port = portBase + hash(request.getTimestamp());
        //Create Redis connection
        Jedis jedis = new Jedis("localhost",port);
        //Add json to redis sorted set
        jedis.zadd("ShipmentJSON",request.getTimestamp(),json.toString());
        //Close redis connection
        jedis.close();
    }

    /*
        Insert shipment information as a Blob in redis sorted set.
        Takes shipment info JSON as input and converts into concatenated string.
        Decides the redis instance using hash function.
        Not to be used now.
     */
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

    /*
        Hash function to decide redis port number
     */
    public int hash(long num){
        return (int)(num % noOfRedisInstances);
    }

    /*
        Stores mapping in redis list.
        Not to be used now.
     */
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
