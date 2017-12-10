package searchengine.engine;

import searchengine.models.Page;
import searchengine.models.PageDB;

public class PageRankMetrics {

    private PageDB db;

    PageRankMetrics(PageDB db) {
        this.db = db;
    }

//    int getSimpleCount(Page page) {
//        int count = 0;
//        for (Page p : db.getPages()) {
//            if (p.isLinkedTo(page.getUrl())) {
//                count++;
//            }
//        }
//        return count;
//    }

    void calculatePageRank() {
        int iterations = 20;
        System.out.println("Calculating Page Rank ...");
        System.out.println("number of pages: " + db.getPages().size());
        for (int i = 0; i < iterations; i++) {
            for (Page p : db.getPages()) {
                iteratePageRank(p);
            }
        }
        System.out.println("Done calculating Page Rank");
    }

    private void iteratePageRank(Page p) {
        double pr = 0;
        for (Page po : db.getPages()) {
            if (po.hasLinkTo(p.getUrl())) {
                pr += po.getPageRank() / (double)po.linksCount();
            }
        }
        pr = 0.85 * pr + 0.15;
        p.setPageRank(pr);
    }
}
