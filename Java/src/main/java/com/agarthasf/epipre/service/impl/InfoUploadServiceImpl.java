package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.mapper.InfoUploadMapper;
import com.agarthasf.epipre.pojo.InfoUpload;
import com.agarthasf.epipre.pojo.InfoUpload;
import com.agarthasf.epipre.mapper.InfoUploadMapper;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IInfoUploadService;
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
 * @since 2022-06-23
 */
@Service
public class InfoUploadServiceImpl extends ServiceImpl<InfoUploadMapper, InfoUpload> implements IInfoUploadService {

    @Resource
    private InfoUploadMapper infoUploadMapper;

    @Override
    public PageResp<InfoUpload> getList(StaffQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<InfoUpload> checkList = infoUploadMapper.getListByIdentity(req.getIdentity());
        PageInfo<InfoUpload> pageInfo = new PageInfo<>(checkList);
        PageResp<InfoUpload> pageResp = new PageResp();
        pageResp.setList(checkList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public PageResp<InfoUpload> getAll(StaffQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<InfoUpload> checkList = infoUploadMapper.getAllWithName(req.getIdentity());
        PageInfo<InfoUpload> pageInfo = new PageInfo<>(checkList);
        PageResp<InfoUpload> pageResp = new PageResp();
        pageResp.setList(checkList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void saveInfoUpload(InfoUpload infoUpload) {
        infoUploadMapper.insert(infoUpload);
    }

    @Override
    public PageResp<InfoUpload> getAnomaly(StaffQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<InfoUpload> checkList = infoUploadMapper.getAnomaly(req.getIdentity());
        PageInfo<InfoUpload> pageInfo = new PageInfo<>(checkList);
        PageResp<InfoUpload> pageResp = new PageResp();
        pageResp.setList(checkList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }
}
