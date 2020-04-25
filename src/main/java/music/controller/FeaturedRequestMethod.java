package music.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import music.model.Model;
import music.view.View;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class FeaturedRequestMethod extends BaseController
        implements RequestMethod {

    @Override
    public void req(final String accessToken,
                    final String apiServer,
                    final String category)
            throws IOException, InterruptedException {

        String uriPart = "featured-playlists";
        String userRequest = "playlists";

        HttpResponse<String> response =
               request(accessToken, apiServer, uriPart);

        String json = response.body();

        JsonObject joPlaylists = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject(userRequest);

        Model model = new Model();

        List<String> playListsNames = model.collectNames(joPlaylists);
        List<String> playListsLinks = model.collectLinks(joPlaylists);

        new View().printNamesAndLinks(playListsNames, playListsLinks);
    }
}
