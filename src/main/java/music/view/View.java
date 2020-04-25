package music.view;

import music.Util;

import java.util.List;

public class View {

    public void showAuthLink() {
        System.out.println("use this link to request the access code:");
        System.out.println(Util.AUTH_SERVER + Util.AUTHORIZE_PART
                + "?client_id=" + Util.CLIENT_ID
                + "&redirect_uri=" + Util.REDIRECT_URI
                + "&response_type=" + Util.RESPONSE_TYPE);
        System.out.println("waiting for code...");
    }

    public void printNames(List<String> names) {

        for (String name : names) {
            System.out.println(name);
        }
    }

    public void printNamesAndLinks(List<String> names, List<String> links) {

        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + "\n"
                    + links.get(i) + "\n");
        }
    }

    public void printNamesArtistsLinks(List<String> names,
                                       List<String> artists,
                                       List<String> links) {

        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + "\n"
                    + artists.get(i) + "\n"
                    + links.get(i) + "\n");

        }
    }
}
