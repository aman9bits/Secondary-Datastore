import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by aman.gupta on 11/11/15.
 */
public class CounterResourceTest {
    CounterConfig config;
    CounterResource resource = new CounterResource(config.getListOfRedisPorts(),config.getListOfLuaScripts(),config.getNumberOfCounters(),config.getCounterQueryParams(),config.getRedisServerIPAddress(),config.getRedisListName());

    @Before
    public void initializeTest(){}

    @Test
    public void testCounterQuery() throws InterruptedException {
        String request ="{\n" +
                "    \"commonJson\":{\"k1\":[\"v1\",\"V1\"]},\n" +
                "    \"diffJson\":[\n" +
                "        {\"k3\":\"v3\",\"k2\":\"v2\"},{\"k4\":\"v4\"},{\"k5\":\"v5\"},{\"k6\":\"v6\"},{\"k2\":\"v2\"}],\n" +
                "    \"lowerBound\":0,\n" +
                "    \"upperBound\":142708999900000\n" +
                "}";
        String answer = "[[1, 123], [1, 123], [1, 123], [1, 123], [1, 123]]";
        assertEquals("Must be equal",answer,resource.counterQuery(request).toString());
    }

    @After
    public void cleanup(){
        CounterQueryThread cqt = Mockito.mock(CounterQueryThread.class);
        //Mockito.when(cqt.start()).then

      //  Mockito.doNothing().when(cqt).start();
    }
}
