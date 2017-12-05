package searchengine;

import java.util.ArrayList;

public class Page {
    private String url;
    private ArrayList<Integer> words;
    Page(String url, ArrayList<Integer> words) {
        this.url = url;
        this.words = words;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<Integer> getWords() {
        return words;
    }
}
