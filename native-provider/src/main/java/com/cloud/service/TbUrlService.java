package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.entity.TbUrl;
import com.cloud.mapper.TbUrlMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yzj
 * @since 2020-01-09
 */
@Service
public class TbUrlService extends ServiceImpl<TbUrlMapper, TbUrl> {

    @Resource
    private TbUrlMapper tbUrlMapper;

}
