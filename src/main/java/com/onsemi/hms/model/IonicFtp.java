package com.onsemi.hms.model;

public class IonicFtp {

    private String id;
    private String refId;
    private String materialPassNo;
    private String materialPassExpiry;
    private String equipmentType;
    private String equipmentId;
    private String type;
    private String quantity;
    private String rack;
    private String slot;
    private String requestedBy;
    private String requestedDate;
    private String remarks;
    private String status;
    private String flag;
    
    public IonicFtp() {
    }

    public IonicFtp(String id, String refId, String materialPassNo, String materialPassExpiry, String equipmentType, String equipmentId, String type, String quantity, String rack, String slot, String requestedBy, String requestedDate, String remarks, String status, String flag) {
        this.id = id;
        this.refId = refId;
        this.materialPassNo = materialPassNo;
        this.materialPassExpiry = materialPassExpiry;
        this.equipmentType = equipmentType;
        this.equipmentId = equipmentId;
        this.type = type;
        this.quantity = quantity;
        this.rack = rack;
        this.slot = slot;
        this.requestedBy = requestedBy;
        this.requestedDate = requestedDate;
        this.remarks = remarks;
        this.status = status;
        this.flag = flag;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    @Override
    public String toString() {
        return "IonicFtp{" + "id=" + id + ", refId=" + refId + ", equipmentType=" + equipmentType + ", equipmentId=" + equipmentId + ", type=" + type + ", quantity=" + quantity + ", requestedBy=" + requestedBy + ", requestedDate=" + requestedDate + ", materialPassNo=" + materialPassNo + ", materialPassExpiry=" + materialPassExpiry + ", rack=" + rack + ", slot=" + slot + ", remarks=" + remarks + ", status=" + status + ", flag=" + flag + '}';
    }
    
    

    
//    private String id;
//    private String eventCode;
//    private String rms;
//    private String intervals;
//    private String currentStatus;
//    private String dateOff;
//    private String equipId;
//    private String lcode;
//    private String hardwareFinal;
//    private String supportItem;
//    private String createdDate;
//
//    public IonicFtp() {
//    }
//
//    public IonicFtp(String eventCode, String rms,
//            String intervals, String currentStatus,
//            String dateOff, String equipId,
//            String lcode, String hardwareFinal, String supportItem) {
//        super();
//        this.eventCode = eventCode;
//        this.rms = rms;
//        this.intervals = intervals;
//        this.currentStatus = currentStatus;
//        this.dateOff = dateOff;
//        this.equipId = equipId;
//        this.lcode = lcode;
//        this.hardwareFinal = hardwareFinal;
//        this.supportItem = supportItem;
//    }
//
//    @Override
//    public String toString() {
//        return "ionicFtp [id=" + id + ",eventCode=" + eventCode + ", rms=" + rms
//                + ", intervals=" + intervals + ", currentStatus=" + currentStatus + ","
//                + "dateOff=" + dateOff + ", equipId=" + equipId
//                + ", lcode=" + lcode + ", hardwareFinal=" + hardwareFinal + ", supportItem=" + supportItem + "]";
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getEventCode() {
//        return eventCode;
//    }
//
//    public void setEventCode(String eventCode) {
//        this.eventCode = eventCode;
//    }
//
//    public String getRms() {
//        return rms;
//    }
//
//    public void setRms(String rms) {
//        this.rms = rms;
//    }
//
//    public String getIntervals() {
//        return intervals;
//    }
//
//    public void setIntervals(String intervals) {
//        this.intervals = intervals;
//    }
//
//    public String getCurrentStatus() {
//        return currentStatus;
//    }
//
//    public void setCurrentStatus(String currentStatus) {
//        this.currentStatus = currentStatus;
//    }
//
//    public String getDateOff() {
//        return dateOff;
//    }
//
//    public void setDateOff(String dateOff) {
//        this.dateOff = dateOff;
//    }
//
//    public String getEquipId() {
//        return equipId;
//    }
//
//    public void setEquipId(String equipId) {
//        this.equipId = equipId;
//    }
//
//    public String getLcode() {
//        return lcode;
//    }
//
//    public void setLcode(String lcode) {
//        this.lcode = lcode;
//    }
//
//    public String getHardwareFinal() {
//        return hardwareFinal;
//    }
//
//    public void setHardwareFinal(String hardwareFinal) {
//        this.hardwareFinal = hardwareFinal;
//    }
//
//    public String getSupportItem() {
//        return supportItem;
//    }
//
//    public void setSupportItem(String supportItem) {
//        this.supportItem = supportItem;
//    }
//
//    public String getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(String createdDate) {
//        this.createdDate = createdDate;
//    }

    
}
