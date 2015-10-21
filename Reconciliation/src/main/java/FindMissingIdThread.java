import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by aman.gupta on 21/09/15.
 */
public class FindMissingIdThread extends Thread {
    Jedis jedis;
    String shardId;
    String checkpoint;

    public FindMissingIdThread(String shardId, String checkpoint) {
        this.shardId = shardId;
        this.checkpoint = checkpoint;
    }

    public void run() {
        //Create a redis connection
        jedis = new Jedis("localhost");
        // Get the list of checkpoint inserted for given shard
        Set<String> list = jedis.zrangeByScore(shardId, Long.parseLong(checkpoint), Long.MAX_VALUE);
        String[] arr = list.toArray(new String[0]);
        //System.out.print(shardId+"sad"+ Long.parseLong(checkpoint));
        /*
        if(shardId.equals("shard1"))
        for(int i=0;i<arr.length;i++){
            System.out.print(shardId+arr[i]);
        }*/

        //Storing the missing entries in an array
        int length = list.size();
        int low = 0;
        int high = length - 1;
        Map<String, Double> arr2 = new HashMap<String, Double>();
        binarySearch(low, high, arr, arr2);

        System.out.println(arr2.toString());

        //Storing the list of missing numbers from a shard in a Redis sorted set
        jedis.zadd("missingFrom" + shardId, arr2);
    }

    void binarySearch(int low, int high, String[] arr, Map arr2) {
        //If only 2 entries, check insert the missing entries between them into arr2
        if ((high - low) == 1) {
            for (long i = Integer.parseInt(arr[low]) + 1; i < Integer.parseInt(arr[high]); i++) {
                arr2.put(String.valueOf(i), (double) i);

            }
            return;
        }

        //If difference in indices is not equal to difference in values, values are missing
        if ((high - low) != (Integer.parseInt(arr[high]) - Integer.parseInt(arr[low]))) {
            int mid = (low + high) / 2;
            //Repeat the process on first half of array
            binarySearch(low, mid, arr, arr2);
            //Repeat the process on second half of array
            binarySearch(mid, high, arr, arr2);
        }
    }
}