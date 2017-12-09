package searchengine;

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

    public List<Page> getPages() {
        return pages;
    }

    public void addPage(Page page) {
        if (!urls.contains(page.getUrl())) {
            pages.add(page);
            urls.add(page.getUrl());
        }
    }
}
