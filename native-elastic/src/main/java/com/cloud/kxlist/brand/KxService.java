package com.cloud.kxlist.brand;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class KxService {

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private KxBrandRepository brandRepository;

    @Resource
    private KxItemRepository itemRepository;


    public String initKxBrand() {

        KxDataVO dataVO = restTemplate.getForObject("https://www.kxlist.com/api/brand/search/page", KxDataVO.class, new Object());

        brandRepository.saveAll(dataVO.getData().getContent());

        return "初始化成功";
    }


    public String initKxItem() {

        Iterable<BrandContent> brandContents = brandRepository.findAll();
        itemRepository.deleteAll();


        JSONObject jsonObject = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);
        brandContents.forEach(bc -> {
            JSONObject object = restTemplate.postForObject("https://www.kxlist.com/api/commodityPublic/" + bc.getId(), httpEntity, JSONObject.class);
            JSONObject data1 = object.getJSONObject("data");
            if (data1!=null){
                JSONArray content1 = data1.getJSONArray("content");
                if (content1 != null) {
                    List<KxItem> itemList = JSON.parseObject(content1.toJSONString(), new TypeReference<List<KxItem>>() {
                    });
                    if (!CollectionUtils.isEmpty(itemList)){
                        itemRepository.saveAll(itemList);
                    }

                }
            }
        });
        return "成功";
    }


}
