import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by aman.gupta on 15/10/15.
 */
public class CounterQueryThread extends Thread {
    String script;
    String commonJSON;
    String diffJSON1;
    String diffJSON2;
    String diffJSON3;
    String diffJSON4;
    int port;
    long min;
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


        ArrayList<ArrayList<Object>> result = (ArrayList<ArrayList<Object>>) jedis.evalsha(script, 1, "testing", String.valueOf(min), String.valueOf(max), commonJSON, diffJSON1, diffJSON2, diffJSON3, diffJSON4, "shipmentID");
        //ArrayList<ArrayList<String>> result = (ArrayList<ArrayList<String>>) jedis.evalsha(script, 1, "test", "0", "1000", "^asd", "a", "^qe", "sd");
        System.out.println(result);
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