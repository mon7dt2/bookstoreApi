package mon7.project.bookstore.utils;

import mon7.project.bookstore.auth.models.User;
import mon7.project.bookstore.response_model.HeaderConstant;

public class UserDecodeUtils {
    public static User decodeFromAuthorizationHeader(String headerValue) {
        headerValue = headerValue.replace(HeaderConstant.AUTHORIZATION_VALUE_PREFIX, "");
        String decodedValue = Base64Utils.decode(headerValue);
        String values[] = decodedValue.split(":");
        return new User(values[0], values[1]);
    }
}
