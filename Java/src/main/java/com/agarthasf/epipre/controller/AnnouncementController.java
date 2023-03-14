package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.Announcement;
import com.agarthasf.epipre.pojo.SchoolStaff;
import com.agarthasf.epipre.req.AnnouncementQueryReq;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IAccountService;
import com.agarthasf.epipre.service.IAnnouncementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-18
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private IAnnouncementService announcementService;


    @ApiOperation(value = "获取通知列表")
    @GetMapping("/list")
    public CommonResp announcementList(@Valid AnnouncementQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<Announcement> respList = announcementService.getList(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "发布或编辑通知")
    @PostMapping("/save")
    public CommonResp announcementSave(@RequestBody @Valid Announcement announcement) {
        CommonResp resp = new CommonResp();
        announcementService.announcementSave(announcement);
        return resp;
    }

    @ApiOperation(value = "删除通知")
    @DeleteMapping("delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp resp = new CommonResp<>();
        announcementService.delete(id);

        return resp;
    }

}
