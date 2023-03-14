package com.agarthasf.epipre.req;


import com.agarthasf.epipre.pojo.Address;
import com.agarthasf.epipre.pojo.OutApplication;

public class OutApplicationReq {
    private Address address;

    private OutApplication outApplication;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public OutApplication getOutApplication() {
        return outApplication;
    }

    public void setOutApplication(OutApplication outApplication) {
        this.outApplication = outApplication;
    }

    @Override
    public String toString() {
        return "OutApplicationReq{" +
                "address=" + address +
                ", outApplication=" + outApplication +
                '}';
    }
}