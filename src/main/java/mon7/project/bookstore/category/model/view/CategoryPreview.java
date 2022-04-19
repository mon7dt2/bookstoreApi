package mon7.project.bookstore.category.model.view;

import mon7.project.bookstore.category.model.Category;

public class CategoryPreview {
    private Long id;
    private String displayName;
    private String avatarUrl;

    public CategoryPreview() {
    }

    public CategoryPreview(Long id, String displayName, String avatarUrl) {
        this.id = id;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }

    public CategoryPreview(Category category){
        this.id = category.getId();
        this.displayName = category.getDisplayName();
        this.avatarUrl = category.getCoverUrl();
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
