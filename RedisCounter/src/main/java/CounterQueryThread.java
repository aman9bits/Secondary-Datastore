import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by aman.gupta on 15/10/15.
 */
public class CounterQueryThread extends Thread {
    //Lua Script Hash code
    String script;
    //Parameters common to all counters
    String commonJSON;
    //Parameters particular to each counter
    String diffJSON;
    //redis port number
    int port;
    //lower bound for timestamp range
    String min;
    //upper bound for timestamp range
    String max;

    int counterIndex = 0;
    ArrayList<ArrayList<Object>> counterWithList;
    Map<Integer,Response> responseCodes;

    String noOfCounters;
    String idToBeReturned;
    String redisListName;
    String redisServerIPAddress; 
    public CounterQueryThread(ArrayList<ArrayList<Object>> counterWithList, Map<String,String> scripts, int port,JSONObject request, Map<Integer,Response> responseCodes, CounterQueryParams counterQueryParams, String redisServerIPAddress) {

        this.counterWithList=counterWithList;
        this.script = scripts.get(counterQueryParams.getCounterQueryScript());
        this.commonJSON = request.get(Constants.COMMON_JSON).toString();
        this.diffJSON = request.get(Constants.DIFF_JSON).toString();
        this.port = port;
        this.min = request.get(Constants.LOWER_BOUND).toString();
        this.max = request.get(Constants.UPPER_BOUND).toString();
        this.responseCodes = responseCodes;
        this.noOfCounters = String.valueOf(counterQueryParams.getNumberOfCounters());
        this.redisListName = counterQueryParams.getRedisListName();
        this.idToBeReturned = counterQueryParams.getIdToBeReturned();
        this.redisServerIPAddress = redisServerIPAddress;
    }

    public void run() {
        Response response = Response.status(Response.Status.OK).build();
        Jedis jedis;
        try{
            jedis = new Jedis(redisServerIPAddress, port);
            try {
                //Get the arraylists with counters and list of shipmentIDs
                ArrayList<ArrayList<Object>> result = (ArrayList<ArrayList<Object>>) jedis.evalsha(script, 1, redisListName, min, max, commonJSON, diffJSON, idToBeReturned, noOfCounters);

                System.out.println(result);

                //Add the counters to the global counterWithList variable and append the list of shipmentIDs
                synchronized (counterWithList) {
                    Iterator<ArrayList<Object>> it = result.iterator();
                    Iterator<ArrayList<Object>> it2 = counterWithList.iterator();
                    while (it.hasNext() && it2.hasNext()) {
                        ArrayList<Object> row = it.next();
                        ArrayList<Object> row2 = it2.next();
                        //System.out.println(Long.parseLong(row.get(counterIndex).toString())+" "+Long.parseLong(row2.get(counterIndex).toString())+" "+(Long.parseLong(row.get(counterIndex).toString())+Long.parseLong(row2.get(counterIndex).toString())));
                        row2.set(counterIndex, (Long.parseLong(row.get(counterIndex).toString())+Long.parseLong(row2.get(counterIndex).toString())));
                        row2.addAll(row.subList(1,row.size()));
                    }
                }
                jedis.close();
            }catch(Exception e){
                if(!jedis.scriptExists(script)){
                    response = Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Redis port:" + port + "\tPlease load the lua script again.").build();
                }
                else {
                    response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Redis port:" + port + "\t" + e.getMessage()).build();
                }
                    System.out.println(e.getClass());
            }
        }catch (JedisConnectionException ex){
            response = Response.status(Response.Status.BAD_GATEWAY).entity("Redis Server at port no. "+port+" is not running.").build();
        }catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Redis port:"+port+"\t"+e.getMessage()).build();
        }
        responseCodes.put(port, response);
    }
}