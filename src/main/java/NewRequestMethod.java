import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewRequestMethod implements RequestMethod {
    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {
        HttpRequest requestForNewAlbums = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServer + "/v1/browse/new-releases"))
                .GET()
                .build();

        HttpResponse<String> responseWithNewAlbums = HttpClient
                .newBuilder()
                .build()
                .send(requestForNewAlbums,
                        HttpResponse.BodyHandlers.ofString());

        String newAlbumsAsJson = responseWithNewAlbums.body();
        JsonObject joAlbums = JsonParser.parseString(newAlbumsAsJson)
                .getAsJsonObject()
                .getAsJsonObject("albums");

        for (JsonElement item : joAlbums.getAsJsonArray("items")) {

            String albumName = item.getAsJsonObject().get("name").getAsString();
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
