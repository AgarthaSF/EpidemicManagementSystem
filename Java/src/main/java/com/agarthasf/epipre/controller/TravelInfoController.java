package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.TravelInfo;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.req.TravelInfoInsertReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.resp.TravelAnomalyResp;
import com.agarthasf.epipre.service.ITravelInfoService;
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
 * @since 2022-06-23
 */
@RestController
@RequestMapping("/travel-info")
public class TravelInfoController {

    @Autowired
    private ITravelInfoService travelInfoService;


    @ApiOperation(value = "记录14日行程信息")
    @PostMapping("/save")
    public CommonResp travelInfoSave(@Valid @RequestBody TravelInfoInsertReq req) {
        CommonResp resp = new CommonResp();
        travelInfoService.travelInfoSave(req);
        return resp;
    }

    @ApiOperation(value = "获取校内人员异常流调信息")
    @GetMapping("/anomaly/school")
    public CommonResp SchoolAnomaly(StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp respList = travelInfoService.getSchoolAnomaly(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "获取校外人员异常流调信息")
    @GetMapping("/anomaly/foreign")
    public CommonResp ForeignAnomaly(StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp respList = travelInfoService.getForeignAnomaly(req);
        resp.setContent(respList);
        return resp;
    }


}
