package music;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UtilityClass {

    static final String CLIENT_ID = "da072c60fcee469e8b0f4140aa4480d5";
    static final String CLIENT_SECRET = "8ada13093c704487b57c3a660448884e";
    static final String AUTHORIZE_PART = "/authorize";
    static final String RESPONSE_TYPE = "code";
    static final String TOKEN_PART = "/api/token";
    static final String GRANT_TYPE = "authorization_code";
    static final String REDIRECT_URI = "http://localhost:8080";

    private UtilityClass() {
        throw new IllegalStateException("Utility class");
    }

    static JsonObject request(final String accessToken,
                              final String apiServer,
                              final String uriPart,
                              final String userRequest)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServer + "/v1/browse/" + uriPart))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient
                .newBuilder()
                .build()
                .send(request,
                        HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        return JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject(userRequest);
    }
}
