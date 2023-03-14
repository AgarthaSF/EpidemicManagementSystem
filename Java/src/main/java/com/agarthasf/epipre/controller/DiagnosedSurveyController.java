package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.AreaEpidemic;
import com.agarthasf.epipre.pojo.DiagnosedCase;
import com.agarthasf.epipre.pojo.DiagnosedSurvey;
import com.agarthasf.epipre.req.DiagnosedCaseReq;
import com.agarthasf.epipre.req.DiagnosedSurveyReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IDiagnosedSurveyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-24
 */
@RestController
@RequestMapping("/diagnosed-survey")
public class DiagnosedSurveyController {

    @Autowired
    private IDiagnosedSurveyService diagnosedSurveyService;


    @ApiOperation(value = "根据ID获取病例流调信息")
    @GetMapping("/list/{caseId}")
    public CommonResp getList(@PathVariable String caseId) {
        CommonResp resp = new CommonResp();
        List<DiagnosedSurvey> respList = diagnosedSurveyService.getSurveyByCase(caseId);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation("获取所有流调信息")
    @GetMapping("/all")
    public CommonResp all(StaffQueryReq req){
        CommonResp resp = new CommonResp();
        PageResp<DiagnosedSurvey> diagnosedCaseList = diagnosedSurveyService.getAll(req);
        resp.setContent(diagnosedCaseList);
        return resp;
    }

    @ApiOperation(value = "插入流调信息")
    @PostMapping("/insert")
    public CommonResp insertApplication(@Valid @RequestBody DiagnosedSurveyReq req) {
        CommonResp resp = new CommonResp();
        diagnosedSurveyService.insertSurvey(req);
        return resp;
    }

    @ApiOperation(value = "删除流调信息")
    @DeleteMapping("delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp resp = new CommonResp<>();
        diagnosedSurveyService.delete(id);
        return resp;
    }

}
