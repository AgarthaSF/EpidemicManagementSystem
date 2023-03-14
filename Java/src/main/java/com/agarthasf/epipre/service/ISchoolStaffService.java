package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.SchoolStaff;
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
public interface ISchoolStaffService extends IService<SchoolStaff> {

    PageResp<SchoolStaff> getStudentListWithCounselorName(StaffQueryReq req);


    void studentSave(SchoolStaff student);


    PageResp<SchoolStaff> getTeacherList(StaffQueryReq req);

    void teacherSave(SchoolStaff student);

    PageResp<SchoolStaff> getStaffList(StaffQueryReq req);

}
