package com.agarthasf.epipre.req;


import java.util.List;

public class TravelInfoInsertReq {
    private String identity;

    private List<String> cityList;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public List<String> getCityList() {
        return cityList;
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return "TravelInfoInsertReq{" +
                "identity='" + identity + '\'' +
                ", cityList=" + cityList +
                '}';
    }
}