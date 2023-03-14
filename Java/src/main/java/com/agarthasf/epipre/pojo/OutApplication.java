package com.agarthasf.epipre.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("out_application")
@ApiModel(value="OutApplication对象", description="")
public class OutApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "application_id", type = IdType.AUTO)
    private Integer applicationId;

    @TableField("student_identity")
    private String studentIdentity;

    @NotEmpty(message = "理由不能为空")
    private String reason;

    @TableField("out_address_id")
    private Integer outAddressId;

    @NotNull(message = "外出时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("out_time")
    private LocalDateTime outTime;

    @NotNull(message = "返回时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("back_time")
    private LocalDateTime backTime;

    @ApiModelProperty(value = "0 represents has not processed yet, 1 represents agree, -1 represents reject")
    @TableField("application_state")
    private Integer applicationState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("application_date")
    private LocalDateTime applicationDate;

    @TableField(exist = false)
    @ApiModelProperty(value = "申请人姓名")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty(value = "申请人电话号码")
    private String phoneNumber;

    @TableField(exist = false)
    @ApiModelProperty(value = "申请人外出地点")
    private String addressName;

    @TableField(exist = false)
    @ApiModelProperty(value = "流调发生地点")
    private String surveyAddressName;

    @TableField(exist = false)
    @ApiModelProperty(value = "外出地点经度")
    private double longitude;

    @TableField(exist = false)
    @ApiModelProperty(value = "外出地点纬度")
    private double latitude;

    @TableField(exist = false)
    @ApiModelProperty(value = "相关流调编号")
    private Integer caseId;

    @TableField(exist = false)
    @ApiModelProperty(value = "流调日期")
    private LocalDateTime surveyDate;

    @TableField(exist = false)
    @ApiModelProperty(value = "地点距离")
    private Double distance;

}
