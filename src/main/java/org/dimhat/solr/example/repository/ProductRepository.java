package org.dimhat.solr.example.repository;

import org.dimhat.solr.example.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.solr.core.query.result.FacetPage;

/**
 * 产品仓库接口，继承CrudRepository
 * 
 * @author dimhat
 * @date 2015年12月24日 下午9:43:50
 * @version 1.0
 */
public interface ProductRepository extends CrudRepository<Product, String> {

    Page<Product> findByPopularity(Integer popularity);

    FacetPage<Product> findByNameStartingWithAndFacetOnAvailable(String namePrefix);

    Page<Product> findByAvailableTrue();

}