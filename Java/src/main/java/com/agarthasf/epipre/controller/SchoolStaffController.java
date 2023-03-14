package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.SchoolStaff;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.ISchoolStaffService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-18
 */
@RestController
@RequestMapping("/school-staff")
public class SchoolStaffController {

    @Autowired
    private ISchoolStaffService schoolStaffService;

    @ApiOperation(value = "获取学生具体信息及其辅导员姓名")
    @GetMapping("/student/list")
    public CommonResp studentList(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<SchoolStaff> respList = schoolStaffService.getStudentListWithCounselorName(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "增加学生或更新学生信息")
    @PostMapping("/student/save")
    public CommonResp studentSave(@RequestBody @Valid SchoolStaff student) {
        CommonResp resp = new CommonResp();
        schoolStaffService.studentSave(student);
        return resp;
    }


    @ApiOperation(value = "获取教职工列表")
    @GetMapping("/teacher/list")
    public CommonResp teacherList(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<SchoolStaff> respList = schoolStaffService.getTeacherList(req);
        resp.setContent(respList);
        return resp;
    }

    @ApiOperation(value = "增加教职工或更新教职工信息")
    @PostMapping("/teacher/save")
    public CommonResp teacherSave(@RequestBody @Valid SchoolStaff teacher) {
        CommonResp resp = new CommonResp();
        schoolStaffService.teacherSave(teacher);
        return resp;
    }

    @ApiOperation(value = "获取校内人员列表")
    @GetMapping("/list")
    public CommonResp staffList(@Valid StaffQueryReq req) {
        CommonResp resp = new CommonResp();
        PageResp<SchoolStaff> respList = schoolStaffService.getStaffList(req);
        resp.setContent(respList);
        return resp;
    }

}
