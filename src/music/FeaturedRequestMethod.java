package music;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

class FeaturedRequestMethod implements RequestMethod {

    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {

        String uriPart = "featured-playlists";
        String userRequest = "playlists";

        JsonObject joPlaylists = Controller
                .request(accessToken, apiServer, uriPart, userRequest);

        List<String> playListsNames = Controller.collectNames(joPlaylists);
        List<String> playListsLinks = Controller.collectLinks(joPlaylists);

        print(playListsNames, playListsLinks);
    }

    private static void print(List<String> names, List<String> links) {

        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + "\n"
                    + links.get(i) + "\n");
        }
    }
}
