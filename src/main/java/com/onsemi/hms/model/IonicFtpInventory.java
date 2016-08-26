package com.onsemi.hms.model;
public class IonicFtpInventory {
    private String refId;
    private String materialPassNo;
    private String equipmentType;
    private String equipmentId;
    private String quantity;
    private String requestedBy;
    private String requestedDate;
    private String remarks;
    private String dateVerify;
    private String inventoryDate;
    private String inventoryRack;
    private String inventorySlot;
    private String inventoryBy;
    private String status;
    
    public IonicFtpInventory() {
    }

    public IonicFtpInventory(String refId, String materialPassNo, String equipmentType, String equipmentId, String quantity, String requestedBy, String requestedDate, String remarks, String dateVerify, String inventoryDate, String inventoryRack, String inventorySlot, String inventoryBy, String status) {
        this.refId = refId;
        this.materialPassNo = materialPassNo;
        this.equipmentType = equipmentType;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.requestedBy = requestedBy;
        this.requestedDate = requestedDate;
        this.remarks = remarks;
        this.dateVerify = dateVerify;
        this.inventoryDate = inventoryDate;
        this.inventoryRack = inventoryRack;
        this.inventorySlot = inventorySlot;
        this.inventoryBy = inventoryBy;
        this.status = status;
    }
    
    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getMaterialPassNo() {
        return materialPassNo;
    }

    public void setMaterialPassNo(String materialPassNo) {
        this.materialPassNo = materialPassNo;
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

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
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

    public String getDateVerify() {
        return dateVerify;
    }

    public void setDateVerify(String dateVerify) {
        this.dateVerify = dateVerify;
    }

    public String getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(String inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getInventoryRack() {
        return inventoryRack;
    }

    public void setInventoryRack(String inventoryRack) {
        this.inventoryRack = inventoryRack;
    }

    public String getInventorySlot() {
        return inventorySlot;
    }

    public void setInventorySlot(String inventorySlot) {
        this.inventorySlot = inventorySlot;
    }
    
    public String getInventoryBy() {
        return inventoryBy;
    }

    public void setInventoryBy(String inventoryBy) {
        this.inventoryBy = inventoryBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
