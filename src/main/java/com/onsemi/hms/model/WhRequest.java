package com.onsemi.hms.model;

public class WhRequest {

    private String id;
    private String refId;
    private String materialPassNo;
    private String materialPassExpiry;
    private String equipmentType;
    private String equipmentId;
    private String reasonRetrieval;
    private String pcbA;
    private String pcbB;
    private String pcbC;
    private String pcbControl;
    private String qtyQualA;
    private String qtyQualB;
    private String qtyQualC;
    private String qtyControl;
    private String quantity;
//    private String inventoryLoc;
    private String inventoryRack;
    private String inventoryShelf;
    private String requestedBy;
    private String requestedEmail;
    private String requestedDate;
    private String remarks;
    private String receivedDate;
    private String barcodeVerify;
    private String dateVerify;
    private String userVerify;
//    private String inventoryLocVerify;
    private String inventoryRackVerify;
    private String inventoryShelfVerify;
    private String inventoryUserVerify;
    private String inventoryDateVerify;
    private String status;
    private String flag;
    private String duration;
    private String tempCount;
    private String csvStatus;

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

//    public String getInventoryLoc() {
//        return inventoryLoc;
//    }
//
//    public void setInventoryLoc(String inventoryLoc) {
//        this.inventoryLoc = inventoryLoc;
//    }
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTempCount() {
        return tempCount;
    }

    public void setTempCount(String tempCount) {
        this.tempCount = tempCount;
    }

    public String getCsvStatus() {
        return csvStatus;
    }

    public void setCsvStatus(String csvStatus) {
        this.csvStatus = csvStatus;
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
