package org.dimhat.solr.example.repository;

import java.util.List;

import org.dimhat.solr.example.model.Product;
import org.dimhat.solr.example.model.SearchableProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * 产品仓库的派生接口，没有实现类
 * 前面3个是没有实现类的，默认实现是方法名称，如findByAvailableTrue的True改成False则查询结果改变。
 * 
 * @author dimhat
 * @date 2015年12月24日 下午9:45:59
 * @version 1.0
 */
public interface DerivedSolrProductRepository extends CustomSolrRepository, SolrCrudRepository<Product, String> {

    Page<Product> findByPopularity(Integer popularity, Pageable page);

    List<Product> findByNameStartingWith(String name);

    /**
     * no property avaliable True2 found for type Product
     */
    Page<Product> findByAvailableTrue(Pageable page);

    @Query(SearchableProduct.AVAILABLE_FIELD + ":false")
    Page<Product> findByAvailableFalseUsingAnnotatedQuery(Pageable page);

}
