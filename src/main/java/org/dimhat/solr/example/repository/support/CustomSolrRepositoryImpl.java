package org.dimhat.solr.example.repository.support;

import java.util.List;

import org.dimhat.solr.example.model.Product;
import org.dimhat.solr.example.model.SearchableProduct;
import org.dimhat.solr.example.repository.CustomSolrRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;

/**
 * 自定义的solr仓库实现
 * 
 * @author dimhat
 * @date 2015年12月24日 下午9:47:52
 * @version 1.0
 */
public class CustomSolrRepositoryImpl implements CustomSolrRepository {

    private SolrOperations solrTemplate;

    public CustomSolrRepositoryImpl() {
        super();
    }

    public CustomSolrRepositoryImpl(SolrOperations solrTemplate) {
        super();
        this.solrTemplate = solrTemplate;
    }

    /** 
     * q查询name:value，对SimpleQuery.setPageRequest(page)
     */
    @Override
    public Page<Product> findProductsByCustomImplementation(String value, Pageable page) {
        return solrTemplate.queryForPage(
            new SimpleQuery(new SimpleStringCriteria("name:" + value)).setPageRequest(page), Product.class);
    }

    /** 
     * 更新list字段
     */
    @Override
    public void updateProductCategory(String productId, List<String> categories) {
        PartialUpdate update = new PartialUpdate(SearchableProduct.ID_FIELD, productId);
        update.setValueOfField(SearchableProduct.CATEGORY_FIELD, categories);

        solrTemplate.saveBean(update);
        solrTemplate.commit();
    }

}
