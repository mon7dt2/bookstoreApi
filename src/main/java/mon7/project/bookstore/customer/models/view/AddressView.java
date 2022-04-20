package mon7.project.bookstore.customer.models.view;

public class AddressView {
    private String id;
    private String address;
    private int isDefault;

    public AddressView(String id, String address, int isDefault) {
        this.id = id;
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
}
