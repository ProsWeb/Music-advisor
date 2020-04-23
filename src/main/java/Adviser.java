import java.io.IOException;

class Adviser {

    private RequestMethod method;

    public void setMethod(final RequestMethod m) {
        this.method = m;
    }

    public void advise(final String accessToken,
                       final String apiServer,
                       final String category)
            throws IOException, InterruptedException {

        this.method.request(accessToken, apiServer, category);
    }
}
