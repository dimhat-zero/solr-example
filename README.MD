#solr-example
使用spring-data-solr操作solr的项目。

>
spring-data-solr官网
@see https://github.com/spring-projects/spring-data-solr
spring-data-solr官方示例
@see https://github.com/spring-projects/spring-data-solr-examples

##配置多core
配置多个SolrTemplate的bean，构造注入不同的core名称，最后注入自定义的repo
```
<!-- solr服务器地址 -->
<solr:solr-server id="solrServer" url="${solr.host}" />

<!-- 多个core设置不同的solrTemplate -->
<bean id="solrProductTemplate" class="org.springframework.data.solr.core.SolrTemplate"
	scope="singleton">
	<constructor-arg ref="solrServer" />
	<constructor-arg value="${solr.product.core.name}" />
</bean>

<!-- 自定义的repo，引用设置了core的template，不指定默认collection1 -->
<bean id="searchRepository"
	class="org.dimhat.solr.example.repository.support.SolrProductRepository">
	<property name="solrOperations" ref="solrProductTemplate" />
</bean>
```

##使用方法说明
1. 使用searchRepository
直接调用bean的方法，跟我们的原来的一样。@see MyTest

2. 使用工厂创建新实例，并注入自定义的实现类
```
@Autowired
SolrOperations                       solrOperations;

private DerivedSolrProductRepository repo;

@Before
public void setUp() {
    // Create new repository instance using Factory and inject custom implementation
    repo = new SolrRepositoryFactory(this.solrOperations).getRepository(DerivedSolrProductRepository.class,
        new CustomSolrRepositoryImpl(this.solrOperations));
}
```

存在三种情况：
接口方法在自定义的实现类中存在实现
接口方法不存在实现，但存在注解
接口方法的实现和注解都不存在

前两种比较好理解，第三种应该跟spring data jpa类似 @see http://uule.iteye.com/blog/2084512
spring-data的映射（直接在接口中定义查询方法，如果是符合规范的，可以不用写实现）

```
/**
 * no property avaliable True2 found for type Product
 */
Page<Product> findByAvailableTrue(Pageable page);
```

***

##环境依赖
* maven
* solr 在solr根目录/example下运行jar -jar start.jar启动solr(http://localhost:8983/solr/)
