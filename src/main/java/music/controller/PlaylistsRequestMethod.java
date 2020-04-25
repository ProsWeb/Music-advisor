package music.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import music.model.Model;
import music.view.View;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class PlaylistsRequestMethod extends BaseController implements RequestMethod {

    @Override
    public void req(final String accessToken,
                    final String apiServer,
                    final String requestedCategory)
            throws IOException, InterruptedException {

        List<String> names = new ArrayList<>();
        List<String> links = new ArrayList<>();

        String uriPart = "categories";
        String userRequest = "categories";

        HttpResponse<String> response = request(accessToken, apiServer, uriPart);

        String json = response.body();

        JsonObject joCategories = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject(userRequest);

        for (JsonElement item : joCategories
                                    .getAsJsonArray("items")) {

            String currentCategory = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            if (currentCategory.equals(requestedCategory)) {
                String categoryId = item.getAsJsonObject()
                        .get("id")
                        .getAsString();

                String uriPartForCategories = "categories/" + categoryId + "/playlists";

                HttpResponse<String> responseWithPlaylistInCategory =
                        request(accessToken, apiServer, uriPartForCategories);

                String playlistsInCategoryAsJson =
                        responseWithPlaylistInCategory.body();

                if (failedRequest(playlistsInCategoryAsJson)) {
                    return;
                }

                JsonObject joPlaylistsInCategory = JsonParser
                        .parseString(playlistsInCategoryAsJson)
                        .getAsJsonObject()
                        .getAsJsonObject("playlists");

                Model model = new Model();

                names = model.collectNames(joPlaylistsInCategory);
                links = model.collectLinks(joPlaylistsInCategory);
            }
        }

        checkCategory(names, links);
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
            new View().printNamesAndLinks(names, links);
        }
    }
}
