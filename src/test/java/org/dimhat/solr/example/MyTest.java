package org.dimhat.solr.example;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;
import org.dimhat.solr.example.model.Product;
import org.dimhat.solr.example.repository.ProductRepository;
import org.dimhat.solr.example.repository.enums.SolrSearchableFields;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * 我的测试类
 * 
 * @author dimhat
 * @date 2015年12月24日 下午10:57:23
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:org/dimhat/solr/example/applicationContext.xml")
public class MyTest extends AbstractSolrIntegrationTest {

    static {
        try {
            Log4jConfigurer.initLogging("classpath:org/dimhat/solr/example/log4j.properties");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
    }

    private Logger    logger = Logger.getLogger(MyTest.class);

    @Autowired
    ProductRepository repo;

    @After
    public void tearDown() {
        //repo.deleteAll();
    }

    @Test
    public void indexOne() {
        Product initial = createProduct(1);
        repo.save(initial);//保存
        logger.info("save success");

        Product loaded = repo.findOne(initial.getId());//查询
        logger.info("query success:" + loaded);

        loaded.setName("changed named");
        repo.save(loaded);//更新
        logger.info("update success");

        loaded = repo.findOne(initial.getId());//查询
        logger.info("query success:" + loaded);
    }

    /**
     * 创建/修改索引  
     */
    @Test
    public void indexTen() {
        List<Product> baseList = createProductList(10);
        repo.save(baseList);
    }

    /**
     * 精确查询  
     */
    @Test
    public void testQuery() {
        Page<Product> popularProducts = repo.findByPopularity(20);//根据popularity字段的精确值查询
        for (Product product : popularProducts.getContent()) {
            logger.info(product);
        }
    }

    /**
     * 导航查询  
     */
    @Test
    public void testFacetQuery() {
        FacetPage<Product> facetPage = repo.findByNameStartingWithAndFacetOnAvailable("pro");//facet导航查询
        for (Product product : facetPage.getContent()) {
            logger.info(product);//查询结果集all
        }

        Page<FacetFieldEntry> page = facetPage.getFacetResultPage(SolrSearchableFields.AVAILABLE);//获取导航结果
        for (FacetFieldEntry entry : page.getContent()) {
            logger.info("[导航字段=" + entry.getField().getName() + ", 字段的值=" + entry.getValue() + ", 值的个数="
                        + entry.getValueCount() + "]");
        }
    }

    /**
     * 过滤查询  
     */
    @Test
    public void testFilterQuery() {
        Page<Product> availableProducts = repo.findByAvailableTrue();
        for (Product product : availableProducts) {
            logger.info(product);
        }
    }

}
