package music;

class View {

    private View() {
        throw new IllegalStateException("View class");
    }

    static void showAuthLink() {
        System.out.println("use this link to request the access code:");
        System.out.println(Util.AUTH_SERVER + Util.AUTHORIZE_PART
                + "?client_id=" + Util.CLIENT_ID
                + "&redirect_uri=" + Util.REDIRECT_URI
                + "&response_type=" + Util.RESPONSE_TYPE);
        System.out.println("waiting for code...");
    }
}
