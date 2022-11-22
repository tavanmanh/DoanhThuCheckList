package com.viettel.coms.dto;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;



import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RpConstructionDTO extends WorkItemDTO {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Long asSignHadoverId;
	private String sysgroupname;
	private String Catprovincecode;
	private String Catstattionhousecode;
	private String Cntcontractcode;
	private String Companyassigndate;
	private String Outofdatereceived;
	private String description;
	private Long sysGroupId;
	private Long catProvinceId;
	private Long catStattionHouseId;
	private Long cntContractId;
	private String cntContractCode;
	private String catProvinceName;
	private String catProvinceCode;
	private String stationCode;
	private String yearComplete;
    private String monthComplete;
    private String dateComplete;
    private String monthYear;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dateFrom;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dateTo;
    private Date dateDo;
    private Long columnHeight;
    private Long numberCo;
    private String houseTypeName;
    private String groundingTypeName;
    private String departmentAssignDate;
    private Long outOfDateStartDate;
    private String haveWorkItemName;
    private Date startingDate;
    private String constructorName;
    private String constructorName1;
    private String constructorName2;
    private String constructionCode;
    private Integer countDateComplete;
    private Integer countCatstationCode;
    private Integer countWorkItemName;
    private Double totalQuantity;
    private String catstationCode;
    private Integer countConstructionCode;
    private Long receivedStatus;
    private Long constructionState;
    private Long completionRecordState;
    private Long outOfdate;
    private Long department;
    private Long outOfDateStart;
    private Long stationType;
    private String sysGroupCode;
    private String catStattionHouseCode;
    private Date  completeCompletionRecord;
    private Date completeDate;
//    hoanm1_20190117_start
    private String cntContractCodeBGMB;
    private String outofdatereceivedBGMB;	
    private Long HeightTM;
    private Long numberCoTM;
    private String houseTypeNameTM;
    private Long HeightDD;
    private Long numberCoDD;
    private String houseTypeNameDD;
    private String workItemOutStanding;
    private String approvedCompleteDate;
    
//    hoanm1_20190117_end
    //Huypq-20190604-start
    private String constructionTypeName;
    public String getConstructionTypeName() {
		return constructionTypeName;
	}

	public void setConstructionTypeName(String constructionTypeName) {
		this.constructionTypeName = constructionTypeName;
	}
    //Huy-end
    
	public String getApprovedCompleteDate() {
		return approvedCompleteDate;
	}

	public void setApprovedCompleteDate(String approvedCompleteDate) {
		this.approvedCompleteDate = approvedCompleteDate;
	}

	public String getWorkItemOutStanding() {
		return workItemOutStanding;
	}

	public void setWorkItemOutStanding(String workItemOutStanding) {
		this.workItemOutStanding = workItemOutStanding;
	}

	public Long getHeightTM() {
		return HeightTM;
	}

	public void setHeightTM(Long heightTM) {
		HeightTM = heightTM;
	}

	public Long getNumberCoTM() {
		return numberCoTM;
	}

	public void setNumberCoTM(Long numberCoTM) {
		this.numberCoTM = numberCoTM;
	}

	public String getHouseTypeNameTM() {
		return houseTypeNameTM;
	}

	public void setHouseTypeNameTM(String houseTypeNameTM) {
		this.houseTypeNameTM = houseTypeNameTM;
	}

	public Long getHeightDD() {
		return HeightDD;
	}

	public void setHeightDD(Long heightDD) {
		HeightDD = heightDD;
	}

	public Long getNumberCoDD() {
		return numberCoDD;
	}

	public void setNumberCoDD(Long numberCoDD) {
		this.numberCoDD = numberCoDD;
	}

	public String getHouseTypeNameDD() {
		return houseTypeNameDD;
	}

	public void setHouseTypeNameDD(String houseTypeNameDD) {
		this.houseTypeNameDD = houseTypeNameDD;
	}

	public String getOutofdatereceivedBGMB() {
		return outofdatereceivedBGMB;
	}

	public void setOutofdatereceivedBGMB(String outofdatereceivedBGMB) {
		this.outofdatereceivedBGMB = outofdatereceivedBGMB;
	}

	public String getCntContractCodeBGMB() {
		return cntContractCodeBGMB;
	}

	public void setCntContractCodeBGMB(String cntContractCodeBGMB) {
		this.cntContractCodeBGMB = cntContractCodeBGMB;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public Date getCompleteCompletionRecord() {
		return completeCompletionRecord;
	}

	public void setCompleteCompletionRecord(Date completeCompletionRecord) {
		this.completeCompletionRecord = completeCompletionRecord;
	}

	public String getSysGroupCode() {
		return sysGroupCode;
	}

	public void setSysGroupCode(String sysGroupCode) {
		this.sysGroupCode = sysGroupCode;
	}

	public String getCatStattionHouseCode() {
		return catStattionHouseCode;
	}

	public void setCatStattionHouseCode(String catStattionHouseCode) {
		this.catStattionHouseCode = catStattionHouseCode;
	}

	public Long getStationType() {
		return stationType;
	}

	public void setStationType(Long stationType) {
		this.stationType = stationType;
	}

	public Long getOutOfDateStart() {
		return outOfDateStart;
	}

	public void setOutOfDateStart(Long outOfDateStart) {
		this.outOfDateStart = outOfDateStart;
	}

	public Long getDepartment() {
		return department;
	}

	public void setDepartment(Long department) {
		this.department = department;
	}

	public Long getOutOfdate() {
		return outOfdate;
	}

	public void setOutOfdate(Long outOfdate) {
		this.outOfdate = outOfdate;
	}

	public Long getCompletionRecordState() {
		return completionRecordState;
	}

	public void setCompletionRecordState(Long completionRecordState) {
		this.completionRecordState = completionRecordState;
	}

	public Long getConstructionState() {
		return constructionState;
	}

	public void setConstructionState(Long constructionState) {
		this.constructionState = constructionState;
	}

	public Long getReceivedStatus() {
		return receivedStatus;
	}

	public void setReceivedStatus(Long receivedStatus) {
		this.receivedStatus = receivedStatus;
	}

	public String getCatstationCode() {
		return catstationCode;
	}

	public void setCatstationCode(String catstationCode) {
		this.catstationCode = catstationCode;
	}

	public Integer getCountConstructionCode() {
		return countConstructionCode;
	}

	public void setCountConstructionCode(Integer countConstructionCode) {
		this.countConstructionCode = countConstructionCode;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public Integer getCountDateComplete() {
		return countDateComplete;
	}

	public void setCountDateComplete(Integer countDateComplete) {
		this.countDateComplete = countDateComplete;
	}

	public Integer getCountCatstationCode() {
		return countCatstationCode;
	}

	public void setCountCatstationCode(Integer countCatstationCode) {
		this.countCatstationCode = countCatstationCode;
	}

	public Integer getCountWorkItemName() {
		return countWorkItemName;
	}

	public void setCountWorkItemName(Integer countWorkItemName) {
		this.countWorkItemName = countWorkItemName;
	}

	public Double getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getConstructorName() {
		return constructorName;
	}

	public void setConstructorName(String constructorName) {
		this.constructorName = constructorName;
	}

	public String getConstructorName1() {
		return constructorName1;
	}

	public void setConstructorName1(String constructorName1) {
		this.constructorName1 = constructorName1;
	}

	public String getConstructorName2() {
		return constructorName2;
	}

	public void setConstructorName2(String constructorName2) {
		this.constructorName2 = constructorName2;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public String getHaveWorkItemName() {
		return haveWorkItemName;
	}

	public void setHaveWorkItemName(String haveWorkItemName) {
		this.haveWorkItemName = haveWorkItemName;
	}

	public String getDepartmentAssignDate() {
		return departmentAssignDate;
	}

	public void setDepartmentAssignDate(String departmentAssignDate) {
		this.departmentAssignDate = departmentAssignDate;
	}

	

	public Long getOutOfDateStartDate() {
		return outOfDateStartDate;
	}

	public void setOutOfDateStartDate(Long outOfDateStartDate) {
		this.outOfDateStartDate = outOfDateStartDate;
	}

	

	public Long getColumnHeight() {
		return columnHeight;
	}

	public void setColumnHeight(Long columnHeight) {
		this.columnHeight = columnHeight;
	}

	public Long getNumberCo() {
		return numberCo;
	}

	public void setNumberCo(Long numberCo) {
		this.numberCo = numberCo;
	}

	public String getHouseTypeName() {
		return houseTypeName;
	}

	public void setHouseTypeName(String houseTypeName) {
		this.houseTypeName = houseTypeName;
	}

	public String getGroundingTypeName() {
		return groundingTypeName;
	}

	public void setGroundingTypeName(String groundingTypeName) {
		this.groundingTypeName = groundingTypeName;
	}

	public String getCatProvinceCode() {
		return catProvinceCode;
	}

	public void setCatProvinceCode(String catProvinceCode) {
		this.catProvinceCode = catProvinceCode;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Date getDateDo() {
		return dateDo;
	}

	public void setDateDo(Date dateDo) {
		this.dateDo = dateDo;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public String getYearComplete() {
		return yearComplete;
	}

	public void setYearComplete(String yearComplete) {
		this.yearComplete = yearComplete;
	}

	public String getMonthComplete() {
		return monthComplete;
	}

	public void setMonthComplete(String monthComplete) {
		this.monthComplete = monthComplete;
	}

	public String getDateComplete() {
		return dateComplete;
	}

	public void setDateComplete(String dateComplete) {
		this.dateComplete = dateComplete;
	}

	public String getCatProvinceName() {
		return catProvinceName;
	}

	public void setCatProvinceName(String catProvinceName) {
		this.catProvinceName = catProvinceName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getCntContractCode() {
		return cntContractCode;
	}

	public void setCntContractCode(String cntContractCode) {
		this.cntContractCode = cntContractCode;
	}

	public Long getCntContractId() {
		return cntContractId;
	}

	public void setCntContractId(Long cntContractId) {
		this.cntContractId = cntContractId;
	}

	public Long getCatStattionHouseId() {
		return catStattionHouseId;
	}

	public void setCatStattionHouseId(Long catStattionHouseId) {
		this.catStattionHouseId = catStattionHouseId;
	}

	public Long getCatProvinceId() {
		return catProvinceId;
	}

	public void setCatProvinceId(Long catProvinceId) {
		this.catProvinceId = catProvinceId;
	}

	public Long getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}

	public Long getAsSignHadoverId() {
		return asSignHadoverId;
	}

	public void setAsSignHadoverId(Long asSignHadoverId) {
		this.asSignHadoverId = asSignHadoverId;
	}

	public String getSysgroupname() {
		return sysgroupname;
	}

	public void setSysgroupname(String sysgroupname) {
		this.sysgroupname = sysgroupname;
	}

	public String getCatprovincecode() {
		return Catprovincecode;
	}

	public void setCatprovincecode(String catprovincecode) {
		Catprovincecode = catprovincecode;
	}

	public String getCatstattionhousecode() {
		return Catstattionhousecode;
	}

	public void setCatstattionhousecode(String catstattionhousecode) {
		Catstattionhousecode = catstattionhousecode;
	}

	public String getCntcontractcode() {
		return Cntcontractcode;
	}

	public void setCntcontractcode(String cntcontractcode) {
		Cntcontractcode = cntcontractcode;
	}

	public String getCompanyassigndate() {
		return Companyassigndate;
	}

	public void setCompanyassigndate(String companyassigndate) {
		Companyassigndate = companyassigndate;
	}

	public String getOutofdatereceived() {
		return Outofdatereceived;
	}

	public void setOutofdatereceived(String outofdatereceived) {
		Outofdatereceived = outofdatereceived;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	hungnx 20180625 end
	
	//HuyPQ-20190724-start
	private String sysGroupType;
//	hoanm1_20190806_start
	private String nguoiGiamSat;
	private String thiCongTrucTiep;
	private String thiCongDoiTac;
	
	
	public String getNguoiGiamSat() {
		return nguoiGiamSat;
	}

	public void setNguoiGiamSat(String nguoiGiamSat) {
		this.nguoiGiamSat = nguoiGiamSat;
	}

	public String getThiCongTrucTiep() {
		return thiCongTrucTiep;
	}

	public void setThiCongTrucTiep(String thiCongTrucTiep) {
		this.thiCongTrucTiep = thiCongTrucTiep;
	}

	public String getThiCongDoiTac() {
		return thiCongDoiTac;
	}

	public void setThiCongDoiTac(String thiCongDoiTac) {
		this.thiCongDoiTac = thiCongDoiTac;
	}
//	hoanm1_20190806_end
	public String getSysGroupType() {
		return sysGroupType;
	}

	public void setSysGroupType(String sysGroupType) {
		this.sysGroupType = sysGroupType;
	}
	
	//Huy-end
//	hoanm1_20190813_start
	private String keySearch;
	public String getKeySearch() {
		return keySearch;
	}

	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}
//	hoanm1_20190813_end
	
//	hoanm1_20190827_start
	private String houseColumnReady;
	private String haveStartPoint;
	private String lengthMeter;
	private String typeMetter;
	private String numColumnsAvaible;
	private String numNewColumn;
	private String typeColumns;
	private String ACReady;
	private String stationReceive;
	
	private String workItemComplete;
	private String xd_dodang;
	private String emai_XD_dodang;
	private String ac_dodang;
	private String emai_AC_dodang;
	private String ld_dodang;
	private String emai_LD_dodang;
	private String thietbi_dodang;
	private String emai_thietbi_dodang;
	private String xong_XD_LD;
	private String xong_LD_TB;
	
	public String getXd_dodang() {
		return xd_dodang;
	}

	public void setXd_dodang(String xd_dodang) {
		this.xd_dodang = xd_dodang;
	}

	public String getAc_dodang() {
		return ac_dodang;
	}

	public void setAc_dodang(String ac_dodang) {
		this.ac_dodang = ac_dodang;
	}

	public String getLd_dodang() {
		return ld_dodang;
	}

	public void setLd_dodang(String ld_dodang) {
		this.ld_dodang = ld_dodang;
	}

	public String getWorkItemComplete() {
		return workItemComplete;
	}

	public void setWorkItemComplete(String workItemComplete) {
		this.workItemComplete = workItemComplete;
	}

	public String getEmai_XD_dodang() {
		return emai_XD_dodang;
	}

	public void setEmai_XD_dodang(String emai_XD_dodang) {
		this.emai_XD_dodang = emai_XD_dodang;
	}

	public String getEmai_AC_dodang() {
		return emai_AC_dodang;
	}

	public void setEmai_AC_dodang(String emai_AC_dodang) {
		this.emai_AC_dodang = emai_AC_dodang;
	}

	public String getEmai_LD_dodang() {
		return emai_LD_dodang;
	}

	public void setEmai_LD_dodang(String emai_LD_dodang) {
		this.emai_LD_dodang = emai_LD_dodang;
	}

	public String getThietbi_dodang() {
		return thietbi_dodang;
	}

	public void setThietbi_dodang(String thietbi_dodang) {
		this.thietbi_dodang = thietbi_dodang;
	}

	public String getEmai_thietbi_dodang() {
		return emai_thietbi_dodang;
	}

	public void setEmai_thietbi_dodang(String emai_thietbi_dodang) {
		this.emai_thietbi_dodang = emai_thietbi_dodang;
	}

	public String getXong_XD_LD() {
		return xong_XD_LD;
	}

	public void setXong_XD_LD(String xong_XD_LD) {
		this.xong_XD_LD = xong_XD_LD;
	}

	public String getXong_LD_TB() {
		return xong_LD_TB;
	}

	public void setXong_LD_TB(String xong_LD_TB) {
		this.xong_LD_TB = xong_LD_TB;
	}

	public String getHouseColumnReady() {
		return houseColumnReady;
	}

	public void setHouseColumnReady(String houseColumnReady) {
		this.houseColumnReady = houseColumnReady;
	}

	public String getHaveStartPoint() {
		return haveStartPoint;
	}

	public void setHaveStartPoint(String haveStartPoint) {
		this.haveStartPoint = haveStartPoint;
	}

	public String getLengthMeter() {
		return lengthMeter;
	}

	public void setLengthMeter(String lengthMeter) {
		this.lengthMeter = lengthMeter;
	}

	public String getTypeMetter() {
		return typeMetter;
	}

	public void setTypeMetter(String typeMetter) {
		this.typeMetter = typeMetter;
	}

	public String getNumColumnsAvaible() {
		return numColumnsAvaible;
	}

	public void setNumColumnsAvaible(String numColumnsAvaible) {
		this.numColumnsAvaible = numColumnsAvaible;
	}

	public String getNumNewColumn() {
		return numNewColumn;
	}

	public void setNumNewColumn(String numNewColumn) {
		this.numNewColumn = numNewColumn;
	}

	public String getTypeColumns() {
		return typeColumns;
	}

	public void setTypeColumns(String typeColumns) {
		this.typeColumns = typeColumns;
	}

	public String getACReady() {
		return ACReady;
	}

	public void setACReady(String aCReady) {
		ACReady = aCReady;
	}

	public String getStationReceive() {
		return stationReceive;
	}

	public void setStationReceive(String stationReceive) {
		this.stationReceive = stationReceive;
	}
//	hoanm1_20190827_end
	
	//Huypq-20191004-start
	private String kpiState;
	
	public String getKpiState() {
		return kpiState;
	}

	public void setKpiState(String kpiState) {
		this.kpiState = kpiState;
	}
	
	
	//Huy-end
}
