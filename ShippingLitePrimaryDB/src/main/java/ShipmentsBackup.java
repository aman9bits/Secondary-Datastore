import javax.persistence.*;

/**
 * Created by aman.gupta on 09/09/15.
 */
@Entity
@Table(name = "shipments")
@Access(value= AccessType.FIELD)
public class ShipmentsBackup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "delivery_type")
    private String delivery_type;

    @Column(name = "merchant_code")
    private String merchant_code;

    @Column(name = "source_type")
    private String source_type;

    @Column(name = "request_type")
    private String request_type;

    @Column(name = "vendor_id")
    private Long vendor_id;

    @Column(name = "vendor_tracking_id")
    private String vendor_tracking_id;

    @Column(name = "state")
    private String state;

    @Column(name = "sla")
    private Integer sla;

    @Column(name = "merchant_facility_id")
    private Long merchant_facility_id;

    @Column(name = "fkl_origin_facility_id")
    private Long fkl_origin_facility_id;

    @Column(name = "fkl_destination_facility_id")
    private Long fkl_destination_facility_id;

    @Column(name = "vendor_facility_id")
    private Long vendor_facility_id;

    @Column(name = "payment_type")
    private String payment_type;

    @Column(name = "estimated_ship_cost")
    private Float estimated_ship_cost;

    @Column(name = "shipment_value")
    private Float shipment_value;

    @Column(name = "tax_paid")
    private Float tax_paid;

    @Column(name = "customer_id")
    private Long customer_id;

    @Column(name = "merchant_reference_id")
    private String merchant_reference_id;

    @Column(name = "service_instructions")
    private String service_instructions;

    @Column(name = "special_instructions")
    private String special_instructions;

    @Column(name = "postal_code")
    private String postal_code;

    @Column(name = "flags")
    private Integer flags;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "updated_at")
    private String updated_at;

    @Column(name = "fkl_current_facility_id")
    private Long fkl_current_facility_id;

    @Column(name = "dispatched_date")
    private String dispatched_date;

    @Column(name = "vendor_receive_date")
    private String vendor_receive_date;

    @Column(name = "amount_to_collect")
    private Float amount_to_collect;

    @Column(name = "fkl_current_station_id")
    private Long fkl_current_station_id;

    public ShipmentsBackup(String type, String delivery_type, String merchant_code, String source_type, String request_type, Long vendor_id, String vendor_tracking_id, String state, Integer sla, Long merchant_facility_id, Long fkl_origin_facility_id, Long fkl_destination_facility_id, Long vendor_facility_id, String payment_type, Float estimated_ship_cost, Float shipment_value, Float tax_paid, Long customer_id, String merchant_reference_id, String service_instructions, String special_instructions, String postal_code, Integer flags, String created_at, String updated_at, Long fkl_current_facility_id, String dispatched_date, String vendor_receive_date, Float amount_to_collect, Long fkl_current_station_id) {
        this.type = type;
        this.delivery_type = delivery_type;
        this.merchant_code = merchant_code;
        this.source_type = source_type;
        this.request_type = request_type;
        this.vendor_id = vendor_id;
        this.vendor_tracking_id = vendor_tracking_id;
        this.state = state;
        this.sla = sla;
        this.merchant_facility_id = merchant_facility_id;
        this.fkl_origin_facility_id = fkl_origin_facility_id;
        this.fkl_destination_facility_id = fkl_destination_facility_id;
        this.vendor_facility_id = vendor_facility_id;
        this.payment_type = payment_type;
        this.estimated_ship_cost = estimated_ship_cost;
        this.shipment_value = shipment_value;
        this.tax_paid = tax_paid;
        this.customer_id = customer_id;
        this.merchant_reference_id = merchant_reference_id;
        this.service_instructions = service_instructions;
        this.special_instructions = special_instructions;
        this.postal_code = postal_code;
        this.flags = flags;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.fkl_current_facility_id = fkl_current_facility_id;
        this.dispatched_date = dispatched_date;
        this.vendor_receive_date = vendor_receive_date;
        this.amount_to_collect = amount_to_collect;
        this.fkl_current_station_id = fkl_current_station_id;
    }

    public ShipmentsBackup(Long id, String type, String delivery_type, String merchant_code, String source_type, String request_type, Long vendor_id, String vendor_tracking_id, String state, Integer sla, Long merchant_facility_id, Long fkl_origin_facility_id, Long fkl_destination_facility_id, Long vendor_facility_id, String payment_type, Float estimated_ship_cost, Float shipment_value, Float tax_paid, Long customer_id, String merchant_reference_id, String service_instructions, String special_instructions, String postal_code, Integer flags, String created_at, String updated_at, Long fkl_current_facility_id, String dispatched_date, String vendor_receive_date, Float amount_to_collect, Long fkl_current_station_id) {
        this.id = id;
        this.type = type;
        this.delivery_type = delivery_type;
        this.merchant_code = merchant_code;
        this.source_type = source_type;
        this.request_type = request_type;
        this.vendor_id = vendor_id;
        this.vendor_tracking_id = vendor_tracking_id;
        this.state = state;
        this.sla = sla;
        this.merchant_facility_id = merchant_facility_id;
        this.fkl_origin_facility_id = fkl_origin_facility_id;
        this.fkl_destination_facility_id = fkl_destination_facility_id;
        this.vendor_facility_id = vendor_facility_id;
        this.payment_type = payment_type;
        this.estimated_ship_cost = estimated_ship_cost;
        this.shipment_value = shipment_value;
        this.tax_paid = tax_paid;
        this.customer_id = customer_id;
        this.merchant_reference_id = merchant_reference_id;
        this.service_instructions = service_instructions;
        this.special_instructions = special_instructions;
        this.postal_code = postal_code;
        this.flags = flags;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.fkl_current_facility_id = fkl_current_facility_id;
        this.dispatched_date = dispatched_date;
        this.vendor_receive_date = vendor_receive_date;
        this.amount_to_collect = amount_to_collect;
        this.fkl_current_station_id = fkl_current_station_id;
    }

    public Integer getSla() {
        return sla;
    }

    public void setSla(Integer sla) {
        this.sla = sla;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public Long getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Long vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendor_tracking_id() {
        return vendor_tracking_id;
    }

    public void setVendor_tracking_id(String vendor_tracking_id) {
        this.vendor_tracking_id = vendor_tracking_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getMerchant_facility_id() {
        return merchant_facility_id;
    }

    public void setMerchant_facility_id(Long merchant_facility_id) {
        this.merchant_facility_id = merchant_facility_id;
    }

    public Long getFkl_origin_facility_id() {
        return fkl_origin_facility_id;
    }

    public void setFkl_origin_facility_id(Long fkl_origin_facility_id) {
        this.fkl_origin_facility_id = fkl_origin_facility_id;
    }

    public Long getFkl_destination_facility_id() {
        return fkl_destination_facility_id;
    }

    public void setFkl_destination_facility_id(Long fkl_destination_facility_id) {
        this.fkl_destination_facility_id = fkl_destination_facility_id;
    }

    public Long getVendor_facility_id() {
        return vendor_facility_id;
    }

    public void setVendor_facility_id(Long vendor_facility_id) {
        this.vendor_facility_id = vendor_facility_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public Float getEstimated_ship_cost() {
        return estimated_ship_cost;
    }

    public void setEstimated_ship_cost(Float estimated_ship_cost) {
        this.estimated_ship_cost = estimated_ship_cost;
    }

    public Float getShipment_value() {
        return shipment_value;
    }

    public void setShipment_value(Float shipment_value) {
        this.shipment_value = shipment_value;
    }

    public Float getTax_paid() {
        return tax_paid;
    }

    public void setTax_paid(Float tax_paid) {
        this.tax_paid = tax_paid;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public String getMerchant_reference_id() {
        return merchant_reference_id;
    }

    public void setMerchant_reference_id(String merchant_reference_id) {
        this.merchant_reference_id = merchant_reference_id;
    }

    public String getService_instructions() {
        return service_instructions;
    }

    public void setService_instructions(String service_instructions) {
        this.service_instructions = service_instructions;
    }

    public String getSpecial_instructions() {
        return special_instructions;
    }

    public void setSpecial_instructions(String special_instructions) {
        this.special_instructions = special_instructions;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Long getFkl_current_facility_id() {
        return fkl_current_facility_id;
    }

    public void setFkl_current_facility_id(Long fkl_current_facility_id) {
        this.fkl_current_facility_id = fkl_current_facility_id;
    }

    public String getDispatched_date() {
        return dispatched_date;
    }

    public void setDispatched_date(String dispatched_date) {
        this.dispatched_date = dispatched_date;
    }

    public String getVendor_receive_date() {
        return vendor_receive_date;
    }

    public void setVendor_receive_date(String vendor_receive_date) {
        this.vendor_receive_date = vendor_receive_date;
    }

    public Float getAmount_to_collect() {
        return amount_to_collect;
    }

    public void setAmount_to_collect(Float amount_to_collect) {
        this.amount_to_collect = amount_to_collect;
    }

    public Long getFkl_current_station_id() {
        return fkl_current_station_id;
    }

    public void setFkl_current_station_id(Long fkl_current_station_id) {
        this.fkl_current_station_id = fkl_current_station_id;
    }

    public ShipmentsBackup(){
        super();
    }
}
