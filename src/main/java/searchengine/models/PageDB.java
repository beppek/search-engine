package searchengine.models;

import java.util.*;

public class PageDB {
    private Map<String, Integer> wordToId = new HashMap<String, Integer>();
    private List<Page> pages = new ArrayList<Page>();
    private HashSet<String> urls = new HashSet<String>();

    public int getIdForWord(String word) {
        if (wordToId.containsKey(word)) {
            return wordToId.get(word);
        } else {
            int id = wordToId.size();
            wordToId.put(word, id);
            return id;
        }
    }

    public void addWordFromFile(String word, Integer id) {
        wordToId.put(word, id);
    }

    public List<Page> getPages() {
        return pages;
    }

    public Page getPage(int i) {
        return pages.get(i);
    }

    public Map<String, Integer> getWordToId() {
        return wordToId;
    }

    public void addPage(Page page) {
        if (!urls.contains(page.getUrl())) {
            pages.add(page);
            urls.add(page.getUrl());
        }
    }

    public void addAllPages(ArrayList<Page> pages) {
        this.pages.addAll(pages);
    }
}
