package mon7.project.bookstore.admin.models.view;

import mon7.project.bookstore.admin.models.Admin;

public class AdminLoginView {
    private String id;
    private String fullName;
    private String DateOfBirth;
    private String email;
    private int gender;
    private String phone;
    private String avatarUrl;
    private String description;
    private String role;

    public AdminLoginView() {
    }

    public AdminLoginView(String id, String fullName, String dateOfBirth, String email, int gender, String phone, String avatarUrl, String description, String role) {
        this.id = id;
        this.fullName = fullName;
        this.DateOfBirth = dateOfBirth;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.description = description;
        this.role = role;
    }

    public AdminLoginView(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public AdminLoginView(Admin admin){
        this.id = admin.getId();
        this.fullName = admin.getFullName();
        this.DateOfBirth = admin.getDateOfBirth();
        this.email = admin.getEmail();
        this.gender = admin.getGender();
        this.phone = admin.getPhone();
        this.avatarUrl = admin.getAvatarUrl();
        this.description = admin.getDescription();
        this.role = admin.getAccount().getRole();
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

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
