package com.agarthasf.epipre.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@ApiModel(value="旅居异常返回对象", description="")
public class TravelAnomalyResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;

    private String identity;

    private String name;

    private List<String> cityList;


    public List<String> getCityList() {
        return cityList;
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
    }


    @TableField("phone_number")
    private String phoneNumber;

    @TableField("city_name")
    private String cityName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "TravelAnomalyResp{" +
                "type='" + type + '\'' +
                ", identity='" + identity + '\'' +
                ", name='" + name + '\'' +
                ", cityList=" + cityList +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
