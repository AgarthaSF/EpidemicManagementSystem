package com.agarthasf.epipre.mapper;

import com.agarthasf.epipre.pojo.DiagnosedSurvey;
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
public interface DiagnosedSurveyMapper extends BaseMapper<DiagnosedSurvey> {

    List<DiagnosedSurvey> getSurveyList(String caseId);

    List<DiagnosedSurvey> getAll(String identity);
}
