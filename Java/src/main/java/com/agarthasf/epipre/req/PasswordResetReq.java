package com.agarthasf.epipre.req;


import javax.validation.constraints.NotEmpty;

public class PasswordResetReq{

    @NotEmpty(message = "编号不能为空")
    private String identity;

    @NotEmpty(message = "密码不能为空")
    private String password;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordResetReq{" +
                "identity='" + identity + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}