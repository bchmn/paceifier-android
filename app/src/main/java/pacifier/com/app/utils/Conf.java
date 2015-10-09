package pacifier.com.app.utils;

public abstract class Conf {
    public final static String API_BASE_URL = "https://bachman.localtunnel.me";
    public final static int BRAINTREE_REQUEST_CODE = 100;
    public final static String EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+";

    public abstract class Prefs {
        public final static String BRAINTREE_TOKEN = "braintree_token";
    }
}
