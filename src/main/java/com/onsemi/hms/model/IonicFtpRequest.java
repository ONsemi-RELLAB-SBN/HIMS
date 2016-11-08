package com.onsemi.hms.model;

public class IonicFtpRequest {
    private String refId;
    private String equipmentType;
    private String equipmentId;
    private String reasonRetrieval;
    private String pcbA;
    private String qtyQualA;
    private String pcbB;
    private String qtyQualB;
    private String pcbC;
    private String qtyQualC;
    private String pcbControl;
    private String qtyControl;
    private String quantity;
    private String materialPassNo;
    private String materialPassExpiry;
//    private String inventoryLoc;
    private String inventoryRack;
    private String inventoryShelf;
    private String requestedBy;
    private String requestedEmail;
    private String requestedDate;
    private String remarks;
    private String csvStatus;
    
    public IonicFtpRequest() {
    }

    public IonicFtpRequest(String refId, String equipmentType, String equipmentId, String reasonRetrieval, String pcbA, String qtyQualA, String pcbB, String qtyQualB, String pcbC, String qtyQualC, String pcbControl, String qtyControl, String quantity, String materialPassNo, String materialPassExpiry, String inventoryRack, String inventoryShelf, String requestedBy, String requestedEmail, String requestedDate, String remarks, String csvStatus) {
        this.refId = refId;
        this.equipmentType = equipmentType;
        this.equipmentId = equipmentId;
        this.reasonRetrieval = reasonRetrieval;
        this.pcbA = pcbA;
        this.qtyQualA = qtyQualA;
        this.pcbB = pcbB;
        this.qtyQualB = qtyQualB;
        this.pcbC = pcbC;
        this.qtyQualC = qtyQualC;
        this.pcbControl = pcbControl;
        this.qtyControl = qtyControl;
        this.quantity = quantity;
        this.materialPassNo = materialPassNo;
        this.materialPassExpiry = materialPassExpiry;
        this.inventoryRack = inventoryRack;
        this.inventoryShelf = inventoryShelf;
        this.requestedBy = requestedBy;
        this.requestedEmail = requestedEmail;
        this.requestedDate = requestedDate;
        this.remarks = remarks;
        this.csvStatus = csvStatus;
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

    public String getReasonRetrieval() {
        return reasonRetrieval;
    }

    public void setReasonRetrieval(String reasonRetrieval) {
        this.reasonRetrieval = reasonRetrieval;
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

    public String getCsvStatus() {
        return csvStatus;
    }

    public void setCsvStatus(String csvStatus) {
        this.csvStatus = csvStatus;
    }
}
