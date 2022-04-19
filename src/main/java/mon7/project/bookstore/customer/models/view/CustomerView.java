package mon7.project.bookstore.customer.models.view;

import mon7.project.bookstore.customer.models.data.Customer;

public class CustomerView {
    private String id;
    private String fullName;
    private String phone;
    private int gender;
    private String avatarUrl;
    private String dateOfBirth;
    private String email;

    public CustomerView() {
    }

    public CustomerView(Customer customer) {
        this.id = customer.getId();
        this.fullName = customer.getFullName();
        this.phone = customer.getPhone();
        this.gender = customer.getGender();
        this.avatarUrl = customer.getAvatarUrl();
        this.dateOfBirth = customer.getDateOfBirth();
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
