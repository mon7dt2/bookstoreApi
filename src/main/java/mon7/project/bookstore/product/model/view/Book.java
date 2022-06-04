package mon7.project.bookstore.product.model.view;

import mon7.project.bookstore.product.model.data.Books;

import java.util.Date;

public class Book {

    private String id;
    private String displayName;
    private String description;
    private String avatarUrl;
    private Double price;
    private Float vote;
    private Long quantity;
    private Date createdAt;
    private Date updatedAt;
    private String author;
    private String publisher;
    private Integer isDeleted;

    public Book(String id, String displayName, String description, String avatarUrl,
                Double price, Float vote, Long quantity, Date createdAt, Date updatedAt,
                String author, String publisher, Integer isDeleted) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.avatarUrl = avatarUrl;
        this.price = price;
        this.vote = vote;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.author = author;
        this.publisher = publisher;
        this.isDeleted = isDeleted;
    }

    public Book() {
    }

    public Book(Books book){
        this.id = book.getId();
        this.displayName = book.getDisplayName();
        this.description = book.getDescription();
        this.avatarUrl = book.getAvatarUrl();
        this.price = book.getPrice();
        this.vote = book.getVote();
        this.quantity = book.getQuantity();
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.isDeleted = book.getIsDeleted();
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

    public Float getVote() {
        return vote;
    }

    public void setVote(Float vote) {
        this.vote = vote;
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

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
