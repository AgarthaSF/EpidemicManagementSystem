package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.Announcement;
import com.agarthasf.epipre.req.AnnouncementQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-20
 */
public interface IAnnouncementService extends IService<Announcement> {

    PageResp<Announcement> getList(AnnouncementQueryReq req);

    void announcementSave(Announcement announcement);

    void delete(String id);

}
