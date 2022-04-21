package mon7.project.bookstore.product.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mon7.project.bookstore.category.model.Category;
import mon7.project.bookstore.provider.model.Provider;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
public class Books {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;

    private String displayName;
    private String description;
    private String avatarUrl;
    private Double price;
    private Long quantity;
    private Date createdAt;
    private Date updatedAt;
    private String author;
    private String publisher;
    private int isDeleted;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryID")
    private Category category;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "providerID")
    private Provider provider;

    public static final String CREATED_AT = "createdAt";
    public static final String NAME = "displayName";

    public Books() {
    }

    public Books(String displayName, String description, Double price, Long quantity, String author, String publisher) {
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.author = author;
        this.publisher = publisher;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
