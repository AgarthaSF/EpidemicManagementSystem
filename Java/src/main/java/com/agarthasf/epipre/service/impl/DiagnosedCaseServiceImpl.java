package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.mapper.AddressMapper;
import com.agarthasf.epipre.mapper.DiagnosedSurveyMapper;
import com.agarthasf.epipre.mapper.OutApplicationMapper;
import com.agarthasf.epipre.pojo.*;
import com.agarthasf.epipre.mapper.DiagnosedCaseMapper;
import com.agarthasf.epipre.req.DiagnosedCaseReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IDiagnosedCaseService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-24
 */
@Service
public class DiagnosedCaseServiceImpl extends ServiceImpl<DiagnosedCaseMapper, DiagnosedCase> implements IDiagnosedCaseService {

    @Resource
    private DiagnosedCaseMapper diagnosedCaseMapper;

    @Resource
    private DiagnosedSurveyMapper diagnosedSurveyMapper;

    @Resource
    private OutApplicationMapper outApplicationMapper;

    @Resource
    private AddressMapper addressMapper;


    @Override
    public PageResp<DiagnosedCase> all(StaffQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DiagnosedCase> diagnosedCaseList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(req.getIdentity())) {
            diagnosedCaseList = diagnosedCaseMapper.getAll(req.getIdentity());
        } else {
            diagnosedCaseList = diagnosedCaseMapper.getAll("");
        }
        PageInfo<DiagnosedCase> pageInfo = new PageInfo<>(diagnosedCaseList);
        PageResp<DiagnosedCase> pageResp = new PageResp();
        pageResp.setList(diagnosedCaseList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public List<DiagnosedCase> getRisk() {
        return diagnosedCaseMapper.gerRisk();
    }

    @Override
    public List<JSON> detail(StaffQueryReq req) {


        List<DiagnosedCase> diagnosedCaseList = diagnosedCaseMapper.getAll(req.getIdentity());

        List<JSON> children = new ArrayList<>();
        List<JSON> result = new ArrayList<>();

        HashMap<String, Object> currentCase = new HashMap<>();
        HashMap<String, Object> currentSurvey = new HashMap<>();


        for (int i = 0; i < diagnosedCaseList.size(); i++) {
            List<DiagnosedSurvey> diagnosedSurveyList = diagnosedSurveyMapper.
                    getSurveyList(diagnosedCaseList.get(i).getCaseId().toString());

            children = new ArrayList<>();
            for (int j = 0; j < diagnosedSurveyList.size(); j++) {
                currentSurvey.put("caseId", diagnosedCaseList.get(i).getCaseId());
                currentSurvey.put("addressName", diagnosedSurveyList.get(j).getAddressName());
                currentSurvey.put("addressPosition", diagnosedSurveyList.get(j).getAddressPosition());
                currentSurvey.put("arrDate", diagnosedSurveyList.get(j).getArrDate());
                children.add(new JSONObject(currentSurvey));
                currentSurvey = new HashMap<>();
            }

            currentCase.put("caseId", diagnosedCaseList.get(i).getCaseId());
            currentCase.put("addressName", diagnosedCaseList.get(i).getAddressName());
            currentCase.put("addressPosition", diagnosedCaseList.get(i).getAddressPosition());
            currentCase.put("arrDate", diagnosedCaseList.get(i).getDiagnosedDate());
            currentCase.put("curedState", diagnosedCaseList.get(i).getCuredState());
            currentCase.put("children", children);
            result.add(new JSONObject(currentCase));
            currentCase = new HashMap<>();
        }

        return result;
    }

    @Override
    public List<OutApplication> getAnomaly(StaffQueryReq req) {

        // 获取包含具体信息的流调列表
        List<DiagnosedCase> diagnosedCaseList = diagnosedCaseMapper.getSurveyDetail();

        List<OutApplication> outApplicationList;
        // 获取学生近14日的外出申请列表
        if(req.getIdentity() == null){
            outApplicationList = outApplicationMapper.getAllRecently("");
        }else{
            outApplicationList = outApplicationMapper.getAllRecently(req.getIdentity());
        }


        HashMap<DiagnosedCase, List<OutApplication>> mightyList = new HashMap<>();

        List<OutApplication> children = new ArrayList<>();

        // 寻找时间上有重叠的外出记录，即外出时间早于流调时间，返回时间迟于流调时间
        for(int i =0  ; i < diagnosedCaseList.size(); i++){
            LocalDateTime arrDate = diagnosedCaseList.get(i).getArrDate();

            for(int j = 0; j < outApplicationList.size(); j++){
                LocalDateTime outTime = outApplicationList.get(j).getOutTime();
                LocalDateTime backTime = outApplicationList.get(j).getBackTime();
                if(Duration.between(outTime, arrDate).toMillis() > 0 && Duration.between(arrDate, backTime).toMillis() > 0){
                    children.add(outApplicationList.get(j));
                }
            }
            if(children.size() != 0){
                mightyList.put(diagnosedCaseList.get(i), children);
                children = new ArrayList<>();
            }
        }

        // 寻找地点上有重叠的外出记录
        List<OutApplication> anomalyList = new ArrayList<>();

        Double sqrtRoot2 = Math.sqrt(2);
        for (Map.Entry<DiagnosedCase, List<OutApplication>> entry : mightyList.entrySet()) {
            DiagnosedCase diagnosedCase = entry.getKey();
            List<OutApplication> outList = entry.getValue();

            // 两点距离在 800m x 800m的网格内即认为为时空伴随，即两点距离在 800 * √2 以内
            for(int i = 0 ;  i < outList.size(); i++){

                Double risk2out = GetDistance(diagnosedCase.getLongitude(), diagnosedCase.getLatitude(),
                        outList.get(i).getLongitude(), outList.get(i).getLatitude());
                if(risk2out < 800 * sqrtRoot2){
                    OutApplication anomalyApplication = new OutApplication();
                    anomalyApplication.setStudentIdentity(outList.get(i).getStudentIdentity());
                    anomalyApplication.setName(outList.get(i).getName());
                    anomalyApplication.setPhoneNumber(outList.get(i).getPhoneNumber());

                    anomalyApplication.setAddressName(outList.get(i).getAddressName());
                    anomalyApplication.setOutTime(outList.get(i).getOutTime());
                    anomalyApplication.setBackTime(outList.get(i).getBackTime());

                    // 设置流调地点
                    anomalyApplication.setSurveyAddressName(diagnosedCase.getAddressName());
                    anomalyApplication.setCaseId(diagnosedCase.getCaseId());
                    anomalyApplication.setSurveyDate(diagnosedCase.getArrDate());
                    anomalyApplication.setDistance(risk2out);

                    anomalyList.add(anomalyApplication);
                }
            }
        }


        return anomalyList;
    }

    @Override
    public List<DiagnosedCase> getCase() {
        return null;
    }

    @Override
    public void insertCase(DiagnosedCaseReq req) {


        Address address = req.getAddress();
        DiagnosedCase diagnosedCase = req.getDiagnosedCase();

        // 首先查询该地址是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("address_name", address.getAddressName());
        Address queryAddr = addressMapper.selectOne(queryWrapper);

        int addressId = 0;
        // 如果不存在则插入并获取ID，存在则直接获取地址ID
        if (queryAddr == null) {
            addressMapper.insert(address);
            addressId = address.getAddressId();
        } else {
            addressId = queryAddr.getAddressId();
        }

        // 再根据地址ID插入该条外出申请或更新位置

        diagnosedCase.setDiagnosedAddressId(addressId);
        if(diagnosedCase.getCaseId() == null || ObjectUtils.isEmpty(diagnosedCase.getCaseId())){
            diagnosedCaseMapper.insert(diagnosedCase);
        }else{
            UpdateWrapper<DiagnosedCase> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("case_id", diagnosedCase.getCaseId());
            updateWrapper.set("diagnosed_date", diagnosedCase.getDiagnosedDate());
            updateWrapper.set("diagnosed_address_id", diagnosedCase.getDiagnosedAddressId());
            updateWrapper.set("cured_state", diagnosedCase.getCuredState());
            diagnosedCaseMapper.update(null, updateWrapper);
        }

    }

    @Override
    public void delete(String id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("case_id", id);
        diagnosedCaseMapper.delete(queryWrapper);
    }

    @Override
    public List<DiagnosedCase> getIds() {


        return null;
    }


    /**
     * 默认地球半径
     */
    private static double EARTH_RADIUS = 6371000;//赤道半径(单位m)

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    /**
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     * */
    public static double GetDistance(double lon1,double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

}
