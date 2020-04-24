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
import java.util.HashMap;
import java.util.Map;

public class AuthSetMethod {

    private String code = "";

    Map<String, String> setServers() {

        Map<String, String> servers = new HashMap<>();

        servers.put("authServer", "https://accounts.spotify.com");
        servers.put("apiServer", "https://api.spotify.com");

        return servers;
    }

    void launchServer(final String accessServer)
            throws IOException, InterruptedException {

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);

        server.start();

        System.out.println("use this link to request the access code:");
        System.out.println(accessServer + Controller.AUTHORIZE_PART
                + "?client_id=" + Controller.CLIENT_ID
                + "&redirect_uri=" + Controller.REDIRECT_URI
                + "&response_type=" + Controller.RESPONSE_TYPE);
        System.out.println("waiting for code...");

        server.createContext("/",
                exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String result;
                    String answer;

                    if (query != null && query.contains("code")) {
                        code = query.substring(5);
                        result = "Got the code. Return back to your program.";
                        answer = "code received";
                    } else {
                        result = "Not found authorization code. Try again.";
                        answer = "code not received";
                    }

                    exchange.sendResponseHeaders(200, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();

                    System.out.println(answer);
                }
        );
        while (code.equals("")) {
            Thread.sleep(10);
        }

        server.stop(10);
    }

    String getAccessToken(final String accessServer)
            throws IOException, InterruptedException {

        System.out.println("Making http request for access_token...");

        HttpRequest requestForAccessToken = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + Controller.CLIENT_ID
                                + "&client_secret=" + Controller.CLIENT_SECRET
                                + "&grant_type=" + Controller.GRANT_TYPE
                                + "&code=" + code
                                + "&redirect_uri=" + Controller.REDIRECT_URI))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(accessServer + Controller.TOKEN_PART))
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
