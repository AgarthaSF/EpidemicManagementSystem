package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.exception.BusinessException;
import com.agarthasf.epipre.exception.BusinessExceptionCode;
import com.agarthasf.epipre.pojo.AccessApplication;
import com.agarthasf.epipre.mapper.AccessApplicationMapper;
import com.agarthasf.epipre.pojo.AccessApplication;
import com.agarthasf.epipre.pojo.AccessApplication;
import com.agarthasf.epipre.pojo.AccessApplication;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IAccessApplicationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.jni.Time;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-22
 */
@Service
public class AccessApplicationServiceImpl extends ServiceImpl<AccessApplicationMapper, AccessApplication> implements IAccessApplicationService {

    @Resource
    private AccessApplicationMapper accessApplicationMapper;

    private static final String DATETIME_FORMAT="yyyy-MM-dd HH:mm:ss";

    @Override
    public PageResp<AccessApplication> getProcessedList(StaffQueryReq req) {
        PageResp<AccessApplication> pageResp = getList(req, "processed");
        return pageResp;
    }

    @Override
    public PageResp<AccessApplication> getUserList(StaffQueryReq req) {
        PageResp<AccessApplication> pageResp = getList(req, "userList");
        return pageResp;
    }

    @Override
    public PageResp<AccessApplication> getTodoList(StaffQueryReq req) {
        PageResp<AccessApplication> pageResp = getList(req, "todo");
        return pageResp;
    }

    @Override
    public void accessApplicationSave(AccessApplication accessApplication) throws ParseException {

        // 若请求没有accessId则说明为插入申请
        if(accessApplication.getAccessId() == null){

            if(Duration.between(accessApplication.getStartDate(),
                    accessApplication.getExpirationDate()).toMillis() < 0){
                throw new BusinessException(BusinessExceptionCode.APPLICATION_TIME_ERROR);
            }

            // 将数据插入accessApplication中
            accessApplication.setApplicationState(0);
            accessApplicationMapper.insert(accessApplication);
        }else{
            // 否则进行更新
            UpdateWrapper<AccessApplication> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("access_id", accessApplication.getAccessId());
            updateWrapper.set("application_state", accessApplication.getApplicationState());
            accessApplicationMapper.update(null, updateWrapper);
        }
    }

    @Override
    public void delete(String id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("access_id", id);
        accessApplicationMapper.delete(wrapper);
    }


    public PageResp<AccessApplication> getList(StaffQueryReq req, String usage) {
        LambdaQueryWrapper<AccessApplication> wrapper = new QueryWrapper<AccessApplication>().lambda();

        if (usage.equals("processed")) {
            wrapper.and(c->{
                c.eq(AccessApplication::getApplicationState, -1).or().eq(AccessApplication::getApplicationState, 1);
            });
            if (!ObjectUtils.isEmpty(req.getIdentity())) {
                wrapper.and(c->{
                    c.like(AccessApplication::getName, req.getIdentity())
                            .or().like(AccessApplication::getAccessId, req.getIdentity());
                });
            }
        } else if (usage.equals("userList")) {
            wrapper.eq(AccessApplication::getForeignerIdentity, req.getIdentity());
        } else if (usage.equals("todo")) {
            wrapper.and(c->{
                c.eq(AccessApplication::getApplicationState, 0);
            });
            if (!ObjectUtils.isEmpty(req.getIdentity())) {
                wrapper.and(c->{
                    c.like(AccessApplication::getName, req.getIdentity())
                            .or().like(AccessApplication::getAccessId, req.getIdentity());
                });
            }
        }

        wrapper.orderByDesc(AccessApplication::getApplicationDate);
        List<AccessApplication> accessApplicationList = accessApplicationMapper.selectList(wrapper);

        PageInfo<AccessApplication> pageInfo = new PageInfo<>(accessApplicationList);
        PageResp<AccessApplication> pageResp = new PageResp();
        pageResp.setList(accessApplicationList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

}
