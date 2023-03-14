package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.pojo.ForeignStaff;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IForeignStaffService;
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
 * @since 2022-06-21
 */
@RestController
@RequestMapping("/foreign-staff")
public class ForeignStaffController {

    @Autowired
    private IForeignStaffService foreignStaffService;

    @ApiOperation(value = "获取外来人员列表")
    @GetMapping("/list")
    public CommonResp list(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<ForeignStaff> respList = foreignStaffService.getForeignerList(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "增加外来人员或更新外来人员信息")
    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid ForeignStaff foreignStaff) {
        CommonResp resp = new CommonResp();
        foreignStaffService.foreignStaffSave(foreignStaff);
        return resp;
    }

    @ApiOperation(value = "外来人员注册")
    @PostMapping("/register")
    public CommonResp register(@RequestBody @Valid ForeignStaff foreignStaff) {
        CommonResp resp = new CommonResp();
        String newIdentity = foreignStaffService.register(foreignStaff);
        resp.setMessage("您的编号为" + newIdentity + "，请妥善保存");
        resp.setContent(newIdentity);
        return resp;
    }

    @ApiOperation(value = "获取应插入的访问人员的ID编码")
    @GetMapping("/newIdentity")
    public CommonResp newIdentity() {
        CommonResp resp = new CommonResp();
        String identity = foreignStaffService.getNewIdnetity();
        resp.setContent(identity);
        return resp;
    }

}
