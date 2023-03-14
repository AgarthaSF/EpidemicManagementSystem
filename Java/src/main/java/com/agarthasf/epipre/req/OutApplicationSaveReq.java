package com.agarthasf.epipre.req;


public class OutApplicationSaveReq {
    private Integer applicationId;

    private Integer applicationState;


    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getApplicationState() {
        return applicationState;
    }

    public void setApplicationState(Integer applicationState) {
        this.applicationState = applicationState;
    }

    @Override
    public String toString() {
        return "OutApplicationSaveReq{" +
                "applicationId=" + applicationId +
                ", applicationState=" + applicationState +
                '}';
    }
}