import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

/**
 * Created by aman.gupta on 26/10/15.
 */
public class CounterQueryPOJO {
    JSONObject commonJson;
    JSONObject diffJsonForCounter1;
    JSONObject diffJsonForCounter2;
    JSONObject diffJsonForCounter3;
    JSONObject diffJsonForCounter4;
    long lowerBound;
    long upperBound;

    public CounterQueryPOJO(JSONObject commonJson, JSONObject diffJsonForCounter1, JSONObject diffJsonForCounter2, JSONObject diffJsonForCounter3, JSONObject diffJsonForCounter4, long lowerBound, long upperBound) {
        this.commonJson = commonJson;
        this.diffJsonForCounter1 = diffJsonForCounter1;
        this.diffJsonForCounter2 = diffJsonForCounter2;
        this.diffJsonForCounter3 = diffJsonForCounter3;
        this.diffJsonForCounter4 = diffJsonForCounter4;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public CounterQueryPOJO(){super();}

    @JsonProperty
    public long getLowerBound() {
        return lowerBound;
    }
    @JsonProperty
    public void setLowerBound(long lowerBound) {
        this.lowerBound = lowerBound;
    }
    @JsonProperty
    public long getUpperBound() {
        return upperBound;
    }
    @JsonProperty
    public void setUpperBound(long upperBound) {
        this.upperBound = upperBound;
    }
    @JsonProperty
    public JSONObject getCommonJson() {
        return commonJson;
    }
    @JsonProperty
    public void setCommonJson(JSONObject commonJson) {
        this.commonJson = commonJson;
    }
    @JsonProperty
    public JSONObject getDiffJsonForCounter1() {
        return diffJsonForCounter1;
    }
    @JsonProperty
    public void setDiffJsonForCounter1(JSONObject diffJsonForCounter1) {
        this.diffJsonForCounter1 = diffJsonForCounter1;
    }
    @JsonProperty
    public JSONObject getDiffJsonForCounter2() {
        return diffJsonForCounter2;
    }
    @JsonProperty
    public void setDiffJsonForCounter2(JSONObject diffJsonForCounter2) {
        this.diffJsonForCounter2 = diffJsonForCounter2;
    }
    @JsonProperty
    public JSONObject getDiffJsonForCounter3() {
        return diffJsonForCounter3;
    }
    @JsonProperty
    public void setDiffJsonForCounter3(JSONObject diffJsonForCounter3) {
        this.diffJsonForCounter3 = diffJsonForCounter3;
    }
    @JsonProperty
    public JSONObject getDiffJsonForCounter4() {
        return diffJsonForCounter4;
    }
    @JsonProperty
    public void setDiffJsonForCounter4(JSONObject diffJsonForCounter4) {
        this.diffJsonForCounter4 = diffJsonForCounter4;
    }
}
