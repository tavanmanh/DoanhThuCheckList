/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.erp.dto;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.base.Strings;
import com.viettel.erp.bo.ConstrEstimateInfoBO;
import com.viettel.erp.bo.EstimatesWorkItemsBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.dto.BaseFWDTOImpl;

/**
 *
 * @author thuannht
 */
@XmlRootElement(name = "ESTIMATES_WORK_ITEMSBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimatesWorkItemsDTO extends BaseFWDTOImpl<EstimatesWorkItemsBO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6943000052038826920L;
	private java.lang.Long status;
	private java.lang.Long estimatesWorkItemId;
	private java.lang.Long constrEstimateInfoId;
	private java.lang.Long constructionId;
	private java.lang.Long estimatesItemChildId;
	private java.lang.String workItemCode;
	private java.lang.String workItemName;
	private java.lang.String unit;
	private java.lang.Double workAmount;
	private java.lang.Long  constructionAcceptanceId;
	private java.lang.Double unitPrice;
	private java.lang.Double totalMoney;
	private java.lang.Long progressType;
	private java.lang.String pathFile;
	private Double totalMoneyBt;
	private List<Long> lstConstrId;
	//HuyPq-20190726-start
	private Long workItemTypeId;
	private String workItemTypeName;
	private String workItemTypeCode;
	private String keySearch;
	private Long workItemId;
	private Long cntContractId;
	
	//tatph start 19112019
	private Long assignedConstructionTaskId;
	private Long catTaskId;
	private String  catTaskName;
	private String  catTaskCode;
	private String  catWorkItemTypeCode;
	private String  catWorkItemTypeName;
	
	
	
	//tatph end 19112019
	
	public Long getAssignedConstructionTaskId() {
		return assignedConstructionTaskId;
	}

	public void setAssignedConstructionTaskId(Long assignedConstructionTaskId) {
		this.assignedConstructionTaskId = assignedConstructionTaskId;
	}

	public Long getCatTaskId() {
		return catTaskId;
	}

	public void setCatTaskId(Long catTaskId) {
		this.catTaskId = catTaskId;
	}

	public String getCatTaskName() {
		return catTaskName;
	}

	public void setCatTaskName(String catTaskName) {
		this.catTaskName = catTaskName;
	}

	public String getCatTaskCode() {
		return catTaskCode;
	}

	public void setCatTaskCode(String catTaskCode) {
		this.catTaskCode = catTaskCode;
	}

	public String getCatWorkItemTypeCode() {
		return catWorkItemTypeCode;
	}

	public void setCatWorkItemTypeCode(String catWorkItemTypeCode) {
		this.catWorkItemTypeCode = catWorkItemTypeCode;
	}

	public String getCatWorkItemTypeName() {
		return catWorkItemTypeName;
	}

	public void setCatWorkItemTypeName(String catWorkItemTypeName) {
		this.catWorkItemTypeName = catWorkItemTypeName;
	}

	public Long getCntContractId() {
		return cntContractId;
	}

	public void setCntContractId(Long cntContractId) {
		this.cntContractId = cntContractId;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}

	public String getKeySearch() {
		return keySearch;
	}

	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}

	public String getWorkItemTypeCode() {
		return workItemTypeCode;
	}

	public void setWorkItemTypeCode(String workItemTypeCode) {
		this.workItemTypeCode = workItemTypeCode;
	}

	public Long getWorkItemTypeId() {
		return workItemTypeId;
	}

	public void setWorkItemTypeId(Long workItemTypeId) {
		this.workItemTypeId = workItemTypeId;
	}

	public String getWorkItemTypeName() {
		return workItemTypeName;
	}

	public void setWorkItemTypeName(String workItemTypeName) {
		this.workItemTypeName = workItemTypeName;
	}

	//Huy-end
	public String getSttCv(){
		if(getType()==1l){
			return "A";
		}else{
			return "B";
		}
	}
