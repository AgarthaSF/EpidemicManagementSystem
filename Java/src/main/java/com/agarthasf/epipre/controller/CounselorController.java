package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.ICounselorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-18
 */
@RestController
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private ICounselorService counselorService;


    @ApiOperation(value = "获取辅导员列表")
    @GetMapping("/list")
    public CommonResp counselorList(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<Counselor> respList = counselorService.getCounselorList(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "增加辅导员或更新辅导员信息")
    @PostMapping("/save")
    public CommonResp counselorSave(@RequestBody @Valid Counselor counselor) {
        CommonResp resp = new CommonResp();
        counselorService.counselorSave(counselor);
        return resp;
    }


}
