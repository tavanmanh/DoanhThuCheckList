package com.viettel.coms.dto.avg;

public class GetWoAvgOutputDto {
    private String phoneNumber;
    private String personalId;
    private String address;
    private String productCode;
    private String paymentStatus;
    private String orderCodeAvg;
    private String orderCodeTgdd;
    private String customerName;
    public GetWoAvgOutputDto(){

    }
    public String getOrderCodeTgdd() {
        return orderCodeTgdd;
    }

    public void setOrderCodeTgdd(String orderCodeTgdd) {
        this.orderCodeTgdd = orderCodeTgdd;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderCodeAvg() {
        return orderCodeAvg;
    }

    public void setOrderCodeAvg(String orderCodeAvg) {
        this.orderCodeAvg = orderCodeAvg;
    }


}
