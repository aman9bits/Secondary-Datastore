/**
 * Created by aman.gupta on 14/10/15.
 */
public class ResponsePOJO {
    long timestamp;
    long shipmentID;
    long vendor_id;
    String status;
    String latest_status;
    String merchant_code;

    public ResponsePOJO(long timestamp, long shipmentID, long vendor_id, String status, String latest_status, String merchant_code) {
        this.timestamp = timestamp;
        this.shipmentID = shipmentID;
        this.vendor_id = vendor_id;
        this.status = status;
        this.latest_status = latest_status;
        this.merchant_code = merchant_code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(long vendor_id) {
        this.vendor_id = vendor_id;
    }

    public long getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(long shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatest_status() {
        return latest_status;
    }

    public void setLatest_status(String latest_status) {
        this.latest_status = latest_status;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }



}
