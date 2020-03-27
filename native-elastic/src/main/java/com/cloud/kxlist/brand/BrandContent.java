package com.cloud.kxlist.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author yzj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "kx_brand")
public class BrandContent {
    private String id;
    private Object createdBy;
    private long createdDate;
    private String lastModifiedBy;
    private long lastModifiedDate;
    private String brandName;
    private String brandNameEN;
    private String brandNameOther;
    private String brandLogo;
    private String location;
    private String homePage;
    private String brandAbout;
    private String brandPoster;
    private String categoryIDs;
    private String adminID;
    private int type;
    private int status;
    private String tempID;
    private String antiFakeStatus;
    private String antiFakeDate;
    private String messageUserImage;
    private String messageUserName;
    private String messageUserPost;
    private String message;
    private String messageImage;
}
