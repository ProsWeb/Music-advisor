package music.controller;

import java.io.IOException;

public interface RequestMethod {
    void req(String accessToken, String apiServer, String category)
            throws IOException, InterruptedException;
}
