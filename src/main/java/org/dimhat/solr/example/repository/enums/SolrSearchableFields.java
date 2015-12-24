package org.dimhat.solr.example.repository.enums;

import org.dimhat.solr.example.model.SearchableProduct;
import org.springframework.data.solr.core.query.Field;

/**
 * solr可搜索的参数的枚举
 * 
 * @author dimhat
 * @date 2015年12月24日 下午9:41:14
 * @version 1.0
 */
public enum SolrSearchableFields implements Field {

    ID(SearchableProduct.ID_FIELD),

    NAME(SearchableProduct.NAME_FIELD),

    PRICE(SearchableProduct.PRICE_FIELD),

    AVAILABLE(SearchableProduct.AVAILABLE_FIELD),

    CATEGORY(SearchableProduct.CATEGORY_FIELD),

    WEIGHT(SearchableProduct.WEIGHT_FIELD),

    POPULARITY(SearchableProduct.POPULARITY_FIELD);

    private final String fieldName;

    private SolrSearchableFields(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getName() {
        return fieldName;
    }

}