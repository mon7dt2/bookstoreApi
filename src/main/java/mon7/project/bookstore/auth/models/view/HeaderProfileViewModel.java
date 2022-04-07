package mon7.project.bookstore.auth.models.view;

public class HeaderProfileViewModel {
    private String customerID;
    private String fullName;
    private String avatarUrl;
    private String email;
    private String userFacebookID;

    public HeaderProfileViewModel(String customerID, String fullName, String avatarUrl, String email, String userFacebookID) {
        this.customerID = customerID;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.userFacebookID = userFacebookID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserFacebookID() {
        return userFacebookID;
    }

    public void setUserFacebookID(String userFacebookID) {
        this.userFacebookID = userFacebookID;
    }
}
