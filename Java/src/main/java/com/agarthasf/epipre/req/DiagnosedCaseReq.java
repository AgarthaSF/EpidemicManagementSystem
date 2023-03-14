package com.agarthasf.epipre.req;


import com.agarthasf.epipre.pojo.Address;
import com.agarthasf.epipre.pojo.DiagnosedCase;
import com.agarthasf.epipre.pojo.OutApplication;

public class DiagnosedCaseReq {
    private Address address;

    private DiagnosedCase diagnosedCase;


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DiagnosedCase getDiagnosedCase() {
        return diagnosedCase;
    }

    public void setDiagnosedCase(DiagnosedCase diagnosedCase) {
        this.diagnosedCase = diagnosedCase;
    }

    @Override
    public String toString() {
        return "DiagnosedCaseReq{" +
                "address=" + address +
                ", diagnosedCase=" + diagnosedCase +
                '}';
    }
}