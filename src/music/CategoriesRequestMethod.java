package music;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

class CategoriesRequestMethod implements RequestMethod {

    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {

        String uriPart = "categories";
        String userRequest = "categories";

        JsonObject joCategories = Controller
                .request(accessToken, apiServer, uriPart, userRequest);

        List<String> categories = Controller.collectNames(joCategories);
        print(categories);
    }

    private static void print(List<String> names) {

        for (String name : names) {
            System.out.println(name);
        }
    }
}
