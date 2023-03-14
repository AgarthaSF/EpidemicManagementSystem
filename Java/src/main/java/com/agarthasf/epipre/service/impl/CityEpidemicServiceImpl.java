package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.exception.BusinessException;
import com.agarthasf.epipre.exception.BusinessExceptionCode;
import com.agarthasf.epipre.pojo.CityEpidemic;
import com.agarthasf.epipre.pojo.CityEpidemic;
import com.agarthasf.epipre.mapper.CityEpidemicMapper;
import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.service.ICityEpidemicService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
public class CityEpidemicServiceImpl extends ServiceImpl<CityEpidemicMapper, CityEpidemic> implements ICityEpidemicService {

    @Resource
    private CityEpidemicMapper cityEpidemicMapper;

    @Override
    public List<String> getCityList() {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.lt("city_adcode", 1000000);
        wrapper.gt("city_adcode", 0);
        List<CityEpidemic> cityEpidemicList = cityEpidemicMapper.selectList(wrapper);

        List<String> res = new ArrayList<>();
        for (int i = 0; i < cityEpidemicList.size(); i++) {
            res.add(cityEpidemicList.get(i).getCityName());
        }
        return res;
    }

    @Override
    public List<JSON> getRiskList(StaffQueryReq req) {

        List<CityEpidemic> cityEpidemicList = cityEpidemicMapper.getRiskList(req.getIdentity());

        List<JSON> children = new ArrayList<>();
        List<JSON> result = new ArrayList<>();

        HashMap<String, Object> currentCity = new HashMap<>();
        HashMap<String, Object> currentArea = new HashMap<>();

        boolean riskFlag = false;
        Integer currentParentAdcode;
        Integer beforeParentAdcode = cityEpidemicList.get(0).getCityAdcode();

        for (int i = 0; i < cityEpidemicList.size() - 1; i++) {

            // 若该城市无区数据则直接加入
            if (cityEpidemicList.get(i).getAreaRiskLevel() == null) {
                currentCity.put("adcode", cityEpidemicList.get(i).getCityAdcode());
                currentCity.put("name", cityEpidemicList.get(i).getCityName());
                currentCity.put("riskLevel", cityEpidemicList.get(i).getRiskLevel());
                result.add(new JSONObject(currentCity));
                currentCity = new HashMap<>();
            } else {

                // 在有子区的情况下，如果下一个城市不是当前城市则将children计入并重新清空
                if (!cityEpidemicList.get(i + 1).getCityAdcode().equals(cityEpidemicList.get(i).getCityAdcode())) {

                    currentArea.put("adcode", cityEpidemicList.get(i).getAreaId());
                    currentArea.put("name", cityEpidemicList.get(i).getAreaName());
                    currentArea.put("riskLevel", cityEpidemicList.get(i).getAreaRiskLevel());
                    children.add(new JSONObject(currentArea));

                    currentCity.put("adcode", cityEpidemicList.get(i).getCityAdcode());
                    currentCity.put("name", cityEpidemicList.get(i).getCityName());
                    currentCity.put("riskLevel", cityEpidemicList.get(i).getRiskLevel());
                    currentCity.put("children", children);
                    result.add(new JSONObject(currentCity));

                    currentArea = new HashMap<>();
                    currentCity = new HashMap<>();
                    children = new ArrayList<>();
                } else {
                    // 否则只加入城市
                    currentArea.put("adcode", cityEpidemicList.get(i).getAreaId());
                    currentArea.put("name", cityEpidemicList.get(i).getAreaName());
                    currentArea.put("riskLevel", cityEpidemicList.get(i).getAreaRiskLevel());
                    children.add(new JSONObject(currentArea));
                    currentArea = new HashMap<>();
                }
            }
        }

        int len = cityEpidemicList.size() - 1;

        if (cityEpidemicList.get(len).getAreaRiskLevel() == null){
            currentCity.put("adcode", cityEpidemicList.get(len).getCityAdcode());
            currentCity.put("name", cityEpidemicList.get(len).getCityName());
            currentCity.put("riskLevel", cityEpidemicList.get(len).getRiskLevel());
            result.add(new JSONObject(currentCity));
        }else{
            currentArea.put("adcode", cityEpidemicList.get(len).getAreaId());
            currentArea.put("name", cityEpidemicList.get(len).getAreaName());
            currentArea.put("riskLevel", cityEpidemicList.get(len).getAreaRiskLevel());
            children.add(new JSONObject(currentArea));

            currentCity.put("adcode", cityEpidemicList.get(len).getCityAdcode());
            currentCity.put("name", cityEpidemicList.get(len).getCityName());
            currentCity.put("riskLevel", cityEpidemicList.get(len).getRiskLevel());
            currentCity.put("children", children);
            result.add(new JSONObject(currentCity));

        }

        return result;
    }

    @Override
    public void saveCity(CityEpidemic cityEpidemic) {
        
        // 查询adcode是否已经存在于cityEpidemic表中，若不存在则为新数据
        CityEpidemic queryCity = cityEpidemicMapper.selectOne(
                new QueryWrapper<CityEpidemic>().eq("city_adcode", cityEpidemic.getCityAdcode()));

        // 若该编号存在于表中则进行更新
        if (!ObjectUtils.isEmpty(queryCity)) {
            UpdateWrapper<CityEpidemic> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("city_adcode", cityEpidemic.getCityAdcode());
            updateWrapper.set("city_name", cityEpidemic.getCityName());
            updateWrapper.set("current_confirmed_count", cityEpidemic.getCurrentConfirmedCount());
            updateWrapper.set("suspected_count", cityEpidemic.getSuspectedCount());
            updateWrapper.set("risk_level", cityEpidemic.getRiskLevel());
            updateWrapper.set("parent_adcode", cityEpidemic.getParentAdcode());
            cityEpidemicMapper.update(null, updateWrapper);

        } else if (ObjectUtils.isEmpty(queryCity)) {
            // 若编号不存在于表中则进行插入

            CityEpidemic newCityEpidemic = new CityEpidemic();
            newCityEpidemic.setCityAdcode(cityEpidemic.getCityAdcode());
            newCityEpidemic.setCityName(cityEpidemic.getCityName());
            newCityEpidemic.setCurrentConfirmedCount(cityEpidemic.getCurrentConfirmedCount());
            newCityEpidemic.setSuspectedCount(cityEpidemic.getSuspectedCount());
            newCityEpidemic.setRiskLevel(cityEpidemic.getRiskLevel());
            newCityEpidemic.setParentAdcode(cityEpidemic.getParentAdcode());

            cityEpidemicMapper.insert(newCityEpidemic);

        }
    }

    @Override
    public void delete(String id) {
        QueryWrapper deleteWrapper = new QueryWrapper();
        deleteWrapper.eq("city_adcode", id);
        cityEpidemicMapper.delete(deleteWrapper);
    }
}
