package com.viettel.coms.dto;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;
import com.viettel.coms.bo.ManageHcqtBO;
import com.viettel.coms.bo.ManageVttbBO;

/**
 *
 * @author tatph1
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "MANAGE_HCQTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageHcqtDTO extends ComsBaseFWDTO<ManageHcqtBO> {

	
	
	private Long manageHcqtId;
	private String constructionName;
	private String constructionCode;
	private Long constructionId;
	private String stationCode;
	private String contractCode;
	private String contractName;
	private String stationName;
	private String provinceCode;
	private String provinceName;
	private String status;
	
	
	private Long vttbReciveValue;
	private Long vttbQtValue;
	private Long vttbDtValue;
	
	private String constructionStatus;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date updateDate;

	private Long createUserId;
	private Long updateUserId;
	
	private String dvt;
	private Long soLuong;
	private Long thanhTien;
	private String deptCode;
	private String deptName;
	private String project;
	private String vttbCode;
	private String vttbName;
	private List<String> provinceIds;

    @Override
    public ManageHcqtBO toModel() {
    	ManageHcqtBO manageHcqtBO = new ManageHcqtBO();
    	manageHcqtBO.setManageHcqtId(this.getManageHcqtId());
    	manageHcqtBO.setConstructionName(this.getConstructionName());
    	manageHcqtBO.setConstructionCode(this.getConstructionCode());
    	manageHcqtBO.setStationCode(this.getStationCode());
    	manageHcqtBO.setContractCode(this.getContractCode());
    	manageHcqtBO.setContractName(this.getContractName());
    	manageHcqtBO.setStationName(this.getStationName());
    	manageHcqtBO.setProvinceCode(this.getProvinceCode());
    	manageHcqtBO.setProvinceName(this.getProvinceName());
    	
    	manageHcqtBO.setVttbReciveValue(this.getVttbReciveValue());
    	manageHcqtBO.setVttbQtValue(this.getVttbQtValue());
    	manageHcqtBO.setVttbDtValue(this.getVttbDtValue());
    	
    	manageHcqtBO.setConstructionStatus(this.getConstructionStatus());
    	manageHcqtBO.setCreateDate(this.getCreateDate());
    	manageHcqtBO.setUpdateDate(this.getUpdateDate());
    	
    	manageHcqtBO.setCreateUserId(this.getCreateUserId());
    	manageHcqtBO.setUpdateUserId(this.getUpdateUserId());
    	
    	manageHcqtBO.setDvt(this.getDvt());
    	manageHcqtBO.setSoLuong(this.getSoLuong());
    	manageHcqtBO.setThanhTien(this.getThanhTien());
    	manageHcqtBO.setDeptCode(this.getDeptCode());
    	manageHcqtBO.setDeptName(this.getDeptName());
    	manageHcqtBO.setProject(this.getProject());
    	manageHcqtBO.setVttbCode(this.getVttbCode());
    	manageHcqtBO.setVttbName(this.getVttbName());
    	manageHcqtBO.setConstructionId(this.getConstructionId());
        return manageHcqtBO;
    }

    
    
	public List<String> getProvinceIds() {
		return provinceIds;
	}



	public void setProvinceIds(List<String> provinceIds) {
		this.provinceIds = provinceIds;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



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
