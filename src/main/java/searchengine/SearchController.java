package searchengine;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@RestController
@CrossOrigin()
public class SearchController {

    @RequestMapping("/")
    public ArrayList<SearchResult> Search(@RequestParam(value="query") String query) {
        SearchEngine engine = new SearchEngine();
        ArrayList<SearchResult> results = engine.query(query);
        return results;
    }
}
