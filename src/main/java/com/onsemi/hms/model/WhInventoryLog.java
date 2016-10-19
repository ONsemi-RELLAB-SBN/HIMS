package com.onsemi.hms.model;

public class WhInventoryLog {
    //log
    private String id;
    private String referenceId;
    private String moduleId;
    private String moduleName;
    private String logStatus;
    private String timestamp;
    private String logVerifyDate;
    private String logVerifyBy;
    
    //retrieve
    private String retrieveId;
    private String materialPassNo;
    private String materialPassExpiry;
    private String equipmentType;
    private String equipmentId;
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
    private String shippingDate;
    private String remarks;
    private String receivedDate;
    private String barcodeVerify;
    private String userVerify;
    private String dateVerify;
    private String status;
    private String flag;
    private String arrivalReceivedDate;
    
    //inventory
    private String inRetrieveId;
    private String inventoryDate;
    private String inventoryLoc;
    private String inventoryRack;
    private String inventoryShelf;
    private String inventoryBy;
    private String inventoryStatus;
    private String inventoryFlag;
    
    //timelapse
    private String shipArrReceive;
    private String arrReceiveInventory;
    private String shipInventory;
//    private String shipReceive;
//    private String receiveVerify;
//    private String verifyInventory;
//    private String receiveInventory;
//    private String shippingInventory;

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

    public String getRetrieveId() {
        return retrieveId;
    }

    public void setRetrieveId(String retrieveId) {
        this.retrieveId = retrieveId;
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

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
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

    public String getArrivalReceivedDate() {
        return arrivalReceivedDate;
    }

    public void setArrivalReceivedDate(String arrivalReceivedDate) {
        this.arrivalReceivedDate = arrivalReceivedDate;
    }

    public String getInRetrieveId() {
        return inRetrieveId;
    }

    public void setInRetrieveId(String inRetrieveId) {
        this.inRetrieveId = inRetrieveId;
    }

    public String getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(String inventoryDate) {
        this.inventoryDate = inventoryDate;
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

    public String getInventoryBy() {
        return inventoryBy;
    }

    public void setInventoryBy(String inventoryBy) {
        this.inventoryBy = inventoryBy;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public String getInventoryFlag() {
        return inventoryFlag;
    }

    public void setInventoryFlag(String inventoryFlag) {
        this.inventoryFlag = inventoryFlag;
    }
    
    public String getShipArrReceive() {
        return shipArrReceive;
    }

    public void setShipArrReceive(String shipArrReceive) {
        this.shipArrReceive = shipArrReceive;
    }

    public String getArrReceiveInventory() {
        return arrReceiveInventory;
    }

    public void setArrReceiveInventory(String arrReceiveInventory) {
        this.arrReceiveInventory = arrReceiveInventory;
    }

    public String getShipInventory() {
        return shipInventory;
    }

    public void setShipInventory(String shipInventory) {
        this.shipInventory = shipInventory;
    }

//    public String getShipReceive() {
//        return shipReceive;
//    }
//
//    public void setShipReceive(String shipReceive) {
//        this.shipReceive = shipReceive;
//    }
//
//    public String getReceiveVerify() {
//        return receiveVerify;
//    }
//
//    public void setReceiveVerify(String receiveVerify) {
//        this.receiveVerify = receiveVerify;
//    }
//
//    public String getVerifyInventory() {
//        return verifyInventory;
//    }
//
//    public void setVerifyInventory(String verifyInventory) {
//        this.verifyInventory = verifyInventory;
//    }
//
//    public String getReceiveInventory() {
//        return receiveInventory;
//    }
//
//    public void setReceiveInventory(String receiveInventory) {
//        this.receiveInventory = receiveInventory;
//    }
//
//    public String getShippingInventory() {
//        return shippingInventory;
//    }
//
//    public void setShippingInventory(String shippingInventory) {
//        this.shippingInventory = shippingInventory;
//    }
}
