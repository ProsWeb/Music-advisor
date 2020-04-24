package music;

import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String ANSWER_DENIED_ACCESS =
            "Please, provide access for application.";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Adviser adviser = new Adviser();
        AuthSetMethod authSetMethod = new AuthSetMethod();

        Map<String, String> servers;
        String apiServer = "";
        String accessToken = "";

        while (sc.hasNext()) {
            String userRequest = sc.next();
            switch (userRequest) {
                case "auth":
                    servers = authSetMethod.setServers();

                    String authServer = servers.get("authServer");
                    authSetMethod.launchServer(authServer);
                    accessToken = authSetMethod.getAccessToken(authServer);

                    apiServer = servers.get("apiServer");

                    System.out.println("Success!");
                    break;
                case "featured":
                    if (accessToken.isEmpty()) {
                        System.out.println(ANSWER_DENIED_ACCESS);
                        break;
                    }

                    adviser.setMethod(new FeaturedRequestMethod());
                    adviser.advise(accessToken, apiServer, "");
                    break;
                case "new":
                    if (accessToken.isEmpty()) {
                        System.out.println(ANSWER_DENIED_ACCESS);
                        break;
                    }

                    adviser.setMethod(new NewRequestMethod());
                    adviser.advise(accessToken, apiServer, "");
                break;
                case "categories":
                    if (accessToken.isEmpty()) {
                        System.out.println(ANSWER_DENIED_ACCESS);
                        break;
                    }

                    adviser.setMethod(new CategoriesRequestMethod());
                    adviser.advise(accessToken, apiServer, "");
                    break;
                case "playlists":
                    String category = sc.nextLine().trim();

                    if (accessToken.isEmpty()) {
                        System.out.println(ANSWER_DENIED_ACCESS);
                        break;
                    }

                    adviser.setMethod(new PlaylistsRequestMethod());
                    adviser.advise(accessToken, apiServer, category);
                    break;
                case "exit":
                    break;
                default:
                    throw new
                            UnsupportedOperationException("Unknown Operation");
            }
        }
    }
}
