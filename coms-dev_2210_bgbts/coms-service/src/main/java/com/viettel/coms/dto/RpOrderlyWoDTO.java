package com.viettel.coms.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;

public class RpOrderlyWoDTO extends ComsBaseFWDTO<BaseFWModelImpl>{

	private String cdLevel1;
	private String cdLevel1Name;
	@JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endDate;
	private String status;
	private String typeBc;
	private String cdLevel2Name;
	private Double tong;
	private Double quahan;
	private Double tyle;
	private Long woId;
	private String woCode;
	private String woName;
	private String constructionCode;
	private String contractCode;
	private String ngayGiaoWo;
	private String ngayNhanWo;
	private String ngayGiaoFt;
	private String ngayFtTiepNhan;
	private String fullName;
	private String email;
	private String phoneNumber;
	private Double moneyValue;
	private String state;
	private String trangThai;
	private String ngayFtThucHienXong;
	private String ngayCdDuyet;
	
	public String getCdLevel1Name() {
		return cdLevel1Name;
	}
	public void setCdLevel1Name(String cdLevel1Name) {
		this.cdLevel1Name = cdLevel1Name;
	}
	public String getNgayFtThucHienXong() {
		return ngayFtThucHienXong;
	}
	public void setNgayFtThucHienXong(String ngayFtThucHienXong) {
		this.ngayFtThucHienXong = ngayFtThucHienXong;
	}
	public String getNgayCdDuyet() {
		return ngayCdDuyet;
	}
	public void setNgayCdDuyet(String ngayCdDuyet) {
		this.ngayCdDuyet = ngayCdDuyet;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTypeBc() {
		return typeBc;
	}
	public void setTypeBc(String typeBc) {
		this.typeBc = typeBc;
	}
	public Long getWoId() {
		return woId;
	}
	public void setWoId(Long woId) {
		this.woId = woId;
	}
	public String getWoCode() {
		return woCode;
	}
	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}
	public String getWoName() {
		return woName;
	}
	public void setWoName(String woName) {
		this.woName = woName;
	}
	public String getConstructionCode() {
		return constructionCode;
	}
	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getNgayGiaoWo() {
		return ngayGiaoWo;
	}
	public void setNgayGiaoWo(String ngayGiaoWo) {
		this.ngayGiaoWo = ngayGiaoWo;
	}
	public String getNgayNhanWo() {
		return ngayNhanWo;
	}
	public void setNgayNhanWo(String ngayNhanWo) {
		this.ngayNhanWo = ngayNhanWo;
	}
	public String getNgayGiaoFt() {
		return ngayGiaoFt;
	}
	public void setNgayGiaoFt(String ngayGiaoFt) {
		this.ngayGiaoFt = ngayGiaoFt;
	}
	public String getNgayFtTiepNhan() {
		return ngayFtTiepNhan;
	}
	public void setNgayFtTiepNhan(String ngayFtTiepNhan) {
		this.ngayFtTiepNhan = ngayFtTiepNhan;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Double getMoneyValue() {
		return moneyValue;
	}
	public void setMoneyValue(Double moneyValue) {
		this.moneyValue = moneyValue;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public String getCdLevel2Name() {
		return cdLevel2Name;
	}
	public void setCdLevel2Name(String cdLevel2Name) {
		this.cdLevel2Name = cdLevel2Name;
	}
	public Double getTong() {
		return tong;
	}
	public void setTong(Double tong) {
		this.tong = tong;
	}
	public Double getQuahan() {
		return quahan;
	}
	public void setQuahan(Double quahan) {
		this.quahan = quahan;
	}
	public Double getTyle() {
		return tyle;
	}
	public void setTyle(Double tyle) {
		this.tyle = tyle;
	}
	public String getCdLevel1() {
		return cdLevel1;
	}
	public void setCdLevel1(String cdLevel1) {
		this.cdLevel1 = cdLevel1;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BaseFWModelImpl toModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
