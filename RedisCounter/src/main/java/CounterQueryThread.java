import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;

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
    long min;
    //upper bound for timestamp range
    long max;

    public CounterQueryThread(String script, String commonJSON, String diffJSON1, String diffJSON2, String diffJSON3, String diffJSON4, int port, long min, long max) {
        this.script = script;
        this.commonJSON = commonJSON;
        this.diffJSON1 = diffJSON1;
        this.diffJSON2 = diffJSON2;
        this.diffJSON3 = diffJSON3;
        this.diffJSON4 = diffJSON4;
        this.port = port;
        this.min = min;
        this.max = max;
    }

    public void run() {
        Jedis jedis = new Jedis("localhost", port);
        //System.out.print(regex);
        //Get the arraylists with counters and list of shipmentIDs
        ArrayList<ArrayList<Object>> result = (ArrayList<ArrayList<Object>>) jedis.evalsha(script, 1, "testing", String.valueOf(min), String.valueOf(max), commonJSON, diffJSON1, diffJSON2, diffJSON3, diffJSON4, "shipmentID");

        System.out.println(result);

        //Add the counters to the global counterWithList variable and append the list of shipmentIDs
        synchronized (StaticCounterResource.counterWithList) {
            Iterator<ArrayList<Object>> it = result.iterator();
            Iterator<ArrayList<Object>> it2 = StaticCounterResource.counterWithList.iterator();
            while (it.hasNext()) {
                ArrayList<Object> row = it.next();
                ArrayList<Object> row2 = it2.next();
                //System.out.println(Long.parseLong(row.get(0).toString())+" "+Long.parseLong(row2.get(0).toString())+" "+(Long.parseLong(row.get(0).toString())+Long.parseLong(row2.get(0).toString())));
                row2.set(0, (Long.parseLong(row.get(0).toString())+Long.parseLong(row2.get(0).toString())));
                row2.addAll(row.subList(1,row.size()));
            }
        }
        jedis.close();
    }
}