package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.DiagnosedCase;
import com.agarthasf.epipre.pojo.OutApplication;
import com.agarthasf.epipre.req.DiagnosedCaseReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.alibaba.fastjson.JSON;
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
public interface IDiagnosedCaseService extends IService<DiagnosedCase> {

    PageResp<DiagnosedCase> all(StaffQueryReq req);

    List<DiagnosedCase> getRisk();

    List<JSON> detail(StaffQueryReq req);

    List<OutApplication> getAnomaly(StaffQueryReq req);

    List<DiagnosedCase> getCase();

    void insertCase(DiagnosedCaseReq req);

    void delete(String id);

    List<DiagnosedCase> getIds();
}
