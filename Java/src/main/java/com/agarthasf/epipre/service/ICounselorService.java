package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-18
 */
public interface ICounselorService extends IService<Counselor> {

    PageResp<Counselor> getCounselorList(StaffQueryReq req);


    void counselorSave(Counselor counselor);

}
