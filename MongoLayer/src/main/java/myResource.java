import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.BsonDocument;
import org.bson.BsonNumber;
import org.bson.BsonValue;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by aman.gupta on 07/10/15.
 */

@Path("/")
public class myResource {

    @POST
    @Path("/insertMongo")
    @Produces("application/json")
    public ResponsePOJO insertShipment(String request) throws ParseException {
        BsonDocument shipment = BsonDocument.parse(request);
        shipment.append("latest_status", new BsonDocument("checkpoint_id", shipment.getNumber("checkpointID")).append("status", shipment.getString("new_status")));
        shipment.append(shipment.getString("new_status").getValue()+"_state_details",new BsonDocument("checkpointID",shipment.get("checkpointID"))
                .append("created_at",shipment.get("created_at"))
                .append("location",shipment.get("location"))
                .append("remarks",shipment.get("remarks"))
                .append("status_date",shipment.get("status_date"))
                .append("status_type",shipment.get("status_type"))
                .append("updated_by",shipment.get("updated_by")));

        String status = shipment.getString("new_status").getValue();
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");

        Date date = sdf.parse(shipment.getString("created_at").getValue());
        long timestamp = date.getTime()/1000;

        shipment.remove("checkpointID");
        shipment.remove("new_status");
        shipment.remove("created_at");
        shipment.remove("location");
        shipment.remove("remarks");
        shipment.remove("shipment_id");
        shipment.remove("status_date");
        shipment.remove("updated_at");
        shipment.remove("status_type");
        shipment.remove("updated_by");
        MongoClient mongoClient =  new MongoClient("localhost");
        MongoDatabase database = mongoClient.getDatabase("shipping_staging");
        MongoCollection<Document> collection = database.getCollection("Shipments");
        Document query = new Document("_id", shipment.getNumber("id"));
        Document update = new Document("$max",shipment);
        //UpdateOptions options = new UpdateOptions().upsert(true);
        //collection.updateOne(query,update,options);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER);
        Document newDoc = collection.findOneAndUpdate(query, update, options);
        mongoClient.close();

        System.out.println(shipment.getNumber("id").toString());
        BufferedWriter bw = null;

        try {
            // APPEND MODE SET HERE
            bw = new BufferedWriter(new FileWriter("shipments.txt", true));
            bw.write(shipment.getNumber("id").toString());
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {                       // always close the file
            if (bw != null) try {
                bw.close();
            } catch (IOException ioe2) {
            }
        }

        String latest_status = ((Document)newDoc.get("latest_status")).getString("status");
        ResponsePOJO response = new ResponsePOJO(timestamp,newDoc.getInteger("id"),newDoc.getInteger("vendor_id"),status,latest_status,newDoc.getString("merchant_code"));
        return response;
    }

    @POST
    @Path("/insertMongo2")
    public void insertShipment2(String request) {
        BsonDocument shipment = BsonDocument.parse(request);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
       // System.out.print(shipment.getString("createdAt").getValue());
        DateTime eventTime = formatter.parseDateTime(shipment.getString("created_at").getValue());
        MongoClient mongoClient = new MongoClient("localhost");
        MongoDatabase database = mongoClient.getDatabase("shipping_staging");
        MongoCollection<Document> collection = database.getCollection("Date" + eventTime.dayOfYear().getAsText());
        BsonNumber checkpointID = shipment.getNumber("checkpointID");
/*
        //Document query = new Document("_id",eventTime.getHourOfDay()).append("shipments.id", shipment.getString("id").toString());
       // Document query = new Document("_id",eventTime.getHourOfDay());
        Document update = new Document("$addToSet",new Document("shipments", shipment)).append("$max",new Document("shipments", shipment));
        //for(Map.Entry<String,BsonValue> entry : shipment.entrySet()){
          //  update.append("$max",new Document("shipments.$."+entry.getKey(),entry.getValue()));
        //}

        UpdateOptions options = new UpdateOptions().upsert(true);
        //update.append("$setOnInsert",new Document("Date" + eventTime.dayOfYear().getAsText()+".shipments","abc"));
        collection.updateOne(query,update,options);
  */
        Document query1 = new Document("_id",eventTime.getHourOfDay()).append("shipments.checkpointID", shipment.getString("_id").getValue());
        Document temp = new Document();
        for(Map.Entry<String,BsonValue> entry : shipment.entrySet()){
          temp.append("shipments.$."+entry.getKey(),entry.getValue());
        }
        //temp.append("shipments.$.latest_status",new Document("checkpointId",new BsonDateTime(eventTime.getMillis()/1000)).append("status",shipment.getString("status").getValue()));
        temp.append("shipments.$.latest_status",new Document("checkpointId",checkpointID).append("status",shipment.getString("status").getValue()));
        //System.out.print(temp.toString());
        Document update1 = new Document("$max",temp);

        Document query2 = new Document("_id",eventTime.getHourOfDay()).append("shipments.checkpointID", new Document("$ne",shipment.getString("_id").getValue()));
        Document update2 = new Document("$push",new Document("shipments",shipment.append("latest_status",new BsonDocument("checkpointId",checkpointID).append("status",shipment.getString("status")))));

        Document query3 = new Document("_id",eventTime.getHourOfDay());
        Document update3 = new Document("$setOnInsert",new Document("shipments",Arrays.asList(shipment.append("latest_status", new BsonDocument("checkpointId",checkpointID).append("status", shipment.getString("status"))))));
        System.out.print(collection.bulkWrite(
                Arrays.asList(
                        new UpdateOneModel<Document>(query1, update1),
                        new UpdateOneModel<Document>(query2, update2),
                        new UpdateOneModel<Document>(query3, update3, new UpdateOptions().upsert(true))
                ),
                new BulkWriteOptions().ordered(false)

        ));
        mongoClient.close();
    }
/*
    @Path("/getSamples")
    @Produces("application/json")
    @POST
    public String getSamples(String input){
        String result;
        MongoClient mongoClient = new MongoClient("localhost");
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("Date" + eventTime.dayOfYear().getAsText());
        return result;
    }*/

}

