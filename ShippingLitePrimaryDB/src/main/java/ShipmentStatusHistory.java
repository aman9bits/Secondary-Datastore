import javax.persistence.*;

/**
 * Created by aman.gupta on 09/09/15.
 */
@Entity
@Table(name = "shipment_status_histories")
@Access(value= AccessType.FIELD)
public class ShipmentStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "location")
    private String location;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "shipment_id")
    private long shipment_id;

    @Column(name = "new_status")
    private String new_status;

    @Column(name = "old_status")
    private String old_status;

    @Column(name = "status_date")
    private String status_date;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name="status_type")
    private  String status_type;

    @Column(name="updated_by")
    private  String updated_by;

    public ShipmentStatusHistory(String created_at, String location, String remarks, long shipment_id, String new_status, String old_status, String status_date, String updated_at, String status_type, String updated_by) {
        this.created_at = created_at;
        this.location = location;
        this.remarks = remarks;
        this.shipment_id = shipment_id;
        this.new_status = new_status;
        this.old_status = old_status;
        this.status_date = status_date;
        this.updated_at = updated_at;
        this.status_type = status_type;
        this.updated_by = updated_by;
    }

    public ShipmentStatusHistory(long id, String created_at, String location, String remarks, long shipment_id, String new_status, String old_status, String status_date, String updated_at, String status_type, String updated_by) {
        this.id = id;
        this.created_at = created_at;
        this.location = location;
        this.remarks = remarks;
        this.shipment_id = shipment_id;
        this.new_status = new_status;
        this.old_status = old_status;
        this.status_date = status_date;
        this.updated_at = updated_at;
        this.status_type = status_type;
        this.updated_by = updated_by;
    }

    public  ShipmentStatusHistory(){
        super();
    }
    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getShipment_id() {
        return shipment_id;
    }

    public void setShipment_id(long shipment_id) {
        this.shipment_id = shipment_id;
    }

    public String getNew_status() {
        return new_status;
    }

    public void setNew_status(String new_status) {
        this.new_status = new_status;
    }

    public String getOld_status() {
        return old_status;
    }

    public void setOld_status(String old_status) {
        this.old_status = old_status;
    }

    public String getStatus_date() {
        return status_date;
    }

    public void setStatus_date(String status_date) {
        this.status_date = status_date;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus_type() {
        return status_type;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String convertToString(){
        return "hello";
    }
}