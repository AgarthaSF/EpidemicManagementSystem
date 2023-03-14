package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.CityEpidemic;
import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.service.ICityEpidemicService;
import com.alibaba.fastjson.JSON;
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
 * @since 2022-06-20
 */
@RestController
@RequestMapping("/city-epidemic")
public class CityEpidemicController {

    @Autowired
    ICityEpidemicService cityEpidemicService;

    @ApiOperation(value="获取所有的城市疫情信息")
    @GetMapping("/list")
    public CommonResp list(){
        CommonResp resp = new CommonResp();
        List<CityEpidemic> cityEpidemicList = cityEpidemicService.list();
        resp.setContent(cityEpidemicList);
        return resp;
    }


    @ApiOperation(value="获得城市名列表")
    @GetMapping("/name")
    public CommonResp getCityList(){
        CommonResp resp = new CommonResp();
        List<String> cityList = cityEpidemicService.getCityList();
        resp.setContent(cityList);
        return resp;
    }


    @ApiOperation(value="获取风险地区列表")
    @GetMapping("/risk")
    public CommonResp getRiskList(StaffQueryReq req){
        CommonResp resp = new CommonResp();
        List<JSON> riskList = cityEpidemicService.getRiskList(req);
        resp.setContent(riskList);
        return resp;
    }

    @ApiOperation(value = "增加城市疫情或更新城市疫情信息")
    @PostMapping("/save")
    public CommonResp cityEpidemicSave(@RequestBody @Valid CityEpidemic cityEpidemic) {
        CommonResp resp = new CommonResp();
        cityEpidemicService.saveCity(cityEpidemic);
        return resp;
    }

    @ApiOperation(value = "删除城市疫情")
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp resp = new CommonResp<>();
        cityEpidemicService.delete(id);
        return resp;
    }

}
