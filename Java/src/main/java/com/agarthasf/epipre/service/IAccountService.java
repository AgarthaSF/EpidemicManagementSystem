package com.agarthasf.epipre.service;

import com.agarthasf.epipre.pojo.Account;
import com.agarthasf.epipre.req.PasswordResetReq;
import com.agarthasf.epipre.resp.AccountLoginResp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-17
 */
public interface IAccountService extends IService<Account> {

    /**
     * 登陆后返回token
     * @param identity
     * @param password
     * @return
     */
    AccountLoginResp login(String identity, String password);

    Account getAccountByIdentity(String identity);


    void resetPassword(PasswordResetReq req);

    void delete(String identity);
}
