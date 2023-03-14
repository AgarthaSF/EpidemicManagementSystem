package com.agarthasf.epipre.controller;


import com.agarthasf.epipre.pojo.SchoolStaff;
import com.agarthasf.epipre.req.PasswordResetReq;
import com.agarthasf.epipre.resp.CommonResp;
import com.agarthasf.epipre.service.IAccountService;
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
 * @since 2022-06-21
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;


    @ApiOperation(value = "重置人员密码")
    @PostMapping("/reset-password")
    public CommonResp resetPassword(@RequestBody @Valid PasswordResetReq req) {
        CommonResp resp = new CommonResp();
        accountService.resetPassword(req);
        return resp;
    }

    @ApiOperation(value = "删除人员账号")
    @DeleteMapping("delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp resp = new CommonResp<>();
        accountService.delete(id);
        return resp;
    }


}
