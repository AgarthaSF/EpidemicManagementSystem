package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.AreaEpidemic;
import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IAreaEpidemicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * @since 2022-06-24
 */
@RestController
@RequestMapping("/area-epidemic")
public class AreaEpidemicController {


    @Autowired
    private IAreaEpidemicService areaEpidemicService;

    @ApiOperation(value = "获取城市区信息")
    @GetMapping("/list/{belongingCity}")
    public CommonResp getAreaByCityId(@PathVariable String belongingCity) {
        CommonResp resp = new CommonResp();
        List<AreaEpidemic> respList = areaEpidemicService.getAreaByCityId(belongingCity);
        resp.setContent(respList);
        return resp;
    }



}
