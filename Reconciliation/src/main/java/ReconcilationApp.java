import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * Created by aman.gupta on 04/09/15.
 */
public class ReconcilationApp extends Application<myConfig> {
    public static void main(String[] args) throws Exception {
        new ReconcilationApp().run(args);
    }

    @Override
    public void run(myConfig conf, Environment environment) throws Exception {
        final ReconciliationModuleResource resource = new ReconciliationModuleResource();
        environment.jersey().register(resource);
    }
}
