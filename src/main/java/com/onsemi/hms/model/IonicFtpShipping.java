package com.onsemi.hms.model;

public class IonicFtpShipping {
    private String requestId;
    private String materialPassNo;
    private String shippingDate;
    private String shippingBy;
    private String status;
    private String flag;

    public IonicFtpShipping(String requestId, String materialPassNo, String shippingDate, String shippingBy, String status, String flag) {
        this.requestId = requestId;
        this.materialPassNo = materialPassNo;
        this.shippingDate = shippingDate;
        this.shippingBy = shippingBy;
        this.status = status;
        this.flag = flag;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    } 
}
