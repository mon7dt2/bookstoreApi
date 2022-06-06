package mon7.project.bookstore.order.model.body;

import mon7.project.bookstore.order.model.data.OrderedProductItem;

import java.util.List;

public class CreateOrderBody {

    private String address;

    private List<OrderedProductItem> listProduct;

    public List<OrderedProductItem> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<OrderedProductItem> listProduct) {
        this.listProduct = listProduct;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
