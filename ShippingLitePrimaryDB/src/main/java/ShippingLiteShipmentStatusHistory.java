import javax.persistence.*;

/**
 * Created by aman.gupta on 09/09/15.
 */
@Entity
@Table(name = "ShipmentStatusHistories")
/*@NamedQueries({
       @NamedQuery(
               name="ShipmentStatusHistories.ByID",
               query = "SELECT s FROM ShipmentStatusHistories s WHERE s.id= :id;"
       )
})*/
@Access(value= AccessType.FIELD)
public class ShippingLiteShipmentStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "parentSrId")
    private String parentSrId;

    @Column(name = "receivedBy")
    private String receivedBy;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "shipmentId")
    private long shipmentId;

    @Column(name = "srId")
    private String srId;

    @Column(name = "status")
    private String status;

    @Column(name = "statusDateTime")
    private String statusDateTime;

    @Column(name = "statusLocation")
    private String statusLocation;

    @Column(name = "updatedAt")
    private String updatedAt;

    @Column(name="secondaryStatus")
    private  String secondaryStatus;

    @Column(name="statusType")
    private  String statusType;

    @Column(name="updatedBy")
    private  String updatedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getParentSrId() {
        return parentSrId;
    }

    public void setParentSrId(String parentSrId) {
        this.parentSrId = parentSrId;
    }

    public String getSecondaryStatus() {
        return secondaryStatus;
    }

    public void setSecondaryStatus(String secondaryStatus) {
        this.secondaryStatus = secondaryStatus;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getReceivedBy() {
        return receivedBy;

    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSrId() {
        return srId;
    }

    public void setSrId(String srId) {
        this.srId = srId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDateTime() {
        return statusDateTime;
    }

    public void setStatusDateTime(String statusDateTime) {
        this.statusDateTime = statusDateTime;
    }

    public String getStatusLocation() {
        return statusLocation;
    }

    public void setStatusLocation(String statusLocation) {
        this.statusLocation = statusLocation;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ShippingLiteShipmentStatusHistory(String createdAt, String parentSrId, String receivedBy, String remarks, long shipmentId, String srId, String status, String statusDateTime, String statusLocation, String updatedAt, String secondaryStatus, String statusType, String updatedBy) {
        this.createdAt = createdAt;
        this.parentSrId = parentSrId;
        this.receivedBy = receivedBy;
        this.remarks = remarks;
        this.shipmentId = shipmentId;
        this.srId = srId;
        this.status = status;
        this.statusDateTime = statusDateTime;
        this.statusLocation = statusLocation;
        this.updatedAt = updatedAt;
        this.secondaryStatus=secondaryStatus;
        this.statusType=statusType;
        this.updatedBy=updatedBy;
    }
    public String convertToString(){
        return "hello";
    }
}