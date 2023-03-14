package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.DailyCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-22
 */
public interface DailyCheckMapper extends BaseMapper<DailyCheck> {

    List<DailyCheck> getAllWithName(String identity);

    List<DailyCheck> getListByIdentity(String identity);

    List<DailyCheck> getAnomaly(String identity);
}
