package mon7.project.bookstore.customer.models.view;

import mon7.project.bookstore.customer.models.data.Customer;

public class CustomerView {
    private String id;
    private String fullName;
    private String phone;
    private int gender;
    private String address;
    private String avatarUrl;
    private String birthday;
    private String email;

    public CustomerView() {
    }

    public CustomerView(Customer customer) {
        this.id = customer.getId();
        this.fullName = customer.getFullName();
        this.phone = customer.getPhone();
        this.gender = customer.getGender();
        this.address = customer.getAddress();
        this.avatarUrl = customer.getAvatarUrl();
        this.birthday = customer.getBirthday();
        this.email = customer.getEmail();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
