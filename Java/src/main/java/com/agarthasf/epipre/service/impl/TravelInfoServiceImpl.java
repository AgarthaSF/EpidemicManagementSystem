package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.mapper.TravelInfoMapper;
import com.agarthasf.epipre.pojo.Announcement;
import com.agarthasf.epipre.pojo.TravelInfo;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.req.TravelInfoInsertReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.resp.TravelAnomalyResp;
import com.agarthasf.epipre.service.ITravelInfoService;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
@Service
public class TravelInfoServiceImpl extends ServiceImpl<TravelInfoMapper, TravelInfo> implements ITravelInfoService {

    @Resource
    private TravelInfoMapper travelInfoMapper;

    @Override
    public void travelInfoSave(TravelInfoInsertReq req) {

        String identity = req.getIdentity();
        LocalDateTime now = LocalDateTime.now();

        // 首先删除旧的流调信息

        QueryWrapper deleteWrapper = new QueryWrapper();
        deleteWrapper.eq("identity", identity);
        travelInfoMapper.delete(deleteWrapper);

        // 再插入新的流调信息
        List<String> cityList = req.getCityList();
        for(int i = 0 ; i < cityList.size(); i++){
            TravelInfo travelInfo = new TravelInfo();
            travelInfo.setIdentity(identity);
            travelInfo.setCityName(cityList.get(i));
            travelInfo.setTravelDate(now);
            travelInfoMapper.insert(travelInfo);
        }
    }

    @Override
    public PageResp<TravelAnomalyResp> getSchoolAnomaly(StaffQueryReq req) {
//        PageHelper.startPage(req.getPage(), req.getSize());
        List<TravelAnomalyResp> anomalyList =  travelInfoMapper.getSchoolAnomaly(req.getIdentity());
        return processResult(anomalyList);
    }

    @Override
    public PageResp<TravelAnomalyResp> getForeignAnomaly(StaffQueryReq req) {
//        PageHelper.startPage(req.getPage(), req.getSize());
        List<TravelAnomalyResp> anomalyList =  travelInfoMapper.getForeignAnomaly(req.getIdentity());
        return processResult(anomalyList);
    }

    public PageResp<TravelAnomalyResp> processResult(List<TravelAnomalyResp> anomalyList){
        if(anomalyList.size() != 0){
            anomalyList =  getResult(anomalyList);
        }
        PageInfo<TravelAnomalyResp> pageInfo = new PageInfo<>(anomalyList);
        PageResp<TravelAnomalyResp> pageResp = new PageResp();
        pageResp.setList(anomalyList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }


    public List<TravelAnomalyResp> getResult(List<TravelAnomalyResp> anomalyList){
        List<TravelAnomalyResp> respList = new ArrayList<>();
        TravelAnomalyResp current = new TravelAnomalyResp();
        List<String> cityList = new ArrayList<>();


        String currentIdentity = anomalyList.get(0).getIdentity();


        for(int i = 0 ; i < anomalyList.size(); i++){
            if(currentIdentity.equals(anomalyList.get(i).getIdentity())){
                cityList.add(anomalyList.get(i).getCityName());
            }else{
                currentIdentity = anomalyList.get(i).getIdentity();
                current.setName(anomalyList.get(i-1).getName());
                current.setIdentity(anomalyList.get(i-1).getIdentity());
                current.setType(anomalyList.get(i-1).getType());
                current.setPhoneNumber(anomalyList.get(i-1).getPhoneNumber());
                current.setCityList(cityList);
                respList.add(current);
                current = new TravelAnomalyResp();
                cityList = new ArrayList<>();
                cityList.add(anomalyList.get(i).getCityName());
            }
        }
        current.setName(anomalyList.get(anomalyList.size() - 1).getName());
        current.setIdentity(anomalyList.get(anomalyList.size() - 1).getIdentity());
        current.setType(anomalyList.get(anomalyList.size() - 1).getType());
        current.setPhoneNumber(anomalyList.get(anomalyList.size() - 1).getPhoneNumber());
        current.setCityList(cityList);
        respList.add(current);

        return respList;
    }





}
