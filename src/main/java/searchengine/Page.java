package searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Page {
    private String url;
    private ArrayList<Integer> words;
    private Set<String> links;
    private double pageRank = 1.0;
    private String fullUrl;

    Page(String url, ArrayList<Integer> words, ArrayList<String> links) {
        this.url = url;
        this.words = words;
        this.links = new HashSet<String>(links);
        this.fullUrl = "https://en.wikipedia.org" + url;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<Integer> getWords() {
        return words;
    }

    public Set<String> getLinks() {
        return links;
    }

    public double getPageRank() {
        return pageRank;
    }

    public void setPageRank(double pr) {
        this.pageRank = pr;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public int linksCount() {
        return links.size();
    }

    public boolean isLinkedTo(String url) {
        return links.contains(url);
    }

    public boolean equals(Page page) {
        return this.getUrl().equals(page.getUrl());
    }
}
