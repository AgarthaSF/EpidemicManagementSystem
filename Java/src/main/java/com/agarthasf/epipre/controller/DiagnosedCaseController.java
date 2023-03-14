package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.DiagnosedCase;
import com.agarthasf.epipre.pojo.DiagnosedSurvey;
import com.agarthasf.epipre.pojo.OutApplication;
import com.agarthasf.epipre.req.DiagnosedCaseReq;
import com.agarthasf.epipre.req.OutApplicationReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IDiagnosedCaseService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
@RequestMapping("/diagnosed-case")
public class DiagnosedCaseController {

    @Autowired
    IDiagnosedCaseService diagnosedCaseService;

    @ApiOperation("获取确诊病例确诊位置信息")
    @GetMapping("/all")
    public CommonResp all(StaffQueryReq req){
        CommonResp resp = new CommonResp();
        PageResp<DiagnosedCase> diagnosedCaseList = diagnosedCaseService.all(req);
        resp.setContent(diagnosedCaseList);
        return resp;
    }

    @ApiOperation("获取确诊病例及其流调信息")
    @GetMapping("/detail")
    public CommonResp detail(StaffQueryReq req){
        CommonResp resp = new CommonResp();
        List<JSON> diagnosedCaseList = diagnosedCaseService.detail(req);
        resp.setContent(diagnosedCaseList);
        return resp;
    }


    @ApiOperation(value = "获取密接异常信息")
    @GetMapping("/anomaly")
    public CommonResp getAnomalySurvey(StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        List<OutApplication> respList = diagnosedCaseService.getAnomaly(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation("获取仍有风险的确诊病例信息")
    @GetMapping("/risk")
    public CommonResp risk(){
        CommonResp resp = new CommonResp();
        List<DiagnosedCase> diagnosedCaseList = diagnosedCaseService.getRisk();
        resp.setContent(diagnosedCaseList);
        return resp;
    }

    @ApiOperation("只获取确诊病例信息")
    @GetMapping("/getCase")
    public CommonResp getCase(){
        CommonResp resp = new CommonResp();
        List<DiagnosedCase> diagnosedCaseList = diagnosedCaseService.getCase();
        resp.setContent(diagnosedCaseList);
        return resp;
    }

    @ApiOperation(value = "插入确诊病例")
    @PostMapping("/insert")
    public CommonResp insertCase(@Valid @RequestBody DiagnosedCaseReq req) {
        CommonResp resp = new CommonResp();
        diagnosedCaseService.insertCase(req);
        return resp;
    }

    @ApiOperation(value = "删除确诊病例")
    @DeleteMapping("delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp resp = new CommonResp<>();
        diagnosedCaseService.delete(id);
        return resp;
    }

    @ApiOperation("获取确诊病例ID")
    @GetMapping("/getIds")
    public CommonResp getIds(){
        CommonResp resp = new CommonResp();
        List<DiagnosedCase> diagnosedCaseList = diagnosedCaseService.list();
        List<Integer> res = new ArrayList<>();
        for(int i = 0 ;i  < diagnosedCaseList.size(); i++){
            res.add(diagnosedCaseList.get(i).getCaseId());
        }
        resp.setContent(res);
        return resp;
    }


}
