import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FeaturedRequestMethod implements RequestMethod {

    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {


        HttpRequest requestForPlaylists = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServer + "/v1/browse/featured-playlists"))
                .GET()
                .build();

        HttpResponse<String> responseWithPlaylists = HttpClient
                .newBuilder()
                .build()
                .send(requestForPlaylists,
                        HttpResponse.BodyHandlers.ofString());

        String playlistsAsJson = responseWithPlaylists.body();
        JsonObject joPlaylists = JsonParser.parseString(playlistsAsJson)
                .getAsJsonObject()
                .getAsJsonObject("playlists");

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
