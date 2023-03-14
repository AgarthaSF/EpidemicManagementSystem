package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.CityEpidemic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-20
 */
public interface CityEpidemicMapper extends BaseMapper<CityEpidemic> {


    List<CityEpidemic> getRiskList(String identity);
}
