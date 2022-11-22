package com.viettel.coms.bo;

import com.viettel.coms.dto.WoChecklistDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_CHECKLIST")
public class WoChecklistBO extends BaseFWModelImpl {

    private Long checklistId;
    private Long woId;
    private String checklistName;
    private Long type;
    private Long objectId;
    private Date dnqtDate;
    private Double dnqtValue;
    private Date vtnetSendDate;
    private Date vtnetConfirmDate;
    private Double vtnetConfirmValue;
    private Date aprovedDocDate;
    private String aprovedPerson;
    private Long status;
    private String state;
    private Long checklistOrder;
    private Long hasProblem;
    private Date resolveDueDate;
    private Long completePersonId;
    private String completePersonName;
    private Date completedDate;
    private String content;
    private Date problemDate;
    private String problemCode;
    private String problemName;
    private Long problemDeclarePersonId;
    private String problemDeclarePersonName;
    private Double vtnetSentValue;
    private Double finalValue;
    private Double aprovedDocValue;
    private Date finalDate;

    private String customerConfirmDate;
    private String rejectReason;
    private String productCode;
    private String productCodeId;
    private Long catStockId;
    private String catStockName;
    private String settopBox;
    private String smartCard;

    @Column(name = "SETTOP_BOX")
    public String getSettopBox() {
        return settopBox;
    }

    public void setSettopBox(String settopBox) {
        this.settopBox = settopBox;
    }

    @Column(name = "SMART_CARD")
    public String getSmartCard() {
        return smartCard;
    }

    public void setSmartCard(String smartCard) {
        this.smartCard = smartCard;
    }


    @Column(name = "CAT_STOCK_ID")
    public Long getCatStockId() {
        return catStockId;
    }

    public void setCatStockId(Long catStockId) {
        this.catStockId = catStockId;
    }

    @Column(name = "CAT_STOCK_NAME")
    public String getCatStockName() {
        return catStockName;
    }

