package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.pojo.DistrictEpidemic;
import com.agarthasf.epipre.mapper.DistrictEpidemicMapper;
import com.agarthasf.epipre.pojo.SchoolStaff;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IDistrictEpidemicService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-20
 */
@Service
public class DistrictEpidemicServiceImpl extends ServiceImpl<DistrictEpidemicMapper, DistrictEpidemic> implements IDistrictEpidemicService {

    @Resource
    private DistrictEpidemicMapper districtEpidemicMapper;


    @Override
    public List<JSON> getEpidemicList(StaffQueryReq req) {

//        PageHelper.startPage(req.getPage(), req.getSize());
        List<DistrictEpidemic> epidemicList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(req.getIdentity())) {
            epidemicList = districtEpidemicMapper.getJoinResult(req.getIdentity());
        } else {
            epidemicList = districtEpidemicMapper.getJoinResult("");
        }

        if(epidemicList.size() == 0){
            return null;
        }
        return epidemicMap2JSON(epidemicList);

//        PageInfo<DistrictEpidemic> pageInfo = new PageInfo<>(epidemicList);
//        PageResp<DistrictEpidemic> pageResp = new PageResp();
//        pageResp.setList(epidemicList);
//        pageResp.setTotal((int) pageInfo.getTotal());
//        return pageResp;
    }


    public List<JSON> epidemicMap2JSON(List<DistrictEpidemic> epidemicList) {

        List<JSON> children = new ArrayList<>();
        List<JSON> result = new ArrayList<>();

        HashMap<String, Object> currentDistrict = new HashMap<>();
        HashMap<String, Object> currentCity = new HashMap<>();


        currentDistrict.put("adcode", epidemicList.get(0).getDistrictAdcode());
        Integer beforeParentAdcode = epidemicList.get(0).getDistrictAdcode();

        Integer currentParentAdcode;

        for (int i = 0; i < epidemicList.size(); i++) {

            currentParentAdcode = epidemicList.get(i).getDistrictAdcode();

            if (currentParentAdcode.equals(beforeParentAdcode)) {

                currentCity.put("adcode", epidemicList.get(i).getCityAdCode().toString());
                currentCity.put("name", epidemicList.get(i).getCityName());
                currentCity.put("currentConfirmedCount", epidemicList.get(i).getCityCurrentConfirmedCount());
                currentCity.put("suspectedCount", epidemicList.get(i).getCitySuspectedCount());
                currentCity.put("riskLevel", epidemicList.get(i).getCityRiskLevel());
                currentCity.put("parentAdcode", epidemicList.get(i).getCityParentAdcode());
                children.add(new JSONObject(currentCity));
                currentCity = new HashMap<>();

            } else {
                beforeParentAdcode = epidemicList.get(i).getDistrictAdcode();
                currentDistrict.put("adcode", epidemicList.get(i - 1).getDistrictAdcode().toString());
                currentDistrict.put("name", epidemicList.get(i - 1).getDistrictName());
                currentDistrict.put("currentConfirmedCount", epidemicList.get(i - 1).getCurrentConfirmedCount());
                currentDistrict.put("suspectedCount", epidemicList.get(i - 1).getSuspectedCount());
                currentDistrict.put("children", children);

                result.add(new JSONObject(currentDistrict));
                currentDistrict = new HashMap<>();
                children = new ArrayList<>();

                currentCity.put("adcode", epidemicList.get(i).getCityAdCode());
                currentCity.put("name", epidemicList.get(i).getCityName());
                currentCity.put("currentConfirmedCount", epidemicList.get(i).getCityCurrentConfirmedCount());
                currentCity.put("suspectedCount", epidemicList.get(i).getCitySuspectedCount());
                currentCity.put("riskLevel", epidemicList.get(i).getCityRiskLevel());
                currentCity.put("parentAdcode", epidemicList.get(i).getCityParentAdcode());
                children.add(new JSONObject(currentCity));
                currentCity = new HashMap<>();
            }
        }

        currentDistrict.put("adcode", epidemicList.get(epidemicList.size() - 1).getDistrictAdcode());
        currentDistrict.put("name", epidemicList.get(epidemicList.size() - 1).getDistrictName());
        currentDistrict.put("currentConfirmedCount", epidemicList.get(epidemicList.size() - 1).getCurrentConfirmedCount());
        currentDistrict.put("suspectedCount", epidemicList.get(epidemicList.size() - 1).getSuspectedCount());
        currentDistrict.put("children", children);

        result.add(new JSONObject(currentDistrict));
        return result;
    }


}
