package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author jobob
 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RabbitOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private Long orderNum;

    /**
     * 订单金额（分）
     */
    private Long orderMoney;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time",fill = FieldFill.INSERT)
    private Date payTime;

    /**
     * 0 未支付  1 已支付  2 已退款
     */
    private Integer payStatus;

    /**
     * 推送ERP状态
     */
    private Integer erpStatus;

    /**
     * 推送erp时间
     */
    private Date erpTime;

    /**
     * 0 未发送 1 已发送
     */
    private Integer isSms;

    /**
     * 订单更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 订单创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;


}
