package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.AssignHandoverDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

//VietNT_20181210_created
@Entity
@Table(name = "ASSIGN_HANDOVER")
public class AssignHandoverBO extends BaseFWModelImpl {

    private Long assignHandoverId;

    private Long sysGroupId;

    private String sysGroupCode;

    private String sysGroupName;

    private Long catProvinceId;

    private String catProvinceCode;

    private Long catStationHouseId;

    private String catStationHouseCode;

    private Long catStationId;

    private String catStationCode;

    private Long constructionId;

    private String constructionCode;

    private Long cntContractId;

    private String cntContractCode;

    private Long isDesign;

    private Date companyAssignDate;

    private Date createDate;

    private Long createUserId;

    private Date updateDate;

    private Long updateUserId;

    private Long status;

    private Long performentId;

    //VietNT_20181220_start
//    @Column(name = "EMAIL", length = 50)
//    private String email;
    //VietNT_end

    private Date departmentAssignDate;

    private Long receivedStatus;

    private Long outOfDateReceived;

    private Long outOfDateStartDate;

    private Date receivedObstructDate;

    private String receivedObstructContent;

    private Date receivedGoodsDate;

    private String receivedGoodsContent;

    private Date receivedDate;

    private Date deliveryConstructionDate;

    private Long performentConstructionId;

    private String performentConstructionName;

    private Long supervisorConstructionId;

    private String supervisorConstructionName;

    private Date startingDate;

    private Long constructionStatus;

    private Long columnHeight;

    private Long stationType;

    private Long numberCo;

    private Long houseTypeId;

    private String houseTypeName;

    private Long groundingTypeId;

    private String groundingTypeName;

    private String haveWorkItemName;

    private Long isFence;

    private Long outOfDateConstruction;

    private String partnerName;

    //HuyPQ-start-20190315
    private Long areaId;
    
    private String areaCode;
    
    private Date ttkvAssignDate;
    
    private String isDelivered;
    
    private String availableColumns;
//    @Column(name = "PLANT_COLUMNS")
//    private String plantColumns;
    private String cableInTankDrain;
    
    private String cableInTank;
    
    private String hiddenImmediacy;
    
    private String totalLength;
    
    @Column(name = "AVAILABLE_COLUMNS")
	public String getAvailableColumns() {
		return availableColumns;
	}

	public void setAvailableColumns(String availableColumns) {
		this.availableColumns = availableColumns;
	}

//	public String getPlantColumns() {
//		return plantColumns;
//	}
//
//	public void setPlantColumns(String plantColumns) {
//		this.plantColumns = plantColumns;
//	}

	@Column(name = "CABLE_IN_TANK_DRAIN")
	public String getCableInTankDrain() {
		return cableInTankDrain;
	}

	public void setCableInTankDrain(String cableInTankDrain) {
		this.cableInTankDrain = cableInTankDrain;
	}

	@Column(name = "CABLE_IN_TANK")
	public String getCableInTank() {
		return cableInTank;
	}

	public void setCableInTank(String cableInTank) {
		this.cableInTank = cableInTank;
	}

	@Column(name = "HIDDEN_IMMEDIACY")
	public String getHiddenImmediacy() {
		return hiddenImmediacy;
	}

	public void setHiddenImmediacy(String hiddenImmediacy) {
		this.hiddenImmediacy = hiddenImmediacy;
	}

	@Column(name = "TOTAL_LENGTH")
	public String getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(String totalLength) {
		this.totalLength = totalLength;
	}

	@Column(name = "IS_DELIVERED")
	public String getIsDelivered() {
		return isDelivered;
	}

	public void setIsDelivered(String isDelivered) {
		this.isDelivered = isDelivered;
	}

