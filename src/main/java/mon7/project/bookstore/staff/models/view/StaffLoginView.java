package mon7.project.bookstore.staff.models.view;

import mon7.project.bookstore.staff.models.data.Staff;

public class StaffLoginView {
    private String id;
    private String displayName;
    private String phone;
    private int gender;
    private String address;
    private String avatarUrl;
    private String dateOfBirth;
    private String email;
    private String accountID;

    public StaffLoginView(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public StaffLoginView() {
    }

    public StaffLoginView(String id, String displayName, String phone, int gender,
                          String address, String avatarUrl, String dateOfBirth, String email, String accountID) {
        this.id = id;
        this.displayName = displayName;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.accountID = accountID;
    }

    public StaffLoginView(Staff staff){
        this.id = staff.getId();
        this.displayName = staff.getDisplayName();
        this.phone = staff.getPhone();
        this.gender = staff.getGender();
        this.address = staff.getAddress();
        this.avatarUrl = staff.getAvatarUrl();
        this.dateOfBirth = staff.getDateOfBirth();
        this.email = staff.getEmail();
        this.accountID = staff.getAccount().getId();
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
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
