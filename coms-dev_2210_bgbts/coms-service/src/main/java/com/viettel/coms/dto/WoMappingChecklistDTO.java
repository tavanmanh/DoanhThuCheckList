package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.coms.bo.WoMappingAttachBO;
import com.viettel.coms.bo.WoMappingChecklistBO;

@XmlRootElement(name = "WO_MAPPING_CHECKLISTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoMappingChecklistDTO extends ComsBaseFWDTO<WoMappingChecklistBO> {
    private Long id;
    private Long woId;
    private String woName;
    private Long checklistId;
    private String checklistName;
    private String state;
    private Long status;
    private List<ImgChecklistDTO> lstImgs;
    private Long ftId;
    private String quantityByDate;
    private Double quantityLength;
    private Double addedQuantityLength;
    private Double price;
    private String loggedInUser;
    private Double totalAmount;
    private Double currentAmount;
    private Double remainAmount;
    private Double completedAmount;
    private Double value;
    private Long confirm;
    private Date confirmDate;
    private String confirmBy;
    private Long hshc;
    private Integer numImgRequire;
    private String tthqResult;

    private Long classId;
    private String className;
    private Long defaultClassValue;
    private Long actualValue;
    private String dbhtVuong;
    private List<WoMappingAttachBO> listWoMappingAttach;

    //Adding checklist for AVG
    private String customerConfirmDate;
    private String rejectReason;
    private String productCode;
    public String getCustomerConfirmDate() {
        return customerConfirmDate;
    }

    public void setCustomerConfirmDate(String customerConfirmDate) {
        this.customerConfirmDate = customerConfirmDate;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    //end add checklist for avg


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public String getWoName() {
        return woName;
    }

    public void setWoName(String woName) {
        this.woName = woName;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public List<ImgChecklistDTO> getLstImgs() {
        return lstImgs;
    }

    public void setLstImgs(List<ImgChecklistDTO> lstImgs) {
        this.lstImgs = lstImgs;
    }

    public Long getFtId() {
        return ftId;
    }

    public void setFtId(Long ftId) {
        this.ftId = ftId;
    }

    public String getQuantityByDate() {
        return quantityByDate;
    }

    public void setQuantityByDate(String quantityByDate) {
        this.quantityByDate = quantityByDate;
    }

    public Double getQuantityLength() {
        return quantityLength;
    }

    public void setQuantityLength(Double quantityLength) {
        this.quantityLength = quantityLength;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAddedQuantityLength() {
        return addedQuantityLength;
    }

    public void setAddedQuantityLength(Double addedQuantityLength) {
        this.addedQuantityLength = addedQuantityLength;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Double getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Double remainAmount) {
        this.remainAmount = remainAmount;
    }

    public Double getCompletedAmount() {
        return completedAmount;
    }

    public void setCompletedAmount(Double completedAmount) {
        this.completedAmount = completedAmount;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getConfirm() {
        return confirm;
    }

    public void setConfirm(Long confirm) {
        this.confirm = confirm;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    public Long getHshc() {
        return hshc;
    }

    public void setHshc(Long hshc) {
        this.hshc = hshc;
    }

    @Override
    public WoMappingChecklistBO toModel() {
        WoMappingChecklistBO bo = new WoMappingChecklistBO();

        bo.setCheckListId(this.checklistId);
        bo.setWoId(this.woId);
        bo.setId(this.id);
        bo.setState(this.state);
        bo.setStatus(this.status);
        bo.setQuantityByDate(this.quantityByDate);
        bo.setQuantityLength(this.quantityLength);
        bo.setAddedQuantityLength(this.addedQuantityLength);
        bo.setName(this.name);
        bo.setNumImgRequire(this.numImgRequire);
        bo.setTthqResult(this.tthqResult);
        bo.setClassId(this.classId);
        bo.setActualValue(this.actualValue);
        bo.setDbhtVuong(this.dbhtVuong);

        return bo;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }

    //additional info
    private String name;
    private Double unapprovedQuantity;
    private Long sysUserId;
    private String woState;
    private Long catConstructionTypeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getUnapprovedQuantity() {
        return unapprovedQuantity;
    }

    public void setUnapprovedQuantity(Double unapprovedQuantity) {
        this.unapprovedQuantity = unapprovedQuantity;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getWoState() {
        return woState;
    }

    public void setWoState(String woState) {
        this.woState = woState;
    }

    public Long getCatConstructionTypeId() {
        return catConstructionTypeId;
    }

    public void setCatConstructionTypeId(Long catConstructionTypeId) {
        this.catConstructionTypeId = catConstructionTypeId;
    }

    public Integer getNumImgRequire() {
        return numImgRequire;
    }

    public void setNumImgRequire(Integer numImgRequire) {
        this.numImgRequire = numImgRequire;
    }

    public String getTthqResult() {
        return tthqResult;
    }

    public void setTthqResult(String tthqResult) {
        this.tthqResult = tthqResult;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getDefaultClassValue() {
        return defaultClassValue;
    }

    public void setDefaultClassValue(Long defaultClassValue) {
        this.defaultClassValue = defaultClassValue;
    }

    public Long getActualValue() {
        return actualValue;
    }

    public void setActualValue(Long actualValue) {
        this.actualValue = actualValue;
    }

    public String getDbhtVuong() {
        return dbhtVuong;
    }

    public void setDbhtVuong(String dbhtVuong) {
        this.dbhtVuong = dbhtVuong;
    }

	public List<WoMappingAttachBO> getListWoMappingAttach() {
		return listWoMappingAttach;
	}

	public void setListWoMappingAttach(List<WoMappingAttachBO> listWoMappingAttach) {
		this.listWoMappingAttach = listWoMappingAttach;
	}
    
}
