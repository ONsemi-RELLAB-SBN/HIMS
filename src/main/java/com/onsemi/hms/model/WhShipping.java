package com.onsemi.hms.model;

public class WhShipping {

    private String id;
    private String requestId;
    private String materialPassNo;
    private String materialPassExpiry;
    private String equipmentType;
    private String equipmentId;
    private String quantity;
    private String requestedBy;
    private String requestedEmail;
    private String requestedDate;
//    private String inventoryLoc;
    private String inventoryRack;
    private String inventoryShelf;
    private String remarks;
    private String barcodeVerify;
    private String userVerify;
    private String dateVerify;
    private String shippingDate;
    private String shippingBy;
    private String status;
    private String flag;
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
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
