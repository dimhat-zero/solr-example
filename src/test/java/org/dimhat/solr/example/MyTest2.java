package org.dimhat.solr.example;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dimhat.solr.example.model.Product;
import org.dimhat.solr.example.model.SearchableProduct;
import org.dimhat.solr.example.repository.DerivedSolrProductRepository;
import org.dimhat.solr.example.repository.support.CustomSolrRepositoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.repository.support.SolrRepositoryFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * TODO
 * @author dimhat
 * @date 2015年12月25日 上午12:51:24
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:org/dimhat/solr/example/applicationContext.xml")
public class MyTest2 extends AbstractSolrIntegrationTest {

    static {
        try {
            Log4jConfigurer.initLogging("classpath:org/dimhat/solr/example/log4j.properties");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
    }

    private Logger                       logger = Logger.getLogger(MyTest2.class);

    @Autowired
    SolrOperations                       solrOperations;

    private DerivedSolrProductRepository repo;

    @Before
    public void setUp() {
        // Create new repository instance using Factory and inject custom implementation
        repo = new SolrRepositoryFactory(this.solrOperations).getRepository(DerivedSolrProductRepository.class,
            new CustomSolrRepositoryImpl(this.solrOperations));
    }

    @After
    public void tearDown() {
        //solrOperations.delete(new SimpleQuery(new SimpleStringCriteria("*:*")));
        //solrOperations.commit();
    }

    /**
     * 分页精确查询  
     */
    @Test
    public void testDerivedQueryFindByPopularity() {
        Page<Product> popularProducts = repo.findByPopularity(30, new PageRequest(0, 3));

        for (Product product : popularProducts.getContent()) {
            logger.info(product);
        }
    }

    /**
     * 根据前缀查找   
     */
    @Test
    public void testFindByNameStartingWith() {
        List<Product> products = repo.findByNameStartingWith("pro");
        for (Product product : products) {
            logger.info(product);
        }
    }

    /**
     * 分页精确查找   
     */
    @Test
    public void testFindByAvailableTrue() {
        Page<Product> products = repo.findByAvailableTrue(new PageRequest(0, 3));
        for (Product product : products.getContent()) {
            logger.info(product);
        }
    }

    /**
     * 通过注解方式分页精确查询
     */
    @Test
    public void testAnnotatedQueryfindByAvailableFalseUsingAnnotatedQuery() {

        Page<Product> unavailableProducts = repo.findByAvailableFalseUsingAnnotatedQuery(new PageRequest(0, 3));
        logger.info("[totalPages=" + unavailableProducts.getTotalPages() + ", thisPageElements="
                    + unavailableProducts.getNumberOfElements() + ", totalElements="
                    + unavailableProducts.getTotalElements() + "]");
        for (Product product : unavailableProducts) {
            logger.info(product);
        }
    }

    /**
     * 分词查询  
     */
    @Test
    public void testCustomRepositoryImplementation() {

        Page<Product> page = repo.findProductsByCustomImplementation("product-1", new PageRequest(0, 5));
        for (Product product : page.getContent()) {
            logger.info(product);
        }
    }

    /**
     * 更新索引的list字段  
     */
    @Test
    public void testPartialUpdate() {

        Product loaded = repo.findOne("1");
        logger.info("find one:" + loaded);
        List<String> categoryUpdate = Arrays.asList("cat-1", "cat-2", "cat-3");
        repo.updateProductCategory(loaded.getId(), categoryUpdate);

        loaded = repo.findOne(loaded.getId());
        for (String category : loaded.getCategories()) {
            logger.info(category);
        }
    }

    /**
     * 直接用solrOperations，更新值为null  
     */
    @Test
    public void testPartialUpdateWithNull() {

        Product loaded = repo.findOne("1");

        PartialUpdate update = new PartialUpdate(SearchableProduct.ID_FIELD, loaded.getId());
        update.setValueOfField(SearchableProduct.POPULARITY_FIELD, 500);
        update.setValueOfField(SearchableProduct.CATEGORY_FIELD, Arrays.asList("cat-1", null, "cat-3"));
        update.setValueOfField(SearchableProduct.NAME_FIELD, null);
        solrOperations.saveBean(update);
        solrOperations.commit();

        loaded = repo.findOne(loaded.getId());
        logger.info("result:" + loaded);
        for (String category : loaded.getCategories()) {
            logger.info(category);
        }
    }
}