	@Column(name = "AREA_ID")
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "AREA_CODE")
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Column(name = "TTKV_ASSIGN_DATE")
	public Date getTtkvAssignDate() {
		return ttkvAssignDate;
	}

	public void setTtkvAssignDate(Date ttkvAssignDate) {
		this.ttkvAssignDate = ttkvAssignDate;
	}
	//HuyPq-end
	@Override
    public AssignHandoverDTO toDTO() {
        AssignHandoverDTO dto = new AssignHandoverDTO();
        dto.setAssignHandoverId(this.getAssignHandoverId());
        dto.setSysGroupId(this.getSysGroupId());
        dto.setSysGroupCode(this.getSysGroupCode());
        dto.setCatProvinceId(this.getCatProvinceId());
        dto.setCatProvinceCode(this.getCatProvinceCode());
        dto.setCatStationHouseId(this.getCatStationHouseId());
        dto.setCatStationHouseCode(this.getCatStationHouseCode());
        dto.setCatStationId(this.getCatStationId());
        dto.setCatStationCode(this.getCatStationCode());
        dto.setConstructionId(this.getConstructionId());
        dto.setConstructionCode(this.getConstructionCode());
        dto.setCntContractId(this.getCntContractId());
        dto.setCntContractCode(this.getCntContractCode());
        dto.setIsDesign(this.getIsDesign());
        dto.setCompanyAssignDate(this.getCompanyAssignDate());
        dto.setCreateDate(this.getCreateDate());
        dto.setCreateUserId(this.getCreateUserId());
        dto.setUpdateDate(this.getUpdateDate());
        dto.setUpdateUserId(this.getUpdateUserId());
        dto.setStatus(this.getStatus());
        dto.setPerformentId(this.getPerformentId());
        dto.setDepartmentAssignDate(this.getDepartmentAssignDate());
        dto.setReceivedStatus(this.getReceivedStatus());
        dto.setOutOfDateReceived(this.getOutOfDateReceived());
        dto.setOutOfDateStartDate(this.getOutOfDateStartDate());
        dto.setReceivedObstructDate(this.getReceivedObstructDate());
        dto.setReceivedObstructContent(this.getReceivedObstructContent());
        dto.setReceivedGoodsDate(this.getReceivedGoodsDate());
        dto.setReceivedGoodsContent(this.getReceivedGoodsContent());
        dto.setReceivedDate(this.getReceivedDate());
        dto.setDeliveryConstructionDate(this.getDeliveryConstructionDate());
        dto.setPerformentConstructionId(this.getPerformentConstructionId());
        dto.setPerformentConstructionName(this.getPerformentConstructionName());
        dto.setSupervisorConstructionId(this.getSupervisorConstructionId());
        dto.setSupervisorConstructionName(this.getSupervisorConstructionName());
        dto.setStartingDate(this.getStartingDate());
        dto.setConstructionStatus(this.getConstructionStatus());
        dto.setColumnHeight(this.getColumnHeight());
        dto.setStationType(this.getStationType());
        dto.setNumberCo(this.getNumberCo());
        dto.setHouseTypeId(this.getHouseTypeId());
        dto.setHouseTypeName(this.getHouseTypeName());
        dto.setGroundingTypeId(this.getGroundingTypeId());
        dto.setGroundingTypeName(this.getGroundingTypeName());
        dto.setHaveWorkItemName(this.getHaveWorkItemName());
        dto.setIsFence(this.getIsFence());
        dto.setSysGroupName(this.getSysGroupName());
        dto.setPartnerName(this.getPartnerName());
        //VietNT_20181220_start
//        dto.setEmail(this.getEmail());
        //VietNT_end
        dto.setOutOfDateConstruction(this.getOutOfDateConstruction());
        //HuyPQ-start
        dto.setAreaId(this.areaId);
        dto.setAreaCode(this.areaCode);
        dto.setTtkvAssignDate(this.ttkvAssignDate);
        dto.setIsDelivered(this.isDelivered);
        dto.setTotalLength(this.totalLength);
        dto.setHiddenImmediacy(this.hiddenImmediacy);
        dto.setCableInTank(this.cableInTank);
        dto.setCableInTankDrain(this.cableInTankDrain);
        dto.setPlantColunms(this.plantColunms);
        dto.setAvailableColumns(this.availableColumns);
        dto.setNumColumnsAvaible(this.getNumColumnsAvaible());
        dto.setLengthOfMeter(this.getLengthOfMeter());
        dto.setHaveStartPoint(this.getHaveStartPoint());
        dto.setTypeOfMeter(this.getTypeOfMeter());
        dto.setNumNewColumn(this.getNumNewColumn());
        dto.setTypeOfColumn(this.getTypeOfColumn());
        dto.setTypeConstructionBgmb(this.getTypeConstructionBgmb());
        //Huy-end
        return dto;
    }

	@Column(name = "PARTNER_NAME", length = 50)
    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    @Column(name = "OUT_OF_DATE_CONSTRUCTION", length = 2)
    public Long getOutOfDateConstruction() {
        return outOfDateConstruction;
    }

    public void setOutOfDateConstruction(Long outOfDateConstruction) {
        this.outOfDateConstruction = outOfDateConstruction;
    }

    @Column(name = "SYS_GROUP_NAME", length = 50)
    public String getSysGroupName() {
        return sysGroupName;
    }


    public void setSysGroupName(String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

//VietNT_20181220_start
//    public String getEmail() {
//        return email;
//    }
//    public void setEmail(String email) {
//        this.email = email;
//    }
//VietNT_end

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence", value = "ASSIGN_HANDOVER_SEQ")})
    @Column(name = "ASSIGN_HANDOVER_ID", length = 11)
    public Long getAssignHandoverId() {
        return assignHandoverId;
    }

    public void setAssignHandoverId(Long assignHandoverId) {
        this.assignHandoverId = assignHandoverId;
    }

    @Column(name = "SYS_GROUP_ID", length = 11)
    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    @Column(name = "SYS_GROUP_CODE", length = 50)
    public String getSysGroupCode() {
        return sysGroupCode;
    }

    public void setSysGroupCode(String sysGroupCode) {
        this.sysGroupCode = sysGroupCode;
    }

    @Column(name = "CAT_PROVINCE_ID", length = 10)
    public Long getCatProvinceId() {
        return catProvinceId;
    }

    public void setCatProvinceId(Long catProvinceId) {
        this.catProvinceId = catProvinceId;
    }

    @Column(name = "CAT_PROVINCE_CODE", length = 50)
    public String getCatProvinceCode() {
        return catProvinceCode;
    }

    public void setCatProvinceCode(String catProvinceCode) {
        this.catProvinceCode = catProvinceCode;
    }

    @Column(name = "CAT_STATION_HOUSE_ID", length = 10)
    public Long getCatStationHouseId() {
        return catStationHouseId;
    }

    public void setCatStationHouseId(Long catStationHouseId) {
        this.catStationHouseId = catStationHouseId;
    }

    @Column(name = "CAT_STATION_HOUSE_CODE", length = 50)
    public String getCatStationHouseCode() {
        return catStationHouseCode;
    }

    public void setCatStationHouseCode(String catStationHouseCode) {
        this.catStationHouseCode = catStationHouseCode;
    }

    @Column(name = "CAT_STATION_ID", length = 10)
    public Long getCatStationId() {
        return catStationId;
    }

    public void setCatStationId(Long catStationId) {
        this.catStationId = catStationId;
    }

    @Column(name = "CAT_STATION_CODE", length = 50)
    public String getCatStationCode() {
        return catStationCode;
    }

    public void setCatStationCode(String catStationCode) {
        this.catStationCode = catStationCode;
    }

    @Column(name = "CONSTRUCTION_ID", length = 10)
    public Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(Long constructionId) {
        this.constructionId = constructionId;
    }

    @Column(name = "CONSTRUCTION_CODE", length = 50)
    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
    }

    @Column(name = "CNT_CONTRACT_ID", length = 10)
    public Long getCntContractId() {
        return cntContractId;
    }

    public void setCntContractId(Long cntContractId) {
        this.cntContractId = cntContractId;
    }

    @Column(name = "CNT_CONTRACT_CODE", length = 50)
    public String getCntContractCode() {
        return cntContractCode;
    }

    public void setCntContractCode(String cntContractCode) {
        this.cntContractCode = cntContractCode;
    }

    @Column(name = "IS_DESIGN", length = 2)
    public Long getIsDesign() {
        return isDesign;
    }

    public void setIsDesign(Long isDesign) {
        this.isDesign = isDesign;
    }

    @Column(name = "COMPANY_ASSIGN_DATE", length = 22)
    public Date getCompanyAssignDate() {
        return companyAssignDate;
    }

    public void setCompanyAssignDate(Date companyAssignDate) {
        this.companyAssignDate = companyAssignDate;
    }

    @Column(name = "CREATE_DATE", length = 22)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "CREATE_USER_ID", length = 10)
    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    @Column(name = "UPDATE_DATE", length = 22)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "UPDATE_USER_ID", length = 10)
    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Column(name = "STATUS", length = 2)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "PERFORMENT_ID", length = 10)
    public Long getPerformentId() {
        return performentId;
    }

    public void setPerformentId(Long performentId) {
        this.performentId = performentId;
    }

    @Column(name = "DEPARTMENT_ASSIGN_DATE", length = 22)
    public Date getDepartmentAssignDate() {
        return departmentAssignDate;
    }

    public void setDepartmentAssignDate(Date departmentAssignDate) {
        this.departmentAssignDate = departmentAssignDate;
    }

    @Column(name = "RECEIVED_STATUS", length = 2)
    public Long getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(Long receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    @Column(name = "OUT_OF_DATE_RECEIVED", length = 10)
    public Long getOutOfDateReceived() {
        return outOfDateReceived;
    }

    public void setOutOfDateReceived(Long outOfDateReceived) {
        this.outOfDateReceived = outOfDateReceived;
    }

    @Column(name = "OUT_OF_DATE_START_DATE", length = 10)
    public Long getOutOfDateStartDate() {
        return outOfDateStartDate;
    }

    public void setOutOfDateStartDate(Long outOfDateStartDate) {
        this.outOfDateStartDate = outOfDateStartDate;
    }

    @Column(name = "RECEIVED_OBSTRUCT_DATE", length = 22)
    public Date getReceivedObstructDate() {
        return receivedObstructDate;
    }

    public void setReceivedObstructDate(Date receivedObstructDate) {
        this.receivedObstructDate = receivedObstructDate;
    }

    @Column(name = "RECEIVED_OBSTRUCT_CONTENT", length = 20)
    public String getReceivedObstructContent() {
        return receivedObstructContent;
    }

    public void setReceivedObstructContent(String receivedObstructContent) {
        this.receivedObstructContent = receivedObstructContent;
    }

    @Column(name = "RECEIVED_GOODS_DATE", length = 22)
    public Date getReceivedGoodsDate() {
        return receivedGoodsDate;
    }

    public void setReceivedGoodsDate(Date receivedGoodsDate) {
        this.receivedGoodsDate = receivedGoodsDate;
    }

    @Column(name = "RECEIVED_GOODS_CONTENT", length = 20)
    public String getReceivedGoodsContent() {
        return receivedGoodsContent;
    }

    public void setReceivedGoodsContent(String receivedGoodsContent) {
        this.receivedGoodsContent = receivedGoodsContent;
    }

    @Column(name = "RECEIVED_DATE", length = 22)
    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Column(name = "DELIVERY_CONSTRUCTION_DATE", length = 22)
    public Date getDeliveryConstructionDate() {
        return deliveryConstructionDate;
    }

    public void setDeliveryConstructionDate(Date deliveryConstructionDate) {
        this.deliveryConstructionDate = deliveryConstructionDate;
    }

    @Column(name = "PERFORMENT_CONSTRUCTION_ID", length = 10)
    public Long getPerformentConstructionId() {
        return performentConstructionId;
    }

    public void setPerformentConstructionId(Long performentConstructionId) {
        this.performentConstructionId = performentConstructionId;
    }

    @Column(name = "PERFORMENT_CONSTRUCTION_NAME", length = 50)
    public String getPerformentConstructionName() {
        return performentConstructionName;
    }

    public void setPerformentConstructionName(String performentConstructionName) {
        this.performentConstructionName = performentConstructionName;
    }

    @Column(name = "SUPERVISOR_CONSTRUCTION_ID", length = 10)
    public Long getSupervisorConstructionId() {
        return supervisorConstructionId;
    }

    public void setSupervisorConstructionId(Long supervisorConstructionId) {
        this.supervisorConstructionId = supervisorConstructionId;
    }

    @Column(name = "SUPERVISOR_CONSTRUCTION_NAME", length = 50)
    public String getSupervisorConstructionName() {
        return supervisorConstructionName;
    }

    public void setSupervisorConstructionName(String supervisorConstructionName) {
        this.supervisorConstructionName = supervisorConstructionName;
    }

    @Column(name = "STARTING_DATE", length = 22)
    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    @Column(name = "CONSTRUCTION_STATUS", length = 2)
    public Long getConstructionStatus() {
        return constructionStatus;
    }

    public void setConstructionStatus(Long constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    @Column(name = "COLUMN_HEIGHT", length = 10)
    public Long getColumnHeight() {
        return columnHeight;
    }

    public void setColumnHeight(Long columnHeight) {
        this.columnHeight = columnHeight;
    }

    @Column(name = "STATION_TYPE", length = 2)
    public Long getStationType() {
        return stationType;
    }

    public void setStationType(Long stationType) {
        this.stationType = stationType;
    }

    @Column(name = "NUMBER_CO", length = 2)
    public Long getNumberCo() {
        return numberCo;
    }

    public void setNumberCo(Long numberCo) {
        this.numberCo = numberCo;
    }

    @Column(name = "HOUSE_TYPE_ID", length = 10)
    public Long getHouseTypeId() {
        return houseTypeId;
    }

    public void setHouseTypeId(Long houseTypeId) {
        this.houseTypeId = houseTypeId;
    }

    @Column(name = "HOUSE_TYPE_NAME", length = 20)
    public String getHouseTypeName() {
        return houseTypeName;
    }

    public void setHouseTypeName(String houseTypeName) {
        this.houseTypeName = houseTypeName;
    }

    @Column(name = "GROUNDING_TYPE_ID", length = 10)
    public Long getGroundingTypeId() {
        return groundingTypeId;
    }

    public void setGroundingTypeId(Long groundingTypeId) {
        this.groundingTypeId = groundingTypeId;
    }

    @Column(name = "GROUNDING_TYPE_NAME", length = 20)
    public String getGroundingTypeName() {
        return groundingTypeName;
    }

    public void setGroundingTypeName(String groundingTypeName) {
        this.groundingTypeName = groundingTypeName;
    }

    @Column(name = "HAVE_WORK_ITEM_NAME", length = 20)
    public String getHaveWorkItemName() {
        return haveWorkItemName;
    }

    public void setHaveWorkItemName(String haveWorkItemName) {
        this.haveWorkItemName = haveWorkItemName;
    }

    @Column(name = "IS_FENCE", length = 2)
    public Long getIsFence() {
        return isFence;
    }

    public void setIsFence(Long isFence) {
        this.isFence = isFence;
    }
    
    /**Hoangnh start 04022019**/
	private String plantColunms;

	@Column(name = "PLANT_COLUMNS", length = 10)

	public String getPlantColunms() {
		return plantColunms;
	}

	public void setPlantColunms(String plantColunms) {
		this.plantColunms = plantColunms;
	}

	
	
	 /**Hoangnh end 04022019**/
	
	//Huypq-20190826-start
	private String numColumnsAvaible;
    private String lengthOfMeter;
    private String haveStartPoint;
    private String typeOfMeter;
    private String numNewColumn;
    private String typeOfColumn;
    private String typeConstructionBgmb;

	@Column(name = "NUM_COLUMNS_AVAIBLE")
	public String getNumColumnsAvaible() {
		return numColumnsAvaible;
	}

	public void setNumColumnsAvaible(String numColumnsAvaible) {
		this.numColumnsAvaible = numColumnsAvaible;
	}

	@Column(name = "LENGTH_METER")
	public String getLengthOfMeter() {
		return lengthOfMeter;
	}

	public void setLengthOfMeter(String lengthOfMeter) {
		this.lengthOfMeter = lengthOfMeter;
	}


	@Column(name = "HAVE_START_POINT")
	public String getHaveStartPoint() {
		return haveStartPoint;
	}

	public void setHaveStartPoint(String haveStartPoint) {
		this.haveStartPoint = haveStartPoint;
	}

	@Column(name = "TYPE_METER")
	public String getTypeOfMeter() {
		return typeOfMeter;
	}

	public void setTypeOfMeter(String typeOfMeter) {
		this.typeOfMeter = typeOfMeter;
	}

	@Column(name = "NUM_NEW_COLUMN")
	public String getNumNewColumn() {
		return numNewColumn;
	}

	public void setNumNewColumn(String numNewColumn) {
		this.numNewColumn = numNewColumn;
	}

	@Column(name = "TYPE_COLUMN")
	public String getTypeOfColumn() {
		return typeOfColumn;
	}

	public void setTypeOfColumn(String typeOfColumn) {
		this.typeOfColumn = typeOfColumn;
	}

	@Column(name = "TYPE_CONSTRUCTION_BGMB")
	public String getTypeConstructionBgmb() {
		return typeConstructionBgmb;
	}


	public void setTypeConstructionBgmb(String typeConstructionBgmb) {
		this.typeConstructionBgmb = typeConstructionBgmb;
	}
	
	
	
	//huy-end
}
