import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by aman.gupta on 04/09/15.
 */
public class ReconciliationApplication extends Application<ReconciliationConfiguration> {
    @Override
    public void initialize(Bootstrap<ReconciliationConfiguration> bootstrap) {
        //bootstrap.addBundle(new RedisBundle());
    }

    public static void main(String[] args) throws Exception {
        new ReconciliationApplication().run(args);
    }

    @Override
    public void run(final ReconciliationConfiguration conf, Environment environment) throws Exception {
/*
        Injector baseInjector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ReconciliationConfiguration.class).toInstance(conf);
                bind(ReconciliationResource.class);
            }
        });
        environment.jersey().register(baseInjector.getInstance(ReconciliationResource.class));
*/
        environment.jersey().register(new ReconciliationResource(conf));
    }

}