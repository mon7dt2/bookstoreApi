package mon7.project.bookstore.utils;

public class PasswordValidate {
    public static boolean isPasswordValidate(String password){
        return password.length() >= 8;
    }
}
