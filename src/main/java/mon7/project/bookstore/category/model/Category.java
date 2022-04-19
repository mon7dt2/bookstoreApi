package mon7.project.bookstore.category.model;

import mon7.project.bookstore.category.model.body.CategoryBody;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "category")
public class Category {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String displayName;
    private String coverUrl;
    private Date createdAt;
    private Date updatedAt;
    private int isDeleted;

    //constance
    public static final String NAME = "displayName";

    public Category() {
    }

    public Category(Long id, String displayName, String coverUrl, Date createdAt, Date updatedAt) {
        this.id = id;
        this.displayName = displayName;
        this.coverUrl = coverUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Category(CategoryBody body){
        this.displayName = body.getDisplayName();
        this.coverUrl = body.getCoverUrl();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Category(String displayName) {
        this.displayName = displayName;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public void update(CategoryBody body){
        this.displayName = body.getDisplayName();
        this.coverUrl = body.getCoverUrl();
        this.updatedAt = new Date();
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
