package searchengine.engine;

import searchengine.models.Page;
import searchengine.models.PageDB;
import searchengine.models.SearchResult;
import searchengine.utils.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The search engine itself, a crude beginning to a brilliant future
 * */
public class SearchEngine {
    private PageDB db;
    private ArrayList<SearchResult> results;

    public SearchEngine() {
        this.db = new PageDB();
        init();
    }

    /**
     * Initialize the db, either by reading pages from the files in the dataset or reading from index.
     * */
    private void init() {
        FileHandler fh = new FileHandler(this.db);
        if (fh.indexExists()) {
            try {
                fh.readDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            fh.readDataFiles();
            PageRankMetrics prm = new PageRankMetrics(this.db);
            prm.calculatePageRank();
            try {
                fh.savePageDBIndex();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Query and rank the results
     * */
    public ArrayList<SearchResult> query(String query) {
        results = new ArrayList<SearchResult>();
        ContentBasedMetrics cbm = new ContentBasedMetrics(this.db);
        double[] content = new double[db.getPages().size()];
        double[] location = new double[db.getPages().size()];
        double[] distance = new double[db.getPages().size()];
        double[] pageRank = new double[db.getPages().size()];
        for (int i = 0; i < db.getPages().size(); i++) {
            Page page = db.getPages().get(i);
            content[i] = cbm.getFrequencyScore(page, query);
            location[i] = cbm.getLocationScore(page, query);
            pageRank[i] = page.getPageRank();
            distance[i] = cbm.getWordDistanceScore(page, query);
        }

        cbm.normalizeScores(content, false);
        cbm.normalizeScores(location, true);
        cbm.normalizeScores(distance, true);
        cbm.normalizeScores(pageRank, false);

        for (int i = 0; i < db.getPages().size(); i++) {
            Page p = db.getPage(i);
            //Pages with no mention of the search query shouldn't be added to result
            if (content[i] > 0) {
              double score = 1.0 * content[i] + 1.0 * pageRank[i] + 0.5 * location[i] + 0.5 * distance[i];
                if (score > 0.1) {
                    results.add(new SearchResult(p, score, content[i], location[i], distance[i]));
                }
            }
        }

        Collections.sort(results);

        System.out.println("\nResults for search \"" + query + "\":");
        int no = results.size() < 5 ? results.size() : 5;
        for (int i = 0; i < no; i++) {
            System.out.println(results.get(i).toString());
        }
        return results;
    }
}
