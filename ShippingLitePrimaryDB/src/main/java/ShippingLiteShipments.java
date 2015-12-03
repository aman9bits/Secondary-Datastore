import javax.persistence.*;

/**
 * Created by aman.gupta on 09/09/15.
 */
@Entity
@Table(name = "Shipments")
@Access(value= AccessType.FIELD)
public class ShippingLiteShipments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name="amountToCollect")
    private double amountToCollect;

    @Column(name="createdAt")
    private String createdAt;

    @Column(name="merchatReferenceId")
    private String merchatReferenceId;

    @Column(name="updatedAt")
    private String updatedAt;

    @Column(name="deliveryType")
    private String deliveryType;

    @Column(name="flag")
    private Integer flag;

    @Column(name="merchantCode")
    private String merchantCode;

    @Column(name="orderId")
    private String orderId;

    @Column(name="paymentType")
    private String paymentType;

    @Column(name="postalCode")
    private String postalCode;

    @Column(name="requestType")
    private String requestType;

    @Column(name="serviceRequestId")
    private String serviceRequestId;

    @Column(name="shipmentValue")
    private double shipmentValue;

    @Column(name="sourceType")
    private String sourceType;

    @Column(name="state")
    private String state;

    @Column(name="type")
    private String type;

    @Column(name="vendorId")
    private long vendorId;

    @Column(name="vendorTrackingId")
    private String vendorTrackingId;

    @Column(name="merchantName")
    private String merchantName;

    @Column(name="vendorName")
    private String vendorName;

    @Column(name="vendorCode")
    private String vendorCode;

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public double getShipmentValue() {
        return shipmentValue;
    }

    public void setShipmentValue(double shipmentValue) {
        this.shipmentValue = shipmentValue;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorTrackingId() {
        return vendorTrackingId;
    }

    public void setVendorTrackingId(String vendorTrackingId) {
        this.vendorTrackingId = vendorTrackingId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmountToCollect() {
        return amountToCollect;
    }

    public void setAmountToCollect(double amountToCollect) {
        this.amountToCollect = amountToCollect;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMerchatReferenceId() {
        return merchatReferenceId;
    }

    public void setMerchatReferenceId(String merchatReferenceId) {
        this.merchatReferenceId = merchatReferenceId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ShippingLiteShipments(double amountToCollect, String createdAt, String merchatReferenceId, String updatedAt, String deliveryType, Integer flag, String merchantCode, String orderId, String paymentType, String postalCode, String requestType, String serviceRequestId, double shipmentValue, String sourceType, String state, String type, long vendorId, String vendorTrackingId, String merchantName, String vendorName, String vendorCode) {
        this.amountToCollect = amountToCollect;
        this.createdAt = createdAt;
        this.merchatReferenceId = merchatReferenceId;
        this.updatedAt = updatedAt;
        this.deliveryType = deliveryType;
        this.flag = flag;
        this.merchantCode = merchantCode;
        this.orderId = orderId;
        this.paymentType = paymentType;
        this.postalCode = postalCode;
        this.requestType = requestType;
        this.serviceRequestId = serviceRequestId;
        this.shipmentValue = shipmentValue;
        this.sourceType = sourceType;
        this.state = state;
        this.type = type;
        this.vendorId = vendorId;
        this.vendorTrackingId = vendorTrackingId;
        this.merchantName = merchantName;
        this.vendorName = vendorName;
        this.vendorCode = vendorCode;
    }

    public ShippingLiteShipments(){
        super();
    }
}
