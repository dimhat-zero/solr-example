package org.dimhat.solr.example.repository.support;

import org.dimhat.solr.example.model.Product;
import org.dimhat.solr.example.repository.ProductRepository;
import org.dimhat.solr.example.repository.enums.SolrSearchableFields;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

/**
 * 产品仓库的实现，继承了SimpleSolrRepository是SolrCrudRepository的实现
 * 在这里实现了3个自定义的查询
 * 
 * @author dimhat
 * @date 2015年12月24日 下午9:43:19
 * @version 1.0
 */
@NoRepositoryBean
public class SolrProductRepository extends SimpleSolrRepository<Product, String> implements ProductRepository {

    @Override
    public Page<Product> findByPopularity(Integer popularity) {
        Query query = new SimpleQuery(new Criteria(SolrSearchableFields.POPULARITY).is(popularity));
        return getSolrOperations().queryForPage(query, Product.class);
    }

    /** 
     * 找到名字前缀匹配并且对结果的可用字段进行导航查询
     */
    @Override
    public FacetPage<Product> findByNameStartingWithAndFacetOnAvailable(String namePrefix) {
        FacetQuery query = new SimpleFacetQuery(new Criteria(SolrSearchableFields.NAME).startsWith(namePrefix));
        query.setFacetOptions(new FacetOptions(SolrSearchableFields.AVAILABLE));
        return getSolrOperations().queryForFacetPage(query, Product.class);
    }

    /** 
     * 过滤查询可用字段为真的
     */
    @Override
    public Page<Product> findByAvailableTrue() {
        Query query = new SimpleQuery(new Criteria(new SimpleField(Criteria.WILDCARD)).expression(Criteria.WILDCARD));
        query.addFilterQuery(new SimpleQuery(new Criteria(SolrSearchableFields.AVAILABLE).is(true)));

        return getSolrOperations().queryForPage(query, Product.class);
    }
}