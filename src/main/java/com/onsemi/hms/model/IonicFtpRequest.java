package com.onsemi.hms.model;

public class IonicFtpRequest {
    private String refId;
    private String equipmentType;
    private String equipmentId;
    private String quantity;
    private String materialPassNo;
    private String materialPassExpiry;
    private String inventoryLoc;
    private String requestedBy;
    private String requestedEmail;
    private String requestedDate;
    private String remarks;
    
    public IonicFtpRequest() {
    }

    public IonicFtpRequest(String refId, String equipmentType, String equipmentId, String quantity, String materialPassNo, String materialPassExpiry, String inventoryLoc, String requestedBy, String requestedEmail, String requestedDate, String remarks) {
        this.refId = refId;
        this.equipmentType = equipmentType;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.materialPassNo = materialPassNo;
        this.materialPassExpiry = materialPassExpiry;
        this.inventoryLoc = inventoryLoc;
        this.requestedBy = requestedBy;
        this.requestedEmail = requestedEmail;
        this.requestedDate = requestedDate;
        this.remarks = remarks;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMaterialPassNo() {
        return materialPassNo;
    }

    public void setMaterialPassNo(String materialPassNo) {
        this.materialPassNo = materialPassNo;
    }

    public String getMaterialPassExpiry() {
        return materialPassExpiry;
    }

    public void setMaterialPassExpiry(String materialPassExpiry) {
        this.materialPassExpiry = materialPassExpiry;
    }

    public String getInventoryLoc() {
        return inventoryLoc;
    }

    public void setInventoryLoc(String inventoryLoc) {
        this.inventoryLoc = inventoryLoc;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getRequestedEmail() {
        return requestedEmail;
    }

    public void setRequestedEmail(String requestedEmail) {
        this.requestedEmail = requestedEmail;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }    
}
