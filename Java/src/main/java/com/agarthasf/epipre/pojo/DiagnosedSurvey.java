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

/**
 * <p>
 * 
 * </p>
 *
 * @author agarthasf
 * @since 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("diagnosed_survey")
@ApiModel(value="DiagnosedSurvey对象", description="")
public class DiagnosedSurvey implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "survey_id", type = IdType.AUTO)
    private Integer surveyId;

    @TableField("case_id")
    private Integer caseId;

    @TableField("address_id")
    private Integer addressId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("arr_date")
    private LocalDateTime arrDate;

    @TableField(exist = false)
    @ApiModelProperty(value = "地点名称")
    private String addressName;

    @TableField(exist = false)
    @ApiModelProperty(value = "地点位置")
    private String addressPosition;

    @TableField(exist = false)
    @ApiModelProperty(value = "地址经度")
    private String longitude;

    @TableField(exist = false)
    @ApiModelProperty(value = "地址纬度")
    private String latitude;

}
