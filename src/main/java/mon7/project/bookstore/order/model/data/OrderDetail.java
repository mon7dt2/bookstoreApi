package mon7.project.bookstore.order.model.data;

import mon7.project.bookstore.product.model.data.Books;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "orderdetail")
public class OrderDetail {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderID")
    private Order order;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookID")
    private Books book;

    private Integer quantity;
    private Double total;

    public OrderDetail() {
    }

    public OrderDetail(Books book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
