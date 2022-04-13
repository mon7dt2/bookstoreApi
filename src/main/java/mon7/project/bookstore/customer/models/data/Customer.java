package mon7.project.bookstore.customer.models.data;

import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.customer.models.body.ProfileBody;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {
    public static final String FULLNAME = "fullName";
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String fullName;
    private String phone;
    private int gender;
    private String avatarUrl;
    private String birthday;
    private String email;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountID")
    private Account account;

    public String getEmail() {
        return email;
    }

    public void update(ProfileBody body) {
        this.fullName = body.getFullName();
        this.phone = body.getPhone();
        this.avatarUrl = body.getAvatarUrl();
        this.gender = body.getGender();
        this.birthday = body.getBirthday();
        this.email = body.getEmail();
    }

    public Customer() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Account getUser() {
        return account;
    }

    public void setUser(Account account) {
        this.account = account;
    }
}
