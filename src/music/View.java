package music;

public class View {

    private View() {
        throw new IllegalStateException("View class");
    }

    static void showAuthLink(String accessServer) {
        System.out.println("use this link to request the access code:");
        System.out.println(accessServer + Controller.AUTHORIZE_PART
                + "?client_id=" + Controller.CLIENT_ID
                + "&redirect_uri=" + Controller.REDIRECT_URI
                + "&response_type=" + Controller.RESPONSE_TYPE);
        System.out.println("waiting for code...");
    }
}
