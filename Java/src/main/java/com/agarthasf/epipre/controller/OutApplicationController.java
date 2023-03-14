package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.OutApplication;
import com.agarthasf.epipre.pojo.SchoolStaff;
import com.agarthasf.epipre.req.OutApplicationReq;
import com.agarthasf.epipre.req.OutApplicationSaveReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IOutApplicationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
@RestController
@RequestMapping("/out-application")
public class OutApplicationController {

    @Autowired
    private IOutApplicationService outApplicationService;

    @ApiOperation(value = "获取待处理的申请列表")
    @GetMapping("/todo")
    public CommonResp getTodo(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<OutApplication> respList = outApplicationService.getTodo(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "获取历史申请列表")
    @GetMapping("/history")
    public CommonResp getHistory(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<OutApplication> respList = outApplicationService.getHistory(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "指定ID获取所有申请")
    @GetMapping("/list")
    public CommonResp getListById(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<OutApplication> respList = outApplicationService.getListById(req);
        resp.setContent(respList);
        return resp;
    }


    @ApiOperation(value = "插入申请")
    @PostMapping("/insert")
    public CommonResp insertApplication(@Valid @RequestBody OutApplicationReq req) {
        CommonResp resp = new CommonResp();
        outApplicationService.insertApplication(req);
        return resp;
    }

    @ApiOperation(value = "更新申请")
    @PostMapping("/save")
    public CommonResp saveApplication(@RequestBody OutApplicationSaveReq req) {
        CommonResp resp = new CommonResp();
        outApplicationService.saveApplication(req);
        return resp;
    }

    @ApiOperation(value = "删除外出申请")
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp resp = new CommonResp<>();
        outApplicationService.delete(id);
        return resp;
    }





}
