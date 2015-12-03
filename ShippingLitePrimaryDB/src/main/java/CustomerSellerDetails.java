/**
 * Created by aman.gupta on 09/11/15.
 */

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "CustomerSellerDetails", indexes = {
        @Index(columnList = "id", name = "customer_seller_index"),
        @Index(columnList = "createdAt", name = "created_at_index")
})
public class CustomerSellerDetails {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "customerSellerPayload")
    private String customerSellerPayload;

    @OneToOne
    @JsonBackReference
    private Shipments shipments;

    @Column(name="createdAt", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @Column(name="updatedAt", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private String serviceRequestId;

    public String getServiceRequestId() {
        return serviceRequestId;
    }


    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }


    public Long getId() {
        return id;
    }


    public String getCustomerSellerPayload() {
        return customerSellerPayload;
    }


    public Shipments getShipments() {
        return shipments;
    }


    public Date getCreatedAt() {
        return createdAt;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setCustomerSellerPayload(String customerSellerPayload) {
        this.customerSellerPayload = customerSellerPayload;
    }


    public void setShipments(Shipments shipments) {
        this.shipments = shipments;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }




}