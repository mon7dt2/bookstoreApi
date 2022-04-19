package mon7.project.bookstore.customer.models.view;

import mon7.project.bookstore.customer.models.data.Customer;

public class Profile {
    private String id;
    private String fullName;
    private String phone;
    private String identityCard;
    private String description;
    private String avatarUrl;
    private int gender;
    private String dateOfBirth;
    private String email;

    public Profile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Profile(Customer customer) {
        this.id = customer.getId();
        this.fullName = customer.getFullName();
        this.phone = customer.getPhone();
        this.avatarUrl = customer.getAvatarUrl();
        this.gender = customer.getGender();
        this.dateOfBirth = customer.getDateOfBirth();
        this.email = customer.getEmail();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
