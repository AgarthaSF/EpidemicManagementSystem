package com.agarthasf.epipre.req;


public class StaffQueryReq extends PageReq {
    private String identity;

    private String counselorIdentity;

    public String getCounselorIdentity() {
        return counselorIdentity;
    }

    public void setCounselorIdentity(String counselorIdentity) {
        this.counselorIdentity = counselorIdentity;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "StaffQueryReq{" +
                "identity='" + identity + '\'' +
                ", counselorIdentity='" + counselorIdentity + '\'' +
                '}';
    }
}