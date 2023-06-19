package com.onsemi.hms.model;

public class FileUploadDO {
    private String mpNo;
    private String mpExpDate;
    private String eqptType;
    private String eqptId;
    private String shelfId;
    private String qty;
    private String reasonsRetrieval;

    public FileUploadDO(String mpNo, String mpExpDate, String eqptType, String eqptId, String shelfId, String qty, String reasonsRetrieval) {
        this.mpNo = mpNo;
        this.mpExpDate = mpExpDate;
        this.eqptType = eqptType;
        this.eqptId = eqptId;
        this.shelfId = shelfId;
        this.qty = qty;
        this.reasonsRetrieval = reasonsRetrieval;
    }

    public String getMpNo() {
        return mpNo;
    }

    public void setMpNo(String mpNo) {
        this.mpNo = mpNo;
    }

    public String getMpExpDate() {
        return mpExpDate;
    }

    public void setMpExpDate(String mpExpDate) {
        this.mpExpDate = mpExpDate;
    }

    public String getEqptType() {
        return eqptType;
    }

    public void setEqptType(String eqptType) {
        this.eqptType = eqptType;
    }

    public String getEqptId() {
        return eqptId;
    }

    public void setEqptId(String eqptId) {
        this.eqptId = eqptId;
    }

    public String getShelfId() {
        return shelfId;
    }

    public void setShelfId(String shelfId) {
        this.shelfId = shelfId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getReasonsRetrieval() {
        return reasonsRetrieval;
    }

    public void setReasonsRetrieval(String reasonsRetrieval) {
        this.reasonsRetrieval = reasonsRetrieval;
    }
    
    
            
}
