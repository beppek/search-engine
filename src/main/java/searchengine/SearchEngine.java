package searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class SearchEngine {
    private PageDB db = new PageDB();
    private ArrayList<SearchResult> results;

    SearchEngine() {
        ArrayList<File> files = new ArrayList<File>();
        File dir = new File("data/words/");
        File[] subs = dir.listFiles();
        for (File s : subs) {
            Collections.addAll(files, s.listFiles());
        }
        for (File f : files) {
            generatePage(regenerateUrl(f.getName()), f);
        }
    }

    private String regenerateUrl(String filename) {
        String[] name = filename.split("\\.");
        String path = name[0].replaceAll("_+", "_");
        if (path.charAt(path.length() - 1) == '_') {
            path = path.substring(0, path.length() - 1);
        }
        return "https://en.wikipedia.org/wiki/" + path;
    }

    public ArrayList<SearchResult> query(String query) {
        results = new ArrayList<SearchResult>();

        double[] content = new double[db.getPages().size()];
        double[] location = new double[db.getPages().size()];
        double[] distance = new double[db.getPages().size()];
        for (int i = 0; i < db.getPages().size(); i++) {
            Page page = db.getPages().get(i);
            content[i] = getCountFrequencyScore(page, query);
            location[i] = getCountLocationScore(page, query);
//            distance[i] = getWordDistanceScore(page, query);
        }

        normalizeScores(content, false);
        normalizeScores(location, true);

        for (int i = 0; i < db.getPages().size(); i++) {
            Page p = db.getPages().get(i);
            double score = 1.0 * content[i] + 0.5 * location[i];
            results.add(new SearchResult(p, score));
        }

        Collections.sort(results);

        System.out.println("\nResults for search \"" + query + "\":");
        int no = 50;
        for (int i = 0; i < no; i++) {
            System.out.println(results.get(i).toString());
        }
        return results;
    }

    private void normalizeScores(double[] scores, boolean smallIsBetter) {
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

    private double getCountFrequencyScore(Page page, String query) {
        double score = 0.0;
        String[] words = query.split(" ");
        for (String w : words) {
            int id = db.getIdForWord(w);
            for (Integer pageWord : page.getWords()) {
                if (id == pageWord) {
                    score++;
                }
            }
        }
        return score;
    }

    private double getCountLocationScore(Page page, String query) {
        double score = 0.0;
        String[] words = query.split(" ");
        for (String w : words) {
            int id = db.getIdForWord(w);
            boolean found = false;
            ArrayList<Integer> pageWords = page.getWords();
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
     * TODO: If time
     * */
    private double getWordDistanceScore(Page page, String query) {
        double score = 0.0;
//        String[] words = query.split(" ");
//        if (words.length < 2) {
//            return 1.0;
//        }
//        for (int i = 1; i < words.length; i++) {
//
//        }
        return score;
    }

    private void generatePage(String url, File wordsFile) {
        try {
            ArrayList<Integer> words = new ArrayList<Integer>();
            BufferedReader win = new BufferedReader(new FileReader(wordsFile));
            String line;
            while ((line = win.readLine()) != null) {
                String[] wlist = line.split(" ");
                for (String w : wlist) {
                    int id = db.getIdForWord(w);
                    words.add(id);
                }
            }
            win.close();

            Page page = new Page(url, words);
            db.addPage(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
