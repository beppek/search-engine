package searchengine.engine;

import searchengine.models.Page;
import searchengine.models.PageDB;

import java.util.List;

/**
 * Calculates content based ranking metrics and normalizes the scores
 * */
public class ContentBasedMetrics {

    private PageDB db;

    ContentBasedMetrics(PageDB db) {
        this.db = db;
    }

    void normalizeScores(double[] scores, boolean smallIsBetter) {
        if (smallIsBetter) {
            double min = Double.MAX_VALUE;
            for (double s : scores) {
                if (s < min) {
                    min = s;
                }
            }
            for (int i = 0; i < scores.length; i++) {
                scores[i] = min / Math.max(scores[i], 0.00001);
            }
        } else {
            double max = Double.MIN_VALUE;
            for (double s : scores) {
                if (s > max) {
                    max = s;
                }
            }
            if (max == 0.0) {
                max = 0.00001;
            }
            for (int i = 0; i < scores.length; i++) {
                scores[i] = scores[i] / max;
            }
        }
    }

    /**
     * Counts the frequency for the word
     * higher is better
     * */
    double getFrequencyScore(Page page, String query) {
        double score = 0.0;
        String[] words = query.split(" ");
        for (String w : words) {
            int id = db.getIdForWord(w);
            for (Integer pageWord : page.getWords()) {
                if (id == pageWord) {
                    score += 1;
                }
            }
        }
        return score;
    }

    /**
     * Calculates location of the word on the page
     * Lower is better
     * */
    double getLocationScore(Page page, String query) {
        double score = 0.0;
        String[] words = query.split(" ");
        for (String w : words) {
            int id = db.getIdForWord(w);
            boolean found = false;
            List<Integer> pageWords = page.getWords();
            for (int i = 0; i < pageWords.size(); i++) {
                if (id == pageWords.get(i)) {
                    score += i;
                    found = true;
                }
            }
            if (!found) {
                score += 100000;
            }
        }
        return score;
    }

    /**
     * Calculates the distance between the words in the query on the page
     * Lower score is better
     * */
    double getWordDistanceScore(Page page, String query) {
        double score = 0;
        String[] words = query.split(" ");
        if (words.length == 1) {
            return 0;
        }
        for (int i = 0; i < words.length - 1; i++) {
            int id1 = db.getIdForWord(words[i]);
            for (int j = i + 1; j < words.length; j++) {
                double distance1 = 0.0;
                double distance2 = 0.0;
                int id2 = db.getIdForWord(words[j]);
                boolean found1 = false;
                boolean found2 = false;
                List<Integer> pageWords = page.getWords();
                for (int k = 0; k < pageWords.size(); k++) {
                    if (id1 == pageWords.get(k)) {
                        distance1 += k;
                        found1 = true;
                    }
                    if (id2 == pageWords.get(k)) {
                        distance2 += k;
                        found2 = true;
                    }
                }
                if (found1 && found2) {
                    if (distance1 < distance2) {
                        score += distance2 - distance1;
                    } else {
                        score += distance1 - distance2;
                    }
                } else {
                    score += 100000;
                }
            }
        }
        return score;
    }
}
