package com.viettel.coms.bo;

import com.viettel.coms.dto.ManageHcqtDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.ManageHcqtBO")
@Table(name = "MANAGE_HCQT")
/**
 *
 * @author: tatph1
 */
public class ManageHcqtBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "MANAGE_HCQT_SEQ") })
	@Column(name = "MANAGE_HCQT_ID", length = 20 )
	private Long manageHcqtId;
	@Column(name = "CONSTRUCTION_NAME", length = 200 )
	private String constructionName;
	@Column(name = "CONSTRUCTION_CODE", length = 200 )
	private String constructionCode;
	@Column(name = "CONSTRUCTION_ID", length = 200)
	private Long constructionId;
	@Column(name = "STATION_CODE", length = 200 )
	private String stationCode;
	@Column(name = "CONTRACT_CODE", length = 200 )
	private String contractCode;
	@Column(name = "CONTRACT_NAME", length = 200 )
	private String contractName;
	@Column(name = "STATION_NAME", length = 200 )
	private String stationName;
	@Column(name = "PROVINCE_CODE", length = 200 )
	private String provinceCode;
	@Column(name = "PROVINCE_NAME", length = 200 )
	private String provinceName;
	
	
	@Column(name = "VTTB_RECIVE_VALUE", length = 200 )
	private Long vttbReciveValue;
	@Column(name = "VTTB_QT_VALUE", length = 200 )
	private Long vttbQtValue;
	@Column(name = "VTTB_DT_VALUE", length = 200 )
	private Long vttbDtValue;
	
	@Column(name = "CONSTRUCTION_STATUS", length = 2000 )
	private String constructionStatus;
	@Column(name = "CREATE_DATE", length = 22)
	private Date createDate;
	@Column(name = "UPDATE_DATE", length = 22)
	private Date updateDate;

	@Column(name = "CREATE_USER_ID", length = 200 )
	private Long createUserId;
	@Column(name = "UPDATE_USER_ID", length = 200 )
	private Long updateUserId;

	@Column(name = "DVT", length = 200 )
	private String dvt;
	@Column(name = "SO_LUONG")
	private Long soLuong;
	@Column(name = "THANH_TIEN")
	private Long thanhTien;
	
	@Column(name = "DEPT_CODE", length = 200 )
	private String deptCode;
	@Column(name = "DEPT_NAME", length = 200 )
	private String deptName;
	@Column(name = "PROJECT", length = 2000 )
	private String project;
	
	@Column(name = "VTTB_CODE", length = 200 )
	private String vttbCode;
	@Column(name = "VTTB_NAME", length = 2000 )
	private String vttbName;
	
	

	
	public Long getConstructionId() {
		return constructionId;
	}

	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
	}

	public String getVttbCode() {
		return vttbCode;
	}

	public void setVttbCode(String vttbCode) {
		this.vttbCode = vttbCode;
	}

	public String getVttbName() {
		return vttbName;
	}






	public void setVttbName(String vttbName) {
		this.vttbName = vttbName;
	}






	public String getDeptCode() {
		return deptCode;
	}






	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}






	public String getDeptName() {
		return deptName;
	}






	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}






	public String getProject() {
		return project;
	}






	public void setProject(String project) {
		this.project = project;
	}






	public String getDvt() {
		return dvt;
	}






	public void setDvt(String dvt) {
		this.dvt = dvt;
	}






	public Long getSoLuong() {
		return soLuong;
	}






	public void setSoLuong(Long soLuong) {
		this.soLuong = soLuong;
	}






	public Long getThanhTien() {
		return thanhTien;
	}






	public void setThanhTien(Long thanhTien) {
		this.thanhTien = thanhTien;
	}






	public Long getManageHcqtId() {
		return manageHcqtId;
	}






	public void setManageHcqtId(Long manageHcqtId) {
		this.manageHcqtId = manageHcqtId;
	}






	public String getConstructionName() {
		return constructionName;
	}






	public void setConstructionName(String constructionName) {
		this.constructionName = constructionName;
	}






	public String getConstructionCode() {
		return constructionCode;
	}






	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}






	public String getStationCode() {
		return stationCode;
	}






	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
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






	public String getStationName() {
		return stationName;
	}






	public void setStationName(String stationName) {
		this.stationName = stationName;
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






	public Long getVttbReciveValue() {
		return vttbReciveValue;
	}






	public void setVttbReciveValue(Long vttbReciveValue) {
		this.vttbReciveValue = vttbReciveValue;
	}






	public Long getVttbQtValue() {
		return vttbQtValue;
	}






	public void setVttbQtValue(Long vttbQtValue) {
		this.vttbQtValue = vttbQtValue;
	}






	public Long getVttbDtValue() {
		return vttbDtValue;
	}






	public void setVttbDtValue(Long vttbDtValue) {
		this.vttbDtValue = vttbDtValue;
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






	public Long getCreateUserId() {
		return createUserId;
	}






	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}






	public Long getUpdateUserId() {
		return updateUserId;
	}






	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}






	@Override
    public ManageHcqtDTO toDTO() {
		ManageHcqtDTO manageHcqtDTO = new ManageHcqtDTO(); 
		manageHcqtDTO.setManageHcqtId(this.getManageHcqtId());
		manageHcqtDTO.setConstructionName(this.getConstructionName());
		manageHcqtDTO.setConstructionCode(this.getConstructionCode());
		manageHcqtDTO.setStationCode(this.getStationCode());
		manageHcqtDTO.setContractCode(this.getContractCode());
		manageHcqtDTO.setContractName(this.getContractName());
		manageHcqtDTO.setStationName(this.getStationName());
		manageHcqtDTO.setProvinceCode(this.getProvinceCode());
		manageHcqtDTO.setProvinceName(this.getProvinceName());
    	
		manageHcqtDTO.setVttbReciveValue(this.getVttbReciveValue());
		manageHcqtDTO.setVttbQtValue(this.getVttbQtValue());
		manageHcqtDTO.setVttbDtValue(this.getVttbDtValue());
    	
		manageHcqtDTO.setConstructionStatus(this.getConstructionStatus());
		manageHcqtDTO.setCreateDate(this.getCreateDate());
		manageHcqtDTO.setUpdateDate(this.getUpdateDate());
    	
		manageHcqtDTO.setCreateUserId(this.getCreateUserId());
		manageHcqtDTO.setUpdateUserId(this.getUpdateUserId());
		
		manageHcqtDTO.setDvt(this.getDvt());
		manageHcqtDTO.setSoLuong(this.getSoLuong());
		manageHcqtDTO.setThanhTien(this.getThanhTien());
    	manageHcqtDTO.setDeptCode(this.getDeptCode());
    	manageHcqtDTO.setDeptName(this.getDeptName());
    	manageHcqtDTO.setProject(this.getProject());
    	manageHcqtDTO.setVttbCode(this.getVttbCode());
    	manageHcqtDTO.setVttbName(this.getVttbName());
        return manageHcqtDTO;
    }
}
