package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.mapper.AddressMapper;
import com.agarthasf.epipre.pojo.*;
import com.agarthasf.epipre.mapper.DiagnosedSurveyMapper;
import com.agarthasf.epipre.req.DiagnosedSurveyReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IDiagnosedSurveyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-24
 */
@Service
public class DiagnosedSurveyServiceImpl extends ServiceImpl<DiagnosedSurveyMapper, DiagnosedSurvey> implements IDiagnosedSurveyService {

    @Resource
    private DiagnosedSurveyMapper diagnosedSurveyMapper;

    @Resource
    private AddressMapper addressMapper;


    @Override
    public List<DiagnosedSurvey> getSurveyByCase(String caseId) {
        return diagnosedSurveyMapper.getSurveyList(caseId);
    }

    @Override
    public PageResp<DiagnosedSurvey> getAll(StaffQueryReq req) {
        List<DiagnosedSurvey> diagnosedSurveyList;

        if (req.getIdentity() == null) {
            diagnosedSurveyList = diagnosedSurveyMapper.getAll("");
        } else {
            diagnosedSurveyList = diagnosedSurveyMapper.getAll(req.getIdentity());
        }
        PageInfo<DiagnosedSurvey> pageInfo = new PageInfo<>(diagnosedSurveyList);
        PageResp<DiagnosedSurvey> pageResp = new PageResp();
        pageResp.setList(diagnosedSurveyList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;




    }

    @Override
    public void insertSurvey(DiagnosedSurveyReq req) {
        Address address = req.getAddress();
        DiagnosedSurvey diagnosedSurvey = req.getDiagnosedSurvey();

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
        diagnosedSurvey.setAddressId(addressId);
        if(diagnosedSurvey.getSurveyId() == null || ObjectUtils.isEmpty(diagnosedSurvey.getSurveyId())){
            diagnosedSurveyMapper.insert(diagnosedSurvey);
        }else{
            UpdateWrapper<DiagnosedSurvey> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("survey_id", diagnosedSurvey.getSurveyId());
            updateWrapper.set("arr_date", diagnosedSurvey.getArrDate());
            updateWrapper.set("address_id", diagnosedSurvey.getAddressId());
            diagnosedSurveyMapper.update(null, updateWrapper);
        }
    }

    @Override
    public void delete(String id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("survey_id", id);
        diagnosedSurveyMapper.delete(queryWrapper);
    }

}
