package music.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Model {

    public List<String> collectNames(final JsonObject jo) {

        List<String> names = new ArrayList<>();

        for (JsonElement item : jo.getAsJsonArray("items")) {

            String name = item.getAsJsonObject()
                    .get("name")
                    .getAsString();

            names.add(name);
        }

        return names;
    }

    public List<String> collectLinks(final JsonObject jo) {

        List<String> links = new ArrayList<>();

        for (JsonElement item : jo.getAsJsonArray("items")) {

            String link = item.getAsJsonObject()
                    .getAsJsonObject("external_urls")
                    .get("spotify")
                    .getAsString();

            links.add(link);
        }

        return links;
    }
}
