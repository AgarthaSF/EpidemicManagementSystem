package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.TravelInfo;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.req.TravelInfoInsertReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.resp.TravelAnomalyResp;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
public interface ITravelInfoService extends IService<TravelInfo> {

    void travelInfoSave(@Valid TravelInfoInsertReq req);

    PageResp<TravelAnomalyResp> getSchoolAnomaly(StaffQueryReq req);

    PageResp<TravelAnomalyResp> getForeignAnomaly(StaffQueryReq req);
}
