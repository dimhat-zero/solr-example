package org.dimhat.solr.example;

import java.util.ArrayList;
import java.util.List;

import org.dimhat.solr.example.model.Product;

/**
 * 抽象solr整合测试
 * 
 * @author dimhat
 * @date 2015年12月24日 下午9:56:02
 * @version 1.0
 */
public abstract class AbstractSolrIntegrationTest {

    protected List<Product> createProductList(int nrProducts) {
        List<Product> products = new ArrayList<Product>(nrProducts);
        for (int i = 0; i < nrProducts; i++) {
            products.add(createProduct(i));
        }
        return products;
    }

    protected Product createProduct(int id) {
        Product product = new Product();
        product.setId(Integer.toString(id));
        product.setAvailable(id % 2 == 0);
        product.setName("product-" + id);
        //product.setPopularity(id * 10);
        product.setPopularity(30);
        product.setPrice((float) id * 100);
        product.setWeight((float) id * 2);
        return product;
    }
}
