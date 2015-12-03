import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * Created by aman.gupta on 05/11/15.
 */
public class HBaseApp extends Application<myConf> {
    public static void main(String[] args) throws Exception {
        new HBaseApp().run(args);
    }

    @Override
    public void run(myConf conf, Environment environment) throws Exception {
        final myResource resource = new myResource();
        environment.jersey().register(resource);

    }
}
