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

class PlaylistsRequestMethod implements RequestMethod {

    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String requestedCategory)
            throws IOException, InterruptedException {

        List<String> names = new ArrayList<>();
        List<String> links = new ArrayList<>();

        String uriPart = "categories";
        String userRequest = "categories";

        JsonObject joCategoriesForPlaylists = Controller
                .request(accessToken, apiServer, uriPart, userRequest);

        for (JsonElement item : joCategoriesForPlaylists
                                    .getAsJsonArray("items")) {

            String currentCategory = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            if (currentCategory.equals(requestedCategory)) {
                String categoryId = item.getAsJsonObject()
                        .get("id")
                        .getAsString();

                HttpResponse<String> responseWithPlaylistInCategory =
                        response(accessToken, apiServer, categoryId);

                String playlistsInCategoryAsJson =
                        responseWithPlaylistInCategory.body();

                if (failedRequest(playlistsInCategoryAsJson)) {
                    return;
                }

                JsonObject joPlaylistsInCategory = JsonParser
                        .parseString(playlistsInCategoryAsJson)
                        .getAsJsonObject()
                        .getAsJsonObject("playlists");

                names = Controller.collectNames(joPlaylistsInCategory);
                links = Controller.collectLinks(joPlaylistsInCategory);
            }
        }

        checkCategory(names, links);
    }

    private static HttpResponse<String> response(final String accessToken,
                                                 final String apiServer,
                                                 final String categoryId)
            throws IOException, InterruptedException {

        HttpRequest requestForPlayListInCategory = HttpRequest
                .newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServer + "/v1/browse/categories/"
                        + categoryId + "/playlists"))
                .GET()
                .build();

        return HttpClient
                .newBuilder()
                .build()
                .send(requestForPlayListInCategory,
                        HttpResponse.BodyHandlers.ofString());
    }

    private boolean failedRequest(String playlistsInCategoryAsJson) {

        if (playlistsInCategoryAsJson.contains("error")) {

            JsonObject error = JsonParser
                    .parseString(playlistsInCategoryAsJson)
                    .getAsJsonObject()
                    .getAsJsonObject("error");
            System.out.println(error.get("message"));

            return true;
        }

        return false;
    }

    private static void checkCategory(List<String> names, List<String> links) {

        if (names.isEmpty()) {
            System.out.println("Unknown category name.");
        } else {
            print(names, links);
        }
    }

    static void print(List<String> names, List<String> links) {

        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + "\n"
                    + links.get(i) + "\n");
        }
    }
}
