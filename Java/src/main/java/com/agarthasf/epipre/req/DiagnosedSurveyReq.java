package com.agarthasf.epipre.req;


import com.agarthasf.epipre.mapper.DiagnosedSurveyMapper;
import com.agarthasf.epipre.pojo.Address;
import com.agarthasf.epipre.pojo.DiagnosedCase;
import com.agarthasf.epipre.pojo.DiagnosedSurvey;

public class DiagnosedSurveyReq {
    private Address address;

    private DiagnosedSurvey diagnosedSurvey;


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public DiagnosedSurvey getDiagnosedSurvey() {
        return diagnosedSurvey;
    }

    public void setDiagnosedSurvey(DiagnosedSurvey diagnosedSurvey) {
        this.diagnosedSurvey = diagnosedSurvey;
    }


    @Override
    public String toString() {
        return "DiagnosedSurveyReq{" +
                "address=" + address +
                ", diagnosedSurvey=" + diagnosedSurvey +
                '}';
    }
}
