package com.viettel.coms.bo;

import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity(name = "com.viettel.coms.bo.WoAvgMappingEntityBO")
@Table(name = "WO_MAPPING_AVG")

public class WoAvgMappingEntityBO extends BaseFWModelImpl {
    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_AVG_MAPPING_SEQ")})
    @Column(name = "ID")
    private Long id;
    @Column(name="ORDER_CODE_TGDD")
    private String orderCodeTgdd;
    @Column(name="ORDER_CODE_AVG")
    private String orderCodeAvg;
    @Column(name="CUSTOMER_NAME")
    private String customerName;
    @Column(name="PHONE_NUMBER")
    private String phoneNumber;
    @Column(name="PERSONAL_ID")
    private String personalId;
    @Column(name="ADDRESS")
    private String address;
    @Column(name="PRODUCT_CODE")
    private String productCode;
    @Column(name="PAYMENT_STATUS")
    private String paymentStatus;
    @Column(name="WO_ID")
    private Long woId;
    @Column(name="SERVICE_PACKAGE")
    private String servicePackage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCodeTgdd() {
        return orderCodeTgdd;
    }

    public void setOrderCodeTgdd(String orderCodeTgdd) {
        this.orderCodeTgdd = orderCodeTgdd;
    }

    public String getOrderCodeAvg() {
        return orderCodeAvg;
    }

    public void setOrderCodeAvg(String orderCodeAvg) {
        this.orderCodeAvg = orderCodeAvg;
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

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }


    public WoAvgMappingEntityBO(){

    }
    @Override
    public BaseFWDTOImpl toDTO() {
        return null;
    }
}
