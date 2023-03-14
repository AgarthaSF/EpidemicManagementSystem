package com.agarthasf.epipre.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="登陆返回对象", description="")
public class AccountLoginResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String identity;

    private String password;

    private String type;

    private String token;

}
