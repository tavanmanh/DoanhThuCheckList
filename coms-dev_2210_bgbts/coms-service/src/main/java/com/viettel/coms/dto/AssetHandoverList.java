package com.viettel.coms.dto;

public class AssetHandoverList {
//    Id trạm
    private Long stationId;
//     Tên tài sản
    private String goodsName ;
//    Mã tài sản
    private String goodsCode ;
//    Số lượng
    private String quantity ;
//    Đơn giá
    private String price ;
//    Tổng tiền
    private String totalPrice ;
//     Trạng thái
    private String goodsStateName ;
//    Báo hỏng-mất…
    private String proposedGoodsState ;
//    Đơn vị
    private String sysGroupName ;
//    Mã trạm
    private String stationCode ;
//    Người quản lý
    private String sysUserName ;
//    Mã nhân viên
    private String sysUserCode ;

    // serial
    private String serial;

    private SysUserRequest sysUserRequest;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGoodsStateName() {
        return goodsStateName;
    }

    public void setGoodsStateName(String goodsStateName) {
        this.goodsStateName = goodsStateName;
    }

    public String getProposedGoodsState() {
        return proposedGoodsState;
    }

    public void setProposedGoodsState(String proposedGoodsState) {
        this.proposedGoodsState = proposedGoodsState;
    }

    public String getSysGroupName() {
        return sysGroupName;
    }

    public void setSysGroupName(String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getSysUserCode() {
        return sysUserCode;
    }

    public void setSysUserCode(String sysUserCode) {
        this.sysUserCode = sysUserCode;
    }

    public SysUserRequest getSysUserRequest() {
        return sysUserRequest;
    }

    public void setSysUserRequest(SysUserRequest sysUserRequest) {
        this.sysUserRequest = sysUserRequest;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
