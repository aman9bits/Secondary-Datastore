import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * Created by aman.gupta on 04/09/15.
 */
public class RedisCounterApp extends Application<CounterConfig> {
    public static void main(String[] args) throws Exception {
        new RedisCounterApp().run(args);
    }

    @Override
    public void run(CounterConfig conf, Environment environment) throws Exception {
        final CounterResource resource = new CounterResource(conf.getListOfRedisPorts(), conf.getListOfLuaScripts(),conf.getNumberOfCounters(),conf.getCounterQueryParams(),conf.getRedisServerIPAddress(),conf.getRedisListName());
        environment.jersey().register(resource);
    }
}
