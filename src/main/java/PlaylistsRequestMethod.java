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
                        final String category)
            throws IOException, InterruptedException {
        Map<String, String> playLists = new HashMap<>();

        JsonObject joCategoriesForPlaylists =
                requestForCategories(accessToken, apiServer);
        for (JsonElement item : joCategoriesForPlaylists
                                    .getAsJsonArray("items")) {

            String categoryName = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            if (categoryName.equals(category)) {
                String categoryId = item.getAsJsonObject()
                        .get("id")
                        .getAsString();

                HttpRequest requestForPlayListInCategory = HttpRequest
                        .newBuilder()
                        .header("Authorization", "Bearer " + accessToken)
                        .uri(URI.create(apiServer + "/v1/browse/categories/"
                                        + categoryId + "/playlists"))
                        .GET()
                        .build();
                HttpResponse<String> responseWithPlaylistInCategory = HttpClient
                        .newBuilder()
                        .build()
                        .send(requestForPlayListInCategory,
                                HttpResponse.BodyHandlers.ofString());

                String playlistsInCategoryAsJson =
                        responseWithPlaylistInCategory.body();

                if (playlistsInCategoryAsJson.contains("error")) {
                    JsonObject error = JsonParser
                            .parseString(playlistsInCategoryAsJson)
                            .getAsJsonObject()
                            .getAsJsonObject("error");
                    System.out.println(error.get("message"));
                    return;
                }

                JsonObject joPlaylistsInCategory = JsonParser
                        .parseString(playlistsInCategoryAsJson)
                        .getAsJsonObject()
                        .getAsJsonObject("playlists");

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
        }
        if (playLists.isEmpty()) {
            System.out.println("Unknown category name.");
        } else {
            playLists.forEach((name, link) ->
                    System.out.println(name + "\n" + link + "\n"));
        }
    }

    private static JsonObject requestForCategories(final String accessToken,
                                                   final String apiServer)
            throws IOException, InterruptedException {
        HttpRequest requestForCategories = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServer + "/v1/browse/categories"))
                .GET()
                .build();

        HttpResponse<String> responseWithCategories = HttpClient
                .newBuilder()
                .build()
                .send(requestForCategories,
                        HttpResponse.BodyHandlers.ofString());

        String categoriesAsJson = responseWithCategories.body();

        return JsonParser.parseString(categoriesAsJson)
                .getAsJsonObject()
                .getAsJsonObject("categories");
    }
}
