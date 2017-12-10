package searchengine.routes;

/**
 * Pong response for ping controller
 * */
public class Pong {
    private final String response;

    public Pong() {
        this.response = "Pong";
    }

    public String getContent() {
        return response;
    }
}
