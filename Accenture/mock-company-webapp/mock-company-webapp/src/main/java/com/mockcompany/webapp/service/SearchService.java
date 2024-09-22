package com.mockcompany.webapp.service;

import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SearchService {

    private final ProductItemRepository productItemRepository;

    @Autowired
    public SearchService(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    /**
     * When entering a search query, please follow these rules:
     *
     * 1. Double Quotes ("): Exact Match
     *    - Example: "\"special offer\"" -> Searches for items that exactly match ""special offer"".
     *
     * 2. No Quotes: Partial Match
     *    - Example: "special offer" -> Searches for items that contain "special offer" anywhere in the name or description.
     *
     * The presence of double quotes around the query determines if an exact match or partial match search is performed.
     */
    public Collection<ProductItem> search(String query){
        // searchService.search(null) returns all ProductItem entries.
        // Retrieves all products using ProductItemRepository instead of
        // writing JPQL queries with EntityManager
        if(query == null || query.trim().isEmpty()){
            return (List<ProductItem>) this.productItemRepository.findAll();
        }

        String queryLowerCase = query.toLowerCase();

        boolean queryWithQuotes = false;
        if(queryLowerCase.startsWith("\"") && queryLowerCase.endsWith("\"")){
            queryWithQuotes = true;

            // Remove quotes and trim whitespace to ensure accurate search

            // Note: The backslash (\) acts as an escape character and is not included in the actual string.
            // For example, in the string "\"cat\"", the length is 5. If we use length - 1, it is 4,
            // so substring(1, 4) will extract the characters from index 1 to 3, which is "cat"

            // Position 0: "
            // Position 1: c
            // Position 2: a
            // Position 3: t
            // Position 4: "
            String exactQuery = queryLowerCase.substring(1, queryLowerCase.length()-1).trim();
            // Using JPQL query within productItemRepository
            return this.productItemRepository.exactSearchByQuery(exactQuery);
        } else {
            return this.productItemRepository.searchByQuery(queryLowerCase);
        }
    }

}
