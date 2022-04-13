package mon7.project.bookstore.utils;

import mon7.project.bookstore.auth.models.Account;
import mon7.project.bookstore.response_model.HeaderConstant;

public class UserDecodeUtils {
    public static Account decodeFromAuthorizationHeader(String headerValue) {
        headerValue = headerValue.replace(HeaderConstant.AUTHORIZATION_VALUE_PREFIX, "");
        String decodedValue = Base64Utils.decode(headerValue);
        String values[] = decodedValue.split(":");
        return new Account(values[0], values[1]);
    }
}
