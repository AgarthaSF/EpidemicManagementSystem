package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.exception.BusinessException;
import com.agarthasf.epipre.exception.BusinessExceptionCode;
import com.agarthasf.epipre.mapper.AccountMapper;
import com.agarthasf.epipre.pojo.AccessApplication;
import com.agarthasf.epipre.pojo.Account;
import com.agarthasf.epipre.pojo.ForeignStaff;
import com.agarthasf.epipre.pojo.SchoolStaff;
import com.agarthasf.epipre.mapper.SchoolStaffMapper;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.ISchoolStaffService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-18
 */
@Service
public class SchoolStaffServiceImpl extends ServiceImpl<SchoolStaffMapper, SchoolStaff> implements ISchoolStaffService {

    @Resource
    private SchoolStaffMapper schoolStaffMapper;

    @Resource
    private AccountMapper accountMapper;

    @Override
    public PageResp<SchoolStaff> getStudentListWithCounselorName(StaffQueryReq req) {
        PageHelper.startPage(req.getPage(), req.getSize());
        List<SchoolStaff> staffList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(req.getIdentity())) {
            staffList = schoolStaffMapper.getStudentListWithCounselorName(req.getIdentity());
        } else {
            staffList = schoolStaffMapper.getStudentListWithCounselorName("");
        }
        PageInfo<SchoolStaff> pageInfo = new PageInfo<>(staffList);
        PageResp<SchoolStaff> pageResp = new PageResp();
        pageResp.setList(staffList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void studentSave(SchoolStaff student) {

        if(student.getCounselorIdentity()== null || student.getCounselorIdentity().isEmpty()){
            throw new BusinessException(BusinessExceptionCode.ATTRIBUTE_EMPTY);
        }


        // 查询identity是否已经存在account表中，若不存在则为新数据
        Account account = accountMapper.selectOne(
                new QueryWrapper<Account>().eq("identity", student.getIdentity()));

        // 若该编号存在于表中且为学生则进行更新
        if (!ObjectUtils.isEmpty(account) && account.getType().equals("student")) {
            UpdateWrapper<SchoolStaff> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("identity", student.getIdentity());
            updateWrapper.set("name", student.getName());
            updateWrapper.set("address", student.getAddress());
            updateWrapper.set("phone_number", student.getPhoneNumber());
            updateWrapper.set("counselor_identity", student.getCounselorIdentity());
            updateWrapper.set("id_card", student.getIdCard());
            schoolStaffMapper.update(null, updateWrapper);

        } else if (ObjectUtils.isEmpty(account)) {

            // 若编号不存在于表中则进行插入

            Validate(student);

            // 先创建该学生的账号
            Account newAccount = new Account();
            newAccount.setIdentity(student.getIdentity());
            newAccount.setPassword(student.getPassword());
            newAccount.setType("student");
            accountMapper.insert(newAccount);

            // 再将数据插入school_staff中
            student.setIsStudent(1);
            schoolStaffMapper.insert(student);
        } else {
            // 否则说明编号在表中且不是学生，提示该编号已被使用
            throw new BusinessException(BusinessExceptionCode.REGISTER_IDENTITY_EXIST);
        }
    }

    @Override
    public PageResp<SchoolStaff> getTeacherList(StaffQueryReq req) {

        LambdaQueryWrapper<SchoolStaff> wrapper = new QueryWrapper<SchoolStaff>().lambda();
        if(!ObjectUtils.isEmpty(req.getIdentity())){
            wrapper.and(c->{
                c.like(SchoolStaff::getIdentity, "%" + req.getIdentity() + "%")
                        .or().like(SchoolStaff::getName, "%" + req.getIdentity() + "%");
            });
        }
        wrapper.eq(SchoolStaff::getIsStudent, 0);
        PageHelper.startPage(req.getPage(), req.getSize());
        List<SchoolStaff> staffList = schoolStaffMapper.selectList(wrapper);

        PageInfo<SchoolStaff> pageInfo = new PageInfo<>(staffList);
        PageResp<SchoolStaff> pageResp = new PageResp();
        pageResp.setList(staffList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void teacherSave(SchoolStaff teacher) {
        // 查询identity是否已经存在account表中，若不存在则为新数据
        Account account = accountMapper.selectOne(
                new QueryWrapper<Account>().eq("identity", teacher.getIdentity()));

        // 若该编号存在于表中且为教职工则进行更新
        if (!ObjectUtils.isEmpty(account) && account.getType().equals("teacher")) {
            UpdateWrapper<SchoolStaff> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("identity", teacher.getIdentity());
            updateWrapper.set("name", teacher.getName());
            updateWrapper.set("address", teacher.getAddress());
            updateWrapper.set("phone_number", teacher.getPhoneNumber());
            updateWrapper.set("id_card", teacher.getIdCard());
            schoolStaffMapper.update(null, updateWrapper);

        } else if (ObjectUtils.isEmpty(account)) {
            // 若编号不存在于表中则进行插入

            Validate(teacher);


            // 先创建该教职工的账号
            Account newAccount = new Account();
            newAccount.setIdentity(teacher.getIdentity());
            newAccount.setPassword(teacher.getPassword());
            newAccount.setType("teacher");
            accountMapper.insert(newAccount);

            // 再将数据插入school_staff中
            teacher.setIsStudent(0);
            schoolStaffMapper.insert(teacher);
        } else {
            // 否则说明编号在表中且不是教职工，提示该编号已被使用
            throw new BusinessException(BusinessExceptionCode.REGISTER_IDENTITY_EXIST);
        }
    }

    @Override
    public PageResp<SchoolStaff> getStaffList(StaffQueryReq req) {
        LambdaQueryWrapper<SchoolStaff> wrapper = new QueryWrapper<SchoolStaff>().lambda();
        if(!ObjectUtils.isEmpty(req.getIdentity())){
            wrapper.like(SchoolStaff::getIdentity, "%" + req.getIdentity() + "%")
                    .or().like(SchoolStaff::getName, "%" + req.getIdentity() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<SchoolStaff> staffList = schoolStaffMapper.selectList(wrapper);
        PageInfo<SchoolStaff> pageInfo = new PageInfo<>(staffList);
        PageResp<SchoolStaff> pageResp = new PageResp();
        pageResp.setList(staffList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }


    public void Validate(SchoolStaff schoolStaff){
        // 存在密码未填则返回
        if(schoolStaff.getPassword()== null || schoolStaff.getPassword().isEmpty()){
            throw new BusinessException(BusinessExceptionCode.ATTRIBUTE_EMPTY);
        }

        // 校验手机号是否重复
        List<SchoolStaff> phoneList =  schoolStaffMapper.selectList(
                new QueryWrapper<SchoolStaff>().eq("phone_number", schoolStaff.getPhoneNumber()));
        if(phoneList.size() > 0){
            throw new BusinessException(BusinessExceptionCode.PHONE_NUMBER_REPEAT);
        }

        // 校验手机号是否重复
        List<SchoolStaff> idCardList =  schoolStaffMapper.selectList(
                new QueryWrapper<SchoolStaff>().eq("id_card", schoolStaff.getIdCard()));
        if(idCardList.size() > 0){
            throw new BusinessException(BusinessExceptionCode.ID_CARD_REPEAT);
        }
    }
}
