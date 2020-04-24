package music;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class AuthSetMethod {

    private String code = "";

    void launchServer()
            throws IOException, InterruptedException {

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);

        server.start();
        View.showAuthLink();

        server.createContext("/",
                exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String result;

                    if (query != null && query.contains("code")) {
                        code = query.substring(5);
                        result = "Got the code. Return back to your program.";
                    } else {
                        result = "Not found authorization code. Try again.";
                    }

                    exchange.sendResponseHeaders(200, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();

                    System.out.println(result);
                }
        );
        while (code.equals("")) {
            Thread.sleep(10);
        }

        server.stop(10);
    }

    String getAccessToken()
            throws IOException, InterruptedException {

        System.out.println("Making http request for access_token...");

        HttpRequest requestForAccessToken = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + Util.CLIENT_ID
                                + "&client_secret=" + Util.CLIENT_SECRET
                                + "&grant_type=" + Util.GRANT_TYPE
                                + "&code=" + code
                                + "&redirect_uri=" + Util.REDIRECT_URI))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Util.AUTH_SERVER + Util.TOKEN_PART))
                .build();

        HttpResponse<String> responseWithAccessToken = HttpClient
                .newBuilder()
                .build()
                .send(requestForAccessToken,
                        HttpResponse.BodyHandlers.ofString());

        String fullToken = responseWithAccessToken.body();

        return parseAccessToken(fullToken);
    }

    private static String parseAccessToken(final String bearerToken) {

        JsonObject jo = JsonParser.parseString(bearerToken).getAsJsonObject();

        return jo.get("access_token").getAsString();
    }
}
