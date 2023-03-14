package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.InfoUpload;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IInfoUploadService;
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
@RequestMapping("/info-upload")
public class InfoUploadController {

    @Autowired
    private IInfoUploadService infoUploadService;


    @ApiOperation(value = "获取指定用户的信息上报")
    @GetMapping("/list")
    public CommonResp list(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<InfoUpload> respList = infoUploadService.getList(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "获取所有的上报记录")
    @GetMapping("/all")
    public CommonResp all(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<InfoUpload> respList = infoUploadService.getAll(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "获取异常上报记录")
    @GetMapping("/anomaly")
    public CommonResp anomaly(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<InfoUpload> respList = infoUploadService.getAnomaly(req);
        resp.setContent(respList);
        return resp;
    }


    @ApiOperation(value = "提交上报记录")
    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid InfoUpload infoUpload) {
        CommonResp resp = new CommonResp();
        infoUploadService.saveInfoUpload(infoUpload);
        return resp;
    }
    
}
