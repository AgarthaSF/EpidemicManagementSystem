package com.agarthasf.epipre.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("foreign_staff")
@ApiModel(value="ForeignStaff对象", description="")
public class ForeignStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    private String identity;

    private String name;

    private String address;

    @TableField("phone_number")
    private String phoneNumber;

    @TableField("id_card")
    private String idCard;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户密码")
    private String password;
}
