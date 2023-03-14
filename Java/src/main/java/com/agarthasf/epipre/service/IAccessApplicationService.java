package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.AccessApplication;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-22
 */
public interface IAccessApplicationService extends IService<AccessApplication> {

    PageResp<AccessApplication> getProcessedList(StaffQueryReq req);

    PageResp<AccessApplication> getUserList(StaffQueryReq req);

    PageResp<AccessApplication> getTodoList(StaffQueryReq req);

    void accessApplicationSave(AccessApplication accessApplication) throws ParseException;

    void delete(String id);

}
