package com.mockcompany.webapp.controller;

import com.mockcompany.webapp.api.SearchReportResponse;
import com.mockcompany.webapp.model.ProductItem;
import com.mockcompany.webapp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Management decided it is super important that we have lots of products that match the following terms.
 * So much so, that they would like a daily report of the number of products for each term along with the total
 * product count.
 */
@RestController
public class ReportController {

    /**
     * The people that wrote this code didn't know about JPA Spring Repository interfaces!
     * > Looks like it is.
     */
    private final SearchService searchService;

    @Autowired
    public ReportController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/api/products/report")
    public SearchReportResponse runReport() {
        Map<String, Integer> hits = new HashMap<>();
        SearchReportResponse response = new SearchReportResponse();
        response.setSearchTermHits(hits);

        // searchService.search(null) or searchService.search("") returns all ProductItem entries.
        // Retrieves all products using ProductItemRepository instead of writing JPQL queries with EntityManager

        List<ProductItem> allItems = (List<ProductItem>) this.searchService.search("");
        int count = allItems.size();
        response.setProductCount(count);

        String[] keywords = {"Cool", "Amazing", "Perfect"};

        // Get the number of products matching the search terms within keywords
        for (String keyword : keywords) {
            // Capitalize the first letter of the keyword
            String capitalizedKeyword = capitalizeFirstLetter(keyword);

            List<ProductItem> items = (List<ProductItem>)searchService.search(keyword);
            System.out.println("Keyword: " + keyword + ", Result size: " + items.size()); // 디버깅용
            response.getSearchTermHits().put(capitalizedKeyword, items.size());
        }

        // Count products containing "kids" in name or description
        int kidCount = 0;
        Pattern kidPattern = Pattern.compile("(.*)[kK][iI][dD][sS](.*)");
        for (ProductItem item : allItems) {
            if (kidPattern.matcher(item.getName()).matches() || kidPattern.matcher(item.getDescription()).matches()) {
                kidCount += 1;
            }
        }
        hits.put("Kids", kidCount);

        return response;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
