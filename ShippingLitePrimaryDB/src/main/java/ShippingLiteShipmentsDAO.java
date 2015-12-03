import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 * Created by aman.gupta on 09/09/15.
 */
public class ShippingLiteShipmentsDAO extends AbstractDAO<ShippingLiteShipments> {
    public ShippingLiteShipmentsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ShippingLiteShipments getShipmentContextBySRId(String SRId) {
        Session session = currentSession();
        Criteria criteria = session.createCriteria(ShippingLiteShipments.class);
        ShippingLiteShipments shipment = (ShippingLiteShipments) criteria.add(Restrictions.eq("serviceRequestId", SRId))
                .uniqueResult();
        return shipment;
    }

    public ShippingLiteShipments getShipmentContextByShipmentId(long id) {
        return get(id);
    }

    public void create(ShippingLiteShipments ship) {
        currentSession();
        persist(ship);
    }
}
