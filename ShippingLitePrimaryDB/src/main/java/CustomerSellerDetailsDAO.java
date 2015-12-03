import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by aman.gupta on 09/11/15.
 */
public class CustomerSellerDetailsDAO extends AbstractDAO<CustomerSellerDetails> {
    public CustomerSellerDetailsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
