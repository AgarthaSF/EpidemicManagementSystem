package com.agarthasf.epipre.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("登录名已存在"),
    LOGIN_USER_ERROR("用户名不存在或密码错误"),
    REGISTER_IDENTITY_EXIST("该编号已被使用"),
    ATTRIBUTE_EMPTY("表单存在未填属性"),
    PHONE_NUMBER_REPEAT("该手机号已被注册"),
    ID_CARD_REPEAT("该身份证号已被注册"),
    APPLICATION_TIME_ERROR("结束日期不能早于开始日期"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
