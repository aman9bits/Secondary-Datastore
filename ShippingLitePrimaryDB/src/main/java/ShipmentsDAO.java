import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by aman.gupta on 09/09/15.
 */
public class ShipmentsDAO extends AbstractDAO<Shipments> {
    public ShipmentsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Shipments getShipmentById(long id) {
        return get(id);
    }

    public void create(Shipments ship) {
        currentSession();
        persist(ship);
    }
}