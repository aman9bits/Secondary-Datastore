import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * Created by aman.gupta on 04/09/15.
 */
public class MongoApp extends Application<myConfig> {
    public static void main(String[] args) throws Exception {
        new MongoApp().run(args);
    }

    @Override
    public void run(myConfig conf, Environment environment) throws Exception {
        final myResource resource = new myResource();
        environment.jersey().register(resource);

    }
}
