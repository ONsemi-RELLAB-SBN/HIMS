package com.onsemi.hms.model;
public class IonicFtpBib {
    private String reqId;
    private String eqptType;
    private String eqptId;
    private String loadCardId;
    private String loadCardQty;
    private String progCardId;
    private String progCardQty;
    private String qty;
    private String mpNo;
    private String mpExp;
    private String reqBy;
    private String reqEmail;
    private String rack;
    private String shelf;

    public IonicFtpBib(String reqId, String eqptType, String eqptId, String loadCardId, String loadCardQty, String progCardId, String progCardQty, String qty, String mpNo, String mpExp, String reqBy, String reqEmail, String rack, String shelf) {
        this.reqId = reqId;
        this.eqptType = eqptType;
        this.eqptId = eqptId;
        this.loadCardId = loadCardId;
        this.loadCardQty = loadCardQty;
        this.progCardId = progCardId;
        this.progCardQty = progCardQty;
        this.qty = qty;
        this.mpNo = mpNo;
        this.mpExp = mpExp;
        this.reqBy = reqBy;
        this.reqEmail = reqEmail;
        this.rack = rack;
        this.shelf = shelf;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
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

    public String getLoadCardId() {
        return loadCardId;
    }

    public void setLoadCardId(String loadCardId) {
        this.loadCardId = loadCardId;
    }

    public String getLoadCardQty() {
        return loadCardQty;
    }

    public void setLoadCardQty(String loadCardQty) {
        this.loadCardQty = loadCardQty;
    }

    public String getProgCardId() {
        return progCardId;
    }

    public void setProgCardId(String progCardId) {
        this.progCardId = progCardId;
    }

    public String getProgCardQty() {
        return progCardQty;
    }

    public void setProgCardQty(String progCardQty) {
        this.progCardQty = progCardQty;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMpNo() {
        return mpNo;
    }

    public void setMpNo(String mpNo) {
        this.mpNo = mpNo;
    }

    public String getMpExp() {
        return mpExp;
    }

    public void setMpExp(String mpExp) {
        this.mpExp = mpExp;
    }

    public String getReqBy() {
        return reqBy;
    }

    public void setReqBy(String reqBy) {
        this.reqBy = reqBy;
    }

    public String getReqEmail() {
        return reqEmail;
    }

    public void setReqEmail(String reqEmail) {
        this.reqEmail = reqEmail;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    
}
