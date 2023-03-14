package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.pojo.DailyCheck;
import com.agarthasf.epipre.mapper.DailyCheckMapper;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IDailyCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-22
 */
@Service
public class DailyCheckServiceImpl extends ServiceImpl<DailyCheckMapper, DailyCheck> implements IDailyCheckService {

    @Resource
    private DailyCheckMapper dailyCheckMapper;

    @Override
    public PageResp<DailyCheck> getList(StaffQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyCheck> checkList = dailyCheckMapper.getListByIdentity(req.getIdentity());
        PageInfo<DailyCheck> pageInfo = new PageInfo<>(checkList);
        PageResp<DailyCheck> pageResp = new PageResp();
        pageResp.setList(checkList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }


    @Override
    public PageResp<DailyCheck> getAll(StaffQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyCheck> checkList = dailyCheckMapper.getAllWithName(req.getIdentity());
        PageInfo<DailyCheck> pageInfo = new PageInfo<>(checkList);
        PageResp<DailyCheck> pageResp = new PageResp();
        pageResp.setList(checkList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void saveCheck(DailyCheck dailyCheck) {
        // 每日打卡不存在编辑，故直接插入即可
        dailyCheckMapper.insert(dailyCheck);
    }

    @Override
    public PageResp<DailyCheck> getAnomaly(StaffQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyCheck> dailyCheckList = dailyCheckMapper.getAnomaly(req.getIdentity());
        PageInfo<DailyCheck> pageInfo = new PageInfo<>(dailyCheckList);
        PageResp<DailyCheck> pageResp = new PageResp();
        pageResp.setList(dailyCheckList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

}
