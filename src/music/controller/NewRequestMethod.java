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

public class NewRequestMethod extends BaseController implements RequestMethod {
    @Override
    public void req(final String accessToken,
                    final String apiServer,
                    final String category)
            throws IOException, InterruptedException {

        String uriPart = "new-releases";
        String userRequest = "albums";

        HttpResponse<String> response =
                request(accessToken, apiServer, uriPart);

        String json = response.body();

        JsonObject joAlbums = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject(userRequest);

        Model model = new Model();

        List<String> names = model.collectNames(joAlbums);
        List<String> artists = collectArtists(joAlbums);
        List<String> links = model.collectLinks(joAlbums);

        new View().printNamesArtistsLinks(names, artists, links);
    }

    private static List<String> collectArtists(final JsonObject jo) {

        List<String> artists = new ArrayList<>();

        for (JsonElement item : jo.getAsJsonArray("items")) {
            for (JsonElement artist : item.getAsJsonObject()
                    .getAsJsonArray("artists")) {
                artists.add(artist.getAsJsonObject()
                        .get("name").getAsString());
            }
        }

        return artists;
    }
}
