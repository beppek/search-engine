package searchengine.routes;

import org.springframework.web.bind.annotation.*;
import searchengine.engine.SearchEngine;
import searchengine.models.SearchResult;

import java.util.ArrayList;

/**
 * REST endpoint to query the engine
 * */
@RestController
@CrossOrigin()
public class SearchController {

    @PostMapping("/")
    public ArrayList<SearchResult> Search(@RequestParam(value="query") String query) {
        SearchEngine engine = new SearchEngine();
        return engine.query(query.toLowerCase());
    }
}
