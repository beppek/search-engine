package searchengine;

public class SearchResult implements Comparable<SearchResult> {

    private double score;
    private Page page;

    SearchResult(Page page, double score) {
        this.page = page;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public Page getPage() {
        return page;
    }

    @Override
    public int compareTo(SearchResult compareTo) {
        return Double.compare(compareTo.getScore(), score);
    }

    @Override
    public String toString() {
        return "score: " + score + " pagerank: " + page.getPageRank() + " url: " + page.getFullUrl();
    }
}
