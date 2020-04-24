package music;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewRequestMethod implements RequestMethod {
    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {

        String uriPart = "new-releases";
        String userRequest = "albums";

        JsonObject joAlbums = Controller
                .request(accessToken, apiServer, uriPart, userRequest);

        showNewAlbums(joAlbums);
    }

    private static void showNewAlbums(JsonObject joAlbums) {

        for (JsonElement item : joAlbums.getAsJsonArray("items")) {

            String albumName = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            System.out.println(albumName);

            List<String> artistsOfOneAlbum = new ArrayList<>();
            for (JsonElement artist : item.getAsJsonObject()
                    .getAsJsonArray("artists")) {
                artistsOfOneAlbum.add(artist.getAsJsonObject()
                        .get("name").getAsString());
            }
            System.out.println(Arrays.toString(artistsOfOneAlbum.toArray()));

            String albumLink = item.getAsJsonObject()
                    .getAsJsonObject("external_urls")
                    .get("spotify")
                    .getAsString();
            System.out.println(albumLink);
            System.out.println();
        }
    }
}
