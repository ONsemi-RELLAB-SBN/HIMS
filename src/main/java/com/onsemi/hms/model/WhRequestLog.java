package com.onsemi.hms.model;

public class WhRequestLog {

    //log
    private String id;
    private String referenceId;
    private String moduleId;
    private String moduleName;
    private String logStatus;
    private String timestamp;
    private String logVerifyDate;
    private String logVerifyBy;

    //request
    private String requestId;
    private String materialPassNo;
    private String materialPassExpiry;
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
    private String requestedBy;
    private String requestedEmail;
    private String requestedDate;
    private String inventoryLoc;
    private String inventoryRack;
    private String inventoryShelf;
    private String remarks;
    private String receivedDate;
    private String barcodeVerify;
    private String userVerify;
    private String dateVerify;
    private String inventoryRackVerify;
    private String inventoryShelfVerify;
    private String inventoryUserVerify;
    private String inventoryDateVerify;
    private String status;
    private String flag;
    private String tempCount;
    //time
    private String requestBarVerify;
    private String barVerifyInvVerify;

    //load card and program card
    private String loadCardId;
    private String loadCardQty;
    private String progCardId;
    private String progCardQty;
    private String pairingType;
    private String boxNo;
    private String gtsNo;

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getGtsNo() {
        return gtsNo;
    }

    public void setGtsNo(String gtsNo) {
        this.gtsNo = gtsNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogVerifyDate() {
        return logVerifyDate;
    }

    public void setLogVerifyDate(String logVerifyDate) {
        this.logVerifyDate = logVerifyDate;
    }

    public String getLogVerifyBy() {
        return logVerifyBy;
    }

    public void setLogVerifyBy(String logVerifyBy) {
        this.logVerifyBy = logVerifyBy;
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

    public String getQtyQualA() {
        return qtyQualA;
    }

    public void setQtyQualA(String qtyQualA) {
        this.qtyQualA = qtyQualA;
    }

    public String getPcbB() {
        return pcbB;
    }

    public void setPcbB(String pcbB) {
        this.pcbB = pcbB;
    }

    public String getQtyQualB() {
        return qtyQualB;
    }

    public void setQtyQualB(String qtyQualB) {
        this.qtyQualB = qtyQualB;
    }

    public String getPcbC() {
        return pcbC;
    }

    public void setPcbC(String pcbC) {
        this.pcbC = pcbC;
    }

    public String getQtyQualC() {
        return qtyQualC;
    }

    public void setQtyQualC(String qtyQualC) {
        this.qtyQualC = qtyQualC;
    }

    public String getPcbControl() {
        return pcbControl;
    }

    public void setPcbControl(String pcbControl) {
        this.pcbControl = pcbControl;
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

    public String getInventoryLoc() {
        return inventoryLoc;
    }

    public void setInventoryLoc(String inventoryLoc) {
        this.inventoryLoc = inventoryLoc;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getBarcodeVerify() {
        return barcodeVerify;
    }

    public void setBarcodeVerify(String barcodeVerify) {
        this.barcodeVerify = barcodeVerify;
    }

    public String getUserVerify() {
        return userVerify;
    }

    public void setUserVerify(String userVerify) {
        this.userVerify = userVerify;
    }

    public String getDateVerify() {
        return dateVerify;
    }

    public void setDateVerify(String dateVerify) {
        this.dateVerify = dateVerify;
    }

    public String getInventoryRackVerify() {
        return inventoryRackVerify;
    }

    public void setInventoryRackVerify(String inventoryRackVerify) {
        this.inventoryRackVerify = inventoryRackVerify;
    }

    public String getInventoryShelfVerify() {
        return inventoryShelfVerify;
    }

    public void setInventoryShelfVerify(String inventoryShelfVerify) {
        this.inventoryShelfVerify = inventoryShelfVerify;
    }

    public String getInventoryUserVerify() {
        return inventoryUserVerify;
    }

    public void setInventoryUserVerify(String inventoryUserVerify) {
        this.inventoryUserVerify = inventoryUserVerify;
    }

    public String getInventoryDateVerify() {
        return inventoryDateVerify;
    }

    public void setInventoryDateVerify(String inventoryDateVerify) {
        this.inventoryDateVerify = inventoryDateVerify;
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

    public String getTempCount() {
        return tempCount;
    }

    public void setTempCount(String tempCount) {
        this.tempCount = tempCount;
    }

    public String getRequestBarVerify() {
        return requestBarVerify;
    }

    public void setRequestBarVerify(String requestBarVerify) {
        this.requestBarVerify = requestBarVerify;
    }

    public String getBarVerifyInvVerify() {
        return barVerifyInvVerify;
    }

    public void setBarVerifyInvVerify(String barVerifyInvVerify) {
        this.barVerifyInvVerify = barVerifyInvVerify;
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

    public String getPairingType() {
        return pairingType;
    }

    public void setPairingType(String pairingType) {
        this.pairingType = pairingType;
    }
}
