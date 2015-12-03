/**
 * Created by aman.gupta on 10/11/15.
 */
public class CounterQueryParams {
    private String counterQueryScript;
    private String redisListName;
    private String idToBeReturned;
    private int numberOfCounters;

    public int getNumberOfCounters() {
        return numberOfCounters;
    }

    public void setNumberOfCounters(int numberOfCounters) {
        this.numberOfCounters = numberOfCounters;
    }

    public String getCounterQueryScript() {
        return counterQueryScript;
    }

    public void setCounterQueryScript(String counterQueryScript) {
        this.counterQueryScript = counterQueryScript;
    }

    public String getRedisListName() {
        return redisListName;
    }

    public void setRedisListName(String redisListName) {
        this.redisListName = redisListName;
    }

    public String getIdToBeReturned() {
        return idToBeReturned;
    }

    public void setIdToBeReturned(String idToBeReturned) {
        this.idToBeReturned = idToBeReturned;
    }
}
