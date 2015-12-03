import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by aman.gupta on 06/11/15.
 */

@Path("/e2e")
public class ShippingLiteResource {
    ShippingLiteShipmentsDAO liteShipmentsDAO;
    ShippingLiteShipmentStatusHistoryDAO liteShipmentStatusHistoryDAO;
    public ShippingLiteResource(ShippingLiteShipmentsDAO liteShipmentsDAO, ShippingLiteShipmentStatusHistoryDAO liteShipmentStatusHistoryDAO) {
        this.liteShipmentsDAO = liteShipmentsDAO;
        this.liteShipmentStatusHistoryDAO = liteShipmentStatusHistoryDAO;
    }

    @GET
    @Path("/getShipmentContextBySrID/{srID}")
    @Timed
    @UnitOfWork
    @Produces("application/json")
    public Response getShipmentContextByServiceRequestID(@PathParam("srID") String srID)
    {
        ShippingLiteShipments ship = liteShipmentsDAO.getShipmentContextBySRId(srID);
        return Response.status(Response.Status.OK).entity(ship).build();
    }

    @GET
    @Path("/getShipmentContextByShipmentID/{id}")
    @Timed
    @UnitOfWork
    @Produces("application/json")
    public Response getShipmentContextByShipmentID(@PathParam("id") long id)
    {
        ShippingLiteShipments ship = liteShipmentsDAO.getShipmentContextByShipmentId(id);
        return Response.status(Response.Status.OK).entity(ship).build();
    }
}
