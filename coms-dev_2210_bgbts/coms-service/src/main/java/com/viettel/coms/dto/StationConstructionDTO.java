package com.viettel.coms.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.WorkItemBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;

public class StationConstructionDTO extends ComsBaseFWDTO<BaseFWModelImpl>{
	private java.lang.String typeBc;
	
	private java.lang.String area;
	
	private java.lang.String provinceCode;
	private java.lang.String provinceId;
	private java.lang.String catProvince;
	
	private java.lang.String stationVtNetCode;
	private java.lang.String stationVccCode;
	private java.lang.String otherCode;
	private java.lang.String projectCode;
	private java.lang.String projectName;
	
	private java.lang.Long catStationTypeId;
	private java.lang.String stationType;
	
	private java.lang.Double longitude;
	private java.lang.Double latitude;

	private java.lang.String maiDat; 
	private java.lang.String address;
	
	private java.lang.String doCaoCot; 
	
	private java.lang.String loaiCot; 
	private java.lang.String mongCo;
	private java.lang.String phongMay;
	private java.lang.String tiepDia;
	private java.lang.String dienAC; 
	private java.lang.String cotTrongMoi;
	private java.lang.String vanChuyenBo;
	private java.lang.String thueNguon; 
	private java.lang.String giaCoDacBiet;
	
	private java.lang.String donviThietKe;
	private java.lang.String nguoiThietKe;
	private java.lang.String coDuToan; 
	private java.lang.String Tham; 
	private java.lang.String bangiaoTTHT;

	private String ngayKC;

	private String ngayThueMB;
	

	private String ngayDB;

	private String ngayPS;
	
	private java.lang.String Vuong; 
	private java.lang.String nguyenNhanVuong;
	
	private java.lang.String kyXHDLan1; 

	private String ngayDTTram;
	
	private BigDecimal DTHaTang;
	private BigDecimal DTNguon;
	private BigDecimal CPThueMB;
	

	private String ngayTTHTPheDuyet;

	private String ngayNhanHSHC;
	private String ngayTTDTHTDuyetWO;
	private BigDecimal giatriQT;
	
	
	private java.lang.Long sumStationVtNet;
	private java.lang.Long sumStationVcc;
	
	private java.lang.Long sumProject;
	
