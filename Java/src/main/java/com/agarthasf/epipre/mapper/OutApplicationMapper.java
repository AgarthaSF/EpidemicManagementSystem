package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.OutApplication;
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
public interface OutApplicationMapper extends BaseMapper<OutApplication> {

    List<OutApplication> getTodoList(String identity, String counselorIdentity);

    List<OutApplication> getHistory(String identity, String counselorIdentity);

    List<OutApplication> getListById(String identity);

    List<OutApplication> getAllRecently(String identity);
}
