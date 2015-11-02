import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by aman.gupta on 04/09/15.
 */
public class myConfig extends Configuration {

    private List<Integer> listOfRedisPorts;
    private List<String> listOfLuaScripts;

    @JsonProperty
    public List<String> getListOfLuaScripts() {
        return listOfLuaScripts;
    }

    @JsonProperty
    public void setListOfLuaScripts(List<String> listOfLuaScripts) {
        this.listOfLuaScripts = listOfLuaScripts;
    }

    @JsonProperty
    public List<Integer> getListOfRedisPorts() {
        return listOfRedisPorts;
    }

    @JsonProperty
    public void setListOfRedisPorts(List<Integer> listOfRedisPorts) {
        this.listOfRedisPorts = listOfRedisPorts;
    }
}
