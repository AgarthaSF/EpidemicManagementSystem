package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.exception.BusinessException;
import com.agarthasf.epipre.exception.BusinessExceptionCode;
import com.agarthasf.epipre.mapper.AccountMapper;
import com.agarthasf.epipre.mapper.CounselorMapper;
import com.agarthasf.epipre.pojo.Account;
import com.agarthasf.epipre.pojo.Counselor;
import com.agarthasf.epipre.pojo.SchoolStaff;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.ICounselorService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
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
public class CounselorServiceImpl extends ServiceImpl<CounselorMapper, Counselor> implements ICounselorService {

    @Resource
    private CounselorMapper counselorMapper;

    @Resource
    private AccountMapper accountMapper;


    @Override
    public PageResp<Counselor> getCounselorList(StaffQueryReq req) {
        LambdaQueryWrapper<Counselor> wrapper = new QueryWrapper<Counselor>().lambda();
        if (!ObjectUtils.isEmpty(req.getIdentity())) {
            wrapper.like(Counselor::getCounselorIdentity, "%" + req.getIdentity() + "%")
                    .or().like(Counselor::getName, "%" + req.getIdentity() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Counselor> counselorList = counselorMapper.selectList(wrapper);
        PageInfo<Counselor> pageInfo = new PageInfo<>(counselorList);
        PageResp<Counselor> pageResp = new PageResp();
        pageResp.setList(counselorList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void counselorSave(Counselor counselor) {
        // 查询identity是否已经存在account表中，若不存在则为新数据
        Account account = accountMapper.selectOne(
                new QueryWrapper<Account>().eq("identity", counselor.getCounselorIdentity()));

        // 若该编号存在于表中且为辅导员则进行更新
        if (!ObjectUtils.isEmpty(account) && account.getType().equals("counselor")) {
            UpdateWrapper<Counselor> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("counselor_identity", counselor.getCounselorIdentity());
            updateWrapper.set("name", counselor.getName());
            updateWrapper.set("address", counselor.getAddress());
            updateWrapper.set("phone_number", counselor.getPhoneNumber());
            counselorMapper.update(null, updateWrapper);

        } else if (ObjectUtils.isEmpty(account)) {
            // 若编号不存在于表中则进行插入

            Validate(counselor);

            // 先创建该辅导员的账号
            Account newAccount = new Account();
            newAccount.setIdentity(counselor.getCounselorIdentity());
            newAccount.setPassword(counselor.getPassword());
            newAccount.setType("counselor");
            accountMapper.insert(newAccount);

            // 再将数据插入counselor中
            counselorMapper.insert(counselor);
        } else {
            // 否则说明编号在表中且不是学生，提示该编号已被使用
            throw new BusinessException(BusinessExceptionCode.REGISTER_IDENTITY_EXIST);
        }
    }

    public void Validate(Counselor counselor){
        // 存在密码未填则返回
        if(counselor.getPassword()== null || counselor.getPassword().isEmpty()){
            throw new BusinessException(BusinessExceptionCode.ATTRIBUTE_EMPTY);
        }

        // 校验手机号是否重复
        List<Counselor> phoneList =  counselorMapper.selectList(
                new QueryWrapper<Counselor>().eq("phone_number", counselor.getPhoneNumber()));
        if(phoneList.size() > 0){
            throw new BusinessException(BusinessExceptionCode.PHONE_NUMBER_REPEAT);
        }

        // 校验手机号是否重复
        List<Counselor> idCardList =  counselorMapper.selectList(
                new QueryWrapper<Counselor>().eq("id_card", counselor.getIdCard()));
        if(idCardList.size() > 0){
            throw new BusinessException(BusinessExceptionCode.ID_CARD_REPEAT);
        }
    }



}
