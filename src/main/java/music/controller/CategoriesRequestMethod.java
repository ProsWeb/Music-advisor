package music.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import music.model.Model;
import music.view.View;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class CategoriesRequestMethod extends BaseController
        implements RequestMethod {

    @Override
    public void req(final String accessToken,
                    final String apiServer,
                    final String category)
            throws IOException, InterruptedException {

        String uriPart = "categories";
        String userRequest = "categories";

        HttpResponse<String> response =
                request(accessToken, apiServer, uriPart);

        String json = response.body();

        JsonObject joCategories = JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject(userRequest);

        List<String> categories = new Model().collectNames(joCategories);
        new View().printNames(categories);
    }
}
