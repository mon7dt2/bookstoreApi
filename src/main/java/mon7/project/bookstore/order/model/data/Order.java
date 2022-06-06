package mon7.project.bookstore.order.model.data;

import mon7.project.bookstore.customer.models.data.Customer;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    private String searchKey;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerID")
    private Customer customer;

    private double totalPrice;
    private Date createdAt;
    private Date updatedAt;
    private String address;

    /*
     * status la cac so nguyen tuong ung voi cac giai doan giao hang
     * 0 la dang chuan bi
     * 1 la dang giao
     * 2 la da giao
     * -1 la da huy
     */
    private int orderStatus;

    public Order() {
    }

    public Order(String id, String searchKey, Customer customer, double totalPrice, Date createdAt, Date updatedAt, int orderStatus, String address) {
        this.id = id;
        this.searchKey = searchKey;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public Order(String searchKey, Customer customer, double totalPrice) {
        this.searchKey = searchKey;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Order(Customer customer) {
        this.customer = customer;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
