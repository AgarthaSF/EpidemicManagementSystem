package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.DailyCheck;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-22
 */
public interface IDailyCheckService extends IService<DailyCheck> {

    PageResp<DailyCheck> getList(StaffQueryReq req);

    PageResp<DailyCheck> getAll(StaffQueryReq req);

    void saveCheck(DailyCheck dailyCheck);

    PageResp<DailyCheck> getAnomaly(StaffQueryReq req);
}
