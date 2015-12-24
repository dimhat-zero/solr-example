package org.dimhat.solr.example;

import java.util.List;

import org.dimhat.solr.example.model.Product;
import org.dimhat.solr.example.repository.enums.SolrSearchableFields;
import org.dimhat.solr.example.repository.support.SolrProductRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 产品仓库测试
 * 
 * @author dimhat
 * @date 2015年12月24日 下午10:00:38
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:org/dimhat/solr/example/applicationContext.xml")
public class ITestSolrProductRepository extends AbstractSolrIntegrationTest {

    @Autowired
    SolrProductRepository repo;

    @After
    public void tearDown() {
        repo.deleteAll();
    }

    @Test
    public void testCRUD() {
        Assert.assertEquals(0, repo.count());

        Product initial = createProduct(1);
        repo.save(initial);
        Assert.assertEquals(1, repo.count());

        Product loaded = repo.findOne(initial.getId());
        Assert.assertEquals(initial.getName(), loaded.getName());

        loaded.setName("changed named");
        repo.save(loaded);
        Assert.assertEquals(1, repo.count());

        loaded = repo.findOne(initial.getId());
        Assert.assertEquals("changed named", loaded.getName());

        repo.delete(loaded);
        Assert.assertEquals(0, repo.count());
    }

    @Test
    public void testQuery() {
        Assert.assertEquals(0, repo.count());

        List<Product> baseList = createProductList(10);
        repo.save(baseList);

        Assert.assertEquals(baseList.size(), repo.count());

        Page<Product> popularProducts = repo.findByPopularity(20);
        Assert.assertEquals(1, popularProducts.getTotalElements());

        Assert.assertEquals("2", popularProducts.getContent().get(0).getId());
    }

    @Test
    public void testFacetQuery() {
        List<Product> baseList = createProductList(10);
        repo.save(baseList);

        FacetPage<Product> facetPage = repo.findByNameStartingWithAndFacetOnAvailable("pro");
        Assert.assertEquals(10, facetPage.getNumberOfElements());

        Page<FacetFieldEntry> page = facetPage.getFacetResultPage(SolrSearchableFields.AVAILABLE);
        Assert.assertEquals(2, page.getNumberOfElements());

        for (FacetFieldEntry entry : page) {
            Assert.assertEquals(SolrSearchableFields.AVAILABLE.getName(), entry.getField().getName());
            Assert.assertEquals(5, entry.getValueCount());
        }

    }

    @Test
    public void testFilterQuery() {
        List<Product> baseList = createProductList(10);
        repo.save(baseList);

        Page<Product> availableProducts = repo.findByAvailableTrue();
        Assert.assertEquals(5, availableProducts.getTotalElements());
        for (Product product : availableProducts) {
            Assert.assertTrue(product.isAvailable());
        }
    }

}