package mon7.project.bookstore.staff.models.data;

import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.staff.models.body.StaffDataBody;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String displayName;
    private String phone;
    private int gender;
    private String address;
    private String avatarUrl;
    private String dateOfBirth;
    private String email;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userID")
    private Account account;

    public Staff() {
    }

    public void update(StaffDataBody body){
        this.displayName = body.getDisplayName();
        this.phone = body.getPhone();
        this.gender = body.getGender();
        this.address = body.getAddress();
        this.avatarUrl = body.getAvatarUrl();
        this.dateOfBirth = body.getDateOfBirth();
        this.email = body.getEmail();
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
