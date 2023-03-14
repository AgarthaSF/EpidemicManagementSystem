package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.TravelInfo;
import com.agarthasf.epipre.resp.TravelAnomalyResp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
public interface TravelInfoMapper extends BaseMapper<TravelInfo> {

    List<TravelAnomalyResp> getSchoolAnomaly(String identity);

    List<TravelAnomalyResp> getForeignAnomaly(String identity);
}
