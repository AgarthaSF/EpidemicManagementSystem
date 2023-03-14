package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.DiagnosedCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-24
 */
public interface DiagnosedCaseMapper extends BaseMapper<DiagnosedCase> {

    List<DiagnosedCase> getAll(String identity);

    List<DiagnosedCase> getSurveyDetail();

    List<DiagnosedCase> gerRisk();
}
