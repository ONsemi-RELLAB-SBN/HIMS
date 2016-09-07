package com.onsemi.hms.model;

public class IonicFtpShipping {
    private String requestId;
    private String materialPassNo;
    private String dateVerify;
    private String userVerify;
    private String shippingDate;
    private String shippingBy;
    private String status;

    public IonicFtpShipping(String requestId, String materialPassNo, String dateVerify, String userVerify, String shippingDate, String shippingBy, String status) {
        this.requestId = requestId;
        this.materialPassNo = materialPassNo;
        this.dateVerify = dateVerify;
        this.userVerify = userVerify;
        this.shippingDate = shippingDate;
        this.shippingBy = shippingBy;
        this.status = status;
    }
    
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMaterialPassNo() {
        return materialPassNo;
    }

    public void setMaterialPassNo(String materialPassNo) {
        this.materialPassNo = materialPassNo;
    }

    public String getDateVerify() {
        return dateVerify;
    }

    public void setDateVerify(String dateVerify) {
        this.dateVerify = dateVerify;
    }

    public String getUserVerify() {
        return userVerify;
    }

    public void setUserVerify(String userVerify) {
        this.userVerify = userVerify;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getShippingBy() {
        return shippingBy;
    }

    public void setShippingBy(String shippingBy) {
        this.shippingBy = shippingBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
