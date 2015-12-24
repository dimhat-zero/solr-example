package org.dimhat.solr.example.repository;

import java.util.List;

import org.dimhat.solr.example.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 自定义的solr仓库接口
 * 
 * @author dimhat
 * @date 2015年12月24日 下午9:46:29
 * @version 1.0
 */
public interface CustomSolrRepository {

    /**
     * 找到产品by自定义的实现
     */
    Page<Product> findProductsByCustomImplementation(String value, Pageable page);

    void updateProductCategory(String productId, List<String> categories);

}
