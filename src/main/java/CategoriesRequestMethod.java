import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CategoriesRequestMethod implements RequestMethod {

    @Override
    public void request(final String accessToken,
                        final String apiServer,
                        final String category)
            throws IOException, InterruptedException {
        JsonObject joCategories = requestForCategories(accessToken, apiServer);

        for (JsonElement item : joCategories.getAsJsonArray("items")) {

            String categoryName = item.getAsJsonObject()
                    .get("name")
                    .getAsString();
            System.out.println(categoryName);
        }
    }

    private static JsonObject requestForCategories(final String accessToken,
                                                   final String apiServer)
            throws IOException, InterruptedException {
        HttpRequest requestForCategories = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiServer + "/v1/browse/categories"))
                .GET()
                .build();

        HttpResponse<String> responseWithCategories = HttpClient
                .newBuilder()
                .build()
                .send(requestForCategories,
                        HttpResponse.BodyHandlers.ofString());

        String categoriesAsJson = responseWithCategories.body();

        return JsonParser.parseString(categoriesAsJson)
                .getAsJsonObject()
                .getAsJsonObject("categories");
    }
}
