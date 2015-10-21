import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by aman.gupta on 09/09/15.
 */
public class ShippingLiteShipmentsDAO extends AbstractDAO<ShippingLiteShipments> {
    public ShippingLiteShipmentsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ShippingLiteShipments getShipmentById(long id) {
        return get(id);
    }

    public void create(ShippingLiteShipments ship) {
        currentSession();
        persist(ship);
    }
}
