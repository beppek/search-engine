package searchengine;

import searchengine.engine.SearchEngine;

import java.util.Scanner;

public class SearchEngineMain {
    public static void main(String[] args) {
        SearchEngine engine = new SearchEngine();
        System.out.println("Search: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        engine.query(query.toLowerCase());
    }
}
