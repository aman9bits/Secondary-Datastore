import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
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
    //Parameters particular to first counter
    String diffJSON1;
    //Parameters particular to second counter
    String diffJSON2;
    //Parameters particular to third counter
    String diffJSON3;
    //Parameters particular to fourth counter
    String diffJSON4;
    //redis port number
    int port;
    //lower bound for timestamp range
    String min;
    //upper bound for timestamp range
    String max;

    ArrayList<ArrayList<Object>> counterWithList;
    Map<Integer,Response> responseCodes;

    public CounterQueryThread(ArrayList<ArrayList<Object>> counterWithList, String script, int port,JSONObject request, Map<Integer,Response> responseCodes) {
        this.counterWithList=counterWithList;
        this.script = script;
        this.commonJSON = request.get("commonJson").toString();
        this.diffJSON1 = request.get("diffJsonForCounter1").toString();
        this.diffJSON2 = request.get("diffJsonForCounter2").toString();
        this.diffJSON3 = request.get("diffJsonForCounter3").toString();
        this.diffJSON4 = request.get("diffJsonForCounter4").toString();
        this.port = port;
        this.min = request.get("lowerBound").toString();
        this.max = request.get("upperBound").toString();
        this.responseCodes = responseCodes;
    }

    public void run() {
        Response response = Response.status(Response.Status.OK).build();
        Jedis jedis;
        try{
            jedis = new Jedis("localhost", port);
            try {
                //Get the arraylists with counters and list of shipmentIDs
                ArrayList<ArrayList<Object>> result = (ArrayList<ArrayList<Object>>) jedis.evalsha(script, 1, "testing", min, max, commonJSON, diffJSON1, diffJSON2, diffJSON3, diffJSON4, "shipmentID");

                System.out.println(result);

                //Add the counters to the global counterWithList variable and append the list of shipmentIDs
                synchronized (counterWithList) {
                    Iterator<ArrayList<Object>> it = result.iterator();
                    Iterator<ArrayList<Object>> it2 = counterWithList.iterator();
                    while (it.hasNext() && it2.hasNext()) {
                        ArrayList<Object> row = it.next();
                        ArrayList<Object> row2 = it2.next();
                        //System.out.println(Long.parseLong(row.get(0).toString())+" "+Long.parseLong(row2.get(0).toString())+" "+(Long.parseLong(row.get(0).toString())+Long.parseLong(row2.get(0).toString())));
                        row2.set(0, (Long.parseLong(row.get(0).toString())+Long.parseLong(row2.get(0).toString())));
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