	private String ngayDTNguon;
	private String dungXHD;
	private String coTK;
	
	
	public java.lang.String getArea() {
		return area;
	}
	public void setArea(java.lang.String area) {
		this.area = area;
	}
	public java.lang.String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(java.lang.String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public java.lang.String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(java.lang.String provinceId) {
		this.provinceId = provinceId;
	}
	public java.lang.String getStationVtNetCode() {
		return stationVtNetCode;
	}
	public void setStationVtNetCode(java.lang.String stationVtNetCode) {
		this.stationVtNetCode = stationVtNetCode;
	}
	public java.lang.String getStationVccCode() {
		return stationVccCode;
	}
	public void setStationVccCode(java.lang.String stationVccCode) {
		this.stationVccCode = stationVccCode;
	}
	public java.lang.String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(java.lang.String projectCode) {
		this.projectCode = projectCode;
	}
	public java.lang.String getProjectName() {
		return projectName;
	}
	public void setProjectName(java.lang.String projectName) {
		this.projectName = projectName;
	}
	public java.lang.Long getCatStationTypeId() {
		return catStationTypeId;
	}
	public void setCatStationTypeId(java.lang.Long catStationTypeId) {
		this.catStationTypeId = catStationTypeId;
	}
	public java.lang.String getStationType() {
		return stationType;
	}
	public void setStationType(java.lang.String stationType) {
		this.stationType = stationType;
	}
	public java.lang.Double getLongitude() {
		return longitude;
	}
	public void setLongitude(java.lang.Double longitude) {
		this.longitude = longitude;
	}
	public java.lang.Double getLatitude() {
		return latitude;
	}
	public void setLatitude(java.lang.Double latitude) {
		this.latitude = latitude;
	}
	public java.lang.String getMaiDat() {
		return maiDat;
	}
	public void setMaiDat(java.lang.String maiDat) {
		this.maiDat = maiDat;
	}
	public java.lang.String getAddress() {
		return address;
	}
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	public java.lang.String getLoaiCot() {
		return loaiCot;
	}
	public void setLoaiCot(java.lang.String loaiCot) {
		this.loaiCot = loaiCot;
	}
	public java.lang.String getMongCo() {
		return mongCo;
	}
	public void setMongCo(java.lang.String mongCo) {
		this.mongCo = mongCo;
	}
	public java.lang.String getPhongMay() {
		return phongMay;
	}
	public void setPhongMay(java.lang.String phongMay) {
		this.phongMay = phongMay;
	}
	public java.lang.String getTiepDia() {
		return tiepDia;
	}
	public void setTiepDia(java.lang.String tiepDia) {
		this.tiepDia = tiepDia;
	}
	public java.lang.String getDienAC() {
		return dienAC;
	}
	public void setDienAC(java.lang.String dienAC) {
		this.dienAC = dienAC;
	}
	public java.lang.String getCotTrongMoi() {
		return cotTrongMoi;
	}
	public void setCotTrongMoi(java.lang.String cotTrongMoi) {
		this.cotTrongMoi = cotTrongMoi;
	}
	public java.lang.String getVanChuyenBo() {
		return vanChuyenBo;
	}
	public void setVanChuyenBo(java.lang.String vanChuyenBo) {
		this.vanChuyenBo = vanChuyenBo;
	}
	public java.lang.String getThueNguon() {
		return thueNguon;
	}
	public void setThueNguon(java.lang.String thueNguon) {
		this.thueNguon = thueNguon;
	}
	public java.lang.String getGiaCoDacBiet() {
		return giaCoDacBiet;
	}
	public void setGiaCoDacBiet(java.lang.String giaCoDacBiet) {
		this.giaCoDacBiet = giaCoDacBiet;
	}
	public java.lang.String getDonviThietKe() {
		return donviThietKe;
	}
	public void setDonviThietKe(java.lang.String donviThietKe) {
		this.donviThietKe = donviThietKe;
	}
	public java.lang.String getNguoiThietKe() {
		return nguoiThietKe;
	}
	public void setNguoiThietKe(java.lang.String nguoiThietKe) {
		this.nguoiThietKe = nguoiThietKe;
	}
	public java.lang.String getCoDuToan() {
		return coDuToan;
	}
	public void setCoDuToan(java.lang.String coDuToan) {
		this.coDuToan = coDuToan;
	}
	public java.lang.String getTham() {
		return Tham;
	}
	public void setTham(java.lang.String tham) {
		Tham = tham;
	}
	public java.lang.String getBangiaoTTHT() {
		return bangiaoTTHT;
	}
	public void setBangiaoTTHT(java.lang.String bangiaoTTHT) {
		this.bangiaoTTHT = bangiaoTTHT;
	}
	
	public java.lang.String getVuong() {
		return Vuong;
	}
	public void setVuong(java.lang.String vuong) {
		Vuong = vuong;
	}
	public java.lang.String getNguyenNhanVuong() {
		return nguyenNhanVuong;
	}
	public void setNguyenNhanVuong(java.lang.String nguyenNhanVuong) {
		this.nguyenNhanVuong = nguyenNhanVuong;
	}
	public java.lang.String getKyXHDLan1() {
		return kyXHDLan1;
	}
	public void setKyXHDLan1(java.lang.String kyXHDLan1) {
		this.kyXHDLan1 = kyXHDLan1;
	}
	
	public BigDecimal getDTHaTang() {
		return DTHaTang;
	}
	public void setDTHaTang(BigDecimal dTHaTang) {
		DTHaTang = dTHaTang;
	}
	public BigDecimal getDTNguon() {
		return DTNguon;
	}
	public void setDTNguon(BigDecimal dTNguon) {
		DTNguon = dTNguon;
	}
	public BigDecimal getCPThueMB() {
		return CPThueMB;
	}
	public void setCPThueMB(BigDecimal cPThueMB) {
		CPThueMB = cPThueMB;
	}
	
	public BigDecimal getGiatriQT() {
		return giatriQT;
	}
	public void setGiatriQT(BigDecimal giatriQT) {
		this.giatriQT = giatriQT;
	}
	public java.lang.Long getSumStationVtNet() {
		return sumStationVtNet;
	}
	public void setSumStationVtNet(java.lang.Long sumStationVtNet) {
		this.sumStationVtNet = sumStationVtNet;
	}
	public java.lang.Long getSumStationVcc() {
		return sumStationVcc;
	}
	public void setSumStationVcc(java.lang.Long sumStationVcc) {
		this.sumStationVcc = sumStationVcc;
	}

	public java.lang.String getTypeBc() {
		return typeBc;
	}
	public void setTypeBc(java.lang.String typeBc) {
		this.typeBc = typeBc;
	}
	
	public java.lang.String getOtherCode() {
		return otherCode;
	}
	public void setOtherCode(java.lang.String otherCode) {
		this.otherCode = otherCode;
	}
	
	public java.lang.String getCatProvince() {
		return catProvince;
	}
	public void setCatProvince(java.lang.String catProvince) {
		this.catProvince = catProvince;
	}
	
	public java.lang.Long getSumProject() {
		return sumProject;
	}
	public void setSumProject(java.lang.Long sumProject) {
		this.sumProject = sumProject;
	}
	public java.lang.String getDoCaoCot() {
		return doCaoCot;
	}
	public void setDoCaoCot(java.lang.String doCaoCot) {
		this.doCaoCot = doCaoCot;
	}
	
	public String getNgayKC() {
		return ngayKC;
	}
	public void setNgayKC(String ngayKC) {
		this.ngayKC = ngayKC;
	}
	public String getNgayThueMB() {
		return ngayThueMB;
	}
	public void setNgayThueMB(String ngayThueMB) {
		this.ngayThueMB = ngayThueMB;
	}
	public String getNgayDB() {
		return ngayDB;
	}
	public void setNgayDB(String ngayDB) {
		this.ngayDB = ngayDB;
	}
	public String getNgayPS() {
		return ngayPS;
	}
	public void setNgayPS(String ngayPS) {
		this.ngayPS = ngayPS;
	}
	public String getNgayDTTram() {
		return ngayDTTram;
	}
	public void setNgayDTTram(String ngayDTTram) {
		this.ngayDTTram = ngayDTTram;
	}
	public String getNgayTTHTPheDuyet() {
		return ngayTTHTPheDuyet;
	}
	public void setNgayTTHTPheDuyet(String ngayTTHTPheDuyet) {
		this.ngayTTHTPheDuyet = ngayTTHTPheDuyet;
	}
	public String getNgayNhanHSHC() {
		return ngayNhanHSHC;
	}
	public void setNgayNhanHSHC(String ngayNhanHSHC) {
		this.ngayNhanHSHC = ngayNhanHSHC;
	}
	public String getNgayTTDTHTDuyetWO() {
		return ngayTTDTHTDuyetWO;
	}
	public void setNgayTTDTHTDuyetWO(String ngayTTDTHTDuyetWO) {
		this.ngayTTDTHTDuyetWO = ngayTTDTHTDuyetWO;
	}
	
	public String getNgayDTNguon() {
		return ngayDTNguon;
	}
	public void setNgayDTNguon(String ngayDTNguon) {
		this.ngayDTNguon = ngayDTNguon;
	}
	public String getDungXHD() {
		return dungXHD;
	}
	public void setDungXHD(String dungXHD) {
		this.dungXHD = dungXHD;
	}
	public String getCoTK() {
		return coTK;
	}
	public void setCoTK(String coTK) {
		this.coTK = coTK;
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
