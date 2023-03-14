package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.DistrictEpidemic;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-20
 */
public interface IDistrictEpidemicService extends IService<DistrictEpidemic> {

    List<JSON> getEpidemicList(StaffQueryReq req);

}
