package searchengine.utils;

import searchengine.models.Page;
import searchengine.models.PageDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FileHandler {

    private PageDB db;

    public FileHandler(PageDB db) {
        this.db = db;
    }

    public void readDataFiles() {
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
    }

    private String regenerateUrl(String filename) {
        String[] name = filename.split("\\.");
        String path = name[0].replaceAll("_+", "_");
        if (path.charAt(path.length() - 1) == '_') {
            path = path.substring(0, path.length() - 1);
        }
        return "/wiki/" + path;
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
