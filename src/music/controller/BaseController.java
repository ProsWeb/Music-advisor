package music.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class BaseController {

    HttpResponse<String> request(final String accessToken,
                              final String apiServer,
                              final String uriPart)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServer + "/v1/browse/" + uriPart))
                .GET()
                .build();

        return HttpClient
                .newBuilder()
                .build()
                .send(request,
                        HttpResponse.BodyHandlers.ofString());
    }
}
