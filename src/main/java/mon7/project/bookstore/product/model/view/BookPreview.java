package mon7.project.bookstore.product.model.view;

public class BookPreview {
    private String id;
    private String displayName;
    private String avatarUrl;
    private Double price;
    private Long quantity;

    public BookPreview() {
    }

    public BookPreview(String id, String displayName, String avatarUrl, double price, long quantity) {
        this.id = id;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.price = price;
        this.quantity = quantity;
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
}
