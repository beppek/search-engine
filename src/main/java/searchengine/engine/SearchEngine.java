package searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SearchEngine {
    private PageDB db = new PageDB();
    private ArrayList<SearchResult> results;

    SearchEngine() {
        ArrayList<File> files = new ArrayList<File>();
        File wordsDir = new File("data/words/");
        File[] subs = wordsDir.listFiles();
        for (File s : subs) {
            Collections.addAll(files, s.listFiles());
        }
        for (File f : files) {
            File lf = new File(f.getAbsolutePath().replaceFirst("words", "links"));
            generatePage(regenerateUrl(f.getName()), f, lf);
        }
        calculatePageRank();
    }

    private String regenerateUrl(String filename) {
        String[] name = filename.split("\\.");
        String path = name[0].replaceAll("_+", "_");
        if (path.charAt(path.length() - 1) == '_') {
            path = path.substring(0, path.length() - 1);
        }
        return "/wiki/" + path;
    }

    public ArrayList<SearchResult> query(String query) {
        results = new ArrayList<SearchResult>();
        double[] content = new double[db.getPages().size()];
        double[] location = new double[db.getPages().size()];
        double[] distance = new double[db.getPages().size()];
        double[] pageRank = new double[db.getPages().size()];
        for (int i = 0; i < db.getPages().size(); i++) {
            Page page = db.getPages().get(i);
            content[i] = getFrequencyScore(page, query);
            location[i] = getLocationScore(page, query);
            pageRank[i] = page.getPageRank();
            distance[i] = getWordDistanceScore(page, query);
        }

        normalizeScores(content, false);
        normalizeScores(location, true);
        normalizeScores(distance, true);
        normalizeScores(pageRank, false);

        for (int i = 0; i < db.getPages().size(); i++) {
            Page p = db.getPages().get(i);
//            double score = 1.0 * content[i] + 1.0 * pageRank[i] + 0.5 * location[i] + 0.5 * distance[i];
            double score = 1.0 * content[i] + 1.0 * p.getPageRank() + 0.5 * location[i];// + 0.5 * distance[i];
            if (score > 0.05) {
                results.add(new SearchResult(p, score, content[i], location[i], distance[i]));
            }
        }

        Collections.sort(results);

        System.out.println("\nResults for search \"" + query + "\":");
        int no = 5;
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

    private double getFrequencyScore(Page page, String query) {
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

    private double getLocationScore(Page page, String query) {
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

    private double getWordDistanceScore(Page page, String query) {
        double score = 0;
        String[] words = query.split(" ");
        if (words.length == 1) {
            return 1;
        }
        for (int i = 1; i < words.length; i++) {
            double distance1 = 0.0;
            double distance2 = 0.0;
            int id1 = db.getIdForWord(words[i]);
            int id2 = db.getIdForWord(words[i-1]);
            boolean found1 = false;
            boolean found2 = false;
            ArrayList<Integer> pageWords = page.getWords();
            for (int j = 0; j < pageWords.size(); j++) {
                if (id1 == pageWords.get(j)) {
                    distance1 += j;
                    found1 = true;
                }
                if (id2 == pageWords.get(j)) {
                    distance2 += j;
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
        return score;
    }

    private int getSimpleCount(Page page) {
        int count = 0;
        for (Page p : db.getPages()) {
            if (p.isLinkedTo(page.getUrl())) {
                count++;
            }
        }
        return count;
    }

    private void calculatePageRank() {
        int iterations = 20;
        System.out.println("Calculating Page Rank ...");
        System.out.println("number of pages: " + db.getPages().size());
        for (int i = 0; i < 20; i++) {
            for (Page p : db.getPages()) {
                iteratePageRank(p);
            }
        }
        System.out.println("Done calculating Page Rank");
    }

    private void iteratePageRank(Page p) {
        double pr = 0;
        for (Page po : db.getPages()) {
            if (po.isLinkedTo(p.getUrl())) {
                pr += po.getPageRank() / (double)po.linksCount();
            }
        }
        pr = 0.85 * pr + 0.15;
        p.setPageRank(pr);
    }

    private void generatePage(String url, File wordsFile, File linksFile) {
        try {
            ArrayList<Integer> words = new ArrayList<Integer>();
            ArrayList<String> links = new ArrayList<String>();
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
            BufferedReader lin = new BufferedReader(new FileReader(linksFile));
            while ((line = lin.readLine()) != null) {
                String[] linksList = line.split(" ");
                links.addAll(Arrays.asList(linksList));
            }
            lin.close();
            Page page = new Page(url, words, links);
            db.addPage(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
