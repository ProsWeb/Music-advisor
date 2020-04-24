package music;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Adviser adviser = new Adviser();
        AuthSetMethod authSetMethod = new AuthSetMethod();

        String accessToken = "";

        while (sc.hasNext()) {
            String userRequest = sc.next();
            switch (userRequest) {
                case "auth":
                    authSetMethod.launchServer();
                    accessToken = authSetMethod.getAccessToken();

                    System.out.println("Success!");
                    break;
                case "featured":
                    if (accessToken.isEmpty()) {
                        System.out.println(Util.ANSWER_DENIED_ACCESS);
                        break;
                    }

                    adviser.setMethod(new FeaturedRequestMethod());
                    adviser.advise(accessToken, Util.API_SERVER, "");
                    break;
                case "new":
                    if (accessToken.isEmpty()) {
                        System.out.println(Util.ANSWER_DENIED_ACCESS);
                        break;
                    }

                    adviser.setMethod(new NewRequestMethod());
                    adviser.advise(accessToken, Util.API_SERVER, "");
                break;
                case "categories":
                    if (accessToken.isEmpty()) {
                        System.out.println(Util.ANSWER_DENIED_ACCESS);
                        break;
                    }

                    adviser.setMethod(new CategoriesRequestMethod());
                    adviser.advise(accessToken, Util.API_SERVER, "");
                    break;
                case "playlists":
                    String category = sc.nextLine().trim();

                    if (accessToken.isEmpty()) {
                        System.out.println(Util.ANSWER_DENIED_ACCESS);
                        break;
                    }

                    adviser.setMethod(new PlaylistsRequestMethod());
                    adviser.advise(accessToken, Util.API_SERVER, category);
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
