package com.cloud.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.UUID;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * <p>
 *
 * </p>
 *
 * @author yzj
 * @since 2020-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid = UUID.randomUUID().toString();

    /**
     * 缩短后的短址id
     */
    private String shorlUrlId;

    /**
     * 原网址
     */
    private String longUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 上一次访问时间
     */
    private LocalDateTime lastView;

    /**
     * 访问密码
     */
    private String viewPwd;


}
