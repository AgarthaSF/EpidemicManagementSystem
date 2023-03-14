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
@TableName("daily_check")
@ApiModel(value="DailyCheck对象", description="")
public class DailyCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "check_id", type = IdType.AUTO)
    private Integer checkId;

    private String identity;

    @NotNull(message = "体温不能为空")
    @TableField("body_temperature")
    private Float bodyTemperature;

    @NotNull(message = "发烧状态不能为空")
    @ApiModelProperty(value = "0 represents do not have fever, 1 represents have")
    @TableField("fever_state")
    private Integer feverState;

    @NotNull(message = "隔离状态不能为空")
    @ApiModelProperty(value = "0 represents  have not gone to hospital, 1 represents have")
    @TableField("consultation_state")
    private Integer consultationState;

    @NotNull(message = "高风险状态不能为空")
    @TableField("high_risk_access")
    private Integer highRiskAccess;

    @NotNull(message = "高风险状态不能为空")
    @TableField("high_risk_member")
    private Integer highRiskMember;

    @NotNull(message = "高风险状态不能为空")
    @TableField("contact_high_risk_people")
    private Integer contactHighRiskPeople;

    @NotNull(message = "离校学习状态不能为空")
    @TableField("remote_study")
    private Integer remoteStudy;

    @NotNull(message = "供应状态不能为空")
    @TableField("supply_enough")
    private Integer supplyEnough;

    @NotNull(message = "口罩数量不能为空")
    @TableField("mask_number")
    private Integer maskNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("check_date")
    private LocalDateTime checkDate;

    @TableField(exist = false)
    @ApiModelProperty(value = "上报者姓名")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty(value = "上报者号码")
    private String phoneNumber;
}
