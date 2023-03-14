package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.exception.BusinessException;
import com.agarthasf.epipre.exception.BusinessExceptionCode;
import com.agarthasf.epipre.mapper.AccountMapper;
import com.agarthasf.epipre.pojo.Account;
import com.agarthasf.epipre.pojo.ForeignStaff;
import com.agarthasf.epipre.mapper.ForeignStaffMapper;
import com.agarthasf.epipre.req.StaffQueryReq;
import com.agarthasf.epipre.resp.PageResp;
import com.agarthasf.epipre.service.IForeignStaffService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-21
 */
@Service
public class ForeignStaffServiceImpl extends ServiceImpl<ForeignStaffMapper, ForeignStaff> implements IForeignStaffService {

    @Resource
    private ForeignStaffMapper foreignStaffMapper;

    @Resource
    private AccountMapper accountMapper;

    
    @Override
    public PageResp<ForeignStaff> getForeignerList(StaffQueryReq req) {
        LambdaQueryWrapper<ForeignStaff> wrapper = new QueryWrapper<ForeignStaff>().lambda();
        if (!ObjectUtils.isEmpty(req.getIdentity())) {
            wrapper.like(ForeignStaff::getIdentity, "%" + req.getIdentity() + "%")
                    .or().like(ForeignStaff::getName, "%" + req.getIdentity() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<ForeignStaff> foreignStaffList = foreignStaffMapper.selectList(wrapper);
        PageInfo<ForeignStaff> pageInfo = new PageInfo<>(foreignStaffList);
        PageResp<ForeignStaff> pageResp = new PageResp();
        pageResp.setList(foreignStaffList);
        pageResp.setTotal((int) pageInfo.getTotal());
        return pageResp;
    }

    @Override
    public void foreignStaffSave(ForeignStaff foreignStaff) {
        // 查询identity是否已经存在account表中，若不存在则为新数据
        Account account = accountMapper.selectOne(
                new QueryWrapper<Account>().eq("identity", foreignStaff.getIdentity()));

        // 若该编号存在于表中且为外来人员则进行更新
        if (!ObjectUtils.isEmpty(account) && account.getType().equals("foreigner")) {
            UpdateWrapper<ForeignStaff> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("identity", foreignStaff.getIdentity());
            updateWrapper.set("name", foreignStaff.getName());
            updateWrapper.set("address", foreignStaff.getAddress());
            updateWrapper.set("phone_number", foreignStaff.getPhoneNumber());
            foreignStaffMapper.update(null, updateWrapper);

        } else if (ObjectUtils.isEmpty(account)) {
            // 若编号不存在于表中则进行插入
            // 先创建该人员的账号
            Account newAccount = new Account();
            newAccount.setIdentity(foreignStaff.getIdentity());
            newAccount.setPassword(foreignStaff.getPassword());
            newAccount.setType("foreigner");
            accountMapper.insert(newAccount);
            // 再将数据插入foreignStaff中
            foreignStaffMapper.insert(foreignStaff);
        } else {
            // 否则说明编号在表中且不是外来人员，提示该编号已被使用
            throw new BusinessException(BusinessExceptionCode.REGISTER_IDENTITY_EXIST);
        }
    }

    @Override
    public String register(ForeignStaff foreignStaff) {

        // 校验手机号是否重复
        List<ForeignStaff> phoneList =  foreignStaffMapper.selectList(
                new QueryWrapper<ForeignStaff>().eq("phone_number", foreignStaff.getPhoneNumber()));
        if(phoneList.size() > 0){
            throw new BusinessException(BusinessExceptionCode.PHONE_NUMBER_REPEAT);
        }

        // 校验手机号是否重复
        List<ForeignStaff> idCardList =  foreignStaffMapper.selectList(
                new QueryWrapper<ForeignStaff>().eq("id_card", foreignStaff.getIdCard()));
        if(idCardList.size() > 0){
            throw new BusinessException(BusinessExceptionCode.ID_CARD_REPEAT);
        }

        // 获取foreignerStaff中当前最大的编号，以该编号为基底加一即为新人员编号
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", "foreigner");
        queryWrapper.orderByDesc("identity");
        List<Account> accountList = accountMapper.selectList(queryWrapper);

        Long identity;
        if(accountList.size() == 0){
            identity = 30004000500L;
        }else{
            identity = Long.parseLong(accountList.get(0).getIdentity()) + 1;
        }
        foreignStaff.setIdentity(identity.toString());

        // 先插入account表中
        Account newAccount = new Account();
        newAccount.setIdentity(foreignStaff.getIdentity());
        newAccount.setPassword(foreignStaff.getPassword());
        newAccount.setType("foreigner");
        accountMapper.insert(newAccount);

        // 再插入foreign_staff表中
        foreignStaffMapper.insert(foreignStaff);

        return foreignStaff.getIdentity();
    }

    @Override
    public String getNewIdnetity() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", "foreigner");
        queryWrapper.orderByDesc("identity");
        List<Account> accountList = accountMapper.selectList(queryWrapper);

        Long identity;
        if(accountList.size() == 0){
            identity = 30004000500L;
        }else{
            identity = Long.parseLong(accountList.get(0).getIdentity()) + 1;
        }
        return identity.toString();
    }

}
