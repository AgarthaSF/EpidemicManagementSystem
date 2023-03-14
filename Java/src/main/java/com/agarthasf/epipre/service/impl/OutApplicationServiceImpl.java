package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.exception.BusinessException;
import com.agarthasf.epipre.exception.BusinessExceptionCode;
import com.agarthasf.epipre.mapper.AddressMapper;
import com.agarthasf.epipre.pojo.*;
import com.agarthasf.epipre.mapper.OutApplicationMapper;
import com.agarthasf.epipre.req.OutApplicationReq;
import com.agarthasf.epipre.req.OutApplicationSaveReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IOutApplicationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
@Service
public class OutApplicationServiceImpl extends ServiceImpl<OutApplicationMapper, OutApplication> implements IOutApplicationService {


    @Resource
    private OutApplicationMapper outApplicationMapper;

    @Resource
    private AddressMapper addressMapper;


    @Override
    public PageResp<OutApplication> getTodo(StaffQueryReq req) {
        List<OutApplication> cityEpidemicList;
        if (req.getIdentity() == null) {
            cityEpidemicList = outApplicationMapper.getTodoList("", req.getCounselorIdentity());
        } else {
            cityEpidemicList = outApplicationMapper.getTodoList(req.getIdentity(), req.getCounselorIdentity());
        }
        PageInfo<OutApplication> pageInfo = new PageInfo<>(cityEpidemicList);
        PageResp<OutApplication> pageResp = new PageResp();
        pageResp.setList(cityEpidemicList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public PageResp<OutApplication> getHistory(StaffQueryReq req) {
        List<OutApplication> cityEpidemicList;
        if (req.getIdentity() == null) {
            cityEpidemicList = outApplicationMapper.getHistory("", req.getCounselorIdentity());
        } else {
            cityEpidemicList = outApplicationMapper.getHistory(req.getIdentity(), req.getCounselorIdentity());
        }
        PageInfo<OutApplication> pageInfo = new PageInfo<>(cityEpidemicList);
        PageResp<OutApplication> pageResp = new PageResp();
        pageResp.setList(cityEpidemicList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void insertApplication(OutApplicationReq req) {

        if (req.getOutApplication().getReason().isEmpty()) {
            throw new BusinessException(BusinessExceptionCode.ATTRIBUTE_EMPTY);
        }

        if(Duration.between(req.getOutApplication().getOutTime(),
                req.getOutApplication().getBackTime()).toMillis() < 0){
            throw new BusinessException(BusinessExceptionCode.APPLICATION_TIME_ERROR);
        }


        Address address = req.getAddress();
        OutApplication outApplication = req.getOutApplication();

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

        // 再根据地址ID插入该条外出申请

        outApplication.setOutAddressId(addressId);
        outApplicationMapper.insert(outApplication);
    }


    @Override
    public PageResp<OutApplication> getListById(StaffQueryReq req) {
        QueryWrapper wrapper = new QueryWrapper();
        List<OutApplication> cityEpidemicList = outApplicationMapper.getListById(req.getIdentity());
        PageInfo<OutApplication> pageInfo = new PageInfo<>(cityEpidemicList);
        PageResp<OutApplication> pageResp = new PageResp();
        pageResp.setList(cityEpidemicList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void saveApplication(OutApplicationSaveReq req) {
        UpdateWrapper<OutApplication> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("application_id", req.getApplicationId());
        updateWrapper.set("application_state", req.getApplicationState());
        outApplicationMapper.update(null, updateWrapper);
    }

    @Override
    public List<OutApplication> getAllRecently() {
        return outApplicationMapper.getAllRecently("");
    }

    @Override
    public void delete(String id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("application_id", id);
        outApplicationMapper.delete(queryWrapper);
    }
}
