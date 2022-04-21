package mon7.project.bookstore.product.model.view;

import mon7.project.bookstore.product.model.data.Comment;

public class CommentView {
    private Long id;
    private String username;
    private String avatarUrl;
    private String comment;

    public CommentView(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.username = comment.getCustomer().getAccount().getUsername();
        this.avatarUrl = comment.getCustomer().getAvatarUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
