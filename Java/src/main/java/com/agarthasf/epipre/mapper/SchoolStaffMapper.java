package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.SchoolStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-18
 */
public interface SchoolStaffMapper extends BaseMapper<SchoolStaff> {

    List<SchoolStaff> getStudentListWithCounselorName(String identity);

}
