package mon7.project.bookstore.provider.model;

import mon7.project.bookstore.provider.model.body.ProviderBody;

import javax.persistence.*;

@Entity
@Table(name = "provider")
public class Provider {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String displayName;
    private String description;
    private String address;
    private String phone;
    private String email;
    private int isDeleted;

    public static final String NAME = "displayName";

    public Provider() {
        this.isDeleted = 0;
    }

    public Provider(Long id, String displayName, String description, String address, String phone, String email) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Provider(String displayName, String description, String address, String phone, String email) {
        this.displayName = displayName;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Provider(ProviderBody body){
        this.displayName = body.getDisplayName();
        this.description = body.getDescription();
        this.address = body.getAddress();
        this.phone = body.getPhone();
        this.email = body.getEmail();
        this.isDeleted = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
