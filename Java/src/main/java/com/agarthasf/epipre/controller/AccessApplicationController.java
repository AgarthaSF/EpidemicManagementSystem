package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.AccessApplication;
import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.pojo.SchoolStaff;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IAccessApplicationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-22
 */
@RestController
@RequestMapping("/access-application")
public class AccessApplicationController {

    @Autowired
    private IAccessApplicationService accessApplicationService;


    @ApiOperation(value = "获取指定用户的全部申请")
    @GetMapping("/list")
    public CommonResp list(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<AccessApplication> respList = accessApplicationService.getUserList(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "获取未处理的申请")
    @GetMapping("/todo")
    public CommonResp todo(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<AccessApplication> respList = accessApplicationService.getTodoList(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "获取已处理的访问申请")
    @GetMapping("/processed")
    public CommonResp processed(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<AccessApplication> respList = accessApplicationService.getProcessedList(req);
        resp.setContent(respList);
        return resp;
    }


    @ApiOperation(value = "增加申请或处理申请")
    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid AccessApplication accessApplication) throws ParseException {
        CommonResp resp = new CommonResp();
        accessApplicationService.accessApplicationSave(accessApplication);
        return resp;
    }

    @ApiOperation(value = "删除申请")
    @DeleteMapping("delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp resp = new CommonResp<>();
        accessApplicationService.delete(id);
        return resp;
    }

}
