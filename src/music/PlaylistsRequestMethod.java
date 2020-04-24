package music;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class PlaylistsRequestMethod implements RequestMethod {

    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String requestedCategory)
            throws IOException, InterruptedException {

        Map<String, String> playLists = new HashMap<>();

        String uriPart = "categories";
        String userRequest = "categories";

        JsonObject joCategoriesForPlaylists = UtilityClass
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

                fillPlaylists(joPlaylistsInCategory, playLists);
            }
        }

        checkCategory(playLists);
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

    private static void fillPlaylists(JsonObject joPlaylistsInCategory, Map<String, String> playLists) {
        for (JsonElement pl : joPlaylistsInCategory
                .getAsJsonArray("items")) {
            String playListName = pl.getAsJsonObject()
                    .get("name")
                    .getAsString();

            String playlistLink = pl.getAsJsonObject()
                    .getAsJsonObject("external_urls")
                    .get("spotify")
                    .getAsString();

            playLists.put(playListName, playlistLink);
        }
    }

    private static void checkCategory(Map<String, String> playLists) {

        if (playLists.isEmpty()) {
            System.out.println("Unknown category name.");
        } else {
            playLists.forEach((name, link) ->
                    System.out.println(name + "\n" + link + "\n"));
        }
    }
}
