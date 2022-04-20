package mon7.project.bookstore.customer.models.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Address")
public class Address {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String address;
    private int isDefault;
    private int isDeleted;

    public static final String NAME = "address";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerID")
    private Customer customer;

    public Address() {
    }

    public Address(String address, int isDefault) {
        this.address = address;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
