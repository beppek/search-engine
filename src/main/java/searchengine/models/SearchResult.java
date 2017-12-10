package searchengine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Search result representation of a page to return to the client as JSON
 * */
public class SearchResult implements Comparable<SearchResult> {

    @JsonIgnore
    private Page page;
    private String url;
    private String fullUrl;
    private double score;
    private double pageRank;
    private double frequency;
    private double location;
    private double distance;

    public SearchResult(Page page, double score, double frequency, double location, double distance) {
        this.page = page;
        this.url = page.getUrl();
        this.fullUrl = page.getFullUrl();
        this.pageRank = page.getPageRank();
        this.frequency = frequency;
        this.location = location;
        this.distance = distance;
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public double getScore() {
        return score;
    }

    public double getPageRank() {
        return pageRank;
    }

    public Page getPage() {
        return page;
    }

    public double getLocation() {
        return location;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(SearchResult compareTo) {
        return Double.compare(compareTo.getScore(), score);
    }

    @Override
    public String toString() {
        return "score: " + score + " Page Rank: " + pageRank + " url: " + page.getFullUrl();
    }
}
