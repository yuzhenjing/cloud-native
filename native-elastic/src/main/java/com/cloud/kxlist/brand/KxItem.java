package com.cloud.kxlist.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "kx_item")
public class KxItem {
    private String id;
    private String createdBy;
    private long createdDate;
    private String lastModifiedBy;
    private long lastModifiedDate;
    private String publicID;
    private String commodityID;
    private String barCode;
    private String brandID;
    private String categoryID;
    private int serialNumber;
    private int status;
    private int verifyStatus;
    private int type;
    private String masterID;
    private String otherName;
    private String language;
    private String commodityGroup;
    private String manufacturerCode;
    private String brandName;
    private String commodityName;
    private String origin;
    private String prices;
    private String priceUnits;
    private String efficacy;
    private String commodityImage;
}
