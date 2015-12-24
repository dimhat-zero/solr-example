package org.dimhat.solr.example.model;

/**
 * field和java属性的映射
 * 属性默认 public static final 
 * 方法默认 public abstract
 * 
 * @author dimhat
 * @date 2015年12月24日 下午9:32:35
 * @version 1.0
 */
public interface SearchableProduct {

    String ID_FIELD         = "id";
    String NAME_FIELD       = "name";
    String PRICE_FIELD      = "price";
    String AVAILABLE_FIELD  = "inStock";
    String CATEGORY_FIELD   = "cat";
    String WEIGHT_FIELD     = "weight";
    String POPULARITY_FIELD = "popularity";

}
