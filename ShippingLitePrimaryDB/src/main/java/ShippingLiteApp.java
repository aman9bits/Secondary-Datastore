import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by aman.gupta on 24/08/15.
 */
public class ShippingLiteApp extends Application<myConfig> {

    public static void main(String[] args) throws Exception {
        new ShippingLiteApp().run(args);
    }

    public void initialize(Bootstrap<myConfig> bootstrap) {
        // nothing to do yet
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(myConfig myConfig, Environment environment) throws Exception {
        final ShipmentStatusHistoryDAO dao1 = new ShipmentStatusHistoryDAO(hibernate.getSessionFactory());
        final ShipmentsDAO dao2 = new ShipmentsDAO(hibernate.getSessionFactory());
        final ShippingLiteShipmentsDAO dao3 = new ShippingLiteShipmentsDAO(hibernate.getSessionFactory());
        final ShippingLiteShipmentStatusHistoryDAO dao4 = new ShippingLiteShipmentStatusHistoryDAO(hibernate.getSessionFactory());

        environment.jersey().register(new myResource(dao1,dao2,dao3,dao4));
    }
    private final HibernateBundle<myConfig> hibernate = new HibernateBundle<myConfig>(ShipmentStatusHistory.class, Shipments.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(myConfig configuration) {
            return configuration.getDataSourceFactory();
        }
    };
}
