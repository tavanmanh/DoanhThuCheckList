package com.viettel.coms.dto;


import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;
import com.viettel.coms.bo.ManageVttbBO;

/**
 *
 * @author tatph1
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "MANAGE_VTTBBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageVttbDTO extends ComsBaseFWDTO<ManageVttbBO> {

	
	private Long manageVttbId;
	private Long manageUsedMaterialId;
	private String constructionCode;
	private String constructionName;
	private Long constructionId;
	private String stationCode;
	private String stationName;
	private String contractCode;
	private String contractName;
	private Long contractType;
	private String provinceCode;
	private String provinceName;
	private String pxkCode;
	private Long vttbValue;
	private String constructionStatus;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date updateDate;
	private String createUserId;
	private String updateUserId;
	private String pxkName;
	private Date pxkDate;
	private String pxkDateS;
	private String vttbName;
	private String vttbCode;
	
	private Long soLuongPxk;
	private Long giaTriPxk;
	private Long soLuongDuThua;
	private Long giaTriDuThua;
	private Long soLuongSuDung;
	private Long giaTriSuDung;
	private Long soLuongThuHoi;
	private Long giaTriThuHoi;
	private Long soPhieu;
	private String description;

	private Long soLuongDDK;
	private Long ttDDK;
	private Long soLuongNTK;
	private Long ttNTK;
	private Long soLuongQT;
	private Long ttQT;
	private Long soLuongTCK;
	private Long ttTCK;
	private Long giaTriXacNhan;
	private Long giaTriChenhLech;
	private String dvt;
	private String sysGroupName;
	private String projectCode;
	private String ngayXN;
	private String nguoiXN;
	
	private Long contractTypeO;
	private String contractTypeOsName;
	private String serial;
	private Long donGia;
	private String stockCode;
	private String catStationHouseCode;
	private String status;
	private String transMan;
	private String phoneNumber;
	private String email;
	private Long sysGroupId;
	private String ngayQuaHan;
	private String transDate;
	private String startDate;
	private String endDate;
	
	List<String> provinceIds;
	
	
    public List<String> getProvinceIds() {
		return provinceIds;
	}


	public void setProvinceIds(List<String> provinceIds) {
		this.provinceIds = provinceIds;
	}


	@Override
    public ManageVttbBO toModel() {
    	ManageVttbBO manageVttbBO = new ManageVttbBO();
    	  manageVttbBO.setManageVttbId(this.manageVttbId);
          manageVttbBO.setConstructionCode(this.getConstructionCode());
          manageVttbBO.setConstructionName(this.getConstructionName());
          manageVttbBO.setStationCode(this.getStationCode());
          manageVttbBO.setStationName(this.getStationName());
          manageVttbBO.setContractCode(this.getContractCode());
          manageVttbBO.setContractName(this.getContractName());
          manageVttbBO.setContractType(this.contractType);
          manageVttbBO.setProvinceCode(this.getProvinceCode());
          manageVttbBO.setProvinceName(this.getProvinceName());
          manageVttbBO.setPxkCode(this.getPxkCode());
          manageVttbBO.setVttbValue(this.vttbValue);
          manageVttbBO.setConstructionStatus(this.getConstructionStatus());
          manageVttbBO.setCreateDate(this.getCreateDate());
          manageVttbBO.setUpdateDate(this.getUpdateDate());
          manageVttbBO.setCreateUserId(this.getCreateUserId());
          manageVttbBO.setUpdateUserId(this.getUpdateUserId());
          manageVttbBO.setPxkName(this.getPxkName());
          manageVttbBO.setPxkDate(this.getPxkDate());
          manageVttbBO.setVttbName(this.getVttbName());
          manageVttbBO.setVttbCode(this.getVttbCode());
          manageVttbBO.setConstructionId(this.getConstructionId());
        return manageVttbBO;
    }
    
    
	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getTransMan() {
		return transMan;
	}


	public void setTransMan(String transMan) {
		this.transMan = transMan;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Long getSysGroupId() {
		return sysGroupId;
	}


	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}


	public String getNgayQuaHan() {
		return ngayQuaHan;
	}


	public void setNgayQuaHan(String ngayQuaHan) {
		this.ngayQuaHan = ngayQuaHan;
	}


	public String getTransDate() {
		return transDate;
	}


	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}


	public String getSerial() {
		return serial;
	}


	public void setSerial(String serial) {
		this.serial = serial;
	}


	public Long getDonGia() {
		return donGia;
	}


	public void setDonGia(Long donGia) {
		this.donGia = donGia;
	}


	public String getStockCode() {
		return stockCode;
	}


	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}


	public String getCatStationHouseCode() {
		return catStationHouseCode;
	}


	public void setCatStationHouseCode(String catStationHouseCode) {
		this.catStationHouseCode = catStationHouseCode;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getNgayXN() {
		return ngayXN;
	}


	public void setNgayXN(String ngayXN) {
		this.ngayXN = ngayXN;
	}


	public String getNguoiXN() {
		return nguoiXN;
	}


	public void setNguoiXN(String nguoiXN) {
		this.nguoiXN = nguoiXN;
	}


	public Long getGiaTriChenhLech() {
		return giaTriChenhLech;
	}


	public void setGiaTriChenhLech(Long giaTriChenhLech) {
		this.giaTriChenhLech = giaTriChenhLech;
	}


	public Long getGiaTriXacNhan() {
		return giaTriXacNhan;
	}


	public void setGiaTriXacNhan(Long giaTriXacNhan) {
		this.giaTriXacNhan = giaTriXacNhan;
	}


	public Long getContractTypeO() {
		return contractTypeO;
	}


	public void setContractTypeO(Long contractTypeO) {
		this.contractTypeO = contractTypeO;
	}


	public String getContractTypeOsName() {
		return contractTypeOsName;
	}


	public void setContractTypeOsName(String contractTypeOsName) {
		this.contractTypeOsName = contractTypeOsName;
	}


	public Long getSoLuongDDK() {
		return soLuongDDK;
	}


	public void setSoLuongDDK(Long soLuongDDK) {
		this.soLuongDDK = soLuongDDK;
	}


	public Long getTtDDK() {
		return ttDDK;
	}


	public void setTtDDK(Long ttDDK) {
		this.ttDDK = ttDDK;
	}


	public Long getSoLuongNTK() {
		return soLuongNTK;
	}


	public void setSoLuongNTK(Long soLuongNTK) {
		this.soLuongNTK = soLuongNTK;
	}


	public Long getTtNTK() {
		return ttNTK;
	}


	public void setTtNTK(Long ttNTK) {
		this.ttNTK = ttNTK;
	}


	public Long getSoLuongQT() {
		return soLuongQT;
	}


	public void setSoLuongQT(Long soLuongQT) {
		this.soLuongQT = soLuongQT;
	}


	public Long getTtQT() {
		return ttQT;
	}


	public void setTtQT(Long ttQT) {
		this.ttQT = ttQT;
	}


	public Long getSoLuongTCK() {
		return soLuongTCK;
	}


	public void setSoLuongTCK(Long soLuongTCK) {
		this.soLuongTCK = soLuongTCK;
	}


	public Long getTtTCK() {
		return ttTCK;
	}


	public void setTtTCK(Long ttTCK) {
		this.ttTCK = ttTCK;
	}


	public String getDvt() {
		return dvt;
	}


	public void setDvt(String dvt) {
		this.dvt = dvt;
	}


	public String getSysGroupName() {
		return sysGroupName;
	}


	public void setSysGroupName(String sysGroupName) {
		this.sysGroupName = sysGroupName;
	}


	public String getProjectCode() {
		return projectCode;
	}


	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}


	public Long getManageUsedMaterialId() {
		return manageUsedMaterialId;
	}


	public void setManageUsedMaterialId(Long manageUsedMaterialId) {
		this.manageUsedMaterialId = manageUsedMaterialId;
	}


	public Long getSoLuongThuHoi() {
		return soLuongThuHoi;
	}


	public void setSoLuongThuHoi(Long soLuongThuHoi) {
		this.soLuongThuHoi = soLuongThuHoi;
	}


	public Long getGiaTriThuHoi() {
		return giaTriThuHoi;
	}


	public void setGiaTriThuHoi(Long giaTriThuHoi) {
		this.giaTriThuHoi = giaTriThuHoi;
	}


	public Long getConstructionId() {
		return constructionId;
	}


	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
	}


	public Long getSoPhieu() {
		return soPhieu;
	}


	public void setSoPhieu(Long soPhieu) {
		this.soPhieu = soPhieu;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Long getSoLuongPxk() {
		return soLuongPxk;
	}


	public void setSoLuongPxk(Long soLuongPxk) {
		this.soLuongPxk = soLuongPxk;
	}


	public Long getGiaTriPxk() {
		return giaTriPxk;
	}


	public void setGiaTriPxk(Long giaTriPxk) {
		this.giaTriPxk = giaTriPxk;
	}


	public Long getSoLuongDuThua() {
		return soLuongDuThua;
	}


	public void setSoLuongDuThua(Long soLuongDuThua) {
		this.soLuongDuThua = soLuongDuThua;
	}


	public Long getGiaTriDuThua() {
		return giaTriDuThua;
	}


	public void setGiaTriDuThua(Long giaTriDuThua) {
		this.giaTriDuThua = giaTriDuThua;
	}


	public Long getSoLuongSuDung() {
		return soLuongSuDung;
	}


	public void setSoLuongSuDung(Long soLuongSuDung) {
		this.soLuongSuDung = soLuongSuDung;
	}


	public Long getGiaTriSuDung() {
		return giaTriSuDung;
	}


	public void setGiaTriSuDung(Long giaTriSuDung) {
		this.giaTriSuDung = giaTriSuDung;
	}


	public String getPxkDateS() {
		return pxkDateS;
	}


	public void setPxkDateS(String pxkDateS) {
		this.pxkDateS = pxkDateS;
	}


	public Date getPxkDate() {
		return pxkDate;
	}


	public void setPxkDate(Date pxkDate) {
		this.pxkDate = pxkDate;
	}


	public Long getManageVttbId() {
		return manageVttbId;
	}

	public void setManageVttbId(Long manageVttbId) {
		this.manageVttbId = manageVttbId;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public String getConstructionName() {
		return constructionName;
	}

	public void setConstructionName(String constructionName) {
		this.constructionName = constructionName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Long getContractType() {
		return contractType;
	}

	public void setContractType(Long contractType) {
		this.contractType = contractType;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getPxkCode() {
		return pxkCode;
	}

	public void setPxkCode(String pxkCode) {
		this.pxkCode = pxkCode;
	}

	public Long getVttbValue() {
		return vttbValue;
	}

	public void setVttbValue(Long vttbValue) {
		this.vttbValue = vttbValue;
	}

	public String getConstructionStatus() {
		return constructionStatus;
	}

	public void setConstructionStatus(String constructionStatus) {
		this.constructionStatus = constructionStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getPxkName() {
		return pxkName;
	}

	public void setPxkName(String pxkName) {
		this.pxkName = pxkName;
	}

	public String getVttbName() {
		return vttbName;
	}

	public void setVttbName(String vttbName) {
		this.vttbName = vttbName;
	}

	public String getVttbCode() {
		return vttbCode;
	}

	public void setVttbCode(String vttbCode) {
		this.vttbCode = vttbCode;
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
    
}
