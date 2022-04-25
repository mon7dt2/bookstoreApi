package mon7.project.bookstore.order.model.data;

public class OrderedProductItem {
    private String productID;
    private Integer qty;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
