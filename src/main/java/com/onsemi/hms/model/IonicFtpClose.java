package com.onsemi.hms.model;

public class IonicFtpClose {
    public String requestId;
    public String hardwareType;
    public String hardwareId;
    public String quantity;
    public String rack;
    public String shelf;
    public String status;

    public IonicFtpClose(String requestId, String hardwareType, String hardwareId, String quantity, String rack, String shelf, String status) {
        this.requestId = requestId;
        this.hardwareType = hardwareType;
        this.hardwareId = hardwareId;
        this.quantity = quantity;
        this.rack = rack;
        this.shelf = shelf;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getHardwareType() {
        return hardwareType;
    }

    public void setHardwareType(String hardwareType) {
        this.hardwareType = hardwareType;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
