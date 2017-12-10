package searchengine;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin()
public class SearchController {

    @PostMapping("/")
    public ArrayList<SearchResult> Search(@RequestParam(value="query") String query) {
        SearchEngine engine = new SearchEngine();
        ArrayList<SearchResult> results = engine.query(query);
        return results;
    }
}
