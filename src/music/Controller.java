package music;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

class Controller {

    private Controller() {
        throw new IllegalStateException("Controller class");
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

    static List<String> collectNames(JsonObject jo) {

        List<String> names = new ArrayList<>();

        for (JsonElement item : jo.getAsJsonArray("items")) {

            String name = item.getAsJsonObject()
                    .get("name")
                    .getAsString();

            names.add(name);
        }

        return names;
    }

    static List<String> collectLinks(JsonObject jo) {

        List<String> links = new ArrayList<>();

        for (JsonElement item : jo.getAsJsonArray("items")) {

            String link = item.getAsJsonObject()
                    .getAsJsonObject("external_urls")
                    .get("spotify")
                    .getAsString();

            links.add(link);
        }

        return links;
    }
}
