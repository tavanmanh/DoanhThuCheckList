package com.viettel.coms.dto;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

public class AIOWoTrDTO {
    private Long contractId;
    private String type;
    private String contractCode;
    private Long userCreated;
    private Date createdDate;
    private Double contractAmount;
    private Long performerId;
    private Date startDateContract;
    private Long status;
    private Long sysGroupId;
    private String performerName;
    private String performerCode;
    private Long performerGroupId;
    private List<AIOWoTrDetailDTO> aioWoTrDetailDTO;
    private String cdLevel2;
    private String cdLevel3;
    private String cdLevel4;
    private String workName;
    private Date startDate;
    private Date endDate;
    private Long customerType;
    private Long contractType;
    private String reasonName;
    private Long contractDetailId;
    private List<AIOWoImageDTO> listImage;
    private Integer qoutaTime;
	// Huypq-09012021-start
	private String serviceCode;
	private Long provinceId;
	private Boolean isDieuPhoi;
	private String fieldAio;

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	// Huy-end

    public List<AIOWoImageDTO> getListImage() {
        return listImage;
    }

    public void setListImage(List<AIOWoImageDTO> listImage) {
        this.listImage = listImage;
    }

    public Long getContractDetailId() {
        return contractDetailId;
    }

    public void setContractDetailId(Long contractDetailId) {
        this.contractDetailId = contractDetailId;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Long getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Long customerType) {
        this.customerType = customerType;
    }
    public String getCdLevel4() {
        return cdLevel4;
    }

    public void setCdLevel4(String cdLevel4) {
        this.cdLevel4 = cdLevel4;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
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

    public Long getPerformerGroupId() {
        return performerGroupId;
    }

    public void setPerformerGroupId(Long performerGroupId) {
        this.performerGroupId = performerGroupId;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getPerformerName() {
        return performerName;
    }

    public void setPerformerName(String performerName) {
        this.performerName = performerName;
    }

    public String getPerformerCode() {
        return performerCode;
    }

    public void setPerformerCode(String performerCode) {
        this.performerCode = performerCode;
    }

    public List<AIOWoTrDetailDTO> getAioWoTrDetailDTO() {
        return aioWoTrDetailDTO;
    }

    public void setAioWoTrDetailDTO(List<AIOWoTrDetailDTO> aioWoTrDetailDTO) {
        this.aioWoTrDetailDTO = aioWoTrDetailDTO;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Long getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(Long userCreated) {
        this.userCreated = userCreated;
    }

    public String getCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(String cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    public String getCdLevel3() {
        return cdLevel3;
    }

    public void setCdLevel3(String cdLevel3) {
        this.cdLevel3 = cdLevel3;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public Date getStartDateContract() {
        return startDateContract;
    }

    public void setStartDateContract(Date startDateContract) {
        this.startDateContract = startDateContract;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getContractType() {
        return contractType;
    }

    public void setContractType(Long contractType) {
        this.contractType = contractType;
    }

    public Integer getQoutaTime() {
        return qoutaTime;
    }

    public void setQoutaTime(Integer qoutaTime) {
        this.qoutaTime = qoutaTime;
    }

	public Boolean getIsDieuPhoi() {
		return isDieuPhoi;
	}

	public void setIsDieuPhoi(Boolean isDieuPhoi) {
		this.isDieuPhoi = isDieuPhoi;
	}

	public String getFieldAio() {
		return fieldAio;
	}

	public void setFieldAio(String fieldAio) {
		this.fieldAio = fieldAio;
	}
    
    
}
