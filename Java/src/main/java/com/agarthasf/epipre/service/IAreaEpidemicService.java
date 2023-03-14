package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.AreaEpidemic;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-24
 */
public interface IAreaEpidemicService extends IService<AreaEpidemic> {

    List<AreaEpidemic> getAreaByCityId(String belongingCity);
}
