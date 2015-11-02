import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * Created by aman.gupta on 04/09/15.
 */
public class RedisCounterApp extends Application<myConfig> {
    public static void main(String[] args) throws Exception {
        new RedisCounterApp().run(args);
    }

    @Override
    public void run(myConfig conf, Environment environment) throws Exception {
        final StaticCounterResource resource = new StaticCounterResource(conf.getListOfRedisPorts(), conf.getListOfLuaScripts());
        environment.jersey().register(resource);
    }
}
