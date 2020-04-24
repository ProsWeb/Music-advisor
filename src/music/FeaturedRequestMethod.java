package music;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

public class FeaturedRequestMethod implements RequestMethod {

    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {

        String uriPart = "featured-playlists";
        String userRequest = "playlists";

        JsonObject joPlaylists = UtilityClass
                .request(accessToken, apiServer, uriPart, userRequest);

        showPlaylists(joPlaylists);
    }

    private static void showPlaylists(JsonObject joPlaylists) {

        for (JsonElement item : joPlaylists.getAsJsonArray("items")) {

            String playListName = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            String playlistLink = item.getAsJsonObject()
                    .getAsJsonObject("external_urls")
                    .get("spotify")
                    .getAsString();

            System.out.println(playListName);
            System.out.println(playlistLink);
            System.out.println();
        }
    }
}
