package searchengine.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import searchengine.models.Page;
import searchengine.models.PageDB;

import java.io.*;
import java.util.*;

/**
 * Handles everything files-related
 * Reads the original dataset from file or reads the saved index
 * Also saves the index as json
 * */
public class FileHandler {

    private PageDB db;
    private String pageIndexPath = "data/page-index.json";

    public FileHandler(PageDB db) {
        this.db = db;
    }

    public boolean indexExists() {
        File pf = new File(pageIndexPath);
        return pf.exists() && !pf.isDirectory();
    }

    /**
     * Saves the PageDB to json file
     * */
    public void savePageDBIndex() throws IOException {
        System.out.println("Saving index to file...");
        File f = new File(pageIndexPath);
        f.createNewFile();
        BufferedWriter w = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
        Gson gson = new Gson();
        w.write(gson.toJson(db));
        w.close();
        System.out.println("Done saving index to file");
    }

    /**
     * Reads the PageDB from json file
     * */
    public void readDB() throws IOException {
        System.out.println("Reading index from file...");
        File f = new File(pageIndexPath);
        JsonReader reader = new JsonReader(new FileReader(f));
        Map<String, Integer> wordToInt = new HashMap<String, Integer>();
        reader.beginObject();
        String n = reader.nextName();
        System.out.println(n);
        reader.beginObject();
        while (reader.hasNext()) {
            String word = reader.nextName();
            Integer id = reader.nextInt();
            db.addWordFromFile(word, id);
            wordToInt.put(word, id);
        }
        reader.endObject();
        String p = reader.nextName();
        System.out.println(p);

        reader.beginArray();
        db.addAllPages(readPagesFromJson(reader));

        reader.endArray();
        System.out.println("Done reading index from file");
    }

    /**
     * Reads the data set from files and generates Page representations
     * */
    public void readDataFiles() {
        System.out.println("Start reading pages from files...");
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
        System.out.println("Done reading pages from files");
    }

    /**
     * Reads the PageDB index from json file
     * */
    private ArrayList<Page> readPagesFromJson(JsonReader reader) throws IOException {
        ArrayList<Page> pages = new ArrayList<Page>();
        int i = 0;
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();

                if (name.equals("url")) {
                    String url = reader.nextString();
                    pages.add(new Page(url));

                } else if (name.equals("words")) {
                    reader.beginArray();
                    ArrayList<Integer> words = new ArrayList<Integer>();
                    while (reader.hasNext()) {
                        words.add(reader.nextInt());
                    }
                    pages.get(i).setWords(words);
                    reader.endArray();

                } else if (name.equals("links")) {
                    reader.beginArray();
                    Set<String> links = new HashSet<String>();
                    while (reader.hasNext()) {
                        links.add(reader.nextString());
                    }
                    pages.get(i).setLinks(links);
                    reader.endArray();

                } else if (name.equals("pageRank")) {
                    Double pageRank = reader.nextDouble();
                    pages.get(i).setPageRank(pageRank);

                } else if (name.equals("linksCount")) {
                    int linksCount = reader.nextInt();
                    pages.get(i).setLinksCount(linksCount);
                    i++;

                } else {
                    //Read any strings that are not needed to recreate the db and just ignore them
                    String s = reader.nextString();
                }
            }
            reader.endObject();
        }
        return pages;
    }

    /**
     * Recreates the internal "/wiki/" link structure from the filename
     * */
    private String regenerateUrl(String filename) {
        String[] name = filename.split("\\.");
        String path = name[0].replaceAll("_+", "_");
        if (path.charAt(path.length() - 1) == '_') {
            path = path.substring(0, path.length() - 1);
        }
        return "/wiki/" + path;
    }

    /**
     * Generates a page and adds it to the db
     * */
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