    public void setCatStockName(String catStockName) {
        this.catStockName = catStockName;
    }

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WO_CHECKLIST_SEQ")})
    @Column(name = "ID")
    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    @Column(name = "WO_ID")
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "CHECKLIST_NAME")
    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    @Column(name = "TYPE")
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Column(name = "OBJECT_ID")
    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    @Column(name = "DNQT_DATE")
    public Date getDnqtDate() {
        return dnqtDate;
    }

    public void setDnqtDate(Date dnqtDate) {
        this.dnqtDate = dnqtDate;
    }

    @Column(name = "DNQT_VALUE")
    public Double getDnqtValue() {
        return dnqtValue;
    }

    public void setDnqtValue(Double dnqtValue) {
        this.dnqtValue = dnqtValue;
    }

    @Column(name = "VTNET_SEND_DATE")
    public Date getVtnetSendDate() {
        return vtnetSendDate;
    }

    public void setVtnetSendDate(Date vtnetSendDate) {
        this.vtnetSendDate = vtnetSendDate;
    }

    @Column(name = "VTNET_CONFIRM_DATE")
    public Date getVtnetConfirmDate() {
        return vtnetConfirmDate;
    }

    public void setVtnetConfirmDate(Date vtnetConfirmDate) {
        this.vtnetConfirmDate = vtnetConfirmDate;
    }

    @Column(name = "VTNET_CONFIRM_VALUE")
    public Double getVtnetConfirmValue() {
        return vtnetConfirmValue;
    }

    public void setVtnetConfirmValue(Double vtnetConfirmValue) {
        this.vtnetConfirmValue = vtnetConfirmValue;
    }

    @Column(name = "APROVED_PERSON")
    public String getAprovedPerson() {
        return aprovedPerson;
    }

    public void setAprovedPerson(String aprovedPerson) {
        this.aprovedPerson = aprovedPerson;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "CHECKLIST_ORDER")
    public Long getChecklistOrder() {
        return checklistOrder;
    }

    public void setChecklistOrder(Long checklistOrder) {
        this.checklistOrder = checklistOrder;
    }

    @Column(name = "HAS_PROBLEM")
    public Long getHasProblem() {
        return hasProblem;
    }

    public void setHasProblem(Long hasProblem) {
        this.hasProblem = hasProblem;
    }

    @Column(name = "RESOLVE_DUE_DATE")
    public Date getResolveDueDate() {
        return resolveDueDate;
    }

    public void setResolveDueDate(Date resolveDueDate) {
        this.resolveDueDate = resolveDueDate;
    }

    @Column(name = "COMPLETE_PERSON_ID")
    public Long getCompletePersonId() {
        return completePersonId;
    }

    public void setCompletePersonId(Long completePersonId) {
        this.completePersonId = completePersonId;
    }

    @Column(name = "COMPLETE_PERSON_NAME")
    public String getCompletePersonName() {
        return completePersonName;
    }

    public void setCompletePersonName(String completePersonName) {
        this.completePersonName = completePersonName;
    }

    @Column(name = "COMPLETED_DATE")
    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "PROBLEM_DATE")
    public Date getProblemDate() {
        return problemDate;
    }

    public void setProblemDate(Date problemDate) {
        this.problemDate = problemDate;
    }

    @Column(name = "PROBLEM_CODE")
    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }

    @Column(name = "PROBLEM_NAME")
    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    @Column(name = "PROBLEM_DECLARE_PERSON_ID")
    public Long getProblemDeclarePersonId() {
        return problemDeclarePersonId;
    }

    public void setProblemDeclarePersonId(Long problemDeclarePersonId) {
        this.problemDeclarePersonId = problemDeclarePersonId;
    }

    @Column(name = "PROBLEM_DECLARE_PERSON_NAME")
    public String getProblemDeclarePersonName() {
        return problemDeclarePersonName;
    }

    public void setProblemDeclarePersonName(String problemDeclarePersonName) {
        this.problemDeclarePersonName = problemDeclarePersonName;
    }

    @Column(name = "VTNET_SENT_VALUE")
    public Double getVtnetSentValue() {
        return vtnetSentValue;
    }

    public void setVtnetSentValue(Double vtnetSentValue) {
        this.vtnetSentValue = vtnetSentValue;
    }

    @Column(name = "FINAL_DATE")
    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    @Column(name = "FINAL_VALUE")
    public Double getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(Double finalValue) {
        this.finalValue = finalValue;
    }

    @Column(name = "APROVED_DOC_DATE")
    public Date getAprovedDocDate() {
        return aprovedDocDate;
    }

    public void setAprovedDocDate(Date aprovedDocDate) {
        this.aprovedDocDate = aprovedDocDate;
    }

    @Column(name = "APROVE_DOC_VALUE")
    public Double getAprovedDocValue() {
        return aprovedDocValue;
    }

    public void setAprovedDocValue(Double aprovedDocValue) {
        this.aprovedDocValue = aprovedDocValue;
    }

    @Column(name = "CUSTOMER_CONFIRM_DATE")
    public String getCustomerConfirmDate() {
        return customerConfirmDate;
    }

    public void setCustomerConfirmDate(String customerConfirmDate) {
        this.customerConfirmDate = customerConfirmDate;
    }

    @Column(name = "REJECT_REASON")
    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    @Column(name = "PRODUCT_CODE")
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Column(name = "PRODUCT_CODE_ID")
    public String getProductCodeId() {
        return productCodeId;
    }

    public void setProductCodeId(String productCodeId) {
        this.productCodeId = productCodeId;
    }

    private String code;

    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public WoChecklistDTO toDTO() {
        WoChecklistDTO dto = new WoChecklistDTO();

        dto.setChecklistId(this.checklistId);
        dto.setWoId(this.woId);
        dto.setChecklistName(this.checklistName);
        dto.setType(this.type);
        dto.setObjectId(this.objectId);
        dto.setDnqtDate(this.dnqtDate);
        dto.setDnqtValue(this.dnqtValue);
        dto.setVtnetSendDate(this.vtnetSendDate);
        dto.setVtnetSentValue(this.vtnetSentValue);
        dto.setVtnetConfirmDate(this.vtnetConfirmDate);
        dto.setVtnetConfirmValue(this.vtnetConfirmValue);
        dto.setAprovedDocDate(this.aprovedDocDate);
        dto.setAprovedDocValue(this.aprovedDocValue);
        dto.setFinalValue(this.finalValue);
        dto.setFinalDate(this.finalDate);
        dto.setRejectReason(this.rejectReason);
        dto.setAprovedPerson(this.aprovedPerson);
        dto.setStatus(this.status);
        dto.setState(this.state);
        dto.setChecklistOrder(this.checklistOrder);
        dto.setHasProblem(this.hasProblem);
        dto.setResolveDueDate(this.resolveDueDate);
        dto.setCompletePersonId(this.completePersonId);
        dto.setCompletePersonName(this.completePersonName);
        dto.setCompletedDate(this.completedDate);
        dto.setContent(this.content);
        dto.setProblemDate(this.problemDate);
        dto.setProblemCode(this.problemCode);
        dto.setProblemName(this.problemName);
        dto.setProblemDeclarePersonId(this.problemDeclarePersonId);
        dto.setProblemDeclarePersonName(this.problemDeclarePersonName);
        dto.setCustomerConfirmDate(this.customerConfirmDate);
        dto.setProductCode(this.productCode);
        dto.setProductCodeId(this.productCodeId);
        dto.setCatStockId(this.catStockId);
        dto.setCatStockName(this.catStockName);
        dto.setCode(this.code);
        dto.setSettopBox(this.settopBox);
        dto.setSmartCard(this.smartCard);
        return dto;
    }
}
