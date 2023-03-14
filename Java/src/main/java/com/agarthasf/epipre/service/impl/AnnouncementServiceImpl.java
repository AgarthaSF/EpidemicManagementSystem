package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.pojo.Account;
import com.agarthasf.epipre.pojo.Announcement;
import com.agarthasf.epipre.mapper.AnnouncementMapper;
import com.agarthasf.epipre.req.AnnouncementQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IAnnouncementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-20
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements IAnnouncementService {

    @Resource
    private AnnouncementMapper announcementMapper;

    @Override
    public PageResp<Announcement> getList(AnnouncementQueryReq req) {

        LambdaQueryWrapper<Announcement> wrapper = new QueryWrapper<Announcement>().lambda();
        if(!ObjectUtils.isEmpty(req.getAnnouncementId())){
            wrapper.like(Announcement::getAnnouncementId, "%" + req.getAnnouncementId() + "%")
                    .or().like(Announcement::getTitle, "%" + req.getAnnouncementId() + "%");
        }
        wrapper.orderByDesc(Announcement::getAnnoDate);

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Announcement> announcementList =  announcementMapper.selectList(wrapper);
        PageInfo<Announcement> pageInfo = new PageInfo<>(announcementList);
        PageResp<Announcement> pageResp = new PageResp();
        pageResp.setList(announcementList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void announcementSave(Announcement announcement) {
        // id存在为更新，id不存在即为插入
        if(!ObjectUtils.isEmpty(announcement.getAnnouncementId())){
            UpdateWrapper<Announcement> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("announcement_id", announcement.getAnnouncementId());
            announcementMapper.update(announcement, updateWrapper);
        }else{
            announcementMapper.insert(announcement);
        }
    }

    @Override
    public void delete(String id) {
        // 先根据id获取对应账号
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("announcement_id", id);
        announcementMapper.delete(wrapper);
    }

}
