package com.viettel.coms.dto;

import com.viettel.coms.bo.WoChecklistBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoChecklistDTO extends ComsBaseFWDTO<WoChecklistBO> {

    private Long checklistId;
    private Long woId;
    private String checklistName;
    private Long type;
    private Long objectId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dnqtDate;
    private Double dnqtValue;

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date vtnetSendDate;
    private Double vtnetSentValue;

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date vtnetConfirmDate;
    private Double vtnetConfirmValue;

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date aprovedDocDate;
    private Double aprovedDocValue;

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finalDate;
    private Double finalValue;

    private String aprovedPerson;
    private Long status;
    private String state;
    private Long checklistOrder;
    private Long hasProblem;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date resolveDueDate;
    private List<ImgChecklistDTO> lstImgs;
    private String userCreated;
    private Long completePersonId;
    private String completePersonName;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date completedDate;
    private String content;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date problemDate;
    private String problemCode;
    private String problemName;
    private Long problemDeclarePersonId;
    private String problemDeclarePersonName;
    private String loggedInUser;
    private List<WoMappingAttachDTO> attachmentLst;

    private String emailTcTct;

    private String customerConfirmDate;
    private String rejectReason;
    private String productCode;
    private String productCodeId;
    private Long catStockId;
    private String catStockName;


    private String settopBox;
    private String smartCard;

    public Long getCatStockId() {
        return catStockId;
    }

    public void setCatStockId(Long catStockId) {
        this.catStockId = catStockId;
    }

    public String getCatStockName() {
        return catStockName;
    }

    public void setCatStockName(String catStockName) {
        this.catStockName = catStockName;
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

    public String getProductCodeId() {
        return productCodeId;
    }

    public void setProductCodeId(String productCodeId) {
        this.productCodeId = productCodeId;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Date getDnqtDate() {
        return dnqtDate;
    }

    public void setDnqtDate(Date dnqtDate) {
        this.dnqtDate = dnqtDate;
    }

    public Double getDnqtValue() {
        return dnqtValue;
    }

    public void setDnqtValue(Double dnqtValue) {
        this.dnqtValue = dnqtValue;
    }

    public Date getVtnetSendDate() {
        return vtnetSendDate;
    }

    public void setVtnetSendDate(Date vtnetSendDate) {
        this.vtnetSendDate = vtnetSendDate;
    }

    public Date getVtnetConfirmDate() {
        return vtnetConfirmDate;
    }

    public void setVtnetConfirmDate(Date vtnetConfirmDate) {
        this.vtnetConfirmDate = vtnetConfirmDate;
    }

    public Date getAprovedDocDate() {
        return aprovedDocDate;
    }

    public void setAprovedDocDate(Date aprovedDocDate) {
        this.aprovedDocDate = aprovedDocDate;
    }

    public String getAprovedPerson() {
        return aprovedPerson;
    }

    public void setAprovedPerson(String aprovedPerson) {
        this.aprovedPerson = aprovedPerson;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getChecklistOrder() {
        return checklistOrder;
    }

    public void setChecklistOrder(Long checklistOrder) {
        this.checklistOrder = checklistOrder;
    }

    public Long getHasProblem() {
        return hasProblem;
    }

    public void setHasProblem(Long hasProblem) {
        this.hasProblem = hasProblem;
    }

    public Date getResolveDueDate() {
        return resolveDueDate;
    }

    public void setResolveDueDate(Date resolveDueDate) {
        this.resolveDueDate = resolveDueDate;
    }

    public List<ImgChecklistDTO> getLstImgs() {
        return lstImgs;
    }

    public void setLstImgs(List<ImgChecklistDTO> lstImgs) {
        this.lstImgs = lstImgs;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Long getCompletePersonId() {
        return completePersonId;
    }

    public void setCompletePersonId(Long completePersonId) {
        this.completePersonId = completePersonId;
    }

    public String getCompletePersonName() {
        return completePersonName;
    }

    public void setCompletePersonName(String completePersonName) {
        this.completePersonName = completePersonName;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getProblemDate() {
        return problemDate;
    }

    public void setProblemDate(Date problemDate) {
        this.problemDate = problemDate;
    }

    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public Long getProblemDeclarePersonId() {
        return problemDeclarePersonId;
    }

    public void setProblemDeclarePersonId(Long problemDeclarePersonId) {
        this.problemDeclarePersonId = problemDeclarePersonId;
    }

    public String getProblemDeclarePersonName() {
        return problemDeclarePersonName;
    }

    public void setProblemDeclarePersonName(String problemDeclarePersonName) {
        this.problemDeclarePersonName = problemDeclarePersonName;
    }

    public Double getVtnetSentValue() {
        return vtnetSentValue;
    }

    public void setVtnetSentValue(Double vtnetSentValue) {
        this.vtnetSentValue = vtnetSentValue;
    }

    public Double getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(Double finalValue) {
        this.finalValue = finalValue;
    }

    public Double getAprovedDocValue() {
        return aprovedDocValue;
    }

    public void setAprovedDocValue(Double aprovedDocValue) {
        this.aprovedDocValue = aprovedDocValue;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Double getVtnetConfirmValue() {
        return vtnetConfirmValue;
    }

    public void setVtnetConfirmValue(Double vtnetConfirmValue) {
        this.vtnetConfirmValue = vtnetConfirmValue;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public String getEmailTcTct() {
        return emailTcTct;
    }

    public void setEmailTcTct(String emailTcTct) {
        this.emailTcTct = emailTcTct;
    }

    public String getCustomerConfirmDate() {
        return customerConfirmDate;
    }

    public void setCustomerConfirmDate(String customerConfirmDate) {
        this.customerConfirmDate = customerConfirmDate;
    }

    public String getSettopBox() {
        return settopBox;
    }

    public void setSettopBox(String settopBox) {
        this.settopBox = settopBox;
    }

    public String getSmartCard() {
        return smartCard;
    }

    public void setSmartCard(String smartCard) {
        this.smartCard = smartCard;
    }

	public List<WoMappingAttachDTO> getAttachmentLst() {
		return attachmentLst;
	}

	public void setAttachmentLst(List<WoMappingAttachDTO> attachmentLst) {
		this.attachmentLst = attachmentLst;
	}

	@Override
    public WoChecklistBO toModel() {
        WoChecklistBO bo = new WoChecklistBO();

        bo.setChecklistId(this.checklistId);
        bo.setWoId(this.woId);
        bo.setChecklistName(this.checklistName);
        bo.setType(this.type);
        bo.setObjectId(this.objectId);

        bo.setDnqtDate(this.dnqtDate);
        bo.setDnqtValue(this.dnqtValue);

        bo.setVtnetSendDate(this.vtnetSendDate);
        bo.setVtnetSentValue(this.vtnetSentValue);

        bo.setVtnetConfirmDate(this.vtnetConfirmDate);
        bo.setVtnetConfirmValue(this.vtnetConfirmValue);

        bo.setAprovedDocDate(this.aprovedDocDate);
        bo.setAprovedDocValue(this.aprovedDocValue);

        bo.setFinalDate(this.finalDate);
        bo.setFinalValue(this.finalValue);

        bo.setAprovedPerson(this.aprovedPerson);
        bo.setStatus(this.status);
        bo.setState(this.state);
        bo.setChecklistOrder(this.checklistOrder);
        bo.setHasProblem(this.hasProblem);
        bo.setResolveDueDate(this.resolveDueDate);
        bo.setCompletePersonId(this.completePersonId);
        bo.setCompletePersonName(this.completePersonName);
        bo.setCompletedDate(this.completedDate);
        bo.setContent(this.content);
        bo.setProblemDate(this.problemDate);
        bo.setProblemCode(this.problemCode);
        bo.setProblemName(this.problemName);
        bo.setProblemDeclarePersonId(this.problemDeclarePersonId);
        bo.setProblemDeclarePersonName(this.problemDeclarePersonName);
        bo.setCustomerConfirmDate(this.customerConfirmDate);
        bo.setProductCode(this.getProductCode());
        bo.setProductCodeId(this.getProductCodeId());
        bo.setCatStockId(this.getCatStockId());
        bo.setCatStockName(this.getCatStockName());
        bo.setSettopBox(this.getSettopBox());
        bo.setSmartCard(this.getSmartCard());
        bo.setCode(this.getCode());
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
}
