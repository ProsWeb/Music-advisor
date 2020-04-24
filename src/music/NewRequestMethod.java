package music;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class NewRequestMethod implements RequestMethod {
    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {

        String uriPart = "new-releases";
        String userRequest = "albums";

        JsonObject joAlbums = Controller
                .request(accessToken, apiServer, uriPart, userRequest);

        List<String> names = Controller.collectNames(joAlbums);
        List<String> artists = collectArtists(joAlbums);
        List<String> links = Controller.collectLinks(joAlbums);

        print(names, artists, links);
    }

    private static List<String> collectArtists(JsonObject jo) {

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

    private static void print(List<String> names, List<String> artists, List<String> links) {

        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + "\n"
                    + artists.get(i) + "\n"
                    + links.get(i) + "\n");
        }
    }
}
