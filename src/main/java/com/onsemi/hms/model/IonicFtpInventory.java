package com.onsemi.hms.model;
public class IonicFtpInventory {
    private String refId;
    private String materialPassNo;
    private String materialPassExpiry;
    private String equipmentType;
    private String equipmentId;
    private String pcbA;
    private String pcbB;
    private String pcbC;
    private String pcbControl;
    private String qtyQualA;
    private String qtyQualB;
    private String qtyQualC;
    private String qtyControl;
    private String quantity;
    private String requestedBy;
    private String requestedDate;
    private String remarks;
    private String dateVerify;
    private String inventoryDate;
//    private String inventoryLoc;
    private String inventoryRack;
    private String inventoryShelf;
    private String inventoryBy;
    private String status;
    
    public IonicFtpInventory() {
    }

    public IonicFtpInventory(String refId, String materialPassNo, String materialPassExpiry, String equipmentType, String equipmentId, String pcbA, String pcbB, String pcbC, String pcbControl, String qtyQualA, String qtyQualB, String qtyQualC, String qtyControl, String quantity, String requestedBy, String requestedDate, String remarks, String dateVerify, String inventoryDate, String inventoryRack, String inventoryShelf, String inventoryBy, String status) {
        this.refId = refId;
        this.materialPassNo = materialPassNo;
        this.materialPassExpiry = materialPassExpiry;
        this.equipmentType = equipmentType;
        this.equipmentId = equipmentId;
        this.pcbA = pcbA;
        this.pcbB = pcbB;
        this.pcbC = pcbC;
        this.pcbControl = pcbControl;
        this.qtyQualA = qtyQualA;
        this.qtyQualB = qtyQualB;
        this.qtyQualC = qtyQualC;
        this.qtyControl = qtyControl;
        this.quantity = quantity;
        this.requestedBy = requestedBy;
        this.requestedDate = requestedDate;
        this.remarks = remarks;
        this.dateVerify = dateVerify;
        this.inventoryDate = inventoryDate;
        this.inventoryRack = inventoryRack;
        this.inventoryShelf = inventoryShelf;
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

    public String getMaterialPassExpiry() {
        return materialPassExpiry;
    }

    public void setMaterialPassExpiry(String materialPassExpiry) {
        this.materialPassExpiry = materialPassExpiry;
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

    public String getPcbA() {
        return pcbA;
    }

    public void setPcbA(String pcbA) {
        this.pcbA = pcbA;
    }

    public String getPcbB() {
        return pcbB;
    }

    public void setPcbB(String pcbB) {
        this.pcbB = pcbB;
    }

    public String getPcbC() {
        return pcbC;
    }

    public void setPcbC(String pcbC) {
        this.pcbC = pcbC;
    }

    public String getPcbControl() {
        return pcbControl;
    }

    public void setPcbControl(String pcbControl) {
        this.pcbControl = pcbControl;
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
    
    public String getQtyQualA() {
        return qtyQualA;
    }

    public void setQtyQualA(String qtyQualA) {
        this.qtyQualA = qtyQualA;
    }

    public String getQtyQualB() {
        return qtyQualB;
    }

    public void setQtyQualB(String qtyQualB) {
        this.qtyQualB = qtyQualB;
    }

    public String getQtyQualC() {
        return qtyQualC;
    }

    public void setQtyQualC(String qtyQualC) {
        this.qtyQualC = qtyQualC;
    }

    public String getQtyControl() {
        return qtyControl;
    }

    public void setQtyControl(String qtyControl) {
        this.qtyControl = qtyControl;
    }

    public String getInventoryRack() {
        return inventoryRack;
    }

    public void setInventoryRack(String inventoryRack) {
        this.inventoryRack = inventoryRack;
    }

    public String getInventoryShelf() {
        return inventoryShelf;
    }

    public void setInventoryShelf(String inventoryShelf) {
        this.inventoryShelf = inventoryShelf;
    }
//    public String getInventoryLoc() {
//        return inventoryLoc;
//    }
//
//    public void setInventoryLoc(String inventoryLoc) {
//        this.inventoryLoc = inventoryLoc;
//    }
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
