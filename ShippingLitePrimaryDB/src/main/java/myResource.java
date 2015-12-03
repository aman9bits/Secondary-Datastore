import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by aman.gupta on 24/08/15.
 */
@Path("/")
public class myResource {
    private ShipmentStatusHistoryDAO dao1;
    private ShipmentsDAO dao2;
    private  ShippingLiteShipmentsDAO dao3;
    private  ShippingLiteShipmentStatusHistoryDAO dao4;
    public myResource(ShipmentStatusHistoryDAO dao1, ShipmentsDAO dao2, ShippingLiteShipmentsDAO dao3, ShippingLiteShipmentStatusHistoryDAO dao4) {
        this.dao1=dao1;
        this.dao2=dao2;
        this.dao3=dao3;
        this.dao4=dao4;
    }

    @POST
    @Path("/insertStatusHistory")
    @Timed
    @UnitOfWork
    public void insertStatus(){
        ShippingLiteShipmentStatusHistory cus = new ShippingLiteShipmentStatusHistory("1990-12-12","a","a","a",123,"a","a","1912-12-12","a","1990-12-12","a","a","a");
        dao4.create(cus);
    }

    @GET
    @Path("/ShipmentStatusHistory/{id}")
    @Timed
    @UnitOfWork
    @Produces("application/json")
    public Response getStatusHistory(@PathParam("id") long id)
    {
        ShipmentStatusHistory his = dao1.getShipmentStatusById(id);
        if(his==null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            return Response.status(Response.Status.OK).entity(his).build();
    }


    @POST
    @Path("/ShipmentStatusHistoryBatch")
    @Timed
    @UnitOfWork
    public String getStatusHistoryBatch(String request)
    {
        // System.out.print(request);
        String cols="id,createdAt, parentSrId, receivedBy,remarks,shipmentId,srId,status,statusDateTime,statusLocation, updatedAt";
        List<String> colList= Arrays.asList(cols.split("\\s*,\\s*"));
        List<Object[]> result = dao1.getShipmentStatusByIdList(request,cols);
        //   System.out.print(result);
        return parseIntoJSONString(result, colList);
    }

    private String parseIntoJSONString(List<Object[]> result,List<String> colList){
        JSONObject jsonString = new JSONObject();
        for (Object[] entity : result) {
            Iterator<String> i =colList.iterator();
            JSONObject tempJSON=new JSONObject();
           // System.out.print(""+entity[0].toString());
            String id = entity[0].toString();
            for (Object entityCol : entity) {
                tempJSON.put(i.next(),entityCol.toString());
            }
            jsonString.put(id,tempJSON);
            //System.out.println("");
        }
        System.out.print(jsonString.toString());
        return jsonString.toString();
    }

    @GET
    @Path("/getLatestTime/{shardId}")
    @Timed
    @UnitOfWork
    public String getLatestTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp nowTime = new Timestamp(now.getTime());
        System.out.print(nowTime);
        String response = "{\"latest_time\":\"2015-09-21 19:23:01.241\",\"now\":\"2015-09-21 19:23:01.241\"}";
        return response;
    }

    @POST
    @Path("/getTimeFromCheckpointBatch")
    @Timed
    @UnitOfWork
    public String getTimeBatch(String request)
    {
        String response="{\"shard1\":\"12312312\",\"shard2\":\"14241212\",\"shard3\":\"11231235\",\"shard4\":\"11232326\"}";
        System.out.print(request);
        return response;
    }

    @GET
    @Path("/Shipment/{id}")
    @Timed
    @UnitOfWork
    @Produces("application/json")
    public Response getShipment(@PathParam("id") long id)
    {
        Shipments ship = dao2.getShipmentById(id);
        //System.out.print(ship.getMerchant_code());
        return Response.status(Response.Status.OK).entity(ship).build();
    }


    @POST
    @Path("/test")
    @UnitOfWork
    public String testPost(){

        List<Object[]> result = dao1.getShipmentStatusByIdList("[4,3,2,1]","id,createdAt, parentSrId, receivedBy,remarks,shipmentId,srId,status,statusDateTime,statusLocation, updatedAt");
        StringBuffer res = new StringBuffer();

        return res.toString();
    }
}