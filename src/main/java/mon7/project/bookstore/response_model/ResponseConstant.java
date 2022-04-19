package mon7.project.bookstore.response_model;

public class ResponseConstant {
    public static final String VI = "vi";
    public static final String EN = "en";

    public static final String MSG_OK = "Ok";

    public static class Vi {
        public static final String WRONG_EMAIL_OR_PASSWORD = "Sai Email hoặc mật khẩu";
        public static final String OLD_PASSWORD_MISMATCH = "Mật khẩu cũ không khớp";


    }

    public static class ErrorMessage {
        public static final String NOT_FOUND = "not found";
        public static final String ACCOUNT_NOT_FOUND = "Tài khoản không tồn tại";
        public static final String CATEGORY_NOT_FOUND = "Danh mục không tồn tại";
        public static final String RESOURCE_EXIST = "Tài khoản đã tồn tại";
        public static final String RESOURCE_CATEGORY_EXIST = "Danh mục đã tồn tại";
        public static final String INVALID_EMAIL = "invalid email";
        public static final String ACCOUNT_NOT_VERIFIED = "Tài khoản chưa được xác nhận";
        public static final String ACCOUNT_VERIFED = "account has been verified";
        public static final String INTERNAL_SERVER_ERROR = "internal server error";
        public static final String PASSWORD_TOO_SHORT = "Mật khẩu phải từ 8 kí tự trở lên";
        public static final String ACCOUNT_FORBIDDEN_ROLE = "Không có quyền truy cập tới chức năng này!";
    }
}