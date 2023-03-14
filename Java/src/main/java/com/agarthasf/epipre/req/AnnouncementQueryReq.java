package com.agarthasf.epipre.req;


public class AnnouncementQueryReq extends PageReq {
    private String announcementId;

    public String getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }

    @Override
    public String toString() {
        return "AnnouncementQueryReq{" +
                "announcementId='" + announcementId + '\'' +
                '}';
    }
}