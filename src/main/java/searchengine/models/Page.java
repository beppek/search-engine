package searchengine.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A page from the dataset
 * */
public class Page {
    private String url;
    private ArrayList<Integer> words;
    private Set<String> links;
    private double pageRank = 1.0;
    private String fullUrl;
    private int linksCount;

    public Page(String url) {
        this.url = url;
        this.fullUrl = "https://en.wikipedia.org" + url;
    }

    public Page(String url, ArrayList<Integer> words, ArrayList<String> links) {
        this.url = url;
        this.words = words;
        this.links = new HashSet<String>(links);
        this.linksCount = links.size();
        this.fullUrl = "https://en.wikipedia.org" + url;
    }

    public Page(String url, ArrayList<Integer> words, ArrayList<String> links, int linksCount) {
        this.url = url;
        this.words = words;
        this.links = new HashSet<String>(links);
        this.linksCount = linksCount;
        this.fullUrl = "https://en.wikipedia.org" + url;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<Integer> getWords() {
        return words;
    }

    public void setWords(ArrayList<Integer> words) {
        this.words = words;
    }

    public Set<String> getLinks() {
        return links;
    }

    public void setLinks(Set<String> links) {
        this.links = links;
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
        return linksCount;
    }

    public void setLinksCount(int linksCount) {
        this.linksCount = linksCount;
    }

    public boolean hasLinkTo(String url) {
        return links.contains(url);
    }

    public boolean equals(Page page) {
        return this.getUrl().equals(page.getUrl());
    }
}
