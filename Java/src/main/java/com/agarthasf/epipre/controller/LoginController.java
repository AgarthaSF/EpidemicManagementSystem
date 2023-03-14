package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.req.AccountLoginReq;
import com.agarthasf.epipre.resp.AccountLoginResp;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.service.IAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "LoginController")
@RestController
public class LoginController {


    @Autowired
    private IAccountService accountService;


    @ApiOperation(value = "登录后返回token")
    @PostMapping("/login")
    public CommonResp login(@RequestBody AccountLoginReq accountLoginReq){

        CommonResp<AccountLoginResp> resp = new CommonResp();
        AccountLoginResp accountLoginResp = accountService.login(accountLoginReq.getIdentity(), accountLoginReq.getPassword());

        resp.setContent(accountLoginResp);
        return resp;
    }


    @ApiOperation(value="退出登陆")
    @PostMapping("/logout")
    public CommonResp logout(){
        CommonResp resp = new CommonResp();
        resp.setMessage("注销成功");
        return resp;
    }

}
