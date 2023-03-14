package com.agarthasf.epipre.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
@TableName("info_upload")
@ApiModel(value="InfoUpload对象", description="")
public class InfoUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "info_id", type = IdType.AUTO)
    private Integer infoId;

    private String identity;

    @NotNull(message = "核酸时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "0 represents not test, 1 represents test")
    @TableField("last_covid_date")
    private LocalDateTime lastCovidDate;

    @NotNull(message = "疫苗接种数量不能为空")
    @ApiModelProperty(value = "0 has not been vaccinated, 1 represent has done")
    @TableField("vaccine_count")
    private Integer vaccineCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("info_date")
    private LocalDateTime infoDate;


    @TableField(exist = false)
    @ApiModelProperty(value = "上报者姓名")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty(value = "上报者号码")
    private String phoneNumber;
}
