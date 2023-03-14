package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.pojo.DistrictEpidemic;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IDistrictEpidemicService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-20
 */
@RestController
@RequestMapping("/district-epidemic")
public class DistrictEpidemicController {

    @Autowired
    private IDistrictEpidemicService districtEpidemicService;


    @ApiOperation(value="获取行政区及其子城市")
    @GetMapping("/list")
    public CommonResp epidemicList(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        List<JSON> respList = districtEpidemicService.getEpidemicList(req);
        resp.setContent(respList);
        return resp;
    }




}
