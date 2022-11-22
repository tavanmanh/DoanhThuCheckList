/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.erp.bo;

import com.viettel.erp.dto.CntContractDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CNT_CONTRACT")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class CntContractBO extends BaseFWModelImpl {

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "CNT_CONTRACT_SEQ")})
    @Column(name = "CNT_CONTRACT_ID", length = 22)
    private java.lang.Long cntContractId;
    @Column(name = "CODE", length = 200)
    private java.lang.String code;
    @Column(name = "NAME", length = 1000)
    private java.lang.String name;
    @Column(name = "CONTRACT_CODE_KTTS", length = 200)
    private java.lang.String contractCodeKtts;
    @Column(name = "CONTENT", length = 4000)
    private java.lang.String content;
    @Column(name = "SIGN_DATE", length = 7)
    private java.util.Date signDate;
    @Column(name = "START_TIME", length = 7)
    private java.util.Date startTime;
    @Column(name = "END_TIME", length = 7)
    private java.util.Date endTime;
    @Column(name = "PRICE", length = 22)
    private java.lang.Double price;
    @Column(name = "APPENDIX_CONTRACT", length = 22)
    private java.lang.Double appendixContract;
    @Column(name = "NUM_STATION", length = 22)
    private java.lang.Double numStation;
    @Column(name = "BIDDING_PACKAGE_ID", length = 22)
    private java.lang.Long biddingPackageId;
    @Column(name = "CAT_PARTNER_ID", length = 22)
    private java.lang.Long catPartnerId;
    @Column(name = "SIGNER_PARTNER", length = 200)
    private java.lang.String signerPartner;
    @Column(name = "SYS_GROUP_ID", length = 22)
    private java.lang.Long sysGroupId;
    @Column(name = "SIGNER_GROUP", length = 22)
    private java.lang.Long signerGroup;
    @Column(name = "SUPERVISOR", length = 200)
    private java.lang.String supervisor;
    @Column(name = "STATUS", length = 22)
    private java.lang.Long status;
    @Column(name = "FORMAL", length = 22)
    private java.lang.Double formal;
    @Column(name = "CONTRACT_TYPE", length = 22)
    private java.lang.Long contractType;
    @Column(name = "CNT_CONTRACT_PARENT_ID", length = 22)
    private java.lang.Double cntContractParentId;
    @Column(name = "CREATED_DATE", length = 7)
    private java.util.Date createdDate;
    @Column(name = "CREATED_USER_ID", length = 22)
    private java.lang.Long createdUserId;
    @Column(name = "CREATED_GROUP_ID", length = 22)
    private java.lang.Long createdGroupId;
    @Column(name = "UPDATED_DATE", length = 7)
    private java.util.Date updatedDate;
    @Column(name = "UPDATED_USER_ID", length = 22)
    private java.lang.Long updatedUserId;
    @Column(name = "UPDATED_GROUP_ID", length = 22)
    private java.lang.Long updatedGroupId;
    @Column(name = "DESCRIPTION", length = 2000)
    private String description;
    @Column(name = "STATE", length = 2)
    private Long state;
    @Column(name = "SYN_STATE", length = 2)
    private java.lang.Long synState;
    @Column(name = "NUM_DAY", length = 30)
    private java.lang.Double numDay;
    @Column(name = "FRAME_PARENT_ID", length = 30)
    private Long frameParentId;
    @Column(name = "MONEY_TYPE", length = 6)
    private Integer moneyType;
    @Column(name = "PROJECT_CONTRACT_ID", length = 22)
    private java.lang.Long projectContractId;

    //trungtv-start-18082022
    //copy properties from CNT_CONTRACT ims

    @Column(name = "EXTENSION_DAYS", length = 22)
    private java.lang.Long extensionDays;
    @Column(name = "PAYMENT_EXPRIED", length = 22)
    private java.lang.Long paymentExpried;
    @Column(name = "DEPLOYMENT_DATE", length = 22)
    private java.util.Date deploymentDate;
    @Column(name = "CODE_EXT", length = 220)
    private java.lang.String codeExt;
    @Column(name = "XDDD_TYPE", length = 220)
    private java.lang.Long xdddType;
    @Column(name = "WARRANTY_NUMBER_DAY", length = 220)
    private Long warrantyNumberDay;
    // hienlt56 start 04052020
    @Column(name = "AREA_CODE", length = 220)
    private java.lang.String areaCode;
    @Column(name = "APPROVE_CONTRACT", length = 220)
    private java.lang.String approveContract;
    @Column(name = "COMMENT_CONTRACT", length = 220)
    private java.lang.String commentContract;
    @Column(name = "APPROVED_USER_ID", length = 22)
    private java.lang.Long approvedUserId;
    @Column(name = "APPROVED_DATE", length = 7)
    private java.util.Date approvedDate;
    // taotq start 27052021
    @Column(name = "PROVINCE_CONSTRUCTION_ID", length = 30)
    private Long provinceConstructionId;

    @Column(name = "PROVINCE_CONSTRUCTION", length = 50)
    private String provinceConstruction;

    @Column(name = "DISTRICT_NAME", length = 200)
    private String districtName;

    @Column(name = "COMMUNE_NAME", length = 200)
    private String communeName;

    @Column(name = "USER_SEARCH_ID", length = 22)
    private Long userSearchId;

    @Column(name = "ADDRESS", length = 1000)
    private String address;

    @Column(name = "ROSE_CONTRACT", length = 22)
    private Double roseContract;

    @Column(name = "DISTRICT_GROUP_ID", length = 30)
    private Long districtGroupId;

    @Column(name = "CNT_CONTRACT_REVENUE", length = 22)
    private java.lang.Long cntContractRevenue;

    @Column(name = "CNT_GROUP_CONTRACT_TYPE", length = 22)
    private java.lang.Long  cntGroupContractType;

    @Column(name = "INTRODUCE_BRANCH", length = 22)
    private java.lang.Long  introduceBranch;

    @Column(name = "PERCENT", length = 22)
    private java.lang.Double  percent;

    @Column(name = "COST_INGERIT", length = 22)
    private java.lang.Double  costIngerit;

    @Column(name = "INTRODUCE_BRANCH_TYPE", length = 1)
    private java.lang.Long  introduceBranchType;

    @Column(name = "STATUS_EXTENSION", length = 22)
    private java.lang.Long statusExtension;

    @Column(name = "PROMULGATE_TTR_NUMBER", length = 200)
    private java.lang.String promulgateTTRNumber;
    @Column(name = "RATE_ADJUSTMENT_TTR_NUMBER", length = 200)
    private java.lang.String rateAdjustmentTTRNumber;
    @Column(name = "OPERATING_UNIT_DEPLOYS_CONTRACT", length = 200)
    private java.lang.String operatingUnitDeploysContract;
    @Column(name = "JOB_DESCRIPTON", length = 200)
    private java.lang.String jobDescription;
    @Column(name = "USER_PROMULGATE_ID", length = 22)
    private java.lang.Long userPromulgateId;
    @Column(name = "UPDATE_PROMULGATE")
    private Date updatePromulgate;
    @Column(name = "HOC_TAC")
    private Date hocTac;
    @Column(name = "ADVANCE_DATE")
    private Date advanceDate;
    @Column(name = "FIRST_PAYMENT_DATE")
    private Date firstPaymentDate;
    @Column(name = "LAST_PAYMENT_DATE")
    private Date lastPaymentDate;
    // taotq end 27052021

    //ducpm23 24062022
    @Column(name = "PRICE_VAT", length=22)
    private Double priceVat;
    //ducpm23-end

    //duonghv13 add 01122021
    @Column(name = "PURCHASE_PROPOSE", length = 1000)
    private String purchasePropose;

    @Column(name = "SOURCE_PURCHASE", length = 30)
    private String sourcePurchase;

    @Column(name = "FINACIAL_VOUCHER_RETURN_DATE", length = 12)
    private java.util.Date finacialVoucherReturnDate;

    @Column(name = "ACTUAL_RECEIVE_GOODS_DATE", length = 12)
    private java.util.Date actualReceiveGoodsDate;
    //duonghv13 end 01122021

    // Huypq-23042020-start
    @Column(name = "CONSTRUCTION_CODE", length = 1000)
    private String constructionCode;
    @Column(name = "STATION_CODE_VCC", length = 1000)
    private String stationCodeVcc;
    @Column(name = "ADDRESS_STATION", length = 1000)
    private String addressStation;
    @Column(name = "STATION_CODE_HTCT", length = 1000)
    private String stationCodeHtct;
    @Column(name = "VALIDITY_DATE", length = 10)
    private java.util.Date validityDate;
    @Column(name = "CAT_PROVINCE_CODE", length = 10)
    private String catProvinceCode;
    // Huy-end
    // Huypq-20191023-start
    @Column(name = "COEFFICIENT")
    private Double coefficient;
    // Huy-end
    // tatph start 8/10/2019
    @Column(name = "IS_XNXD", length = 22)
    private java.lang.Long isXNXD;
    @Column(name = "CONSTRUCTION_FORM", length = 22)
    private java.lang.Long constructionForm;
    @Column(name = "CURRENT_PROGRESS", length = 220)
    private java.lang.String currentProgess;
    @Column(name = "HANDOVER_USE_DATE")
    private java.util.Date handoverUseDate;
    @Column(name = "WARRANTY_EXPIRED_DATE")
    private java.util.Date warrantyExpiredDate;
    @Column(name = "STRUCTURE_FILTER", length = 22)
    private java.lang.Long structureFilter;
    @Column(name = "DESCRIPTION_XNXD", length = 220)
    private java.lang.String descriptionXNXD;
    // tatph - start
    @Column(name = "PROJECT_ID", length = 11)
    private java.lang.Long projectId;
    @Column(name = "PROJECT_CODE", length = 220)
    private java.lang.String projectCode;
    @Column(name = "PROJECT_NAME", length = 2200)
    private java.lang.String projectName;

    // tatph - end

    /** Hoangnh start 28012019 **/
    @Column(name = "CONTRACT_TYPE_O", length = 2)
    private java.lang.Long contractTypeO;
    @Column(name = "CONTRACT_TYPE_OS_NAME", length = 50)
    private java.lang.String contractTypeOsName;
    /** Hoangnh start 28012019 **/

    // hienvd: START 7/9/2019
    @Column(name = "TYPE_HTCT")
    private java.lang.Long typeHTCT;
    @Column(name = "PRICE_HTCT")
    private java.lang.Double priceHTCT;
    @Column(name = "MONTH_HTCT")
    private java.lang.Long monthHTCT;
    // hienvd: END 7/9/2019

    // Huypq-24082020-start
    @Column(name = "CONTRACT_BRANCH", length = 220)
    private java.lang.String contractBranch;
    @Column(name = "CONTRACT_SCOPE", length = 2200)
    private java.lang.String contractScope;
    @Column(name = "B2B_B2C", length = 2200)
    private java.lang.String b2bB2c;
    @Column(name = "CHANNEL", length = 2200)
    private java.lang.String channel;
    // Huy-end

    // HienLT56 start 25092020
    @Column(name = "PHONE_PARTNER", length = 220)
    private String phonePartner;
    // HienLT56 end 25092020

    // Huypq-27102020-start
    @Column(name = "LIST_CODE_CONTRACT_OUT", length = 220)
    private String listCodeContractOut;
    @Column(name = "LIST_ID_CONTRACT_OUT", length = 220)
    private String listIdContractOut;
    // huy-end

    // HienLT56 start 29092020
    @Column(name = "DEPLOYMENT_DATE_REALITY", length = 7)
    private Date deploymentDateReality;
    @Column(name = "HANDOVER_USE_DATE_REALITY", length = 7)
    private Date handoverUseDateReality;
    // HienLT56 end 29092020

    // HienLT56 start 16122020
    @Column(name = "CNT_CONTRACT_APPROVE", length = 22)
    private java.lang.Long cntContractApprove;
    @Column(name = "REASON_DENY_CONTRACT", length = 2200)
    private java.lang.String reasonDenyContract;
    @Column(name = "STATE_EDIT", length = 22)
    private java.lang.Long stateEdit;
    // HienLT56 end 16122020
    // HienLT56 start 18032021
    @Column(name = "CNT_CONTRACT_EDITOR_TYPE", length = 22)
    private java.lang.Long cntContractEditorType;
    @Column(name = "CNT_CONTRACT_TYPE", length = 22)
    private java.lang.Long cntContractType;
    @Column(name = "CNT_AUTHORITY_DATE", length = 7)
    private java.util.Date cntAuthorityDate;
    @Column(name = "CNT_AUTHORITY_NUMBER", length = 1000)
    private java.lang.String cntAuthorityNumber;
    @Column(name = "CNT_AUTHORITY_TYPE", length = 22)
    private java.lang.Long cntAuthorityType;
    @Column(name = "CNT_CONTRACT_EDITOR_STATUS", length = 22)
    private java.lang.Long cntContractEditorStatus;
    @Column(name = "USER_APPROVE_EDITOR_ID", length = 200)
    private java.lang.Long userApproveEditorId;
    @Column(name = "USER_APPROVE_SIGNED_ID", length = 200)
    private java.lang.Long userApproveSignedId;
    @Column(name = "APPROVE_EDITOR_DATE", length = 7)
    private java.util.Date approveEditorDate;
    @Column(name = "APPROVE_SIGNED_DATE", length = 7)
    private java.util.Date approveSignedDate;
    @Column(name = "COMMENT_EDITOR", length = 1000)
    private java.lang.String commentEditor;
    @Column(name = "INFORMATION_AUTHORITY_SIGN", length = 1000)
    private java.lang.String informationAuthorityForPartnerSign;
    // HienLT56 end 01032021

    // HienLT56 end 01032021
    @Column(name = "UNIT_SETTLEMENT", length = 22)
    private String unitSettlement;
    @Column(name = "SYSTEM_CAPACITY", length = 22)
    private Double systemCapacity;
    @Column(name = "HCQT_PROJECT_ID", length = 22)
    private Long hcqtProjectId;
    @Column(name = "CHECK_IMPORT_WO", length = 22)
    private String checkImportWo;
    @Column(name = "IS_AIO", length = 7)
    private java.lang.Long isAio;
    // HienLT56 end 18032021

    // HienLT56 start 01032021
    @Column(name = "COLLABORATORS_ID", length = 22)
    private java.lang.Long collaboratorId;
    // HienLT56 end 01032021

    // Huypq-19052021-start
    @Column(name = "CODE_EDITOR", length = 22)
    private String codeEditor;
    @Column(name = "CODE_EDITOR_REASON", length = 22)
    private Long codeEditorReason;

    //trungtv-end copy properties from ims

    public Double getNumDay() {
        return numDay;
    }

    public void setNumDay(Double numDay) {
        this.numDay = numDay;
    }

    public java.lang.Long getSynState() {
        return synState;
    }

    public void setSynState(java.lang.Long synState) {
        this.synState = synState;
    }

    public java.lang.Long getState() {
        return state;
    }

    public void setState(java.lang.Long state) {
        this.state = state;
    }

    public java.lang.Long getCntContractId() {
        return cntContractId;
    }

    public void setCntContractId(java.lang.Long cntContractId) {
        this.cntContractId = cntContractId;
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

    public java.lang.String getContractCodeKtts() {
        return contractCodeKtts;
    }

    public void setContractCodeKtts(java.lang.String contractCodeKtts) {
        this.contractCodeKtts = contractCodeKtts;
    }

    public java.lang.String getContent() {
        return content;
    }

    public void setContent(java.lang.String content) {
        this.content = content;
    }

    public java.util.Date getSignDate() {
        return signDate;
    }

    public void setSignDate(java.util.Date signDate) {
        this.signDate = signDate;
    }

    public java.util.Date getStartTime() {
        return startTime;
    }

    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }

    public java.util.Date getEndTime() {
        return endTime;
    }

    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    public java.lang.Double getPrice() {
        return price;
    }

    public void setPrice(java.lang.Double price) {
        this.price = price;
    }

    public java.lang.Double getAppendixContract() {
        return appendixContract;
    }

    public void setAppendixContract(java.lang.Double appendixContract) {
        this.appendixContract = appendixContract;
    }

    public java.lang.Double getNumStation() {
        return numStation;
    }

    public void setNumStation(java.lang.Double numStation) {
        this.numStation = numStation;
    }

    public java.lang.Long getBiddingPackageId() {
        return biddingPackageId;
    }

    public void setBiddingPackageId(java.lang.Long biddingPackageId) {
        this.biddingPackageId = biddingPackageId;
    }

    public java.lang.Long getCatPartnerId() {
        return catPartnerId;
    }

    public void setCatPartnerId(java.lang.Long catPartnerId) {
        this.catPartnerId = catPartnerId;
    }

    public java.lang.String getSignerPartner() {
        return signerPartner;
    }

    public void setSignerPartner(java.lang.String signerPartner) {
        this.signerPartner = signerPartner;
    }

    public java.lang.Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(java.lang.Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public java.lang.Long getSignerGroup() {
        return signerGroup;
    }

    public void setSignerGroup(java.lang.Long signerGroup) {
        this.signerGroup = signerGroup;
    }

    public java.lang.String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(java.lang.String supervisor) {
        this.supervisor = supervisor;
    }

    public java.lang.Long getStatus() {
        return status;
    }

    public void setStatus(java.lang.Long status) {
        this.status = status;
    }

    public java.lang.Double getFormal() {
        return formal;
    }

    public void setFormal(java.lang.Double formal) {
        this.formal = formal;
    }

    public java.lang.Long getContractType() {
        return contractType;
    }

    public void setContractType(java.lang.Long contractType) {
        this.contractType = contractType;
    }

    public java.lang.Double getCntContractParentId() {
        return cntContractParentId;
    }

    public void setCntContractParentId(java.lang.Double cntContractParentId) {
        this.cntContractParentId = cntContractParentId;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public CntContractDTO toDTO() {
        CntContractDTO cntContractDTO = new CntContractDTO();
        cntContractDTO.setCntContractId(this.cntContractId);
        cntContractDTO.setCode(this.code);
        cntContractDTO.setName(this.name);
        cntContractDTO.setContractCodeKtts(this.contractCodeKtts);
        cntContractDTO.setContent(this.content);
        cntContractDTO.setSignDate(this.signDate);
        cntContractDTO.setStartTime(this.startTime);
        cntContractDTO.setEndTime(this.endTime);
        cntContractDTO.setPrice(this.price);
        cntContractDTO.setAppendixContract(this.appendixContract);
        cntContractDTO.setNumStation(this.numStation);
        cntContractDTO.setBiddingPackageId(this.biddingPackageId);
        cntContractDTO.setCatPartnerId(this.catPartnerId);
        cntContractDTO.setSignerPartner(this.signerPartner);
        cntContractDTO.setSysGroupId(this.sysGroupId);
        cntContractDTO.setSignerGroup(this.signerGroup);
        cntContractDTO.setSupervisor(this.supervisor);
        cntContractDTO.setStatus(this.status);
        cntContractDTO.setState(this.state);
        cntContractDTO.setFormal(this.formal);
        cntContractDTO.setContractType(this.contractType);
        cntContractDTO.setCntContractParentId(this.cntContractParentId);
        cntContractDTO.setCreatedDate(this.createdDate);
        cntContractDTO.setCreatedUserId(this.createdUserId);
        cntContractDTO.setCreatedGroupId(this.createdGroupId);
        cntContractDTO.setUpdatedDate(this.updatedDate);
        cntContractDTO.setUpdatedUserId(this.updatedUserId);
        cntContractDTO.setUpdatedGroupId(this.updatedGroupId);
        cntContractDTO.setDescription(description);
        cntContractDTO.setSynState(this.synState);
        cntContractDTO.setNumDay(this.numDay);
        cntContractDTO.setMoneyType(moneyType);
        cntContractDTO.setFrameParentId(frameParentId);
        cntContractDTO.setProjectContractId(projectContractId);
        return cntContractDTO;
    }

    public Long getFrameParentId() {
        return frameParentId;
    }

    public void setFrameParentId(Long frameParentId) {
        this.frameParentId = frameParentId;
    }

    public Integer getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }

    public java.lang.Long getProjectContractId() {
        return projectContractId;
    }

    public void setProjectContractId(java.lang.Long projectContractId) {
        this.projectContractId = projectContractId;
    }

    //trungtv-start 18082022 copy properties from ims

    public Long getExtensionDays() {
        return extensionDays;
    }

    public void setExtensionDays(Long extensionDays) {
        this.extensionDays = extensionDays;
    }

    public Long getPaymentExpried() {
        return paymentExpried;
    }

    public void setPaymentExpried(Long paymentExpried) {
        this.paymentExpried = paymentExpried;
    }

    public Date getDeploymentDate() {
        return deploymentDate;
    }

    public void setDeploymentDate(Date deploymentDate) {
        this.deploymentDate = deploymentDate;
    }

    public String getCodeExt() {
        return codeExt;
    }

    public void setCodeExt(String codeExt) {
        this.codeExt = codeExt;
    }

    public Long getXdddType() {
        return xdddType;
    }

    public void setXdddType(Long xdddType) {
        this.xdddType = xdddType;
    }

    public Long getWarrantyNumberDay() {
        return warrantyNumberDay;
    }

    public void setWarrantyNumberDay(Long warrantyNumberDay) {
        this.warrantyNumberDay = warrantyNumberDay;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getApproveContract() {
        return approveContract;
    }

    public void setApproveContract(String approveContract) {
        this.approveContract = approveContract;
    }

    public String getCommentContract() {
        return commentContract;
    }

    public void setCommentContract(String commentContract) {
        this.commentContract = commentContract;
    }

    public Long getApprovedUserId() {
        return approvedUserId;
    }

    public void setApprovedUserId(Long approvedUserId) {
        this.approvedUserId = approvedUserId;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Long getProvinceConstructionId() {
        return provinceConstructionId;
    }

    public void setProvinceConstructionId(Long provinceConstructionId) {
        this.provinceConstructionId = provinceConstructionId;
    }

    public String getProvinceConstruction() {
        return provinceConstruction;
    }

    public void setProvinceConstruction(String provinceConstruction) {
        this.provinceConstruction = provinceConstruction;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCommuneName() {
        return communeName;
    }

    public void setCommuneName(String communeName) {
        this.communeName = communeName;
    }

    public Long getUserSearchId() {
        return userSearchId;
    }

    public void setUserSearchId(Long userSearchId) {
        this.userSearchId = userSearchId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getRoseContract() {
        return roseContract;
    }

    public void setRoseContract(Double roseContract) {
        this.roseContract = roseContract;
    }

    public Long getDistrictGroupId() {
        return districtGroupId;
    }

    public void setDistrictGroupId(Long districtGroupId) {
        this.districtGroupId = districtGroupId;
    }

    public Long getCntContractRevenue() {
        return cntContractRevenue;
    }

    public void setCntContractRevenue(Long cntContractRevenue) {
        this.cntContractRevenue = cntContractRevenue;
    }

    public Long getCntGroupContractType() {
        return cntGroupContractType;
    }

    public void setCntGroupContractType(Long cntGroupContractType) {
        this.cntGroupContractType = cntGroupContractType;
    }

    public Long getIntroduceBranch() {
        return introduceBranch;
    }

    public void setIntroduceBranch(Long introduceBranch) {
        this.introduceBranch = introduceBranch;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Double getCostIngerit() {
        return costIngerit;
    }

    public void setCostIngerit(Double costIngerit) {
        this.costIngerit = costIngerit;
    }

    public Long getIntroduceBranchType() {
        return introduceBranchType;
    }

    public void setIntroduceBranchType(Long introduceBranchType) {
        this.introduceBranchType = introduceBranchType;
    }

    public Long getStatusExtension() {
        return statusExtension;
    }

    public void setStatusExtension(Long statusExtension) {
        this.statusExtension = statusExtension;
    }

    public String getPromulgateTTRNumber() {
        return promulgateTTRNumber;
    }

    public void setPromulgateTTRNumber(String promulgateTTRNumber) {
        this.promulgateTTRNumber = promulgateTTRNumber;
    }

    public String getRateAdjustmentTTRNumber() {
        return rateAdjustmentTTRNumber;
    }

    public void setRateAdjustmentTTRNumber(String rateAdjustmentTTRNumber) {
        this.rateAdjustmentTTRNumber = rateAdjustmentTTRNumber;
    }

    public String getOperatingUnitDeploysContract() {
        return operatingUnitDeploysContract;
    }

    public void setOperatingUnitDeploysContract(String operatingUnitDeploysContract) {
        this.operatingUnitDeploysContract = operatingUnitDeploysContract;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Long getUserPromulgateId() {
        return userPromulgateId;
    }

    public void setUserPromulgateId(Long userPromulgateId) {
        this.userPromulgateId = userPromulgateId;
    }

    public Date getUpdatePromulgate() {
        return updatePromulgate;
    }

    public void setUpdatePromulgate(Date updatePromulgate) {
        this.updatePromulgate = updatePromulgate;
    }

    public Date getHocTac() {
        return hocTac;
    }

    public void setHocTac(Date hocTac) {
        this.hocTac = hocTac;
    }

    public Date getAdvanceDate() {
        return advanceDate;
    }

    public void setAdvanceDate(Date advanceDate) {
        this.advanceDate = advanceDate;
    }

    public Date getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(Date firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public Date getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Date lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public Double getPriceVat() {
        return priceVat;
    }

    public void setPriceVat(Double priceVat) {
        this.priceVat = priceVat;
    }

    public String getPurchasePropose() {
        return purchasePropose;
    }

    public void setPurchasePropose(String purchasePropose) {
        this.purchasePropose = purchasePropose;
    }

    public String getSourcePurchase() {
        return sourcePurchase;
    }

    public void setSourcePurchase(String sourcePurchase) {
        this.sourcePurchase = sourcePurchase;
    }

    public Date getFinacialVoucherReturnDate() {
        return finacialVoucherReturnDate;
    }

    public void setFinacialVoucherReturnDate(Date finacialVoucherReturnDate) {
        this.finacialVoucherReturnDate = finacialVoucherReturnDate;
    }

    public Date getActualReceiveGoodsDate() {
        return actualReceiveGoodsDate;
    }

    public void setActualReceiveGoodsDate(Date actualReceiveGoodsDate) {
        this.actualReceiveGoodsDate = actualReceiveGoodsDate;
    }

    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
    }

    public String getStationCodeVcc() {
        return stationCodeVcc;
    }

    public void setStationCodeVcc(String stationCodeVcc) {
        this.stationCodeVcc = stationCodeVcc;
    }

    public String getAddressStation() {
        return addressStation;
    }

    public void setAddressStation(String addressStation) {
        this.addressStation = addressStation;
    }

    public String getStationCodeHtct() {
        return stationCodeHtct;
    }

    public void setStationCodeHtct(String stationCodeHtct) {
        this.stationCodeHtct = stationCodeHtct;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public String getCatProvinceCode() {
        return catProvinceCode;
    }

    public void setCatProvinceCode(String catProvinceCode) {
        this.catProvinceCode = catProvinceCode;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Long getIsXNXD() {
        return isXNXD;
    }

    public void setIsXNXD(Long isXNXD) {
        this.isXNXD = isXNXD;
    }

    public Long getConstructionForm() {
        return constructionForm;
    }

    public void setConstructionForm(Long constructionForm) {
        this.constructionForm = constructionForm;
    }

    public String getCurrentProgess() {
        return currentProgess;
    }

    public void setCurrentProgess(String currentProgess) {
        this.currentProgess = currentProgess;
    }

    public Date getHandoverUseDate() {
        return handoverUseDate;
    }

    public void setHandoverUseDate(Date handoverUseDate) {
        this.handoverUseDate = handoverUseDate;
    }

    public Date getWarrantyExpiredDate() {
        return warrantyExpiredDate;
    }

    public void setWarrantyExpiredDate(Date warrantyExpiredDate) {
        this.warrantyExpiredDate = warrantyExpiredDate;
    }

    public Long getStructureFilter() {
        return structureFilter;
    }

    public void setStructureFilter(Long structureFilter) {
        this.structureFilter = structureFilter;
    }

    public String getDescriptionXNXD() {
        return descriptionXNXD;
    }

    public void setDescriptionXNXD(String descriptionXNXD) {
        this.descriptionXNXD = descriptionXNXD;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public Long getTypeHTCT() {
        return typeHTCT;
    }

    public void setTypeHTCT(Long typeHTCT) {
        this.typeHTCT = typeHTCT;
    }

    public Double getPriceHTCT() {
        return priceHTCT;
    }

    public void setPriceHTCT(Double priceHTCT) {
        this.priceHTCT = priceHTCT;
    }

    public Long getMonthHTCT() {
        return monthHTCT;
    }

    public void setMonthHTCT(Long monthHTCT) {
        this.monthHTCT = monthHTCT;
    }

    public String getContractBranch() {
        return contractBranch;
    }

    public void setContractBranch(String contractBranch) {
        this.contractBranch = contractBranch;
    }

    public String getContractScope() {
        return contractScope;
    }

    public void setContractScope(String contractScope) {
        this.contractScope = contractScope;
    }

    public String getB2bB2c() {
        return b2bB2c;
    }

    public void setB2bB2c(String b2bB2c) {
        this.b2bB2c = b2bB2c;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPhonePartner() {
        return phonePartner;
    }

    public void setPhonePartner(String phonePartner) {
        this.phonePartner = phonePartner;
    }

    public String getListCodeContractOut() {
        return listCodeContractOut;
    }

    public void setListCodeContractOut(String listCodeContractOut) {
        this.listCodeContractOut = listCodeContractOut;
    }

    public String getListIdContractOut() {
        return listIdContractOut;
    }

    public void setListIdContractOut(String listIdContractOut) {
        this.listIdContractOut = listIdContractOut;
    }

    public Date getDeploymentDateReality() {
        return deploymentDateReality;
    }

    public void setDeploymentDateReality(Date deploymentDateReality) {
        this.deploymentDateReality = deploymentDateReality;
    }

    public Date getHandoverUseDateReality() {
        return handoverUseDateReality;
    }

    public void setHandoverUseDateReality(Date handoverUseDateReality) {
        this.handoverUseDateReality = handoverUseDateReality;
    }

    public Long getCntContractApprove() {
        return cntContractApprove;
    }

    public void setCntContractApprove(Long cntContractApprove) {
        this.cntContractApprove = cntContractApprove;
    }

    public String getReasonDenyContract() {
        return reasonDenyContract;
    }

    public void setReasonDenyContract(String reasonDenyContract) {
        this.reasonDenyContract = reasonDenyContract;
    }

    public Long getStateEdit() {
        return stateEdit;
    }

    public void setStateEdit(Long stateEdit) {
        this.stateEdit = stateEdit;
    }

    public Long getCntContractEditorType() {
        return cntContractEditorType;
    }

    public void setCntContractEditorType(Long cntContractEditorType) {
        this.cntContractEditorType = cntContractEditorType;
    }

    public Long getCntContractType() {
        return cntContractType;
    }

    public void setCntContractType(Long cntContractType) {
        this.cntContractType = cntContractType;
    }

    public Date getCntAuthorityDate() {
        return cntAuthorityDate;
    }

    public void setCntAuthorityDate(Date cntAuthorityDate) {
        this.cntAuthorityDate = cntAuthorityDate;
    }

    public String getCntAuthorityNumber() {
        return cntAuthorityNumber;
    }

    public void setCntAuthorityNumber(String cntAuthorityNumber) {
        this.cntAuthorityNumber = cntAuthorityNumber;
    }

    public Long getCntAuthorityType() {
        return cntAuthorityType;
    }

    public void setCntAuthorityType(Long cntAuthorityType) {
        this.cntAuthorityType = cntAuthorityType;
    }

    public Long getCntContractEditorStatus() {
        return cntContractEditorStatus;
    }

    public void setCntContractEditorStatus(Long cntContractEditorStatus) {
        this.cntContractEditorStatus = cntContractEditorStatus;
    }

    public Long getUserApproveEditorId() {
        return userApproveEditorId;
    }

    public void setUserApproveEditorId(Long userApproveEditorId) {
        this.userApproveEditorId = userApproveEditorId;
    }

    public Long getUserApproveSignedId() {
        return userApproveSignedId;
    }

    public void setUserApproveSignedId(Long userApproveSignedId) {
        this.userApproveSignedId = userApproveSignedId;
    }

    public Date getApproveEditorDate() {
        return approveEditorDate;
    }

    public void setApproveEditorDate(Date approveEditorDate) {
        this.approveEditorDate = approveEditorDate;
    }

    public Date getApproveSignedDate() {
        return approveSignedDate;
    }

    public void setApproveSignedDate(Date approveSignedDate) {
        this.approveSignedDate = approveSignedDate;
    }

    public String getCommentEditor() {
        return commentEditor;
    }

    public void setCommentEditor(String commentEditor) {
        this.commentEditor = commentEditor;
    }

    public String getInformationAuthorityForPartnerSign() {
        return informationAuthorityForPartnerSign;
    }

    public void setInformationAuthorityForPartnerSign(String informationAuthorityForPartnerSign) {
        this.informationAuthorityForPartnerSign = informationAuthorityForPartnerSign;
    }

    public String getUnitSettlement() {
        return unitSettlement;
    }

    public void setUnitSettlement(String unitSettlement) {
        this.unitSettlement = unitSettlement;
    }

    public Double getSystemCapacity() {
        return systemCapacity;
    }

    public void setSystemCapacity(Double systemCapacity) {
        this.systemCapacity = systemCapacity;
    }

    public Long getHcqtProjectId() {
        return hcqtProjectId;
    }

    public void setHcqtProjectId(Long hcqtProjectId) {
        this.hcqtProjectId = hcqtProjectId;
    }

    public String getCheckImportWo() {
        return checkImportWo;
    }

    public void setCheckImportWo(String checkImportWo) {
        this.checkImportWo = checkImportWo;
    }

    public Long getIsAio() {
        return isAio;
    }

    public void setIsAio(Long isAio) {
        this.isAio = isAio;
    }

    public Long getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorId(Long collaboratorId) {
        this.collaboratorId = collaboratorId;
    }

    public String getCodeEditor() {
        return codeEditor;
    }

    public void setCodeEditor(String codeEditor) {
        this.codeEditor = codeEditor;
    }

    public Long getCodeEditorReason() {
        return codeEditorReason;
    }

    public void setCodeEditorReason(Long codeEditorReason) {
        this.codeEditorReason = codeEditorReason;
    }
    //trungtv-end 18082022 copy properties from ims
}
