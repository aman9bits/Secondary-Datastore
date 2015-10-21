import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by aman.gupta on 09/09/15.
 */
public class ShippingLiteShipmentStatusHistoryDAO extends AbstractDAO<ShippingLiteShipmentStatusHistory> {
    public ShippingLiteShipmentStatusHistoryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public void create(ShippingLiteShipmentStatusHistory his) {
        persist(his);
    }

    public ShippingLiteShipmentStatusHistory getShipmentStatusById(long id) {
        return get(id);
    }

    public List<Object[]> getShipmentStatusByIdList(String request, String cols) {
        request = request.replaceAll("\\[", "(").replaceAll("\\]", ")");
/*
        //List result = currentSession().createSQLQuery("Select id,createdAt, parentSrId, receivedBy,remarks,shipmentId,srId,status,statusDateTime,statusLocation, updatedAt from ShipmentStatusHistories where id =1").list();
        List<ShipmentStatusHistory> shipmentStatusHistories = list(namedQuery("ShipmentStatusHistories.ByID").setInteger("id",1));
        for(ShipmentStatusHistory object : shipmentStatusHistories)
        {
           // Map row = (Map)object;
            System.out.print(object.toString());
            //System.out.print("id: " + row.get("id"));

        }

  */
        String query = "SELECT " + cols + " FROM ShipmentStatusHistories where id in " + request;
        //System.out.print(query);
        List<Object[]> result = currentSession().createSQLQuery(query).list();
        return result;
        //return null;
    }
}
