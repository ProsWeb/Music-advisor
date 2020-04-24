package music;

public final class Util {

    private Util() {
        throw new IllegalStateException("Util class");
    }

    public static final String AUTH_SERVER = "https://accounts.spotify.com";
    public static final String API_SERVER = "https://api.spotify.com";

    public static final String CLIENT_ID = "da072c60fcee469e8b0f4140aa4480d5";
    public static final String CLIENT_SECRET = "8ada13093c704487b57c3a660448884e";
    public static final String AUTHORIZE_PART = "/authorize";
    public static final String RESPONSE_TYPE = "code";
    public static final String TOKEN_PART = "/api/token";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String REDIRECT_URI = "http://localhost:8080";

    public static final String ANSWER_DENIED_ACCESS =
            "Please, provide access for application.";
}
