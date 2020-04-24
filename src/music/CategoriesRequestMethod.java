package music;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

public class CategoriesRequestMethod implements RequestMethod {

    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {

        String uriPart = "categories";
        String userRequest = "categories";

        JsonObject joCategories = UtilityClass
                .request(accessToken, apiServer, uriPart, userRequest);

        showCategories(joCategories);
    }

    private static void showCategories(JsonObject joCategories) {
        for (JsonElement item : joCategories.getAsJsonArray("items")) {

            String categoryName = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            System.out.println(categoryName);
        }
    }
}
