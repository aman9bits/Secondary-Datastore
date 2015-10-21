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

    public ShippingLiteShipments(double amountToCollect, String createdAt, String merchatReferenceId, String updatedAt) {
        this.amountToCollect = amountToCollect;
        this.createdAt = createdAt;
        this.merchatReferenceId = merchatReferenceId;
        this.updatedAt = updatedAt;
    }
    public ShippingLiteShipments(){
        super();
    }
}
