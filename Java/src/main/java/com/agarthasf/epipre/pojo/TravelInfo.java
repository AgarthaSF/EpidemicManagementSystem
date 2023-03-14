package com.agarthasf.epipre.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2022-06-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("travel_info")
@ApiModel(value="TravelInfo对象", description="")
public class TravelInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "travel_id", type = IdType.AUTO)
    private Integer travelId;

    private String identity;

    @TableField("city_name")
    private String cityName;

    @TableField("travel_date")
    private LocalDateTime travelDate;


}
