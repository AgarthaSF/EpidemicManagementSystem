package com.agarthasf.epipre.service.impl;

import com.agarthasf.epipre.exception.BusinessException;
import com.agarthasf.epipre.exception.BusinessExceptionCode;
import com.agarthasf.epipre.mapper.AccountMapper;
import com.agarthasf.epipre.mapper.CounselorMapper;
import com.agarthasf.epipre.mapper.ForeignStaffMapper;
import com.agarthasf.epipre.mapper.SchoolStaffMapper;
import com.agarthasf.epipre.pojo.Account;
import com.agarthasf.epipre.req.PasswordResetReq;
import com.agarthasf.epipre.resp.AccountLoginResp;
import com.agarthasf.epipre.service.IAccountService;
import com.agarthasf.epipre.util.CopyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-17
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private SchoolStaffMapper schoolStaffMapper;

    @Resource
    private ForeignStaffMapper foreignStaffMapper;

    @Resource
    private CounselorMapper counselorMapper;

    /**
     * 登陆后返回token
     *
     * @param identity
     * @param password
     * @return
     */
    @Override
    public AccountLoginResp login(String identity, String password) {

        // 登陆
        Account account = getAccountByIdentity(identity);
        if (null == account || !password.equals(account.getPassword())) {
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }

        AccountLoginResp accountLoginResp = CopyUtil.copy(account, AccountLoginResp.class);
        accountLoginResp.setPassword(null);
        return accountLoginResp;
    }

    /**
     * 根据用户编码获取用户账号信息
     *
     * @param identity
     * @return
     */
    @Override
    public Account getAccountByIdentity(String identity) {
        return accountMapper.selectOne(new QueryWrapper<Account>().eq("identity", identity));
    }

    @Override
    public void resetPassword(PasswordResetReq req) {
        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("identity", req.getIdentity());
        updateWrapper.set("password", req.getPassword());
        accountMapper.update(null, updateWrapper);
    }

    @Override
    public void delete(String identity) {

        // 先根据id获取对应账号
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("identity", identity);
        Account account = accountMapper.selectOne(wrapper);

        QueryWrapper deleteWrapper = new QueryWrapper();
        // 根据人员类型删除对应数据库中的信息
        if(account.getType().equals("student") || account.getType().equals("teacher")){
            deleteWrapper.eq("identity", identity);
            schoolStaffMapper.delete(deleteWrapper);

        }else if((account.getType().equals("counselor"))){
            deleteWrapper.eq("counselor_identity", identity);
            counselorMapper.delete(deleteWrapper);

        }else if((account.getType().equals("foreigner"))){
            deleteWrapper.eq("identity", identity);
            foreignStaffMapper.delete(deleteWrapper);
        }

        accountMapper.delete(wrapper);

    }


}