//	public String getCvTitle(){
//		return "";
////		if(getType()==1l){
////			return "Công việc phát sinh trong hợp đồng";
////		}else{
////			return "Công việc phát sinh ngoài hợp đồng";
////		}
//	}
	
	public List<Long> getLstConstrId() {
		return lstConstrId;
	}



	public void setLstConstrId(List<Long> lstConstrId) {
		this.lstConstrId = lstConstrId;
	}

	private Long isActive;
	public Long getIsActive() {
		return isActive;
	}



	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}



	public Double getTotalMoneyBt() {
		return totalMoneyBt;
	}



	public void setTotalMoneyBt(Double totalMoneyBt) {
		this.totalMoneyBt = totalMoneyBt;
	}



	public Double getTax() {
		return tax;
	}



	public void setTax(Double tax) {
		this.tax = tax;
	}

	private Double tax;
	
	
	//minhpvn
	private Long workType;
	
	//so thu tu cho bao cao de xuat
	private String sttProposal;
	//Xac dinh la workItem hay ItemChild
	private boolean parent;
	private Double totalMoneySettlement;
	
	private Double totalMoneyEvaluate;
	
	
	
	public Double getTotalMoneyEvaluate() {
		if(parent){
			return null;
		}
		if(totalMoneyEvaluate==null){
			return 0d;
		}
		return totalMoneyEvaluate;
//		return totalMoneyEvaluate;
	}



	public void setTotalMoneyEvaluate(Double totalMoneyEvaluate) {
		this.totalMoneyEvaluate = totalMoneyEvaluate;
	}



	public Double getChenhLechTien() {
		if(totalMoneySettlement==null||totalMoney==null){
			return null;
		}
		return totalMoneySettlement-totalMoney;
	}

	public Double getChenhLechTienPheDuyetDeXuat() {
		if(totalMoneyEvaluate==null||totalMoneySettlement==null){
			return null;
		}
		return totalMoneyEvaluate-totalMoneySettlement;
	}
	

	public Double getTotalMoneySettlement() {
		if(parent){
			return totalMoneySettlement;
		}
		if(totalMoneySettlement==null){
			return 0d;
		}
		return totalMoneySettlement;
	}

	public void setTotalMoneySettlement(Double totalMoneySettlement) {
		this.totalMoneySettlement = totalMoneySettlement;
	}
	
	

	public Boolean isParent() {
		return parent;
	}

	public void setParent(Boolean parent) {
		this.parent = parent;
	}

	public String getSttProposal() {
		return sttProposal;
	}

	public void setSttProposal(String sttProposal) {
		this.sttProposal = sttProposal;
	}

	public Long getWorkType() {
		return workType;
	}

	public void setWorkType(Long workType) {
		this.workType = workType;
	}

	public Long getSceneGenWorkListId() {
		return sceneGenWorkListId;
	}

	public void setSceneGenWorkListId(Long sceneGenWorkListId) {
		this.sceneGenWorkListId = sceneGenWorkListId;
	}

	private Long sceneGenWorkListId;
	//estimates detal analyst
	private java.lang.Long progressTypeAnalyst;
	//end
	private java.lang.Long unitPriceBid;
	private java.lang.Long totalMoneyBid;
	private java.lang.Long unitPriceBidAward;
	private java.lang.Long totalMoneyBidAward;
	private java.lang.Long unitPriceContract;
	private java.lang.Long totalMoneyContract;
	private java.lang.Long type;
	private java.lang.Double evaluateQuantity;
	
	private java.lang.Double evaluateUnitPrice;
	
	private java.lang.Long constrAcceptWorkListId;
	private java.lang.String explain;
	private java.lang.Double executeQuantity;
	private java.lang.Double settleUnitPrice;
	private java.lang.Double normIndexCT;
	
	private java.lang.Boolean TypeworkitemIncontract = false;
	
	public java.lang.Double getNormIndexCT() {
		return normIndexCT;
	}

	public void setNormIndexCT(java.lang.Double normIndexCT) {
		this.normIndexCT = normIndexCT;
	}

	public java.lang.Double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(java.lang.Double coefficient) {
		this.coefficient = coefficient;
	}

	private java.lang.Double coefficient;
	private java.lang.Long typeworkitem;

	private java.lang.String instanceDrawCode = " ";
	private java.lang.String comments;
	private java.lang.Double tang;
	private java.lang.Double giam;

	private java.lang.String constrtCode;
	private java.lang.Long isAccepted;
	private java.lang.String code;
	private java.lang.Long checkcode;
	private java.lang.String partnerName;
	private java.lang.Long constrType;
	private java.lang.String constrtName;
	private java.lang.Long provinceId;

	private java.lang.String itemsCode;
	private java.lang.String itemName;
	private java.lang.Integer stt;
	private java.lang.Boolean checkUpdate;
	
	private java.lang.Double allotmentAmount;
	private java.lang.Double advanceAmount;
	private java.lang.Double approvalAmount;
	private java.lang.Double revaluationAmount;
	
	// thông tin chung
	private String stationCode;
	private java.lang.Double thanhtien1;
	private java.lang.Double thanhtien2;
	private java.lang.Double deviantAdvance;
	private java.lang.Double deviantApproval;

	public java.lang.Double getDeviantAdvance() {
		if(deviantAdvance==null){
			return 0d;
		}
		return deviantAdvance;
	}

	public void setDeviantAdvance(java.lang.Double deviantAdvance) {
		this.deviantAdvance = deviantAdvance;
	}

	public java.lang.Double getDeviantApproval() {
		return deviantApproval;
	}

	public void setDeviantApproval(java.lang.Double deviantApproval) {
		this.deviantApproval = deviantApproval;
	}

	private java.lang.String evaluateComments;
	private java.lang.String Category = "";
	

	//minhpvn begin
	private Long constructId;
	private Long abComplementWorkDescribeId;
	private String aDirectoridPath;
	private String bDirectoridPath;
	private String aInchargeMonitoridPath;
	private String bInchargeConstructidPath;
	private Long statusCa;
	private String totalMoneyFormula;
	
	//minhpvn end
	
	
	

	public String getTotalMoneyFormula() {
		return totalMoneyFormula;
	}

	public void setTotalMoneyFormula(String totalMoneyFormula) {
		this.totalMoneyFormula = totalMoneyFormula;
	}

	public Long getStatusCa() {
		return statusCa;
	}

	public void setStatusCa(Long statusCa) {
		this.statusCa = statusCa;
	}

	public String getaDirectoridPath() {
		return aDirectoridPath;
	}

	public void setaDirectoridPath(String aDirectoridPath) {
		this.aDirectoridPath = aDirectoridPath;
	}

	public String getbDirectoridPath() {
		return bDirectoridPath;
	}

	public void setbDirectoridPath(String bDirectoridPath) {
		this.bDirectoridPath = bDirectoridPath;
	}

	public String getaInchargeMonitoridPath() {
		return aInchargeMonitoridPath;
	}

	public void setaInchargeMonitoridPath(String aInchargeMonitoridPath) {
		this.aInchargeMonitoridPath = aInchargeMonitoridPath;
	}

	public String getbInchargeConstructidPath() {
		return bInchargeConstructidPath;
	}

	public void setbInchargeConstructidPath(String bInchargeConstructidPath) {
		this.bInchargeConstructidPath = bInchargeConstructidPath;
	}

	public Long getConstructId() {
		return constructId;
	}
	public void setConstructId(Long constructId) {
		this.constructId = constructId;
	}
	public Long getAbComplementWorkDescribeId() {
		return abComplementWorkDescribeId;
	}

	public void setAbComplementWorkDescribeId(Long abComplementWorkDescribeId) {
		this.abComplementWorkDescribeId = abComplementWorkDescribeId;
	}



	//du lieu lay bang khac linh nn
	private String constrtAddress;
	private String contractCode;
	private String contractName;
	private String constructorName;
	private String aDirectorFullName;
	private String aInchargeMonitorFullName;
	private String bDirectorFullName;
	private String bInchargeConstructFullName;
	private String station_code;
	private java.lang.Integer rownum = 0;
	//kiem tra cong viec chua nghiem thu
	private java.lang.Boolean checkstatus;
	
	private ConstrCompleteRecordsMapDTO constrCompleteRecordMap;
	private String costIngredientCode;
	private String costIngredientName;
	
	private java.lang.Long normIndex;
	//Lấy Quyết toán biểu 5
	
	private java.lang.String workItemCode4;
	private java.lang.String workItemName4;
	private java.lang.String unit4;
	private java.lang.Double unitPrice4;
	private java.lang.Double normIndex4;
	private java.lang.Double totalMoney4;
	private String costIngredientName4;
	private java.lang.Long type4;
	private java.lang.Long type2;
	private java.lang.String chenhLech;
	private String hyberLink;
	private String ctAdd;
	
	
	//minhpvn
	private java.lang.String incurredContent;
	private java.lang.Double designQuantity;
	private java.lang.Double incurredQuantity;
	private java.lang.String note;
	
	public void changeData(){
		this.incurredContent = this.workItemName;
		this.designQuantity = this.workAmount;
	}
	

	
	public java.lang.String getIncurredContent() {
		return Strings.nullToEmpty(incurredContent);
	}

	public void setIncurredContent(java.lang.String incurredContent) {
		this.incurredContent = incurredContent;
	}

	public java.lang.Double getDesignQuantity() {
		if(designQuantity==null){
			return 0d;
		}
		return designQuantity;
	}

	public void setDesignQuantity(java.lang.Double designQuantity) {
		this.designQuantity = designQuantity;
	}

	public java.lang.Double getIncurredQuantity() {
		return incurredQuantity;
	}

	public void setIncurredQuantity(java.lang.Double incurredQuantity) {
		this.incurredQuantity = incurredQuantity;
	}

	public java.lang.String getNote() {
		return note;
	}

	public void setNote(java.lang.String note) {
		this.note = note;
	}

	public String getCtAdd() {
		return ctAdd;
	}

	public void setCtAdd(String ctAdd) {
		this.ctAdd = ctAdd;
	}

	public String getHyberLink() {
		return hyberLink;
	}

	public void setHyberLink(String hyberLink) {
		this.hyberLink = hyberLink;
	}

	public java.lang.Long getNormIndex() {
		if(normIndex == null){
			return normIndex = 0l;
		}
		return normIndex;
	}

	public void setNormIndex(java.lang.Long normIndex) {
		this.normIndex = normIndex;
	}

	public java.lang.String getWorkItemCode4() {
		return workItemCode4;
	}

	public void setWorkItemCode4(java.lang.String workItemCode4) {
		this.workItemCode4 = workItemCode4;
	}

	public java.lang.String getWorkItemName4() {
		return workItemName4;
	}

	public void setWorkItemName4(java.lang.String workItemName4) {
		this.workItemName4 = workItemName4;
	}

	public java.lang.String getUnit4() {
		return Strings.nullToEmpty(unit4);
	}

	public void setUnit4(java.lang.String unit4) {
		this.unit4 = unit4;
	}

	public java.lang.Double getUnitPrice4() {		
		/*if(unitPrice4 == null){
			unitPrice4 = 1l;
		}*/
		return unitPrice4;
	}

	public void setUnitPrice4(java.lang.Double unitPrice4) {
		this.unitPrice4 = unitPrice4;
	}

	public java.lang.Double getNormIndex4() {
		if(normIndex4 == null){
			normIndex4 = 0D;
		}
		return normIndex4;
	}

	public void setNormIndex4(java.lang.Double normIndex4) {
		this.normIndex4 = normIndex4;
	}

	public java.lang.Double getTotalMoney4() {
		if(totalMoney4 == null){
			totalMoney4 = 0D;
		}
		return totalMoney4;
	}

	public void setTotalMoney4(java.lang.Double totalMoney4) {
		this.totalMoney4 = totalMoney4;
	}

	public String getCostIngredientName4() {
		return costIngredientName4;
	}

	public void setCostIngredientName4(String costIngredientName4) {
		this.costIngredientName4 = costIngredientName4;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// Toan Bê Đê
	private List<EstimatesDetailAnalystDTO> estimatesDetailAnalysts = new ArrayList<>();
	
	
	public ConstrCompleteRecordsMapDTO getConstrCompleteRecordMap() {
		return constrCompleteRecordMap;
	}

	public void setConstrCompleteRecordMap(ConstrCompleteRecordsMapDTO constrCompleteRecordMap) {
		this.constrCompleteRecordMap = constrCompleteRecordMap;
	}

	public EstimatesWorkItemsDTO(Long constructionId) {
		super();
		this.constructionId = constructionId;
	}

	public EstimatesWorkItemsDTO() {
		super();
	}

	public java.lang.String getEvaluateComments() {
		return evaluateComments==null?"":evaluateComments;
	}

	public void setEvaluateComments(java.lang.String evaluateComments) {
		this.evaluateComments = evaluateComments;
	}

	public java.lang.Double getAllotmentAmount() {
		return allotmentAmount;
	}

	public void setAllotmentAmount(java.lang.Double allotmentAmount) {
		this.allotmentAmount = allotmentAmount;
	}

	public java.lang.Double getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(java.lang.Double advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	public java.lang.Double getApprovalAmount() {
		return approvalAmount;
	}

	public void setApprovalAmount(java.lang.Double approvalAmount) {
		this.approvalAmount = approvalAmount;
	}

	public java.lang.Double getRevaluationAmount() {
		return revaluationAmount;
	}

	public void setRevaluationAmount(java.lang.Double revaluationAmount) {
		this.revaluationAmount = revaluationAmount;
	}
	

	public java.lang.Boolean getCheckUpdate() {
		return checkUpdate;
	}

	public void setCheckUpdate(java.lang.Boolean checkUpdate) {
		this.checkUpdate = checkUpdate;
	}

	/**
	 * @return the stt
	 */
	public java.lang.Integer getStt() {
		return stt;
	}

	/**
	 * @param stt the stt to set
	 */
	public void setStt(java.lang.Integer stt) {
		this.stt = stt;
	}

	public java.lang.String getItemsCode() {
		return itemsCode;
	}

	public void setItemsCode(java.lang.String itemsCode) {
		this.itemsCode = itemsCode;
	}

	public java.lang.String getItemName() {			
		return Strings.nullToEmpty(itemName);
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	@Override
	public EstimatesWorkItemsBO toModel() {
		EstimatesWorkItemsBO estimatesWorkItemsBO = new EstimatesWorkItemsBO();
		estimatesWorkItemsBO.setStatus(this.status);
		estimatesWorkItemsBO.setEstimatesWorkItemId(this.estimatesWorkItemId);
        if(constrEstimateInfoId != null){
        	estimatesWorkItemsBO.setConstrestimateinfo(new ConstrEstimateInfoBO(constrEstimateInfoId));
        }
		estimatesWorkItemsBO.setEstimatesItemChildId(this.estimatesItemChildId);
		estimatesWorkItemsBO.setWorkItemCode(this.workItemCode);
		estimatesWorkItemsBO.setWorkItemName(this.workItemName);
		estimatesWorkItemsBO.setUnit(this.unit);
		estimatesWorkItemsBO.setWorkAmount(this.workAmount);
		estimatesWorkItemsBO.setUnitPrice(this.unitPrice);
		estimatesWorkItemsBO.setTotalMoney(this.totalMoney);
		estimatesWorkItemsBO.setProgressType(this.progressType);
		estimatesWorkItemsBO.setUnitPriceBid(this.unitPriceBid);
		estimatesWorkItemsBO.setTotalMoneyBid(this.totalMoneyBid);
		estimatesWorkItemsBO.setUnitPriceBidAward(this.unitPriceBidAward);
		estimatesWorkItemsBO.setTotalMoneyBidAward(this.totalMoneyBidAward);
		estimatesWorkItemsBO.setUnitPriceContract(this.unitPriceContract);
		estimatesWorkItemsBO.setTotalMoneyContract(this.totalMoneyContract);
		estimatesWorkItemsBO.setType(this.type);
		estimatesWorkItemsBO.setWorkType(this.workType);
		estimatesWorkItemsBO.setSceneGenWorkListId(this.sceneGenWorkListId);
		estimatesWorkItemsBO.setTotalMoneyBt(totalMoneyBt);
		estimatesWorkItemsBO.setTax(tax);
		estimatesWorkItemsBO.setIsActive(isActive);
		
		estimatesWorkItemsBO.setConstructionId(constructionId);
		return estimatesWorkItemsBO;
	}

	public java.lang.Long getStatus() {
		return status;
	}

	public void setStatus(java.lang.Long status) {
		this.status = status;
	}

	@Override
	public Long getFWModelId() {
		return estimatesWorkItemId;
	}

	@Override
	public String catchName() {
		return getEstimatesWorkItemId().toString();
	}

	public java.lang.Long getEstimatesWorkItemId() {
		return estimatesWorkItemId;
	}

	public void setEstimatesWorkItemId(java.lang.Long estimatesWorkItemId) {
		this.estimatesWorkItemId = estimatesWorkItemId;
	}

	public java.lang.Long getConstrEstimateInfoId() {
		return constrEstimateInfoId;
	}

	public void setConstrEstimateInfoId(java.lang.Long constrEstimateInfoId) {
		this.constrEstimateInfoId = constrEstimateInfoId;
	}

	public java.lang.Long getConstructionId() {
		return constructionId;
	}

	public void setConstructionId(java.lang.Long constructionId) {
		this.constructionId = constructionId;
	}

	public java.lang.Long getEstimatesItemChildId() {
		return estimatesItemChildId;
	}

	public void setEstimatesItemChildId(java.lang.Long estimatesItemChildId) {
		this.estimatesItemChildId = estimatesItemChildId;
	}

	public java.lang.String getWorkItemCode() {
		return Strings.nullToEmpty(workItemCode);
	}

	public void setWorkItemCode(java.lang.String workItemCode) {
		this.workItemCode = workItemCode;
	}

	public java.lang.String getWorkItemName() {
		return Strings.nullToEmpty(workItemName);
	}

	public void setWorkItemName(java.lang.String workItemName) {
		this.workItemName = workItemName;
	}

	public java.lang.String getUnit() {
		return Strings.nullToEmpty(unit);
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}

	public java.lang.Double getWorkAmount() {
		if(parent){
			return workAmount;
		}
		return workAmount==null?0D:workAmount;
	}

	public void setWorkAmount(java.lang.Double workAmount) {
		this.workAmount = workAmount;
	}

	public java.lang.Double getUnitPrice() {
		if(parent){
			return unitPrice;
		}
		if(unitPrice == null){
			unitPrice = 0D;
		}
		return unitPrice;
	}
	private static final Double DEFAULT_TAX=1.1;
	
	public java.lang.Double getUnitPriceTax() {
		if(parent){
			if(unitPrice!=null){
				return unitPrice*DEFAULT_TAX;
			}
			return unitPrice;
		}
		if(unitPrice == null){
			unitPrice = 0D;
		}
		return unitPrice*DEFAULT_TAX;
	}

	public void setUnitPrice(java.lang.Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public java.lang.Double getTotalMoney() {
		if(parent){
			return null;
		}
		if(totalMoney == null){
			totalMoney = 0D;
		}
		return totalMoney;
	}

	public void setTotalMoney(java.lang.Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public java.lang.Long getProgressType() {
		return progressType;
	}

	public void setProgressType(java.lang.Long progressType) {
		this.progressType = progressType;
	}

	public java.lang.Long getUnitPriceBid() {
		return unitPriceBid;
	}

	public void setUnitPriceBid(java.lang.Long unitPriceBid) {
		this.unitPriceBid = unitPriceBid;
	}

	public java.lang.Long getTotalMoneyBid() {
		return totalMoneyBid;
	}

	public void setTotalMoneyBid(java.lang.Long totalMoneyBid) {
		this.totalMoneyBid = totalMoneyBid;
	}

	public java.lang.Long getUnitPriceBidAward() {
		return unitPriceBidAward;
	}

	public void setUnitPriceBidAward(java.lang.Long unitPriceBidAward) {
		this.unitPriceBidAward = unitPriceBidAward;
	}

	public java.lang.Long getTotalMoneyBidAward() {
		return totalMoneyBidAward;
	}

	public void setTotalMoneyBidAward(java.lang.Long totalMoneyBidAward) {
		this.totalMoneyBidAward = totalMoneyBidAward;
	}

	public java.lang.Long getUnitPriceContract() {
		return unitPriceContract;
	}

	public void setUnitPriceContract(java.lang.Long unitPriceContract) {
		this.unitPriceContract = unitPriceContract;
	}

	public java.lang.Long getTotalMoneyContract() {
		return totalMoneyContract;
	}

	public void setTotalMoneyContract(java.lang.Long totalMoneyContract) {
		this.totalMoneyContract = totalMoneyContract;
	}

	public java.lang.Long getType() {
		if(type == null){
			type= 1l;
		}
		return type;
	}

	public void setType(java.lang.Long type) {
		this.type = type;
	}

	public java.lang.String getConstrtCode() {
		return Strings.nullToEmpty(constrtCode);
	}

	public void setConstrtCode(java.lang.String constrtCode) {
		this.constrtCode = constrtCode;
	}

	public java.lang.Long getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(java.lang.Long isAccepted) {
		this.isAccepted = isAccepted;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.lang.Long getCheckcode() {
		return checkcode;
	}

	public void setCheckcode(java.lang.Long checkcode) {
		this.checkcode = checkcode;
	}

	public java.lang.String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(java.lang.String partnerName) {
		this.partnerName = partnerName;
	}

	public java.lang.Long getConstrType() {
		return constrType;
	}

	public void setConstrType(java.lang.Long constrType) {
		this.constrType = constrType;
	}

	public java.lang.String getConstrtName() {
		return Strings.nullToEmpty(constrtName);
	}

	public void setConstrtName(java.lang.String constrtName) {
		this.constrtName = constrtName;
	}

	public java.lang.Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(java.lang.Long provinceId) {
		this.provinceId = provinceId;
	}

	public java.lang.Long getConstrAcceptWorkListId() {
		return constrAcceptWorkListId;
	}

	public void setConstrAcceptWorkListId(java.lang.Long constrAcceptWorkListId) {
		this.constrAcceptWorkListId = constrAcceptWorkListId;
	}

	public java.lang.String getExplain() {
		return explain;
	}

	public void setExplain(java.lang.String explain) {
		this.explain = explain;
	}

	public java.lang.Double getExecuteQuantity() {		
		return executeQuantity;
	}

	public void setExecuteQuantity(java.lang.Double executeQuantity) {
		this.executeQuantity = executeQuantity;
	}

	public java.lang.String getInstanceDrawCode() {
		return instanceDrawCode==null?"":instanceDrawCode;
	}

	public void setInstanceDrawCode(java.lang.String instanceDrawCode) {
		this.instanceDrawCode = instanceDrawCode;
	}

	public java.lang.String getComments() {
		return comments;
	}

	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}
	
	public java.lang.Double getSettleUnitPrice() {
		return settleUnitPrice;
	}

	public void setSettleUnitPrice(java.lang.Double settleUnitPrice) {
		this.settleUnitPrice = settleUnitPrice;
	}

	public java.lang.Double getEvaluateQuantity() {
		if(parent){
			return null;
		}
		if(evaluateQuantity == null){
			evaluateQuantity = 0d;
		}
		return evaluateQuantity;
	}

	public void setEvaluateQuantity(java.lang.Double evaluateQuantity) {
		this.evaluateQuantity = evaluateQuantity;
	}

	public java.lang.Double getEvaluateUnitPrice() {
		if(parent){
			return null;
		}
		if(evaluateUnitPrice == null){
			evaluateUnitPrice = 0d;
		}
		return evaluateUnitPrice;
	}
	
	public java.lang.Double getEvaluateUnitPriceTax() {
		if(parent){
			return null;
		}
		if(evaluateUnitPrice == null){
			evaluateUnitPrice = 0d;
		}
		return evaluateUnitPrice*DEFAULT_TAX;
	}

	public void setEvaluateUnitPrice(java.lang.Double evaluateUnitPrice) {
		this.evaluateUnitPrice = evaluateUnitPrice;
	}

	public java.lang.Integer getRownum() {
		
		return rownum ;
	}

	public void setRownum(java.lang.Integer rownum) {
		this.rownum = rownum;
	}

	public java.lang.Boolean getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(java.lang.Boolean checkstatus) {
		this.checkstatus = checkstatus;
	}

	public java.lang.Long getTypeworkitem() {
		return typeworkitem;
	}

	public void setTypeworkitem(java.lang.Long typeworkitem) {
		this.typeworkitem = typeworkitem;
	}

	public java.lang.Double getThanhtien1() {
		if(thanhtien1==null){
			return 0d;
		}
		return thanhtien1;
	}

	public void setThanhtien1(java.lang.Double thanhtien1) {
		this.thanhtien1 = thanhtien1;
	}

	public java.lang.Double getThanhtien2() {
		if(thanhtien2 == null){
			thanhtien2= 0d;
		}
		return thanhtien2;
	}

	public void setThanhtien2(java.lang.Double thanhtien2) {
		this.thanhtien2 = thanhtien2;
	}

	public java.lang.Double getTang() {
		 if(tang == null){
			 tang = 0d;
		 };
		return tang;
	}

	public void setTang(java.lang.Double tang) {
		this.tang = tang;
	}

	public java.lang.Double getGiam() {
		 if(giam == null){
			 giam = 0d;
		 };
		return giam;
	}

	public void setGiam(java.lang.Double giam) {
		this.giam = giam;
	}

	public String getConstrtAddress() {
		return Strings.nullToEmpty(constrtAddress);
	}

	public void setConstrtAddress(String constrtAddress) {
		this.constrtAddress = constrtAddress;
	}

	public String getContractCode() {
		return Strings.nullToEmpty(contractCode);
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return Strings.nullToEmpty(contractName);
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	
	public java.lang.Long getConstructionAcceptanceId() {
		return constructionAcceptanceId;
	}

	public void setConstructionAcceptanceId(java.lang.Long constructionAcceptanceId) {
		this.constructionAcceptanceId = constructionAcceptanceId;
	}

	public java.lang.String getCategory() {
		return Category;
	}

	public void setCategory(java.lang.String category) {
		Category = category;
	}
	

	public String getConstructorName() {
		return Strings.nullToEmpty(constructorName);
	}

	public void setConstructorName(String constructorName) {
		this.constructorName = constructorName;
	}

	public String getaDirectorFullName() {
		return aDirectorFullName;
	}

	public void setaDirectorFullName(String aDirectorFullName) {
		this.aDirectorFullName = aDirectorFullName;
	}

	public String getaInchargeMonitorFullName() {
		return aInchargeMonitorFullName;
	}

	public void setaInchargeMonitorFullName(String aInchargeMonitorFullName) {
		this.aInchargeMonitorFullName = aInchargeMonitorFullName;
	}

	public String getbDirectorFullName() {
		return bDirectorFullName;
	}

	public void setbDirectorFullName(String bDirectorFullName) {
		this.bDirectorFullName = bDirectorFullName;
	}

	public String getbInchargeConstructFullName() {
		return bInchargeConstructFullName;
	}

	public void setbInchargeConstructFullName(String bInchargeConstructFullName) {
		this.bInchargeConstructFullName = bInchargeConstructFullName;
	}

	public String getStation_code() {
		return Strings.nullToEmpty(station_code);
	}

	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}


	public String getCostIngredientCode() {
		return costIngredientCode;
	}

	public void setCostIngredientCode(String costIngredientCode) {
		this.costIngredientCode = costIngredientCode;
	}

	public String getCostIngredientName() {
		return costIngredientName;
	}

	public void setCostIngredientName(String costIngredientName) {
		this.costIngredientName = costIngredientName;
	}

	public List<EstimatesDetailAnalystDTO> getEstimatesDetailAnalysts() {
		return estimatesDetailAnalysts;
	}

	public void setEstimatesDetailAnalysts(List<EstimatesDetailAnalystDTO> estimatesDetailAnalysts) {
		this.estimatesDetailAnalysts = estimatesDetailAnalysts;
	}

	public java.lang.Long getProgressTypeAnalyst() {
		return progressTypeAnalyst;
	}

	public void setProgressTypeAnalyst(java.lang.Long progressTypeAnalyst) {
		this.progressTypeAnalyst = progressTypeAnalyst;
	}

	public java.lang.Long getType4() {
		return type4;
	}

	public void setType4(java.lang.Long type4) {
		this.type4 = type4;
	}

	public java.lang.Long getType2() {
		return type2;
	}

	public void setType2(java.lang.Long type2) {
		this.type2 = type2;
	}

	DecimalFormat df = new DecimalFormat("###.###");
	public java.lang.String getChenhLech() {
		if(totalMoney4 != null && totalMoney != null){
			chenhLech = String.valueOf(df.format(totalMoney4 - totalMoney));			
		}else{
			return chenhLech = "";
		}
		return chenhLech;
	}

	public void setChenhLech(java.lang.String chenhLech) {
		this.chenhLech = chenhLech;
	}

	public java.lang.Boolean getTypeworkitemIncontract() {
		return TypeworkitemIncontract;
	}

	public void setTypeworkitemIncontract(java.lang.Boolean typeworkitemIncontract) {
		TypeworkitemIncontract = typeworkitemIncontract;
	}

	public java.lang.String getPathFile() {
		return pathFile;
	}

	public void setPathFile(java.lang.String pathFile) {
		this.pathFile = pathFile;
	}

	//Huypq-03062020-start
	private Integer page;
	private Integer pageSize;
	private Integer totalRecord;



	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}
	
	//Huy-end
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date acceptanceDate;

	public Date getAcceptanceDate() {
		return acceptanceDate;
	}

	public void setAcceptanceDate(Date acceptanceDate) {
		this.acceptanceDate = acceptanceDate;
	}
	
	public Date getLogDate() {
		return logDate;
	}
	//HienLT56 start 27/7/2020
	private Long typeIncurred;
	public Long getTypeIncurred() {
		return typeIncurred;
	}
	public void setTypeIncurred(Long typeIncurred) {
		this.typeIncurred = typeIncurred;
	}
	//Huypq-27072020-start
	private String provinceCode;

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date logDate;
	

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	//HienLT56 end 27/7/2020
	//HienLT56 start 11092020
	private String unitCode;
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	//HienLT56 end 11092020
	//Huy-end


	
	
	

}
