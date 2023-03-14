package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.DistrictEpidemic;
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
public interface DistrictEpidemicMapper extends BaseMapper<DistrictEpidemic> {

    List<DistrictEpidemic> getJoinResult(String identity);

}
