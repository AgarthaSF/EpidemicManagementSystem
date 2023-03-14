package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.ForeignStaff;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-21
 */
public interface IForeignStaffService extends IService<ForeignStaff> {

    PageResp<ForeignStaff> getForeignerList(StaffQueryReq req);

    void foreignStaffSave(ForeignStaff foreignStaff);

    String register(ForeignStaff foreignStaff);

    String getNewIdnetity();
}
