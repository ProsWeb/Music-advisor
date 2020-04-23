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

    private static final String CLIENT_ID = "da072c60fcee469e8b0f4140aa4480d5";
    private static final String CLIENT_SECRET = "8ada13093c704487b57c3a660448884e";
    private static final String AUTHORIZE_PART = "/authorize";
    private static final String RESPONSE_TYPE = "code";
    private static final String TOKEN_PART = "/api/token";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URI = "http://localhost:8080";

    private String code = "";

    Map<String, String> setServers(final String[] args) {

        Map<String, String> servers = new HashMap<>();

        if (args.length != 0 && args[0].contains("-access")) {
            servers.put("authServer", args[1]);
        } else {
            servers.put("authServer", "https://accounts.spotify.com");
        }

        if (args.length != 0 && args[2].contains("-resource")) {
            servers.put("apiServer", args[3]);
        } else {
            servers.put("apiServer", "https://api.spotify.com");
        }

        return servers;
    }

    void launchServer(final String accessServer)
            throws IOException, InterruptedException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);

        server.start();

        System.out.println("use this link to request the access code:");
        System.out.println(accessServer + AUTHORIZE_PART
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE);
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
                        "client_id=" + CLIENT_ID
                                + "&client_secret=" + CLIENT_SECRET
                                + "&grant_type=" + GRANT_TYPE
                                + "&code=" + code
                                + "&redirect_uri=" + REDIRECT_URI))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(accessServer + TOKEN_PART))
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
