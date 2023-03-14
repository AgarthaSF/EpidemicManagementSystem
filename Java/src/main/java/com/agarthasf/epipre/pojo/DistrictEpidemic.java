package com.agarthasf.epipre.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("district_epidemic")
@ApiModel(value="DistrictEpidemic对象", description="")
public class DistrictEpidemic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("district_adcode")
    private Integer districtAdcode;

    @TableField("district_name")
    private String districtName;

    @TableField("current_confirmed_count")
    private Integer currentConfirmedCount;

    @TableField("suspected_count")
    private Integer suspectedCount;


    @TableField(exist = false)
    @ApiModelProperty(value = "子城市adCode")
    private String cityAdCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "子城市名")
    private String cityName;

    @TableField(exist = false)
    @ApiModelProperty(value = "子城市确诊人数")
    private String cityCurrentConfirmedCount;

    @TableField(exist = false)
    @ApiModelProperty(value = "子城市疑似人数")
    private String citySuspectedCount;

    @TableField(exist = false)
    @ApiModelProperty(value = "子城市风险等级")
    private String cityRiskLevel;

    @TableField(exist = false)
    @ApiModelProperty(value = "子城市附属省份")
    private String cityParentAdcode;

}
