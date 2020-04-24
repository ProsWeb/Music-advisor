package music;

import java.io.IOException;

interface RequestMethod {
    void request(String accessToken, String apiServer, String category)
            throws IOException, InterruptedException;
}
