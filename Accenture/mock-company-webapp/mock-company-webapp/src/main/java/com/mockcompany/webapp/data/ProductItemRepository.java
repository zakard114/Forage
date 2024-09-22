package com.mockcompany.webapp.data;

import com.mockcompany.webapp.model.ProductItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This Spring pattern is Java/Spring magic.  At runtime, spring will generate an implementation of this class based on
 * the name/arguments of the method signatures defined in the interface.  See this link for details on doing data access:
 *
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 *
 * It's also possible to do specific queries using the @Query annotation:
 *
 * https://www.baeldung.com/spring-data-jpa-query
 */

@Repository
public interface ProductItemRepository extends CrudRepository<ProductItem, Long> {


    @Query("select p from ProductItem p where lower(p.name) like %:query% or lower(p.description) like %:query%")
    // Unlike Collection or Set, List guarantees order. It maintains the order in which elements are added, and elements can be accessed via an index.
    List<ProductItem> searchByQuery(String query);

    @Query("select p from ProductItem p where lower(p.name)=:query or lower(p.description)=:query")
    List<ProductItem> exactSearchByQuery(String query);

}
