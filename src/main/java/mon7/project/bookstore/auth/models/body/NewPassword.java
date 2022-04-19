package mon7.project.bookstore.auth.models.body;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class    NewPassword {
    @NotEmpty
    @NotNull
    private String oldPassword;
    @NotEmpty
    @NotNull
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
