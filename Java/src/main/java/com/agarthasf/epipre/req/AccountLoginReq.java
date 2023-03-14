package com.agarthasf.epipre.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 登陆实体类
 */

@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel(value = "AccountLogin对象", description = "")
public class AccountLoginReq {

    @ApiModelProperty(value = "用户编号", required = true)
    private String identity;
    @ApiModelProperty(value = "密码", required = true)
    private String password;




}
