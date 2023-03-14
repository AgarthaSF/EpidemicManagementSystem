package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.AreaEpidemic;
import com.agarthasf.epipre.pojo.DiagnosedSurvey;
import com.agarthasf.epipre.req.DiagnosedSurveyReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-24
 */
public interface IDiagnosedSurveyService extends IService<DiagnosedSurvey> {

    List<DiagnosedSurvey> getSurveyByCase(String caseId);

    PageResp<DiagnosedSurvey> getAll(StaffQueryReq req);

    void insertSurvey(DiagnosedSurveyReq req);

    void delete(String id);

    ;
}
