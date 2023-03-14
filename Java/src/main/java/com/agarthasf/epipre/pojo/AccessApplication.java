package com.agarthasf.epipre.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("access_application")
@ApiModel(value="AccessApplication对象", description="")
public class AccessApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "access_id", type = IdType.AUTO)
    private Integer accessId;

    @TableField("foreigner_identity")
    private String foreignerIdentity;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @NotEmpty(message = "手机号不能为空")
    @TableField("phone_number")
    private String phoneNumber;

    @NotEmpty(message = "理由不能为空")
    private String reason;

    @TableField("application_state")
    private Integer applicationState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "结束日期不能为空")
    @TableField("expiration_date")
    private LocalDateTime expirationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("application_date")
    private LocalDateTime applicationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "开始日期不能为空")
    @TableField("start_date")
    private LocalDateTime startDate;


}
