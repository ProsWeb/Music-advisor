import java.io.IOException;

public interface RequestMethod {
    void request(String accessToken, String apiServer, String category)
            throws IOException, InterruptedException;
}
