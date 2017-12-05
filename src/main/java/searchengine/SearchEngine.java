package searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Indexer {
    private PageDB db = new PageDB();

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
