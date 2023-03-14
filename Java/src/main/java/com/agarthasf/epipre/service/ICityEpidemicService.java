package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.CityEpidemic;
import com.agarthasf.epipre.req.StaffQueryReq;
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
public interface ICityEpidemicService extends IService<CityEpidemic> {

    List<String> getCityList();

    List<JSON> getRiskList(StaffQueryReq req);

    void saveCity(CityEpidemic cityEpidemic);

    void delete(String id);
}
