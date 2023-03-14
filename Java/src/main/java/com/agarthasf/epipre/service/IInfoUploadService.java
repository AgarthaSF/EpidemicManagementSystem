package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.InfoUpload;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
public interface IInfoUploadService extends IService<InfoUpload> {

    PageResp<InfoUpload> getList(StaffQueryReq req);

    PageResp<InfoUpload> getAll(StaffQueryReq req);

    void saveInfoUpload(InfoUpload infoUpload);

    PageResp<InfoUpload> getAnomaly(StaffQueryReq req);
}
