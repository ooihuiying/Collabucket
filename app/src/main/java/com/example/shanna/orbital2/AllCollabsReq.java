package com.example.shanna.orbital2;

public class AllCollabsReq {

    private String Pay;
    private String Duration;
    private String BufferWait;
    private String MaxChanges;
    private String DateOfRequest;
    private String Partner; //ID of project requester
    private String SenderFullName;
    private String OwnerFullName;
    private String Title;
    private String OwnerID;
    /*
    private String DateofListing;
    private String ProjectQualifications;
    private String ProjectResponsibilities;
    private String ProjectStatus;
    private String ProjectSummary;
    */
    public AllCollabsReq(){

    }


    public AllCollabsReq(String pay, String duration, String bufferWait,
                         String maxChanges, String dateOfRequest, String partner, String senderFullName, String title, String ownerFullName, String ownerID){
                        // ,String dateofListing, String qualifications, String responsibilities, String status, String summary) {
        this.Pay = pay;
        this.Duration = duration;
        this.BufferWait = bufferWait;
        this.MaxChanges = maxChanges;
        this.DateOfRequest = dateOfRequest;
        this.Partner = partner;
        this.SenderFullName = senderFullName;
        this.Title = title;
        this.OwnerFullName = ownerFullName;
        this.OwnerID = ownerID;
        /*
        this.DateofListing = dateofListing;
        this.ProjectQualifications = qualifications;
        this.ProjectResponsibilities = responsibilities;
        this.ProjectStatus = status;
        this.ProjectSummary = summary;
        */
    }


    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getOwnerFullName() {
        return OwnerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        OwnerFullName = ownerFullName;
    }

    public String getPay() {
        return Pay;
    }

    public void setPay(String pay) {
        Pay = pay;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getBufferWait() {
        return BufferWait;
    }

    public void setBufferWait(String bufferWait) {
        BufferWait = bufferWait;
    }

    public String getMaxChanges() {
        return MaxChanges;
    }

    public void setMaxChanges(String maxChanges) {
        MaxChanges = maxChanges;
    }

    public String getDateOfRequest() {
        return DateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        DateOfRequest = dateOfRequest;
    }

    public String getPartner() {
        return Partner;
    }

    public void setPartner(String partner) {
        Partner = partner;
    }

    public String getSenderFullName() {
        return SenderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        SenderFullName = senderFullName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}