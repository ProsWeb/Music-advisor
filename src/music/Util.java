package music;

class Util {

    private Util() {
        throw new IllegalStateException("Util class");
    }

    static final String AUTH_SERVER = "https://accounts.spotify.com";
    static final String API_SERVER = "https://api.spotify.com";

    static final String CLIENT_ID = "da072c60fcee469e8b0f4140aa4480d5";
    static final String CLIENT_SECRET = "8ada13093c704487b57c3a660448884e";
    static final String AUTHORIZE_PART = "/authorize";
    static final String RESPONSE_TYPE = "code";
    static final String TOKEN_PART = "/api/token";
    static final String GRANT_TYPE = "authorization_code";
    static final String REDIRECT_URI = "http://localhost:8080";

    static final String ANSWER_DENIED_ACCESS =
            "Please, provide access for application.";
}
