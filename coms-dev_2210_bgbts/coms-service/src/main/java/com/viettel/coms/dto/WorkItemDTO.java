/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.WorkItemBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

/**
 * @author thuannht
 */
@XmlRootElement(name = "WORK_ITEMBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemDTO extends ComsBaseFWDTO<WorkItemBO> {

    private java.lang.Long workItemId;
    private java.lang.Long constructionId;
    private java.lang.Long catWorkItemTypeId;
    private java.lang.String code;
    private java.lang.String name;
    private java.lang.String isInternal;
    private java.lang.Long constructorId;
    private java.lang.Long supervisorId;
    private java.util.Date startingDate;
    private java.util.Date completeDate;
    private java.lang.String status;
    private java.lang.Double quantity;
    private java.lang.Double approveQuantity;
    private java.lang.String approveState;
    private java.util.Date approveDate;
    private java.lang.Long approveUserId;
    private java.lang.String approveDescription;
    private java.util.Date createdDate;
    private java.lang.Long createdUserId;
    private java.lang.Long createdGroupId;
    private java.util.Date updatedDate;
    private java.lang.Long updatedUserId;
    private java.lang.Long updatedGroupId;
    private java.lang.Long performerId;
    private java.lang.Long catWorkItemGroupId;
    private java.lang.String nodeCode;
    private java.lang.String woType;
    private java.lang.String branch;
    
    public java.lang.String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(java.lang.String nodeCode) {
		this.nodeCode = nodeCode;
	}
	//  hoanm1_20181015_start
    private String partnerName;

    public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
//	hoanm1_20181015_end

    @Override
    public WorkItemBO toModel() {
        WorkItemBO workItemBO = new WorkItemBO();
        workItemBO.setWorkItemId(this.workItemId);
        workItemBO.setConstructionId(this.constructionId);
        workItemBO.setCatWorkItemTypeId(this.catWorkItemTypeId);
        workItemBO.setCode(this.code);
        workItemBO.setName(this.name);
        workItemBO.setIsInternal(this.isInternal);
        workItemBO.setConstructorId(this.constructorId);
        workItemBO.setSupervisorId(this.supervisorId);
        workItemBO.setStartingDate(this.startingDate);
        workItemBO.setCompleteDate(this.completeDate);
        workItemBO.setStatus(this.status);
        workItemBO.setQuantity(this.quantity);
        workItemBO.setApproveQuantity(this.approveQuantity);
        workItemBO.setApproveState(this.approveState);
        workItemBO.setApproveDate(this.approveDate);
        workItemBO.setApproveUserId(this.approveUserId);
        workItemBO.setApproveDescription(this.approveDescription);
        workItemBO.setCreatedDate(this.createdDate);
        workItemBO.setCreatedUserId(this.createdUserId);
        workItemBO.setCreatedGroupId(this.createdGroupId);
        workItemBO.setUpdatedDate(this.updatedDate);
        workItemBO.setUpdatedUserId(this.updatedUserId);
        workItemBO.setUpdatedGroupId(this.updatedGroupId);
        workItemBO.setPerformerId(this.performerId);
        workItemBO.setCatWorkItemGroupId(catWorkItemGroupId);
        workItemBO.setAmount(this.amount);
        workItemBO.setPrice(this.price);
        workItemBO.setTotalAmountChest(this.totalAmountChest);
        workItemBO.setPriceChest(this.priceChest);
        workItemBO.setTotalAmountGate(this.totalAmountGate);
        workItemBO.setPriceGate(this.priceGate);
        workItemBO.setBranch(this.branch);
        return workItemBO;
    }

    public java.lang.Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(java.lang.Long performerId) {
        this.performerId = performerId;
    }

    public java.lang.Long getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(java.lang.Long workItemId) {
        this.workItemId = workItemId;
    }

    public java.lang.Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(java.lang.Long constructionId) {
        this.constructionId = constructionId;
    }

    public java.lang.Long getCatWorkItemTypeId() {
        return catWorkItemTypeId;
    }

    public void setCatWorkItemTypeId(java.lang.Long catWorkItemTypeId) {
        this.catWorkItemTypeId = catWorkItemTypeId;
    }

    public java.lang.String getCode() {
        return code;
    }

    public void setCode(java.lang.String code) {
        this.code = code;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(java.lang.String isInternal) {
        this.isInternal = isInternal;
    }

    public java.lang.Long getConstructorId() {
        return constructorId;
    }

    public void setConstructorId(java.lang.Long constructorId) {
        this.constructorId = constructorId;
    }

    public java.lang.Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(java.lang.Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public java.util.Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(java.util.Date startingDate) {
        this.startingDate = startingDate;
    }

    public java.util.Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(java.util.Date completeDate) {
        this.completeDate = completeDate;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public java.lang.Double getQuantity() {
        return quantity;
    }

    public void setQuantity(java.lang.Double quantity) {
        this.quantity = quantity;
    }

    public java.lang.Double getApproveQuantity() {
        return approveQuantity;
    }

    public void setApproveQuantity(java.lang.Double approveQuantity) {
        this.approveQuantity = approveQuantity;
    }

    public java.lang.String getApproveState() {
        return approveState;
    }

    public void setApproveState(java.lang.String approveState) {
        this.approveState = approveState;
    }

    public java.util.Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(java.util.Date approveDate) {
        this.approveDate = approveDate;
    }

    public java.lang.Long getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(java.lang.Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public java.lang.String getApproveDescription() {
        return approveDescription;
    }

    public void setApproveDescription(java.lang.String approveDescription) {
        this.approveDescription = approveDescription;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public java.lang.Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(java.lang.Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public java.lang.Long getCreatedGroupId() {
        return createdGroupId;
    }

    public void setCreatedGroupId(java.lang.Long createdGroupId) {
        this.createdGroupId = createdGroupId;
    }

    public java.util.Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(java.util.Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public java.lang.Long getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(java.lang.Long updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public java.lang.Long getUpdatedGroupId() {
        return updatedGroupId;
    }

    public void setUpdatedGroupId(java.lang.Long updatedGroupId) {
        this.updatedGroupId = updatedGroupId;
    }
    
    

    public java.lang.Long getCatWorkItemGroupId() {
		return catWorkItemGroupId;
	}

	public void setCatWorkItemGroupId(java.lang.Long catWorkItemGroupId) {
		this.catWorkItemGroupId = catWorkItemGroupId;
	}

	@Override
    public Long getFWModelId() {
        return workItemId;
    }

    @Override
    public String catchName() {
        return getWorkItemId().toString();
    }
    private Double amount;
    private Double price;
    private Double totalAmountChest;
    private Double priceChest;
    private Double totalAmountGate;
    private Double priceGate;
    private String constructorName;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalAmountChest() {
		return totalAmountChest;
	}

	public void setTotalAmountChest(Double totalAmountChest) {
		this.totalAmountChest = totalAmountChest;
	}

	public Double getPriceChest() {
		return priceChest;
	}

	public void setPriceChest(Double priceChest) {
		this.priceChest = priceChest;
	}

	public Double getTotalAmountGate() {
		return totalAmountGate;
	}

	public void setTotalAmountGate(Double totalAmountGate) {
		this.totalAmountGate = totalAmountGate;
	}

	public Double getPriceGate() {
		return priceGate;
	}

	public void setPriceGate(Double priceGate) {
		this.priceGate = priceGate;
	}

	public String getConstructorName() {
		return constructorName;
	}

	public void setConstructorName(String constructorName) {
		this.constructorName = constructorName;
	}

	//Huypq-20191008-start
	private String sysGroupName;
	private String constructionCode;
	private String workItemName;
	private String email;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updateDate;
	private Long sysGroupId;
	private String catStationCode;
	private String projectName;
	
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCatStationCode() {
		return catStationCode;
	}

	public void setCatStationCode(String catStationCode) {
		this.catStationCode = catStationCode;
	}

	public Long getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}

	public String getSysGroupName() {
		return sysGroupName;
	}

	public void setSysGroupName(String sysGroupName) {
		this.sysGroupName = sysGroupName;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public String getWorkItemName() {
		return workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	//huy-end
   
	//Huypq-20191021-start
	private Long typeImport;
	private String cntContractCode;
	
	public String getCntContractCode() {
		return cntContractCode;
	}

	public void setCntContractCode(String cntContractCode) {
		this.cntContractCode = cntContractCode;
	}

	public Long getTypeImport() {
		return typeImport;
	}

	public void setTypeImport(Long typeImport) {
		this.typeImport = typeImport;
	}
	//Huy-end

	public java.lang.String getWoType() {
		return woType;
	}

	public void setWoType(java.lang.String woType) {
		this.woType = woType;
	}
	

	public java.lang.String getBranch() {
		return branch;
	}

	public void setBranch(java.lang.String branch) {
		this.branch = branch;
	}
	
}
