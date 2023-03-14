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
@TableName("city_epidemic")
@ApiModel(value="CityEpidemic对象", description="")
public class CityEpidemic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("city_adcode")
    private Integer cityAdcode;

    @TableField("city_name")
    private String cityName;

    @TableField("current_confirmed_count")
    private Integer currentConfirmedCount;

    @TableField("suspected_count")
    private Integer suspectedCount;

    @TableField("risk_level")
    private String riskLevel;

    @TableField("parent_adcode")
    private Integer parentAdcode;

    @TableField(exist = false)
    @ApiModelProperty(value = "区id")
    private String areaId;

    @TableField(exist = false)
    @ApiModelProperty(value = "区名")
    private String areaName;

    @TableField(exist = false)
    @ApiModelProperty(value = "区风险等级")
    private String areaRiskLevel;

}
