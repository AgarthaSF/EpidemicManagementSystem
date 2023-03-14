package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.pojo.AreaEpidemic;
import com.agarthasf.epipre.mapper.AreaEpidemicMapper;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IAreaEpidemicService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.geom.Area;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-24
 */
@Service
public class AreaEpidemicServiceImpl extends ServiceImpl<AreaEpidemicMapper, AreaEpidemic> implements IAreaEpidemicService {

    @Resource
    private AreaEpidemicMapper areaEpidemicMapper;


    @Override
    public List<AreaEpidemic> getAreaByCityId(String belongingCity) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("belonging_city", belongingCity);
        return areaEpidemicMapper.selectList(queryWrapper);

    }
}
