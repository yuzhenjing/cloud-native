package com.cloud.kxlist.brand;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yzj
 */
@Repository
public interface KxBrandRepository extends ElasticsearchRepository<BrandContent, String> {
}
