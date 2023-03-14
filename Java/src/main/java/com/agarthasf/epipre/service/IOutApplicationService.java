package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.OutApplication;
import com.agarthasf.epipre.req.OutApplicationReq;
import com.agarthasf.epipre.req.OutApplicationSaveReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
public interface IOutApplicationService extends IService<OutApplication> {

    PageResp<OutApplication> getTodo(StaffQueryReq req);

    PageResp<OutApplication> getHistory(StaffQueryReq req);

    PageResp<OutApplication> getListById(StaffQueryReq req);

    void saveApplication(OutApplicationSaveReq req);

    void insertApplication(OutApplicationReq req);

    List<OutApplication> getAllRecently();


    void delete(String id);
}
