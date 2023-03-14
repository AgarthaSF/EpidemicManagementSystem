package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.AccessApplication;
import com.agarthasf.epipre.pojo.DailyCheck;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IDailyCheckService;
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
@RequestMapping("/daily-check")
public class DailyCheckController {

    @Autowired
    private IDailyCheckService dailyCheckService;


    @ApiOperation(value = "获取指定用户的全部打卡")
    @GetMapping("/list")
    public CommonResp list(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<DailyCheck> respList = dailyCheckService.getList(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "获取所有打卡记录")
    @GetMapping("/all")
    public CommonResp all(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<DailyCheck> respList = dailyCheckService.getAll(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "提交打卡记录")
    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid DailyCheck dailyCheck) {
        CommonResp resp = new CommonResp();
        dailyCheckService.saveCheck(dailyCheck);
        return resp;
    }

    @ApiOperation(value = "获取异常打卡记录")
    @GetMapping("/anomaly")
    public CommonResp anomaly(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<DailyCheck> respList = dailyCheckService.getAnomaly(req);
        resp.setContent(respList);
        return resp;
    }

}
