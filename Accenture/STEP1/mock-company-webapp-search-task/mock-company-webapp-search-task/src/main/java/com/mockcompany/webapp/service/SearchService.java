package com.mockcompany.webapp.service;

import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SearchService {

    private final ProductItemRepository productItemRepository;

    @Autowired
    public SearchService(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    public Collection<ProductItem> search(String query){
        if(query == null || query.trim().isEmpty()){
            return (List<ProductItem>) this.productItemRepository.findAll();
        }

        String queryLowerCase = query.toLowerCase();

        boolean queryWithQuotes = queryLowerCase.startsWith("\"") && queryLowerCase.endsWith("\"");

        if(queryWithQuotes){
            String exactQuery = queryLowerCase.substring(1, queryLowerCase.length()-1);
            // Using JPQL query within productItemRepository
            return this.productItemRepository.exactSearchByQuery(exactQuery);
        } else {
            return this.productItemRepository.searchByQuery(queryLowerCase);
        }
    }

}
