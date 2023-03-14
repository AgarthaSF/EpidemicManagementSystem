package com.agarthasf.epipre.pojo;

import com.agarthasf.epipre.req.PageReq;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
@TableName("school_staff")
@ApiModel(value="SchoolStaff对象", description="")
public class SchoolStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "用户名不能为空")
    private String identity;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @NotEmpty(message = "地址不能为空")
    private String address;

    @NotEmpty(message = "手机号码不能为空")
    @TableField("phone_number")
    private String phoneNumber;

    @NotEmpty(message = "身份证号不能为空")
    @TableField("id_card")
    private String idCard;

    @ApiModelProperty(value = "0 represents students, 1 represents teachers")
    @TableField("is_student")
    private Integer isStudent;

    @TableField("counselor_identity")
    private String counselorIdentity;

    @TableField(exist = false)
    @ApiModelProperty(value = "负责该学生的辅导员的姓名")
    private String counselorName;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户密码")
    private String password;

}
