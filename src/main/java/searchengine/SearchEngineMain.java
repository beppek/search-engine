package searchengine;

public class SearchEngineMain {
    public static void main(String[] args) {
        SearchEngine engine = new SearchEngine();
        engine.query("blockchain bitcoin sweden".toLowerCase());
    }
}
