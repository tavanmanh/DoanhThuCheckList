package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ProgressTaskOsBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

/**
 *
 * @author hailh10
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "PROGRESS_TASK_OSBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgressTaskOsDTO extends ComsBaseFWDTO<ProgressTaskOsBO> {

	private Long progressTaskOsId;
	private String constructionCode;
	private String cntContractCode;
	private String ttkv;
	private String ttkt;
	private String description;
	private String sourceTask;
	private String constructionType;
	private Long quantityValue;
	private Long hshcValue;
	private Long salaryValue;
	private Long billValue;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdslAccomplishedDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdslConstructing;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdslExpectedCompleteDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdhsTctNotApproval;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdhsSigningGdcn;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdhsControl4a;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdhsPhtApprovaling;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdhsPhtAcceptancing;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdhsTtktProfile;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdhsExpectedCompleteDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdttCollectMoneyDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdttProfilePht;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdttProfilePtc;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date tdttExpectedCompleteDate;
	private Long createdUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createdDate;
	private Long updatedUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date updatedDate;
	private String status;
	private String monthYear;
	private String checkHtct;

	// tatph-start-9/1/2020
	private Long giaTriSLXL;
	private Long giaTriSLCP;
	private Long giaTriSLNTD;
	private Long giaTriQuyLuong;
	private Long giaTriHSHCXL;
	private Long giaTriHSHCNTD;
	private Long giaTriHSHCCP;
	private Long giaTriTHDT;
	private String provinceName;
	// tatph-end-9/1/2020

	@Override
	public ProgressTaskOsBO toModel() {
		ProgressTaskOsBO progressTaskOsBO = new ProgressTaskOsBO();
		progressTaskOsBO.setProgressTaskOsId(this.progressTaskOsId);
		progressTaskOsBO.setConstructionCode(this.constructionCode);
		progressTaskOsBO.setCntContractCode(this.cntContractCode);
		progressTaskOsBO.setTtkv(this.ttkv);
		progressTaskOsBO.setTtkt(this.ttkt);
		progressTaskOsBO.setDescription(this.description);
		progressTaskOsBO.setSourceTask(this.sourceTask);
		progressTaskOsBO.setConstructionType(this.constructionType);
		progressTaskOsBO.setQuantityValue(this.quantityValue);
		progressTaskOsBO.setHshcValue(this.hshcValue);
		progressTaskOsBO.setSalaryValue(this.salaryValue);
		progressTaskOsBO.setBillValue(this.billValue);
		progressTaskOsBO.setTdslAccomplishedDate(this.tdslAccomplishedDate);
		progressTaskOsBO.setTdslConstructing(this.tdslConstructing);
		progressTaskOsBO.setTdslExpectedCompleteDate(this.tdslExpectedCompleteDate);
		progressTaskOsBO.setTdhsTctNotApproval(this.tdhsTctNotApproval);
		progressTaskOsBO.setTdhsSigningGdcn(this.tdhsSigningGdcn);
		progressTaskOsBO.setTdhsControl4a(this.tdhsControl4a);
		progressTaskOsBO.setTdhsPhtApprovaling(this.tdhsPhtApprovaling);
		progressTaskOsBO.setTdhsPhtAcceptancing(this.tdhsPhtAcceptancing);
		progressTaskOsBO.setTdhsTtktProfile(this.tdhsTtktProfile);
		progressTaskOsBO.setTdhsExpectedCompleteDate(this.tdhsExpectedCompleteDate);
		progressTaskOsBO.setTdttCollectMoneyDate(this.tdttCollectMoneyDate);
		progressTaskOsBO.setTdttProfilePht(this.tdttProfilePht);
		progressTaskOsBO.setTdttProfilePtc(this.tdttProfilePtc);
		progressTaskOsBO.setTdttExpectedCompleteDate(this.tdttExpectedCompleteDate);
		progressTaskOsBO.setCreatedUserId(this.getCreatedUserId());
		progressTaskOsBO.setCreatedDate(this.getCreatedDate());
		progressTaskOsBO.setUpdatedUserId(this.getUpdatedUserId());
		progressTaskOsBO.setUpdatedDate(this.getUpdatedDate());
		progressTaskOsBO.setStatus(this.status);
		progressTaskOsBO.setMonthYear(this.monthYear);
		progressTaskOsBO.setCheckHtct(this.checkHtct);
		progressTaskOsBO.setApproveRevenueDate(this.approveRevenueDate);
		progressTaskOsBO.setApproveCompleteDate(this.approveCompleteDate);
		progressTaskOsBO.setTtktId(this.ttktId);
		progressTaskOsBO.setWorkItemName(this.workItemName);
		progressTaskOsBO.setType(this.type);
		return progressTaskOsBO;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Long getGiaTriSLXL() {
		return giaTriSLXL;
	}

	public void setGiaTriSLXL(Long giaTriSLXL) {
		this.giaTriSLXL = giaTriSLXL;
	}

	public Long getGiaTriSLCP() {
		return giaTriSLCP;
	}

	public void setGiaTriSLCP(Long giaTriSLCP) {
		this.giaTriSLCP = giaTriSLCP;
	}

	public Long getGiaTriSLNTD() {
		return giaTriSLNTD;
	}

	public void setGiaTriSLNTD(Long giaTriSLNTD) {
		this.giaTriSLNTD = giaTriSLNTD;
	}

	public Long getGiaTriQuyLuong() {
		return giaTriQuyLuong;
	}

	public void setGiaTriQuyLuong(Long giaTriQuyLuong) {
		this.giaTriQuyLuong = giaTriQuyLuong;
	}

	public Long getGiaTriHSHCXL() {
		return giaTriHSHCXL;
	}

	public void setGiaTriHSHCXL(Long giaTriHSHCXL) {
		this.giaTriHSHCXL = giaTriHSHCXL;
	}

	public Long getGiaTriHSHCNTD() {
		return giaTriHSHCNTD;
	}

	public void setGiaTriHSHCNTD(Long giaTriHSHCNTD) {
		this.giaTriHSHCNTD = giaTriHSHCNTD;
	}

	public Long getGiaTriHSHCCP() {
		return giaTriHSHCCP;
	}

	public void setGiaTriHSHCCP(Long giaTriHSHCCP) {
		this.giaTriHSHCCP = giaTriHSHCCP;
	}

	public Long getGiaTriTHDT() {
		return giaTriTHDT;
	}

	public void setGiaTriTHDT(Long giaTriTHDT) {
		this.giaTriTHDT = giaTriTHDT;
	}

	@Override
	public Long getFWModelId() {
		return progressTaskOsId;
	}

	@Override
	public String catchName() {
		return progressTaskOsId.toString();
	}

	public Long getProgressTaskOsId() {
		return progressTaskOsId;
	}

	public void setProgressTaskOsId(Long progressTaskOsId) {
		this.progressTaskOsId = progressTaskOsId;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public String getCntContractCode() {
		return cntContractCode;
	}

	public void setCntContractCode(String cntContractCode) {
		this.cntContractCode = cntContractCode;
	}

	public String getTtkv() {
		return ttkv;
	}

	public void setTtkv(String ttkv) {
		this.ttkv = ttkv;
	}

	public String getTtkt() {
		return ttkt;
	}

	public void setTtkt(String ttkt) {
		this.ttkt = ttkt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSourceTask() {
		return sourceTask;
	}

	public void setSourceTask(String sourceTask) {
		this.sourceTask = sourceTask;
	}

	public String getConstructionType() {
		return constructionType;
	}

	public void setConstructionType(String constructionType) {
		this.constructionType = constructionType;
	}

	public Long getQuantityValue() {
		return quantityValue;
	}

	public void setQuantityValue(Long quantityValue) {
		this.quantityValue = quantityValue;
	}

	public Long getHshcValue() {
		return hshcValue;
	}

	public void setHshcValue(Long hshcValue) {
		this.hshcValue = hshcValue;
	}

	public Long getSalaryValue() {
		return salaryValue;
	}

	public void setSalaryValue(Long salaryValue) {
		this.salaryValue = salaryValue;
	}

	public Long getBillValue() {
		return billValue;
	}

	public void setBillValue(Long billValue) {
		this.billValue = billValue;
	}

	public Date getTdslAccomplishedDate() {
		return tdslAccomplishedDate;
	}

	public void setTdslAccomplishedDate(Date tdslAccomplishedDate) {
		this.tdslAccomplishedDate = tdslAccomplishedDate;
	}

	public Date getTdslConstructing() {
		return tdslConstructing;
	}

	public void setTdslConstructing(Date tdslConstructing) {
		this.tdslConstructing = tdslConstructing;
	}

	public Date getTdslExpectedCompleteDate() {
		return tdslExpectedCompleteDate;
	}

	public void setTdslExpectedCompleteDate(Date tdslExpectedCompleteDate) {
		this.tdslExpectedCompleteDate = tdslExpectedCompleteDate;
	}

	public Date getTdhsTctNotApproval() {
		return tdhsTctNotApproval;
	}

	public void setTdhsTctNotApproval(Date tdhsTctNotApproval) {
		this.tdhsTctNotApproval = tdhsTctNotApproval;
	}

	public Date getTdhsSigningGdcn() {
		return tdhsSigningGdcn;
	}

	public void setTdhsSigningGdcn(Date tdhsSigningGdcn) {
		this.tdhsSigningGdcn = tdhsSigningGdcn;
	}

	public Date getTdhsControl4a() {
		return tdhsControl4a;
	}

	public void setTdhsControl4a(Date tdhsControl4a) {
		this.tdhsControl4a = tdhsControl4a;
	}

	public Date getTdhsPhtApprovaling() {
		return tdhsPhtApprovaling;
	}

	public void setTdhsPhtApprovaling(Date tdhsPhtApprovaling) {
		this.tdhsPhtApprovaling = tdhsPhtApprovaling;
	}

	public Date getTdhsPhtAcceptancing() {
		return tdhsPhtAcceptancing;
	}

	public void setTdhsPhtAcceptancing(Date tdhsPhtAcceptancing) {
		this.tdhsPhtAcceptancing = tdhsPhtAcceptancing;
	}

	public Date getTdhsTtktProfile() {
		return tdhsTtktProfile;
	}

	public void setTdhsTtktProfile(Date tdhsTtktProfile) {
		this.tdhsTtktProfile = tdhsTtktProfile;
	}

	public Date getTdhsExpectedCompleteDate() {
		return tdhsExpectedCompleteDate;
	}

	public void setTdhsExpectedCompleteDate(Date tdhsExpectedCompleteDate) {
		this.tdhsExpectedCompleteDate = tdhsExpectedCompleteDate;
	}

	public Date getTdttCollectMoneyDate() {
		return tdttCollectMoneyDate;
	}

	public void setTdttCollectMoneyDate(Date tdttCollectMoneyDate) {
		this.tdttCollectMoneyDate = tdttCollectMoneyDate;
	}

	public Date getTdttProfilePht() {
		return tdttProfilePht;
	}

	public void setTdttProfilePht(Date tdttProfilePht) {
		this.tdttProfilePht = tdttProfilePht;
	}

	public Date getTdttProfilePtc() {
		return tdttProfilePtc;
	}

	public void setTdttProfilePtc(Date tdttProfilePtc) {
		this.tdttProfilePtc = tdttProfilePtc;
	}

	public Date getTdttExpectedCompleteDate() {
		return tdttExpectedCompleteDate;
	}

	public void setTdttExpectedCompleteDate(Date tdttExpectedCompleteDate) {
		this.tdttExpectedCompleteDate = tdttExpectedCompleteDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public String getCheckHtct() {
		return checkHtct;
	}

	public void setCheckHtct(String checkHtct) {
		this.checkHtct = checkHtct;
	}

	private List<ProgressTaskOsDTO> listProgressTask;

	public List<ProgressTaskOsDTO> getListProgressTask() {
		return listProgressTask;
	}

	public void setListProgressTask(List<ProgressTaskOsDTO> listProgressTask) {
		this.listProgressTask = listProgressTask;
	}

	// Huypq-09012020-start
	private Long tdQlKhThang;
	private Long tdQlTong;
	private Long tdQlTthtDaDuyet;
	private Long tdQlTrenDuongValue;
	private Double tdQlTrenDuongTyle;
	private Long tdQlGdCnKyValue;
	private Double tdQlGdCnKyTyLe;
	private Long tdQlDoiSoat4aValue;
	private Double tdQlDoiSoat4aTyLe;
	private Long tdQlPhtThamDuyetValue;
	private Double tdQlPhtThamDuyetTyLe;
	private Long tdQlPhtNghiemThuValue;
	private Double tdQlPhtNghiemThuTyLe;
	private Long tdQlDangLamHoSoValue;
	private Double tdQlDangLamHoSoTyLe;
	private Long tdQlDuKienHoanThanhValue;
	private Double tdQlDuKienHoanThanhTyLe;
	private String tdQlNguyenNhanKoHt;
	//
	private Long tdQlTthtDaDuyetValue;
	private Double tdQlTthtDaDuyetTyLe;
	//

	private Long tdHshcCpKhThang;
	private Long tdHshcCpTong;
	private Long tdHshcCpTthtDaDuyet;
	private Long tdHshcCpTrenDuong;
	private Long tdHshcCpTrenDuongValue;
	private Double tdHshcCpTrenDuongTyLe;
	private Long tdHshcCpGdCnKyValue;
	private Double tdHshcCpGdCnKyTyLe;
	private Long tdHshcCpDoiSoat4aValue;
	private Double tdHshcCpDoiSoat4aTyLe;
	private Long tdHshcCpPhtThamDuyetValue;
	private Double tdHshcCpPhtThamDuyetTyLe;
	private Long tdHshcCpPhtNghiemThuValue;
	private Double tdHshcCpPhtNghiemThuTyLe;
	private Long tdHshcCpDangLamHoSoValue;
	private Double tdHshcCpDangLamHoSoTyLe;
	private Long tdHshcCpDuKienHoanThanhValue;
	private Double tdHshcCpDuKienHoanThanhTyLe;
	private String tdHshcCpNguyenNhanKoHt;
	//
	private Long tdHshcCpTthtDaDuyetValue;
	private Long tdHshcCpTthtDaDuyetTyLe;
	//

	private Long tdHshcXlKhThang;
	private Long tdHshcXlTong;
	private Long tdHshcXlTthtDaDuyet;
	private Long tdHshcXlTrenDuongValue;
	private Double tdHshcXlTrenDuongTyLe;
	private Long tdHshcXlGdCnKyValue;
	private Double tdHshcXlGdCnKyTyLe;
	private Long tdHshcXlDoiSoat4aValue;
	private Double tdHshcXlDoiSoat4aTyLe;
	private Long tdHshcXlPhtThamDuyetValue;
	private Double tdHshcXlPhtThamDuyetTyLe;
	private Long tdHshcXlPhtNghiemThuValue;
	private Double tdHshcXlPhtNghiemThuTyLe;
	private Long tdHshcXlDangLamHoSoValue;
	private Double tdHshcXlDangLamHoSoTyLe;
	private Long tdHshcXlDuKienHoanThanhValue;
	private Double tdHshcXlDuKienHoanThanhTyLe;
	private String tdHshcXlNguyenNhanKoHt;

	private Long tdHshcNtdKhThang;
	private Long tdHshcNtdTong;
	private Long tdHshcNtdPtkDaDuyet;
	private Long tdHshcNtdTrenDuongValue;
	private Double tdHshcNtdTrenDuongTyLe;
	private Long tdHshcNtdGdCnKyValue;
	private Double tdHshcNtdGdCnKyTyLe;
	private Long tdHshcNtdDoiSoat4aValue;
	private Double tdHshcNtdDoiSoat4aTyLe;
	private Long tdHshcNtdPhtThamDuyetValue;
	private Double tdHshcNtdPhtThamDuyetTyLe;
	private Long tdHshcNtdPhtNghiemThuValue;
	private Double tdHshcNtdPhtNghiemThuTyLe;
	private Long tdHshcNtdDangLamHoSoValue;
	private Double tdHshcNtdDangLamHoSoTyLe;
	private Long tdHshcNtdDuKienHoanThanhValue;
	private Double tdHshcNtdDuKienHoanThanhTyLe;
	private String tdHshcNtdNguyenNhanKoHt;

	private Long tdSlCpKhThang;
	private Long tdSlCpDaHoanThanh;
	private Double tdSlCpDaHoanThanhTyLe;
	private Long tdSlCpDangThiCongValue;
	private Double tdSlCpDangThiCongTyLe;
	private Long tdSlCpDuKienHoanThanhValue;
	private Double tdSlCpDuKienHoanThanhTyLe;
	private String tdSlCpNguyenNhanKoHt;

	private Long tdSlXlKhThang;
	private Long tdSlXlDaHoanThanh;
	private Double tdSlXlDaHoanThanhTyLe;
	private Long tdSlXlDangThiCongValue;
	private Double tdSlXlDangThiCongTyLe;
	private Long tdSlXlDuKienHoanThanhValue;
	private Double tdSlXlDuKienHoanThanhTyLe;
	private String tdSlXlNguyenNhanKoHt;

	private Long tdSlNtdKhThang;
	private Long tdSlNtdDaHoanThanh;
	private Double tdSlNtdDaHoanThanhTyLe;
	private Long tdSlNtdDangThiCongValue;
	private Double tdSlNtdDangThiCongTyLe;
	private Long tdSlNtdDuKienHoanThanhValue;
	private Double tdSlNtdDuKienHoanThanhTyLe;
	private String tdSlNtdNguyenNhanKoHt;

	private Long tdSlXdddKhThang;
	private Long tdSlXdddDaHoanThanh;
	private Double tdSlXdddDaHoanThanhTyLe;
	private Long tdSlXdddDangThiCongValue;
	private Double tdSlXdddDangThiCongTyLe;
	private Long tdSlXdddDuKienHoanThanhValue;
	private Double tdSlXdddDuKienHoanThanhTyLe;
	private String tdSlXdddNguyenNhanKoHt;

	private Long tdThdtKhThang;
	private Long tdThdtDaHoanThanh;
	private Double tdThdtDaHoanThanhTyLe;
	private Long tdThdtPhtDangKiemTraValue;
	private Double tdThdtPhtDangKiemTraTyLe;
	private Long tdThdtPtcDangKiemTraValue;
	private Double tdThdtPtcDangKiemTraTyLe;
	private Long tdThdtDuKienHoanThanhValue;
	private Double tdThdtDuKienHoanThanhTyLe;
	private String tdThdtNguyenNhanKoHt;

	private Long tdHtctXdmKhThang;
	private Long tdHtctXdmDaHt;
	private Long tdHtctXdmDangTc;
	private Double tdHtctXdmDangTcTyLe;
	private Long tdHtctXdmDuKienHt;
	private Double tdHtctXdmDuKienHtTyLe;
	private Double tdHtctXdmTyLe;

	private Long tdHtctHtKhThang;
	private Long tdHtctHtDaHt;
	private Long tdHtctHtDangTc;
	private Long tdHtctHtDuKienHt;
	private Double tdHtctHtDangTcTyLe;
	private Double tdHtctHtDuKienHtTyLe;
	private Double tdHtctHtTyLe;
	private String tdHtctNguyenNhanKoHt;

	private Long tdHshcHtctKhThang;
	private Long tdHshcHtctTong;
	private Long tdHshcHtctPtkDuyet;
	private Long tdHshcHtctTrenDuongValue;
	private Double tdHshcHtctTrenDuongTyLe;
	private Long tdHshcHtctGdCnKyValue;
	private Double tdHshcHtctGdCnKyTyLe;
	private Long tdHshcHtctDoiSoat4aValue;
	private Double tdHshcHtctDoiSoat4aTyLe;
	private Long tdHshcHtctPhtThamDuyetValue;
	private Double tdHshcHtctPhtThamDuyetTyLe;
	private Long tdHshcHtctPhtNghiemThuValue;
	private Double tdHshcHtctPhtNghiemThuTyLe;
	private Long tdHshcHtctTtktHoSoValue;
	private Double tdHshcHtctTtktHoSoTyLe;
	private Long tdHshcHtctDuKienHtValue;
	private Double tdHshcHtctDuKienHtTyLe;
	private String tdHshcHtctNguyenNhanKoHt;

	private Long tdTongKhThang;
	private Long tdTongDaHt;
	private Long tdTongDangTcValue;
	private Double tdTongDangTcTyLe;
	private Long tdTongDuKienHtValue;
	private Double tdTongDuKienHtTyLe;
	private String tdTongDeXuatVuongMac;

	private Long tdHshcNdKhThang;
	private Long tdHshcNdTrenDuongValue;
	private Double tdHshcNdTrenDuongTyLe;
	private Long tdHshcNdGdCnKyValue;
	private Double tdHshcNdGdCnKyTyLe;
	private Long tdHshcNdDoiSoat4aValue;
	private Double tdHshcNdDoiSoat4aTyLe;
	private Long tdHshcNdThamDuyetValue;
	private Double tdHshcNdPhtThamDuyetTyLe;
	private Long tdHshcNdPhtNghiemThuValue;
	private Double tdHshcNdPhtNghiemThuTyLe;
	private Long tdHshcNdHoSoValue;
	private Double tdHshcNdHoSoTyLe;
	private Long tdHshcNdDuKienHtValue;
	private Double tdHshcNdDuKienHtTyLe;

	private Long tdSlHtctKhThang;
	private Long tdSlHtctDaHt;
	private Long tdSlHtctDangTcValue;
	private Double tdSlHtctDangTcTyLe;
	private Long tdSlHtctDuKienHtValue;
	private Double tdSlHtctDuKienHtTyLe;
	private Long tdSlHtctNguyenNhanKoHt;
	private Double tdSlHtctDaHtTyLe;

	public Long getTdHshcNdKhThang() {
		return tdHshcNdKhThang;
	}

	public void setTdHshcNdKhThang(Long tdHshcNdKhThang) {
		this.tdHshcNdKhThang = tdHshcNdKhThang;
	}

	public Long getTdHshcNdTrenDuongValue() {
		return tdHshcNdTrenDuongValue;
	}

	public void setTdHshcNdTrenDuongValue(Long tdHshcNdTrenDuongValue) {
		this.tdHshcNdTrenDuongValue = tdHshcNdTrenDuongValue;
	}

	public Double getTdHshcNdTrenDuongTyLe() {
		return tdHshcNdTrenDuongTyLe;
	}

	public void setTdHshcNdTrenDuongTyLe(Double tdHshcNdTrenDuongTyLe) {
		this.tdHshcNdTrenDuongTyLe = tdHshcNdTrenDuongTyLe;
	}

	public Long getTdHshcNdGdCnKyValue() {
		return tdHshcNdGdCnKyValue;
	}

	public void setTdHshcNdGdCnKyValue(Long tdHshcNdGdCnKyValue) {
		this.tdHshcNdGdCnKyValue = tdHshcNdGdCnKyValue;
	}

	public Double getTdHshcNdGdCnKyTyLe() {
		return tdHshcNdGdCnKyTyLe;
	}

	public void setTdHshcNdGdCnKyTyLe(Double tdHshcNdGdCnKyTyLe) {
		this.tdHshcNdGdCnKyTyLe = tdHshcNdGdCnKyTyLe;
	}

	public Long getTdHshcNdDoiSoat4aValue() {
		return tdHshcNdDoiSoat4aValue;
	}

	public void setTdHshcNdDoiSoat4aValue(Long tdHshcNdDoiSoat4aValue) {
		this.tdHshcNdDoiSoat4aValue = tdHshcNdDoiSoat4aValue;
	}

	public Double getTdHshcNdDoiSoat4aTyLe() {
		return tdHshcNdDoiSoat4aTyLe;
	}

	public void setTdHshcNdDoiSoat4aTyLe(Double tdHshcNdDoiSoat4aTyLe) {
		this.tdHshcNdDoiSoat4aTyLe = tdHshcNdDoiSoat4aTyLe;
	}

	public Long getTdHshcNdThamDuyetValue() {
		return tdHshcNdThamDuyetValue;
	}

	public void setTdHshcNdThamDuyetValue(Long tdHshcNdThamDuyetValue) {
		this.tdHshcNdThamDuyetValue = tdHshcNdThamDuyetValue;
	}

	public Double getTdHshcNdPhtThamDuyetTyLe() {
		return tdHshcNdPhtThamDuyetTyLe;
	}

	public void setTdHshcNdPhtThamDuyetTyLe(Double tdHshcNdPhtThamDuyetTyLe) {
		this.tdHshcNdPhtThamDuyetTyLe = tdHshcNdPhtThamDuyetTyLe;
	}

	public Long getTdHshcNdPhtNghiemThuValue() {
		return tdHshcNdPhtNghiemThuValue;
	}

	public void setTdHshcNdPhtNghiemThuValue(Long tdHshcNdPhtNghiemThuValue) {
		this.tdHshcNdPhtNghiemThuValue = tdHshcNdPhtNghiemThuValue;
	}

	public Double getTdHshcNdPhtNghiemThuTyLe() {
		return tdHshcNdPhtNghiemThuTyLe;
	}

	public void setTdHshcNdPhtNghiemThuTyLe(Double tdHshcNdPhtNghiemThuTyLe) {
		this.tdHshcNdPhtNghiemThuTyLe = tdHshcNdPhtNghiemThuTyLe;
	}

	public Long getTdHshcNdHoSoValue() {
		return tdHshcNdHoSoValue;
	}

	public void setTdHshcNdHoSoValue(Long tdHshcNdHoSoValue) {
		this.tdHshcNdHoSoValue = tdHshcNdHoSoValue;
	}

	public Double getTdHshcNdHoSoTyLe() {
		return tdHshcNdHoSoTyLe;
	}

	public void setTdHshcNdHoSoTyLe(Double tdHshcNdHoSoTyLe) {
		this.tdHshcNdHoSoTyLe = tdHshcNdHoSoTyLe;
	}

	public Long getTdHshcNdDuKienHtValue() {
		return tdHshcNdDuKienHtValue;
	}

	public void setTdHshcNdDuKienHtValue(Long tdHshcNdDuKienHtValue) {
		this.tdHshcNdDuKienHtValue = tdHshcNdDuKienHtValue;
	}

	public Double getTdHshcNdDuKienHtTyLe() {
		return tdHshcNdDuKienHtTyLe;
	}

	public void setTdHshcNdDuKienHtTyLe(Double tdHshcNdDuKienHtTyLe) {
		this.tdHshcNdDuKienHtTyLe = tdHshcNdDuKienHtTyLe;
	}

	public Long getTdSlHtctKhThang() {
		return tdSlHtctKhThang;
	}

	public void setTdSlHtctKhThang(Long tdSlHtctKhThang) {
		this.tdSlHtctKhThang = tdSlHtctKhThang;
	}

	public Long getTdSlHtctDaHt() {
		return tdSlHtctDaHt;
	}

	public void setTdSlHtctDaHt(Long tdSlHtctDaHt) {
		this.tdSlHtctDaHt = tdSlHtctDaHt;
	}

	public Long getTdSlHtctDangTcValue() {
		return tdSlHtctDangTcValue;
	}

	public void setTdSlHtctDangTcValue(Long tdSlHtctDangTcValue) {
		this.tdSlHtctDangTcValue = tdSlHtctDangTcValue;
	}

	public Double getTdSlHtctDangTcTyLe() {
		return tdSlHtctDangTcTyLe;
	}

	public void setTdSlHtctDangTcTyLe(Double tdSlHtctDangTcTyLe) {
		this.tdSlHtctDangTcTyLe = tdSlHtctDangTcTyLe;
	}

	public Long getTdSlHtctDuKienHtValue() {
		return tdSlHtctDuKienHtValue;
	}

	public void setTdSlHtctDuKienHtValue(Long tdSlHtctDuKienHtValue) {
		this.tdSlHtctDuKienHtValue = tdSlHtctDuKienHtValue;
	}

	public Double getTdSlHtctDuKienHtTyLe() {
		return tdSlHtctDuKienHtTyLe;
	}

	public void setTdSlHtctDuKienHtTyLe(Double tdSlHtctDuKienHtTyLe) {
		this.tdSlHtctDuKienHtTyLe = tdSlHtctDuKienHtTyLe;
	}

	public Long getTdSlHtctNguyenNhanKoHt() {
		return tdSlHtctNguyenNhanKoHt;
	}

	public void setTdSlHtctNguyenNhanKoHt(Long tdSlHtctNguyenNhanKoHt) {
		this.tdSlHtctNguyenNhanKoHt = tdSlHtctNguyenNhanKoHt;
	}

	public Long getTdHshcCpTthtDaDuyetValue() {
		return tdHshcCpTthtDaDuyetValue;
	}

	public void setTdHshcCpTthtDaDuyetValue(Long tdHshcCpTthtDaDuyetValue) {
		this.tdHshcCpTthtDaDuyetValue = tdHshcCpTthtDaDuyetValue;
	}

	public Long getTdHshcCpTthtDaDuyetTyLe() {
		return tdHshcCpTthtDaDuyetTyLe;
	}

	public void setTdHshcCpTthtDaDuyetTyLe(Long tdHshcCpTthtDaDuyetTyLe) {
		this.tdHshcCpTthtDaDuyetTyLe = tdHshcCpTthtDaDuyetTyLe;
	}

	public Long getTdQlTthtDaDuyetValue() {
		return tdQlTthtDaDuyetValue;
	}

	public void setTdQlTthtDaDuyetValue(Long tdQlTthtDaDuyetValue) {
		this.tdQlTthtDaDuyetValue = tdQlTthtDaDuyetValue;
	}

	public Double getTdQlTthtDaDuyetTyLe() {
		return tdQlTthtDaDuyetTyLe;
	}

	public void setTdQlTthtDaDuyetTyLe(Double tdQlTthtDaDuyetTyLe) {
		this.tdQlTthtDaDuyetTyLe = tdQlTthtDaDuyetTyLe;
	}

	public Long getTdHtctXdmKhThang() {
		return tdHtctXdmKhThang;
	}

	public void setTdHtctXdmKhThang(Long tdHtctXdmKhThang) {
		this.tdHtctXdmKhThang = tdHtctXdmKhThang;
	}

	public Long getTdHtctXdmDaHt() {
		return tdHtctXdmDaHt;
	}

	public void setTdHtctXdmDaHt(Long tdHtctXdmDaHt) {
		this.tdHtctXdmDaHt = tdHtctXdmDaHt;
	}

	public Long getTdHtctXdmDangTc() {
		return tdHtctXdmDangTc;
	}

	public void setTdHtctXdmDangTc(Long tdHtctXdmDangTc) {
		this.tdHtctXdmDangTc = tdHtctXdmDangTc;
	}

	public Long getTdHtctXdmDuKienHt() {
		return tdHtctXdmDuKienHt;
	}

	public void setTdHtctXdmDuKienHt(Long tdHtctXdmDuKienHt) {
		this.tdHtctXdmDuKienHt = tdHtctXdmDuKienHt;
	}

	public Double getTdHtctXdmTyLe() {
		return tdHtctXdmTyLe;
	}

	public void setTdHtctXdmTyLe(Double tdHtctXdmTyLe) {
		this.tdHtctXdmTyLe = tdHtctXdmTyLe;
	}

	public Long getTdHtctHtKhThang() {
		return tdHtctHtKhThang;
	}

	public void setTdHtctHtKhThang(Long tdHtctHtKhThang) {
		this.tdHtctHtKhThang = tdHtctHtKhThang;
	}

	public Long getTdHtctHtDaHt() {
		return tdHtctHtDaHt;
	}

	public void setTdHtctHtDaHt(Long tdHtctHtDaHt) {
		this.tdHtctHtDaHt = tdHtctHtDaHt;
	}

	public Long getTdHtctHtDangTc() {
		return tdHtctHtDangTc;
	}

	public void setTdHtctHtDangTc(Long tdHtctHtDangTc) {
		this.tdHtctHtDangTc = tdHtctHtDangTc;
	}

	public Long getTdHtctHtDuKienHt() {
		return tdHtctHtDuKienHt;
	}

	public void setTdHtctHtDuKienHt(Long tdHtctHtDuKienHt) {
		this.tdHtctHtDuKienHt = tdHtctHtDuKienHt;
	}

	public Double getTdHtctHtTyLe() {
		return tdHtctHtTyLe;
	}

	public void setTdHtctHtTyLe(Double tdHtctHtTyLe) {
		this.tdHtctHtTyLe = tdHtctHtTyLe;
	}

	public String getTdHtctNguyenNhanKoHt() {
		return tdHtctNguyenNhanKoHt;
	}

	public void setTdHtctNguyenNhanKoHt(String tdHtctNguyenNhanKoHt) {
		this.tdHtctNguyenNhanKoHt = tdHtctNguyenNhanKoHt;
	}

	public Long getTdHshcHtctKhThang() {
		return tdHshcHtctKhThang;
	}

	public void setTdHshcHtctKhThang(Long tdHshcHtctKhThang) {
		this.tdHshcHtctKhThang = tdHshcHtctKhThang;
	}

	public Long getTdHshcHtctTong() {
		return tdHshcHtctTong;
	}

	public void setTdHshcHtctTong(Long tdHshcHtctTong) {
		this.tdHshcHtctTong = tdHshcHtctTong;
	}

	public Long getTdHshcHtctPtkDuyet() {
		return tdHshcHtctPtkDuyet;
	}

	public void setTdHshcHtctPtkDuyet(Long tdHshcHtctPtkDuyet) {
		this.tdHshcHtctPtkDuyet = tdHshcHtctPtkDuyet;
	}

	public Long getTdHshcHtctGdCnKyValue() {
		return tdHshcHtctGdCnKyValue;
	}

	public void setTdHshcHtctGdCnKyValue(Long tdHshcHtctGdCnKyValue) {
		this.tdHshcHtctGdCnKyValue = tdHshcHtctGdCnKyValue;
	}

	public Double getTdHshcHtctGdCnKyTyLe() {
		return tdHshcHtctGdCnKyTyLe;
	}

	public void setTdHshcHtctGdCnKyTyLe(Double tdHshcHtctGdCnKyTyLe) {
		this.tdHshcHtctGdCnKyTyLe = tdHshcHtctGdCnKyTyLe;
	}

	public Long getTdHshcHtctDoiSoat4aValue() {
		return tdHshcHtctDoiSoat4aValue;
	}

	public void setTdHshcHtctDoiSoat4aValue(Long tdHshcHtctDoiSoat4aValue) {
		this.tdHshcHtctDoiSoat4aValue = tdHshcHtctDoiSoat4aValue;
	}

	public Double getTdHshcHtctDoiSoat4aTyLe() {
		return tdHshcHtctDoiSoat4aTyLe;
	}

	public void setTdHshcHtctDoiSoat4aTyLe(Double tdHshcHtctDoiSoat4aTyLe) {
		this.tdHshcHtctDoiSoat4aTyLe = tdHshcHtctDoiSoat4aTyLe;
	}

	public Long getTdHshcHtctPhtThamDuyetValue() {
		return tdHshcHtctPhtThamDuyetValue;
	}

	public void setTdHshcHtctPhtThamDuyetValue(Long tdHshcHtctPhtThamDuyetValue) {
		this.tdHshcHtctPhtThamDuyetValue = tdHshcHtctPhtThamDuyetValue;
	}

	public Double getTdHshcHtctPhtThamDuyetTyLe() {
		return tdHshcHtctPhtThamDuyetTyLe;
	}

	public void setTdHshcHtctPhtThamDuyetTyLe(Double tdHshcHtctPhtThamDuyetTyLe) {
		this.tdHshcHtctPhtThamDuyetTyLe = tdHshcHtctPhtThamDuyetTyLe;
	}

	public Long getTdHshcHtctPhtNghiemThuValue() {
		return tdHshcHtctPhtNghiemThuValue;
	}

	public void setTdHshcHtctPhtNghiemThuValue(Long tdHshcHtctPhtNghiemThuValue) {
		this.tdHshcHtctPhtNghiemThuValue = tdHshcHtctPhtNghiemThuValue;
	}

	public Double getTdHshcHtctPhtNghiemThuTyLe() {
		return tdHshcHtctPhtNghiemThuTyLe;
	}

	public void setTdHshcHtctPhtNghiemThuTyLe(Double tdHshcHtctPhtNghiemThuTyLe) {
		this.tdHshcHtctPhtNghiemThuTyLe = tdHshcHtctPhtNghiemThuTyLe;
	}

	public Long getTdHshcHtctTtktHoSoValue() {
		return tdHshcHtctTtktHoSoValue;
	}

	public void setTdHshcHtctTtktHoSoValue(Long tdHshcHtctTtktHoSoValue) {
		this.tdHshcHtctTtktHoSoValue = tdHshcHtctTtktHoSoValue;
	}

	public Double getTdHshcHtctTtktHoSoTyLe() {
		return tdHshcHtctTtktHoSoTyLe;
	}

	public void setTdHshcHtctTtktHoSoTyLe(Double tdHshcHtctTtktHoSoTyLe) {
		this.tdHshcHtctTtktHoSoTyLe = tdHshcHtctTtktHoSoTyLe;
	}

	public Long getTdHshcHtctDuKienHtValue() {
		return tdHshcHtctDuKienHtValue;
	}

	public void setTdHshcHtctDuKienHtValue(Long tdHshcHtctDuKienHtValue) {
		this.tdHshcHtctDuKienHtValue = tdHshcHtctDuKienHtValue;
	}

	public Double getTdHshcHtctDuKienHtTyLe() {
		return tdHshcHtctDuKienHtTyLe;
	}

	public void setTdHshcHtctDuKienHtTyLe(Double tdHshcHtctDuKienHtTyLe) {
		this.tdHshcHtctDuKienHtTyLe = tdHshcHtctDuKienHtTyLe;
	}

	public String getTdHshcHtctNguyenNhanKoHt() {
		return tdHshcHtctNguyenNhanKoHt;
	}

	public void setTdHshcHtctNguyenNhanKoHt(String tdHshcHtctNguyenNhanKoHt) {
		this.tdHshcHtctNguyenNhanKoHt = tdHshcHtctNguyenNhanKoHt;
	}

	public Long getTdTongKhThang() {
		return tdTongKhThang;
	}

	public void setTdTongKhThang(Long tdTongKhThang) {
		this.tdTongKhThang = tdTongKhThang;
	}

	public Long getTdTongDaHt() {
		return tdTongDaHt;
	}

	public void setTdTongDaHt(Long tdTongDaHt) {
		this.tdTongDaHt = tdTongDaHt;
	}

	public Long getTdTongDangTcValue() {
		return tdTongDangTcValue;
	}

	public void setTdTongDangTcValue(Long tdTongDangTcValue) {
		this.tdTongDangTcValue = tdTongDangTcValue;
	}

	public Double getTdTongDangTcTyLe() {
		return tdTongDangTcTyLe;
	}

	public void setTdTongDangTcTyLe(Double tdTongDangTcTyLe) {
		this.tdTongDangTcTyLe = tdTongDangTcTyLe;
	}

	public Long getTdTongDuKienHtValue() {
		return tdTongDuKienHtValue;
	}

	public void setTdTongDuKienHtValue(Long tdTongDuKienHtValue) {
		this.tdTongDuKienHtValue = tdTongDuKienHtValue;
	}

	public Double getTdTongDuKienHtTyLe() {
		return tdTongDuKienHtTyLe;
	}

	public void setTdTongDuKienHtTyLe(Double tdTongDuKienHtTyLe) {
		this.tdTongDuKienHtTyLe = tdTongDuKienHtTyLe;
	}

	public String getTdTongDeXuatVuongMac() {
		return tdTongDeXuatVuongMac;
	}

	public void setTdTongDeXuatVuongMac(String tdTongDeXuatVuongMac) {
		this.tdTongDeXuatVuongMac = tdTongDeXuatVuongMac;
	}

	public Long getTdQlKhThang() {
		return tdQlKhThang;
	}

	public void setTdQlKhThang(Long tdQlKhThang) {
		this.tdQlKhThang = tdQlKhThang;
	}

	public Long getTdQlTong() {
		return tdQlTong;
	}

	public void setTdQlTong(Long tdQlTong) {
		this.tdQlTong = tdQlTong;
	}

	public Long getTdQlTthtDaDuyet() {
		return tdQlTthtDaDuyet;
	}

	public void setTdQlTthtDaDuyet(Long tdQlTthtDaDuyet) {
		this.tdQlTthtDaDuyet = tdQlTthtDaDuyet;
	}

	public Long getTdQlGdCnKyValue() {
		return tdQlGdCnKyValue;
	}

	public void setTdQlGdCnKyValue(Long tdQlGdCnKyValue) {
		this.tdQlGdCnKyValue = tdQlGdCnKyValue;
	}

	public Double getTdQlGdCnKyTyLe() {
		return tdQlGdCnKyTyLe;
	}

	public void setTdQlGdCnKyTyLe(Double tdQlGdCnKyTyLe) {
		this.tdQlGdCnKyTyLe = tdQlGdCnKyTyLe;
	}

	public Long getTdQlDoiSoat4aValue() {
		return tdQlDoiSoat4aValue;
	}

	public void setTdQlDoiSoat4aValue(Long tdQlDoiSoat4aValue) {
		this.tdQlDoiSoat4aValue = tdQlDoiSoat4aValue;
	}

	public Double getTdQlDoiSoat4aTyLe() {
		return tdQlDoiSoat4aTyLe;
	}

	public void setTdQlDoiSoat4aTyLe(Double tdQlDoiSoat4aTyLe) {
		this.tdQlDoiSoat4aTyLe = tdQlDoiSoat4aTyLe;
	}

	public Long getTdQlPhtThamDuyetValue() {
		return tdQlPhtThamDuyetValue;
	}

	public void setTdQlPhtThamDuyetValue(Long tdQlPhtThamDuyetValue) {
		this.tdQlPhtThamDuyetValue = tdQlPhtThamDuyetValue;
	}

	public Double getTdQlPhtThamDuyetTyLe() {
		return tdQlPhtThamDuyetTyLe;
	}

	public void setTdQlPhtThamDuyetTyLe(Double tdQlPhtThamDuyetTyLe) {
		this.tdQlPhtThamDuyetTyLe = tdQlPhtThamDuyetTyLe;
	}

	public Long getTdQlPhtNghiemThuValue() {
		return tdQlPhtNghiemThuValue;
	}

	public void setTdQlPhtNghiemThuValue(Long tdQlPhtNghiemThuValue) {
		this.tdQlPhtNghiemThuValue = tdQlPhtNghiemThuValue;
	}

	public Double getTdQlPhtNghiemThuTyLe() {
		return tdQlPhtNghiemThuTyLe;
	}

	public void setTdQlPhtNghiemThuTyLe(Double tdQlPhtNghiemThuTyLe) {
		this.tdQlPhtNghiemThuTyLe = tdQlPhtNghiemThuTyLe;
	}

	public Long getTdQlDangLamHoSoValue() {
		return tdQlDangLamHoSoValue;
	}

	public void setTdQlDangLamHoSoValue(Long tdQlDangLamHoSoValue) {
		this.tdQlDangLamHoSoValue = tdQlDangLamHoSoValue;
	}

	public Double getTdQlDangLamHoSoTyLe() {
		return tdQlDangLamHoSoTyLe;
	}

	public void setTdQlDangLamHoSoTyLe(Double tdQlDangLamHoSoTyLe) {
		this.tdQlDangLamHoSoTyLe = tdQlDangLamHoSoTyLe;
	}

	public Long getTdQlDuKienHoanThanhValue() {
		return tdQlDuKienHoanThanhValue;
	}

	public void setTdQlDuKienHoanThanhValue(Long tdQlDuKienHoanThanhValue) {
		this.tdQlDuKienHoanThanhValue = tdQlDuKienHoanThanhValue;
	}

	public Double getTdQlDuKienHoanThanhTyLe() {
		return tdQlDuKienHoanThanhTyLe;
	}

	public void setTdQlDuKienHoanThanhTyLe(Double tdQlDuKienHoanThanhTyLe) {
		this.tdQlDuKienHoanThanhTyLe = tdQlDuKienHoanThanhTyLe;
	}

	public String getTdQlNguyenNhanKoHt() {
		return tdQlNguyenNhanKoHt;
	}

	public void setTdQlNguyenNhanKoHt(String tdQlNguyenNhanKoHt) {
		this.tdQlNguyenNhanKoHt = tdQlNguyenNhanKoHt;
	}

	public Long getTdHshcCpKhThang() {
		return tdHshcCpKhThang;
	}

	public void setTdHshcCpKhThang(Long tdHshcCpKhThang) {
		this.tdHshcCpKhThang = tdHshcCpKhThang;
	}

	public Long getTdHshcCpTong() {
		return tdHshcCpTong;
	}

	public void setTdHshcCpTong(Long tdHshcCpTong) {
		this.tdHshcCpTong = tdHshcCpTong;
	}

	public Long getTdHshcCpTthtDaDuyet() {
		return tdHshcCpTthtDaDuyet;
	}

	public void setTdHshcCpTthtDaDuyet(Long tdHshcCpTthtDaDuyet) {
		this.tdHshcCpTthtDaDuyet = tdHshcCpTthtDaDuyet;
	}

	public Long getTdHshcCpTrenDuong() {
		return tdHshcCpTrenDuong;
	}

	public void setTdHshcCpTrenDuong(Long tdHshcCpTrenDuong) {
		this.tdHshcCpTrenDuong = tdHshcCpTrenDuong;
	}

	public Long getTdHshcCpGdCnKyValue() {
		return tdHshcCpGdCnKyValue;
	}

	public void setTdHshcCpGdCnKyValue(Long tdHshcCpGdCnKyValue) {
		this.tdHshcCpGdCnKyValue = tdHshcCpGdCnKyValue;
	}

	public Double getTdHshcCpGdCnKyTyLe() {
		return tdHshcCpGdCnKyTyLe;
	}

	public void setTdHshcCpGdCnKyTyLe(Double tdHshcCpGdCnKyTyLe) {
		this.tdHshcCpGdCnKyTyLe = tdHshcCpGdCnKyTyLe;
	}

	public Long getTdHshcCpDoiSoat4aValue() {
		return tdHshcCpDoiSoat4aValue;
	}

	public void setTdHshcCpDoiSoat4aValue(Long tdHshcCpDoiSoat4aValue) {
		this.tdHshcCpDoiSoat4aValue = tdHshcCpDoiSoat4aValue;
	}

	public Double getTdHshcCpDoiSoat4aTyLe() {
		return tdHshcCpDoiSoat4aTyLe;
	}

	public void setTdHshcCpDoiSoat4aTyLe(Double tdHshcCpDoiSoat4aTyLe) {
		this.tdHshcCpDoiSoat4aTyLe = tdHshcCpDoiSoat4aTyLe;
	}

	public Long getTdHshcCpPhtThamDuyetValue() {
		return tdHshcCpPhtThamDuyetValue;
	}

	public void setTdHshcCpPhtThamDuyetValue(Long tdHshcCpPhtThamDuyetValue) {
		this.tdHshcCpPhtThamDuyetValue = tdHshcCpPhtThamDuyetValue;
	}

	public Double getTdHshcCpPhtThamDuyetTyLe() {
		return tdHshcCpPhtThamDuyetTyLe;
	}

	public void setTdHshcCpPhtThamDuyetTyLe(Double tdHshcCpPhtThamDuyetTyLe) {
		this.tdHshcCpPhtThamDuyetTyLe = tdHshcCpPhtThamDuyetTyLe;
	}

	public Long getTdHshcCpPhtNghiemThuValue() {
		return tdHshcCpPhtNghiemThuValue;
	}

	public void setTdHshcCpPhtNghiemThuValue(Long tdHshcCpPhtNghiemThuValue) {
		this.tdHshcCpPhtNghiemThuValue = tdHshcCpPhtNghiemThuValue;
	}

	public Double getTdHshcCpPhtNghiemThuTyLe() {
		return tdHshcCpPhtNghiemThuTyLe;
	}

	public void setTdHshcCpPhtNghiemThuTyLe(Double tdHshcCpPhtNghiemThuTyLe) {
		this.tdHshcCpPhtNghiemThuTyLe = tdHshcCpPhtNghiemThuTyLe;
	}

	public Long getTdHshcCpDangLamHoSoValue() {
		return tdHshcCpDangLamHoSoValue;
	}

	public void setTdHshcCpDangLamHoSoValue(Long tdHshcCpDangLamHoSoValue) {
		this.tdHshcCpDangLamHoSoValue = tdHshcCpDangLamHoSoValue;
	}

	public Double getTdHshcCpDangLamHoSoTyLe() {
		return tdHshcCpDangLamHoSoTyLe;
	}

	public void setTdHshcCpDangLamHoSoTyLe(Double tdHshcCpDangLamHoSoTyLe) {
		this.tdHshcCpDangLamHoSoTyLe = tdHshcCpDangLamHoSoTyLe;
	}

	public Long getTdHshcCpDuKienHoanThanhValue() {
		return tdHshcCpDuKienHoanThanhValue;
	}

	public void setTdHshcCpDuKienHoanThanhValue(Long tdHshcCpDuKienHoanThanhValue) {
		this.tdHshcCpDuKienHoanThanhValue = tdHshcCpDuKienHoanThanhValue;
	}

	public Double getTdHshcCpDuKienHoanThanhTyLe() {
		return tdHshcCpDuKienHoanThanhTyLe;
	}

	public void setTdHshcCpDuKienHoanThanhTyLe(Double tdHshcCpDuKienHoanThanhTyLe) {
		this.tdHshcCpDuKienHoanThanhTyLe = tdHshcCpDuKienHoanThanhTyLe;
	}

	public String getTdHshcCpNguyenNhanKoHt() {
		return tdHshcCpNguyenNhanKoHt;
	}

	public void setTdHshcCpNguyenNhanKoHt(String tdHshcCpNguyenNhanKoHt) {
		this.tdHshcCpNguyenNhanKoHt = tdHshcCpNguyenNhanKoHt;
	}

	public Long getTdHshcXlKhThang() {
		return tdHshcXlKhThang;
	}

	public void setTdHshcXlKhThang(Long tdHshcXlKhThang) {
		this.tdHshcXlKhThang = tdHshcXlKhThang;
	}

	public Long getTdHshcXlTong() {
		return tdHshcXlTong;
	}

	public void setTdHshcXlTong(Long tdHshcXlTong) {
		this.tdHshcXlTong = tdHshcXlTong;
	}

	public Long getTdHshcXlTthtDaDuyet() {
		return tdHshcXlTthtDaDuyet;
	}

	public void setTdHshcXlTthtDaDuyet(Long tdHshcXlTthtDaDuyet) {
		this.tdHshcXlTthtDaDuyet = tdHshcXlTthtDaDuyet;
	}

	public Long getTdHshcXlGdCnKyValue() {
		return tdHshcXlGdCnKyValue;
	}

	public void setTdHshcXlGdCnKyValue(Long tdHshcXlGdCnKyValue) {
		this.tdHshcXlGdCnKyValue = tdHshcXlGdCnKyValue;
	}

	public Double getTdHshcXlGdCnKyTyLe() {
		return tdHshcXlGdCnKyTyLe;
	}

	public void setTdHshcXlGdCnKyTyLe(Double tdHshcXlGdCnKyTyLe) {
		this.tdHshcXlGdCnKyTyLe = tdHshcXlGdCnKyTyLe;
	}

	public Long getTdHshcXlDoiSoat4aValue() {
		return tdHshcXlDoiSoat4aValue;
	}

	public void setTdHshcXlDoiSoat4aValue(Long tdHshcXlDoiSoat4aValue) {
		this.tdHshcXlDoiSoat4aValue = tdHshcXlDoiSoat4aValue;
	}

	public Double getTdHshcXlDoiSoat4aTyLe() {
		return tdHshcXlDoiSoat4aTyLe;
	}

	public void setTdHshcXlDoiSoat4aTyLe(Double tdHshcXlDoiSoat4aTyLe) {
		this.tdHshcXlDoiSoat4aTyLe = tdHshcXlDoiSoat4aTyLe;
	}

	public Long getTdHshcXlPhtThamDuyetValue() {
		return tdHshcXlPhtThamDuyetValue;
	}

	public void setTdHshcXlPhtThamDuyetValue(Long tdHshcXlPhtThamDuyetValue) {
		this.tdHshcXlPhtThamDuyetValue = tdHshcXlPhtThamDuyetValue;
	}

	public Double getTdHshcXlPhtThamDuyetTyLe() {
		return tdHshcXlPhtThamDuyetTyLe;
	}

	public void setTdHshcXlPhtThamDuyetTyLe(Double tdHshcXlPhtThamDuyetTyLe) {
		this.tdHshcXlPhtThamDuyetTyLe = tdHshcXlPhtThamDuyetTyLe;
	}

	public Long getTdHshcXlPhtNghiemThuValue() {
		return tdHshcXlPhtNghiemThuValue;
	}

	public void setTdHshcXlPhtNghiemThuValue(Long tdHshcXlPhtNghiemThuValue) {
		this.tdHshcXlPhtNghiemThuValue = tdHshcXlPhtNghiemThuValue;
	}

	public Double getTdHshcXlPhtNghiemThuTyLe() {
		return tdHshcXlPhtNghiemThuTyLe;
	}

	public void setTdHshcXlPhtNghiemThuTyLe(Double tdHshcXlPhtNghiemThuTyLe) {
		this.tdHshcXlPhtNghiemThuTyLe = tdHshcXlPhtNghiemThuTyLe;
	}

	public Long getTdHshcXlDangLamHoSoValue() {
		return tdHshcXlDangLamHoSoValue;
	}

	public void setTdHshcXlDangLamHoSoValue(Long tdHshcXlDangLamHoSoValue) {
		this.tdHshcXlDangLamHoSoValue = tdHshcXlDangLamHoSoValue;
	}

	public Double getTdHshcXlDangLamHoSoTyLe() {
		return tdHshcXlDangLamHoSoTyLe;
	}

	public void setTdHshcXlDangLamHoSoTyLe(Double tdHshcXlDangLamHoSoTyLe) {
		this.tdHshcXlDangLamHoSoTyLe = tdHshcXlDangLamHoSoTyLe;
	}

	public Long getTdHshcXlDuKienHoanThanhValue() {
		return tdHshcXlDuKienHoanThanhValue;
	}

	public void setTdHshcXlDuKienHoanThanhValue(Long tdHshcXlDuKienHoanThanhValue) {
		this.tdHshcXlDuKienHoanThanhValue = tdHshcXlDuKienHoanThanhValue;
	}

	public Double getTdHshcXlDuKienHoanThanhTyLe() {
		return tdHshcXlDuKienHoanThanhTyLe;
	}

	public void setTdHshcXlDuKienHoanThanhTyLe(Double tdHshcXlDuKienHoanThanhTyLe) {
		this.tdHshcXlDuKienHoanThanhTyLe = tdHshcXlDuKienHoanThanhTyLe;
	}

	public String getTdHshcXlNguyenNhanKoHt() {
		return tdHshcXlNguyenNhanKoHt;
	}

	public void setTdHshcXlNguyenNhanKoHt(String tdHshcXlNguyenNhanKoHt) {
		this.tdHshcXlNguyenNhanKoHt = tdHshcXlNguyenNhanKoHt;
	}

	public Long getTdHshcNtdKhThang() {
		return tdHshcNtdKhThang;
	}

	public void setTdHshcNtdKhThang(Long tdHshcNtdKhThang) {
		this.tdHshcNtdKhThang = tdHshcNtdKhThang;
	}

	public Long getTdHshcNtdTong() {
		return tdHshcNtdTong;
	}

	public void setTdHshcNtdTong(Long tdHshcNtdTong) {
		this.tdHshcNtdTong = tdHshcNtdTong;
	}

	public Long getTdHshcNtdPtkDaDuyet() {
		return tdHshcNtdPtkDaDuyet;
	}

	public void setTdHshcNtdPtkDaDuyet(Long tdHshcNtdPtkDaDuyet) {
		this.tdHshcNtdPtkDaDuyet = tdHshcNtdPtkDaDuyet;
	}

	public Long getTdHshcNtdGdCnKyValue() {
		return tdHshcNtdGdCnKyValue;
	}

	public void setTdHshcNtdGdCnKyValue(Long tdHshcNtdGdCnKyValue) {
		this.tdHshcNtdGdCnKyValue = tdHshcNtdGdCnKyValue;
	}

	public Double getTdHshcNtdGdCnKyTyLe() {
		return tdHshcNtdGdCnKyTyLe;
	}

	public void setTdHshcNtdGdCnKyTyLe(Double tdHshcNtdGdCnKyTyLe) {
		this.tdHshcNtdGdCnKyTyLe = tdHshcNtdGdCnKyTyLe;
	}

	public Long getTdHshcNtdDoiSoat4aValue() {
		return tdHshcNtdDoiSoat4aValue;
	}

	public void setTdHshcNtdDoiSoat4aValue(Long tdHshcNtdDoiSoat4aValue) {
		this.tdHshcNtdDoiSoat4aValue = tdHshcNtdDoiSoat4aValue;
	}

	public Double getTdHshcNtdDoiSoat4aTyLe() {
		return tdHshcNtdDoiSoat4aTyLe;
	}

	public void setTdHshcNtdDoiSoat4aTyLe(Double tdHshcNtdDoiSoat4aTyLe) {
		this.tdHshcNtdDoiSoat4aTyLe = tdHshcNtdDoiSoat4aTyLe;
	}

	public Long getTdHshcNtdPhtThamDuyetValue() {
		return tdHshcNtdPhtThamDuyetValue;
	}

	public void setTdHshcNtdPhtThamDuyetValue(Long tdHshcNtdPhtThamDuyetValue) {
		this.tdHshcNtdPhtThamDuyetValue = tdHshcNtdPhtThamDuyetValue;
	}

	public Double getTdHshcNtdPhtThamDuyetTyLe() {
		return tdHshcNtdPhtThamDuyetTyLe;
	}

	public void setTdHshcNtdPhtThamDuyetTyLe(Double tdHshcNtdPhtThamDuyetTyLe) {
		this.tdHshcNtdPhtThamDuyetTyLe = tdHshcNtdPhtThamDuyetTyLe;
	}

	public Long getTdHshcNtdPhtNghiemThuValue() {
		return tdHshcNtdPhtNghiemThuValue;
	}

	public void setTdHshcNtdPhtNghiemThuValue(Long tdHshcNtdPhtNghiemThuValue) {
		this.tdHshcNtdPhtNghiemThuValue = tdHshcNtdPhtNghiemThuValue;
	}

	public Double getTdHshcNtdPhtNghiemThuTyLe() {
		return tdHshcNtdPhtNghiemThuTyLe;
	}

	public void setTdHshcNtdPhtNghiemThuTyLe(Double tdHshcNtdPhtNghiemThuTyLe) {
		this.tdHshcNtdPhtNghiemThuTyLe = tdHshcNtdPhtNghiemThuTyLe;
	}

	public Long getTdHshcNtdDangLamHoSoValue() {
		return tdHshcNtdDangLamHoSoValue;
	}

	public void setTdHshcNtdDangLamHoSoValue(Long tdHshcNtdDangLamHoSoValue) {
		this.tdHshcNtdDangLamHoSoValue = tdHshcNtdDangLamHoSoValue;
	}

	public Double getTdHshcNtdDangLamHoSoTyLe() {
		return tdHshcNtdDangLamHoSoTyLe;
	}

	public void setTdHshcNtdDangLamHoSoTyLe(Double tdHshcNtdDangLamHoSoTyLe) {
		this.tdHshcNtdDangLamHoSoTyLe = tdHshcNtdDangLamHoSoTyLe;
	}

	public Long getTdHshcNtdDuKienHoanThanhValue() {
		return tdHshcNtdDuKienHoanThanhValue;
	}

	public void setTdHshcNtdDuKienHoanThanhValue(Long tdHshcNtdDuKienHoanThanhValue) {
		this.tdHshcNtdDuKienHoanThanhValue = tdHshcNtdDuKienHoanThanhValue;
	}

	public Double getTdHshcNtdDuKienHoanThanhTyLe() {
		return tdHshcNtdDuKienHoanThanhTyLe;
	}

	public void setTdHshcNtdDuKienHoanThanhTyLe(Double tdHshcNtdDuKienHoanThanhTyLe) {
		this.tdHshcNtdDuKienHoanThanhTyLe = tdHshcNtdDuKienHoanThanhTyLe;
	}

	public String getTdHshcNtdNguyenNhanKoHt() {
		return tdHshcNtdNguyenNhanKoHt;
	}

	public void setTdHshcNtdNguyenNhanKoHt(String tdHshcNtdNguyenNhanKoHt) {
		this.tdHshcNtdNguyenNhanKoHt = tdHshcNtdNguyenNhanKoHt;
	}

	public Long getTdSlCpKhThang() {
		return tdSlCpKhThang;
	}

	public void setTdSlCpKhThang(Long tdSlCpKhThang) {
		this.tdSlCpKhThang = tdSlCpKhThang;
	}

	public Long getTdSlCpDaHoanThanh() {
		return tdSlCpDaHoanThanh;
	}

	public void setTdSlCpDaHoanThanh(Long tdSlCpDaHoanThanh) {
		this.tdSlCpDaHoanThanh = tdSlCpDaHoanThanh;
	}

	public Long getTdSlCpDangThiCongValue() {
		return tdSlCpDangThiCongValue;
	}

	public void setTdSlCpDangThiCongValue(Long tdSlCpDangThiCongValue) {
		this.tdSlCpDangThiCongValue = tdSlCpDangThiCongValue;
	}

	public Double getTdSlCpDangThiCongTyLe() {
		return tdSlCpDangThiCongTyLe;
	}

	public void setTdSlCpDangThiCongTyLe(Double tdSlCpDangThiCongTyLe) {
		this.tdSlCpDangThiCongTyLe = tdSlCpDangThiCongTyLe;
	}

	public Long getTdSlCpDuKienHoanThanhValue() {
		return tdSlCpDuKienHoanThanhValue;
	}

	public void setTdSlCpDuKienHoanThanhValue(Long tdSlCpDuKienHoanThanhValue) {
		this.tdSlCpDuKienHoanThanhValue = tdSlCpDuKienHoanThanhValue;
	}

	public Double getTdSlCpDuKienHoanThanhTyLe() {
		return tdSlCpDuKienHoanThanhTyLe;
	}

	public void setTdSlCpDuKienHoanThanhTyLe(Double tdSlCpDuKienHoanThanhTyLe) {
		this.tdSlCpDuKienHoanThanhTyLe = tdSlCpDuKienHoanThanhTyLe;
	}

	public String getTdSlCpNguyenNhanKoHt() {
		return tdSlCpNguyenNhanKoHt;
	}

	public void setTdSlCpNguyenNhanKoHt(String tdSlCpNguyenNhanKoHt) {
		this.tdSlCpNguyenNhanKoHt = tdSlCpNguyenNhanKoHt;
	}

	public Long getTdSlXlKhThang() {
		return tdSlXlKhThang;
	}

	public void setTdSlXlKhThang(Long tdSlXlKhThang) {
		this.tdSlXlKhThang = tdSlXlKhThang;
	}

	public Long getTdSlXlDaHoanThanh() {
		return tdSlXlDaHoanThanh;
	}

	public void setTdSlXlDaHoanThanh(Long tdSlXlDaHoanThanh) {
		this.tdSlXlDaHoanThanh = tdSlXlDaHoanThanh;
	}

	public Long getTdSlXlDangThiCongValue() {
		return tdSlXlDangThiCongValue;
	}

	public void setTdSlXlDangThiCongValue(Long tdSlXlDangThiCongValue) {
		this.tdSlXlDangThiCongValue = tdSlXlDangThiCongValue;
	}

	public Double getTdSlXlDangThiCongTyLe() {
		return tdSlXlDangThiCongTyLe;
	}

	public void setTdSlXlDangThiCongTyLe(Double tdSlXlDangThiCongTyLe) {
		this.tdSlXlDangThiCongTyLe = tdSlXlDangThiCongTyLe;
	}

	public Long getTdSlXlDuKienHoanThanhValue() {
		return tdSlXlDuKienHoanThanhValue;
	}

	public void setTdSlXlDuKienHoanThanhValue(Long tdSlXlDuKienHoanThanhValue) {
		this.tdSlXlDuKienHoanThanhValue = tdSlXlDuKienHoanThanhValue;
	}

	public Double getTdSlXlDuKienHoanThanhTyLe() {
		return tdSlXlDuKienHoanThanhTyLe;
	}

	public void setTdSlXlDuKienHoanThanhTyLe(Double tdSlXlDuKienHoanThanhTyLe) {
		this.tdSlXlDuKienHoanThanhTyLe = tdSlXlDuKienHoanThanhTyLe;
	}

	public String getTdSlXlNguyenNhanKoHt() {
		return tdSlXlNguyenNhanKoHt;
	}

	public void setTdSlXlNguyenNhanKoHt(String tdSlXlNguyenNhanKoHt) {
		this.tdSlXlNguyenNhanKoHt = tdSlXlNguyenNhanKoHt;
	}

	public Long getTdSlNtdKhThang() {
		return tdSlNtdKhThang;
	}

	public void setTdSlNtdKhThang(Long tdSlNtdKhThang) {
		this.tdSlNtdKhThang = tdSlNtdKhThang;
	}

	public Long getTdSlNtdDaHoanThanh() {
		return tdSlNtdDaHoanThanh;
	}

	public void setTdSlNtdDaHoanThanh(Long tdSlNtdDaHoanThanh) {
		this.tdSlNtdDaHoanThanh = tdSlNtdDaHoanThanh;
	}

	public Long getTdSlNtdDangThiCongValue() {
		return tdSlNtdDangThiCongValue;
	}

	public void setTdSlNtdDangThiCongValue(Long tdSlNtdDangThiCongValue) {
		this.tdSlNtdDangThiCongValue = tdSlNtdDangThiCongValue;
	}

	public Double getTdSlNtdDangThiCongTyLe() {
		return tdSlNtdDangThiCongTyLe;
	}

	public void setTdSlNtdDangThiCongTyLe(Double tdSlNtdDangThiCongTyLe) {
		this.tdSlNtdDangThiCongTyLe = tdSlNtdDangThiCongTyLe;
	}

	public Long getTdSlNtdDuKienHoanThanhValue() {
		return tdSlNtdDuKienHoanThanhValue;
	}

	public void setTdSlNtdDuKienHoanThanhValue(Long tdSlNtdDuKienHoanThanhValue) {
		this.tdSlNtdDuKienHoanThanhValue = tdSlNtdDuKienHoanThanhValue;
	}

	public Double getTdSlNtdDuKienHoanThanhTyLe() {
		return tdSlNtdDuKienHoanThanhTyLe;
	}

	public void setTdSlNtdDuKienHoanThanhTyLe(Double tdSlNtdDuKienHoanThanhTyLe) {
		this.tdSlNtdDuKienHoanThanhTyLe = tdSlNtdDuKienHoanThanhTyLe;
	}

	public String getTdSlNtdNguyenNhanKoHt() {
		return tdSlNtdNguyenNhanKoHt;
	}

	public void setTdSlNtdNguyenNhanKoHt(String tdSlNtdNguyenNhanKoHt) {
		this.tdSlNtdNguyenNhanKoHt = tdSlNtdNguyenNhanKoHt;
	}

	public Long getTdThdtKhThang() {
		return tdThdtKhThang;
	}

	public void setTdThdtKhThang(Long tdThdtKhThang) {
		this.tdThdtKhThang = tdThdtKhThang;
	}

	public Long getTdThdtDaHoanThanh() {
		return tdThdtDaHoanThanh;
	}

	public void setTdThdtDaHoanThanh(Long tdThdtDaHoanThanh) {
		this.tdThdtDaHoanThanh = tdThdtDaHoanThanh;
	}

	public Long getTdThdtPhtDangKiemTraValue() {
		return tdThdtPhtDangKiemTraValue;
	}

	public void setTdThdtPhtDangKiemTraValue(Long tdThdtPhtDangKiemTraValue) {
		this.tdThdtPhtDangKiemTraValue = tdThdtPhtDangKiemTraValue;
	}

	public Double getTdThdtPhtDangKiemTraTyLe() {
		return tdThdtPhtDangKiemTraTyLe;
	}

	public void setTdThdtPhtDangKiemTraTyLe(Double tdThdtPhtDangKiemTraTyLe) {
		this.tdThdtPhtDangKiemTraTyLe = tdThdtPhtDangKiemTraTyLe;
	}

	public Long getTdThdtPtcDangKiemTraValue() {
		return tdThdtPtcDangKiemTraValue;
	}

	public void setTdThdtPtcDangKiemTraValue(Long tdThdtPtcDangKiemTraValue) {
		this.tdThdtPtcDangKiemTraValue = tdThdtPtcDangKiemTraValue;
	}

	public Double getTdThdtPtcDangKiemTraTyLe() {
		return tdThdtPtcDangKiemTraTyLe;
	}

	public void setTdThdtPtcDangKiemTraTyLe(Double tdThdtPtcDangKiemTraTyLe) {
		this.tdThdtPtcDangKiemTraTyLe = tdThdtPtcDangKiemTraTyLe;
	}

	public Long getTdThdtDuKienHoanThanhValue() {
		return tdThdtDuKienHoanThanhValue;
	}

	public void setTdThdtDuKienHoanThanhValue(Long tdThdtDuKienHoanThanhValue) {
		this.tdThdtDuKienHoanThanhValue = tdThdtDuKienHoanThanhValue;
	}

	public Double getTdThdtDuKienHoanThanhTyLe() {
		return tdThdtDuKienHoanThanhTyLe;
	}

	public void setTdThdtDuKienHoanThanhTyLe(Double tdThdtDuKienHoanThanhTyLe) {
		this.tdThdtDuKienHoanThanhTyLe = tdThdtDuKienHoanThanhTyLe;
	}

	public String getTdThdtNguyenNhanKoHt() {
		return tdThdtNguyenNhanKoHt;
	}

	public void setTdThdtNguyenNhanKoHt(String tdThdtNguyenNhanKoHt) {
		this.tdThdtNguyenNhanKoHt = tdThdtNguyenNhanKoHt;
	}

	private Long giaTriHSHCHTCT;

	private Long giaTriSLXLThNgay;
	private Long giaTriSLCPThNgay;
	private Long giaTriSLNTDThNgay;
	private Long giaTriQuyLuongThNgay;
	private Long giaTriHSHCXLThNgay;
	private Long giaTriHSHCNTDThNgay;
	private Long giaTriHSHCCPThNgay;
	private Long giaTriHSHCHTCTThNgay;
	private Long giaTriTHDTThNgay;

	private Long giaTriSLXLLuyKe;
	private Long giaTriSLCPLuyKe;
	private Long giaTriSLNTDLuyKe;
	private Long giaTriQuyLuongLuyKe;
	private Long giaTriHSHCXLLuyKe;
	private Long giaTriHSHCNTDLuyKe;
	private Long giaTriHSHCCPLuyKe;
	private Long giaTriHSHCHTCTLuyKe;
	private Long giaTriTHDTLuyKe;

	private Long tdHshcXlXepThu;
	private Long tdHshcNtdXepThu;
	private Long tdHshcCpXepThu;
	private Long tdHshcHtctXepThu;
	private Long tdThdtXepThu;
	private Long tdHshcXdddXepThu;
	private Long tdSlHtctXepThu;
	
	public Long getTdSlHtctXepThu() {
		return tdSlHtctXepThu;
	}

	public void setTdSlHtctXepThu(Long tdSlHtctXepThu) {
		this.tdSlHtctXepThu = tdSlHtctXepThu;
	}

	public Long getTdHshcXdddXepThu() {
		return tdHshcXdddXepThu;
	}

	public void setTdHshcXdddXepThu(Long tdHshcXdddXepThu) {
		this.tdHshcXdddXepThu = tdHshcXdddXepThu;
	}

	public Long getTdThdtXepThu() {
		return tdThdtXepThu;
	}

	public void setTdThdtXepThu(Long tdThdtXepThu) {
		this.tdThdtXepThu = tdThdtXepThu;
	}

	public Long getTdHshcHtctXepThu() {
		return tdHshcHtctXepThu;
	}

	public void setTdHshcHtctXepThu(Long tdHshcHtctXepThu) {
		this.tdHshcHtctXepThu = tdHshcHtctXepThu;
	}

	public Long getTdHshcCpXepThu() {
		return tdHshcCpXepThu;
	}

	public void setTdHshcCpXepThu(Long tdHshcCpXepThu) {
		this.tdHshcCpXepThu = tdHshcCpXepThu;
	}

	public Long getTdHshcNtdXepThu() {
		return tdHshcNtdXepThu;
	}

	public void setTdHshcNtdXepThu(Long tdHshcNtdXepThu) {
		this.tdHshcNtdXepThu = tdHshcNtdXepThu;
	}

	public Long getTdHshcXlXepThu() {
		return tdHshcXlXepThu;
	}

	public void setTdHshcXlXepThu(Long tdHshcXlXepThu) {
		this.tdHshcXlXepThu = tdHshcXlXepThu;
	}

	public Long getGiaTriHSHCHTCT() {
		return giaTriHSHCHTCT;
	}

	public void setGiaTriHSHCHTCT(Long giaTriHSHCHTCT) {
		this.giaTriHSHCHTCT = giaTriHSHCHTCT;
	}

	public Long getGiaTriSLXLThNgay() {
		return giaTriSLXLThNgay;
	}

	public void setGiaTriSLXLThNgay(Long giaTriSLXLThNgay) {
		this.giaTriSLXLThNgay = giaTriSLXLThNgay;
	}

	public Long getGiaTriSLCPThNgay() {
		return giaTriSLCPThNgay;
	}

	public void setGiaTriSLCPThNgay(Long giaTriSLCPThNgay) {
		this.giaTriSLCPThNgay = giaTriSLCPThNgay;
	}

	public Long getGiaTriSLNTDThNgay() {
		return giaTriSLNTDThNgay;
	}

	public void setGiaTriSLNTDThNgay(Long giaTriSLNTDThNgay) {
		this.giaTriSLNTDThNgay = giaTriSLNTDThNgay;
	}

	public Long getGiaTriQuyLuongThNgay() {
		return giaTriQuyLuongThNgay;
	}

	public void setGiaTriQuyLuongThNgay(Long giaTriQuyLuongThNgay) {
		this.giaTriQuyLuongThNgay = giaTriQuyLuongThNgay;
	}

	public Long getGiaTriHSHCXLThNgay() {
		return giaTriHSHCXLThNgay;
	}

	public void setGiaTriHSHCXLThNgay(Long giaTriHSHCXLThNgay) {
		this.giaTriHSHCXLThNgay = giaTriHSHCXLThNgay;
	}

	public Long getGiaTriHSHCNTDThNgay() {
		return giaTriHSHCNTDThNgay;
	}

	public void setGiaTriHSHCNTDThNgay(Long giaTriHSHCNTDThNgay) {
		this.giaTriHSHCNTDThNgay = giaTriHSHCNTDThNgay;
	}

	public Long getGiaTriHSHCCPThNgay() {
		return giaTriHSHCCPThNgay;
	}

	public void setGiaTriHSHCCPThNgay(Long giaTriHSHCCPThNgay) {
		this.giaTriHSHCCPThNgay = giaTriHSHCCPThNgay;
	}

	public Long getGiaTriHSHCHTCTThNgay() {
		return giaTriHSHCHTCTThNgay;
	}

	public void setGiaTriHSHCHTCTThNgay(Long giaTriHSHCHTCTThNgay) {
		this.giaTriHSHCHTCTThNgay = giaTriHSHCHTCTThNgay;
	}

	public Long getGiaTriTHDTThNgay() {
		return giaTriTHDTThNgay;
	}

	public void setGiaTriTHDTThNgay(Long giaTriTHDTThNgay) {
		this.giaTriTHDTThNgay = giaTriTHDTThNgay;
	}

	public Long getGiaTriSLXLLuyKe() {
		return giaTriSLXLLuyKe;
	}

	public void setGiaTriSLXLLuyKe(Long giaTriSLXLLuyKe) {
		this.giaTriSLXLLuyKe = giaTriSLXLLuyKe;
	}

	public Long getGiaTriSLCPLuyKe() {
		return giaTriSLCPLuyKe;
	}

	public void setGiaTriSLCPLuyKe(Long giaTriSLCPLuyKe) {
		this.giaTriSLCPLuyKe = giaTriSLCPLuyKe;
	}

	public Long getGiaTriSLNTDLuyKe() {
		return giaTriSLNTDLuyKe;
	}

	public void setGiaTriSLNTDLuyKe(Long giaTriSLNTDLuyKe) {
		this.giaTriSLNTDLuyKe = giaTriSLNTDLuyKe;
	}

	public Long getGiaTriQuyLuongLuyKe() {
		return giaTriQuyLuongLuyKe;
	}

	public void setGiaTriQuyLuongLuyKe(Long giaTriQuyLuongLuyKe) {
		this.giaTriQuyLuongLuyKe = giaTriQuyLuongLuyKe;
	}

	public Long getGiaTriHSHCXLLuyKe() {
		return giaTriHSHCXLLuyKe;
	}

	public void setGiaTriHSHCXLLuyKe(Long giaTriHSHCXLLuyKe) {
		this.giaTriHSHCXLLuyKe = giaTriHSHCXLLuyKe;
	}

	public Long getGiaTriHSHCNTDLuyKe() {
		return giaTriHSHCNTDLuyKe;
	}

	public void setGiaTriHSHCNTDLuyKe(Long giaTriHSHCNTDLuyKe) {
		this.giaTriHSHCNTDLuyKe = giaTriHSHCNTDLuyKe;
	}

	public Long getGiaTriHSHCCPLuyKe() {
		return giaTriHSHCCPLuyKe;
	}

	public void setGiaTriHSHCCPLuyKe(Long giaTriHSHCCPLuyKe) {
		this.giaTriHSHCCPLuyKe = giaTriHSHCCPLuyKe;
	}

	public Long getGiaTriHSHCHTCTLuyKe() {
		return giaTriHSHCHTCTLuyKe;
	}

	public void setGiaTriHSHCHTCTLuyKe(Long giaTriHSHCHTCTLuyKe) {
		this.giaTriHSHCHTCTLuyKe = giaTriHSHCHTCTLuyKe;
	}

	public Long getGiaTriTHDTLuyKe() {
		return giaTriTHDTLuyKe;
	}

	public void setGiaTriTHDTLuyKe(Long giaTriTHDTLuyKe) {
		this.giaTriTHDTLuyKe = giaTriTHDTLuyKe;
	}

	public Long getTdHshcHtctTrenDuongValue() {
		return tdHshcHtctTrenDuongValue;
	}

	public void setTdHshcHtctTrenDuongValue(Long tdHshcHtctTrenDuongValue) {
		this.tdHshcHtctTrenDuongValue = tdHshcHtctTrenDuongValue;
	}

	public Double getTdHshcHtctTrenDuongTyLe() {
		return tdHshcHtctTrenDuongTyLe;
	}

	public void setTdHshcHtctTrenDuongTyLe(Double tdHshcHtctTrenDuongTyLe) {
		this.tdHshcHtctTrenDuongTyLe = tdHshcHtctTrenDuongTyLe;
	}

	public Long getTdQlTrenDuongValue() {
		return tdQlTrenDuongValue;
	}

	public void setTdQlTrenDuongValue(Long tdQlTrenDuongValue) {
		this.tdQlTrenDuongValue = tdQlTrenDuongValue;
	}

	public Double getTdQlTrenDuongTyle() {
		return tdQlTrenDuongTyle;
	}

	public void setTdQlTrenDuongTyle(Double tdQlTrenDuongTyle) {
		this.tdQlTrenDuongTyle = tdQlTrenDuongTyle;
	}

	public Long getTdHshcCpTrenDuongValue() {
		return tdHshcCpTrenDuongValue;
	}

	public void setTdHshcCpTrenDuongValue(Long tdHshcCpTrenDuongValue) {
		this.tdHshcCpTrenDuongValue = tdHshcCpTrenDuongValue;
	}

	public Double getTdHshcCpTrenDuongTyLe() {
		return tdHshcCpTrenDuongTyLe;
	}

	public void setTdHshcCpTrenDuongTyLe(Double tdHshcCpTrenDuongTyLe) {
		this.tdHshcCpTrenDuongTyLe = tdHshcCpTrenDuongTyLe;
	}

	public Long getTdHshcXlTrenDuongValue() {
		return tdHshcXlTrenDuongValue;
	}

	public void setTdHshcXlTrenDuongValue(Long tdHshcXlTrenDuongValue) {
		this.tdHshcXlTrenDuongValue = tdHshcXlTrenDuongValue;
	}

	public Double getTdHshcXlTrenDuongTyLe() {
		return tdHshcXlTrenDuongTyLe;
	}

	public void setTdHshcXlTrenDuongTyLe(Double tdHshcXlTrenDuongTyLe) {
		this.tdHshcXlTrenDuongTyLe = tdHshcXlTrenDuongTyLe;
	}

	public Long getTdHshcNtdTrenDuongValue() {
		return tdHshcNtdTrenDuongValue;
	}

	public void setTdHshcNtdTrenDuongValue(Long tdHshcNtdTrenDuongValue) {
		this.tdHshcNtdTrenDuongValue = tdHshcNtdTrenDuongValue;
	}

	public Double getTdHshcNtdTrenDuongTyLe() {
		return tdHshcNtdTrenDuongTyLe;
	}

	public void setTdHshcNtdTrenDuongTyLe(Double tdHshcNtdTrenDuongTyLe) {
		this.tdHshcNtdTrenDuongTyLe = tdHshcNtdTrenDuongTyLe;
	}

	public Long getTdSlXdddKhThang() {
		return tdSlXdddKhThang;
	}

	public void setTdSlXdddKhThang(Long tdSlXdddKhThang) {
		this.tdSlXdddKhThang = tdSlXdddKhThang;
	}

	public Long getTdSlXdddDaHoanThanh() {
		return tdSlXdddDaHoanThanh;
	}

	public void setTdSlXdddDaHoanThanh(Long tdSlXdddDaHoanThanh) {
		this.tdSlXdddDaHoanThanh = tdSlXdddDaHoanThanh;
	}

	public Long getTdSlXdddDangThiCongValue() {
		return tdSlXdddDangThiCongValue;
	}

	public void setTdSlXdddDangThiCongValue(Long tdSlXdddDangThiCongValue) {
		this.tdSlXdddDangThiCongValue = tdSlXdddDangThiCongValue;
	}

	public Double getTdSlXdddDangThiCongTyLe() {
		return tdSlXdddDangThiCongTyLe;
	}

	public void setTdSlXdddDangThiCongTyLe(Double tdSlXdddDangThiCongTyLe) {
		this.tdSlXdddDangThiCongTyLe = tdSlXdddDangThiCongTyLe;
	}

	public Long getTdSlXdddDuKienHoanThanhValue() {
		return tdSlXdddDuKienHoanThanhValue;
	}

	public void setTdSlXdddDuKienHoanThanhValue(Long tdSlXdddDuKienHoanThanhValue) {
		this.tdSlXdddDuKienHoanThanhValue = tdSlXdddDuKienHoanThanhValue;
	}

	public Double getTdSlXdddDuKienHoanThanhTyLe() {
		return tdSlXdddDuKienHoanThanhTyLe;
	}

	public void setTdSlXdddDuKienHoanThanhTyLe(Double tdSlXdddDuKienHoanThanhTyLe) {
		this.tdSlXdddDuKienHoanThanhTyLe = tdSlXdddDuKienHoanThanhTyLe;
	}

	public String getTdSlXdddNguyenNhanKoHt() {
		return tdSlXdddNguyenNhanKoHt;
	}

	public void setTdSlXdddNguyenNhanKoHt(String tdSlXdddNguyenNhanKoHt) {
		this.tdSlXdddNguyenNhanKoHt = tdSlXdddNguyenNhanKoHt;
	}

	private Long tdHshcXdddKhThang;
	private Long tdHshcXdddTrenDuongValue;
	private Double tdHshcXdddTrenDuongTyLe;
	private Long tdHshcXdddGdCnKyValue;
	private Double tdHshcXdddGdCnKyTyLe;
	private Long tdHshcXdddDoiSoat4aValue;
	private Double tdHshcXdddDoiSoat4aTyLe;
	private Long tdHshcXdddPhtThamDuyetValue;
	private Double tdHshcXdddPhtThamDuyetTyLe;
	private Long tdHshcXdddPhtNghiemThuValue;
	private Double tdHshcXdddPhtNghiemThuTyLe;
	private Long tdHshcXdddDangLamHoSoValue;
	private Double tdHshcXdddDangLamHoSoTyLe;
	private Long tdHshcXdddDuKienHoanThanhValue;
	private Double tdHshcXdddDuKienHoanThanhTyLe;
	private String tdHshcXdddNguyenNhanKoHt;
	
	public Long getTdHshcXdddKhThang() {
		return tdHshcXdddKhThang;
	}

	public void setTdHshcXdddKhThang(Long tdHshcXdddKhThang) {
		this.tdHshcXdddKhThang = tdHshcXdddKhThang;
	}

	public Long getTdHshcXdddTrenDuongValue() {
		return tdHshcXdddTrenDuongValue;
	}

	public void setTdHshcXdddTrenDuongValue(Long tdHshcXdddTrenDuongValue) {
		this.tdHshcXdddTrenDuongValue = tdHshcXdddTrenDuongValue;
	}

	public Double getTdHshcXdddTrenDuongTyLe() {
		return tdHshcXdddTrenDuongTyLe;
	}

	public void setTdHshcXdddTrenDuongTyLe(Double tdHshcXdddTrenDuongTyLe) {
		this.tdHshcXdddTrenDuongTyLe = tdHshcXdddTrenDuongTyLe;
	}

	public Long getTdHshcXdddGdCnKyValue() {
		return tdHshcXdddGdCnKyValue;
	}

	public void setTdHshcXdddGdCnKyValue(Long tdHshcXdddGdCnKyValue) {
		this.tdHshcXdddGdCnKyValue = tdHshcXdddGdCnKyValue;
	}

	public Double getTdHshcXdddGdCnKyTyLe() {
		return tdHshcXdddGdCnKyTyLe;
	}

	public void setTdHshcXdddGdCnKyTyLe(Double tdHshcXdddGdCnKyTyLe) {
		this.tdHshcXdddGdCnKyTyLe = tdHshcXdddGdCnKyTyLe;
	}

	public Long getTdHshcXdddDoiSoat4aValue() {
		return tdHshcXdddDoiSoat4aValue;
	}

	public void setTdHshcXdddDoiSoat4aValue(Long tdHshcXdddDoiSoat4aValue) {
		this.tdHshcXdddDoiSoat4aValue = tdHshcXdddDoiSoat4aValue;
	}

	public Double getTdHshcXdddDoiSoat4aTyLe() {
		return tdHshcXdddDoiSoat4aTyLe;
	}

	public void setTdHshcXdddDoiSoat4aTyLe(Double tdHshcXdddDoiSoat4aTyLe) {
		this.tdHshcXdddDoiSoat4aTyLe = tdHshcXdddDoiSoat4aTyLe;
	}

	public Long getTdHshcXdddPhtThamDuyetValue() {
		return tdHshcXdddPhtThamDuyetValue;
	}

	public void setTdHshcXdddPhtThamDuyetValue(Long tdHshcXdddPhtThamDuyetValue) {
		this.tdHshcXdddPhtThamDuyetValue = tdHshcXdddPhtThamDuyetValue;
	}

	public Double getTdHshcXdddPhtThamDuyetTyLe() {
		return tdHshcXdddPhtThamDuyetTyLe;
	}

	public void setTdHshcXdddPhtThamDuyetTyLe(Double tdHshcXdddPhtThamDuyetTyLe) {
		this.tdHshcXdddPhtThamDuyetTyLe = tdHshcXdddPhtThamDuyetTyLe;
	}

	public Long getTdHshcXdddPhtNghiemThuValue() {
		return tdHshcXdddPhtNghiemThuValue;
	}

	public void setTdHshcXdddPhtNghiemThuValue(Long tdHshcXdddPhtNghiemThuValue) {
		this.tdHshcXdddPhtNghiemThuValue = tdHshcXdddPhtNghiemThuValue;
	}

	public Double getTdHshcXdddPhtNghiemThuTyLe() {
		return tdHshcXdddPhtNghiemThuTyLe;
	}

	public void setTdHshcXdddPhtNghiemThuTyLe(Double tdHshcXdddPhtNghiemThuTyLe) {
		this.tdHshcXdddPhtNghiemThuTyLe = tdHshcXdddPhtNghiemThuTyLe;
	}

	public Long getTdHshcXdddDangLamHoSoValue() {
		return tdHshcXdddDangLamHoSoValue;
	}

	public void setTdHshcXdddDangLamHoSoValue(Long tdHshcXdddDangLamHoSoValue) {
		this.tdHshcXdddDangLamHoSoValue = tdHshcXdddDangLamHoSoValue;
	}

	public Double getTdHshcXdddDangLamHoSoTyLe() {
		return tdHshcXdddDangLamHoSoTyLe;
	}

	public void setTdHshcXdddDangLamHoSoTyLe(Double tdHshcXdddDangLamHoSoTyLe) {
		this.tdHshcXdddDangLamHoSoTyLe = tdHshcXdddDangLamHoSoTyLe;
	}

	public Long getTdHshcXdddDuKienHoanThanhValue() {
		return tdHshcXdddDuKienHoanThanhValue;
	}

	public void setTdHshcXdddDuKienHoanThanhValue(Long tdHshcXdddDuKienHoanThanhValue) {
		this.tdHshcXdddDuKienHoanThanhValue = tdHshcXdddDuKienHoanThanhValue;
	}

	public Double getTdHshcXdddDuKienHoanThanhTyLe() {
		return tdHshcXdddDuKienHoanThanhTyLe;
	}

	public void setTdHshcXdddDuKienHoanThanhTyLe(Double tdHshcXdddDuKienHoanThanhTyLe) {
		this.tdHshcXdddDuKienHoanThanhTyLe = tdHshcXdddDuKienHoanThanhTyLe;
	}

	public String getTdHshcXdddNguyenNhanKoHt() {
		return tdHshcXdddNguyenNhanKoHt;
	}

	public void setTdHshcXdddNguyenNhanKoHt(String tdHshcXdddNguyenNhanKoHt) {
		this.tdHshcXdddNguyenNhanKoHt = tdHshcXdddNguyenNhanKoHt;
	}

	public Double getTdSlHtctDaHtTyLe() {
		return tdSlHtctDaHtTyLe;
	}

	public void setTdSlHtctDaHtTyLe(Double tdSlHtctDaHtTyLe) {
		this.tdSlHtctDaHtTyLe = tdSlHtctDaHtTyLe;
	}

	public Double getTdSlCpDaHoanThanhTyLe() {
		return tdSlCpDaHoanThanhTyLe;
	}

	public void setTdSlCpDaHoanThanhTyLe(Double tdSlCpDaHoanThanhTyLe) {
		this.tdSlCpDaHoanThanhTyLe = tdSlCpDaHoanThanhTyLe;
	}

	public Double getTdSlXlDaHoanThanhTyLe() {
		return tdSlXlDaHoanThanhTyLe;
	}

	public void setTdSlXlDaHoanThanhTyLe(Double tdSlXlDaHoanThanhTyLe) {
		this.tdSlXlDaHoanThanhTyLe = tdSlXlDaHoanThanhTyLe;
	}

	public Double getTdSlNtdDaHoanThanhTyLe() {
		return tdSlNtdDaHoanThanhTyLe;
	}

	public void setTdSlNtdDaHoanThanhTyLe(Double tdSlNtdDaHoanThanhTyLe) {
		this.tdSlNtdDaHoanThanhTyLe = tdSlNtdDaHoanThanhTyLe;
	}

	public Double getTdSlXdddDaHoanThanhTyLe() {
		return tdSlXdddDaHoanThanhTyLe;
	}

	public void setTdSlXdddDaHoanThanhTyLe(Double tdSlXdddDaHoanThanhTyLe) {
		this.tdSlXdddDaHoanThanhTyLe = tdSlXdddDaHoanThanhTyLe;
	}

	public Double getTdThdtDaHoanThanhTyLe() {
		return tdThdtDaHoanThanhTyLe;
	}

	public void setTdThdtDaHoanThanhTyLe(Double tdThdtDaHoanThanhTyLe) {
		this.tdThdtDaHoanThanhTyLe = tdThdtDaHoanThanhTyLe;
	}

	public Double getTdHtctXdmDangTcTyLe() {
		return tdHtctXdmDangTcTyLe;
	}

	public void setTdHtctXdmDangTcTyLe(Double tdHtctXdmDangTcTyLe) {
		this.tdHtctXdmDangTcTyLe = tdHtctXdmDangTcTyLe;
	}

	public Double getTdHtctXdmDuKienHtTyLe() {
		return tdHtctXdmDuKienHtTyLe;
	}

	public void setTdHtctXdmDuKienHtTyLe(Double tdHtctXdmDuKienHtTyLe) {
		this.tdHtctXdmDuKienHtTyLe = tdHtctXdmDuKienHtTyLe;
	}

	public Double getTdHtctHtDangTcTyLe() {
		return tdHtctHtDangTcTyLe;
	}

	public void setTdHtctHtDangTcTyLe(Double tdHtctHtDangTcTyLe) {
		this.tdHtctHtDangTcTyLe = tdHtctHtDangTcTyLe;
	}

	public Double getTdHtctHtDuKienHtTyLe() {
		return tdHtctHtDuKienHtTyLe;
	}

	public void setTdHtctHtDuKienHtTyLe(Double tdHtctHtDuKienHtTyLe) {
		this.tdHtctHtDuKienHtTyLe = tdHtctHtDuKienHtTyLe;
	}

	private Long giaTriSLXLXepThu;
	private Long giaTriSLCPXepThu;
	private Long giaTriSLNTDXepThu;
	private Long giaTriQuyLuongXepThu;
	
	private Long giaTriSLXddd;
	private Long giaTriSLXdddLuyKe;
	private Double giaTriSLXdddTyLe;
	private Long giaTriSLXdddXepThu;
	private Long giaTriSLXdddNguyenNhan;
	
	private Long giaTriHshcXddd;
	private Long giaTrigiaTriHshcXdddLuyKe;
	private Double giaTrigiaTriHshcXdddTyLe;
	private Long giaTrigiaTriHshcXdddXepThu;
	private Long giaTrigiaTriHshcXdddNguyenNhan;

	private Long giaTriSLHtct;
	private Long giaTriSLHtctLuyKe;
	private Double giaTriSLHtctTyLe;
	private Long giaTriSLHtctXepThu;
	private Long giaTriSLHtctNguyenNhan;
	

	public Long getGiaTriQuyLuongXepThu() {
		return giaTriQuyLuongXepThu;
	}

	public void setGiaTriQuyLuongXepThu(Long giaTriQuyLuongXepThu) {
		this.giaTriQuyLuongXepThu = giaTriQuyLuongXepThu;
	}

	public Long getGiaTriSLNTDXepThu() {
		return giaTriSLNTDXepThu;
	}

	public void setGiaTriSLNTDXepThu(Long giaTriSLNTDXepThu) {
		this.giaTriSLNTDXepThu = giaTriSLNTDXepThu;
	}

	public Long getGiaTriSLCPXepThu() {
		return giaTriSLCPXepThu;
	}

	public void setGiaTriSLCPXepThu(Long giaTriSLCPXepThu) {
		this.giaTriSLCPXepThu = giaTriSLCPXepThu;
	}

	public Long getGiaTriSLXLXepThu() {
		return giaTriSLXLXepThu;
	}

	public void setGiaTriSLXLXepThu(Long giaTriSLXLXepThu) {
		this.giaTriSLXLXepThu = giaTriSLXLXepThu;
	}

	public Long getGiaTriSLHtct() {
		return giaTriSLHtct;
	}

	public void setGiaTriSLHtct(Long giaTriSLHtct) {
		this.giaTriSLHtct = giaTriSLHtct;
	}

	public Long getGiaTriSLHtctLuyKe() {
		return giaTriSLHtctLuyKe;
	}

	public void setGiaTriSLHtctLuyKe(Long giaTriSLHtctLuyKe) {
		this.giaTriSLHtctLuyKe = giaTriSLHtctLuyKe;
	}

	public Double getGiaTriSLHtctTyLe() {
		return giaTriSLHtctTyLe;
	}

	public void setGiaTriSLHtctTyLe(Double giaTriSLHtctTyLe) {
		this.giaTriSLHtctTyLe = giaTriSLHtctTyLe;
	}

	public Long getGiaTriSLHtctXepThu() {
		return giaTriSLHtctXepThu;
	}

	public void setGiaTriSLHtctXepThu(Long giaTriSLHtctXepThu) {
		this.giaTriSLHtctXepThu = giaTriSLHtctXepThu;
	}

	public Long getGiaTriSLHtctNguyenNhan() {
		return giaTriSLHtctNguyenNhan;
	}

	public void setGiaTriSLHtctNguyenNhan(Long giaTriSLHtctNguyenNhan) {
		this.giaTriSLHtctNguyenNhan = giaTriSLHtctNguyenNhan;
	}

	public Long getGiaTriHshcXddd() {
		return giaTriHshcXddd;
	}

	public void setGiaTriHshcXddd(Long giaTriHshcXddd) {
		this.giaTriHshcXddd = giaTriHshcXddd;
	}

	public Long getGiaTrigiaTriHshcXdddLuyKe() {
		return giaTrigiaTriHshcXdddLuyKe;
	}

	public void setGiaTrigiaTriHshcXdddLuyKe(Long giaTrigiaTriHshcXdddLuyKe) {
		this.giaTrigiaTriHshcXdddLuyKe = giaTrigiaTriHshcXdddLuyKe;
	}

	public Double getGiaTrigiaTriHshcXdddTyLe() {
		return giaTrigiaTriHshcXdddTyLe;
	}

	public void setGiaTrigiaTriHshcXdddTyLe(Double giaTrigiaTriHshcXdddTyLe) {
		this.giaTrigiaTriHshcXdddTyLe = giaTrigiaTriHshcXdddTyLe;
	}

	public Long getGiaTrigiaTriHshcXdddXepThu() {
		return giaTrigiaTriHshcXdddXepThu;
	}

	public void setGiaTrigiaTriHshcXdddXepThu(Long giaTrigiaTriHshcXdddXepThu) {
		this.giaTrigiaTriHshcXdddXepThu = giaTrigiaTriHshcXdddXepThu;
	}

	public Long getGiaTrigiaTriHshcXdddNguyenNhan() {
		return giaTrigiaTriHshcXdddNguyenNhan;
	}

	public void setGiaTrigiaTriHshcXdddNguyenNhan(Long giaTrigiaTriHshcXdddNguyenNhan) {
		this.giaTrigiaTriHshcXdddNguyenNhan = giaTrigiaTriHshcXdddNguyenNhan;
	}

	public Long getGiaTriSLXddd() {
		return giaTriSLXddd;
	}

	public void setGiaTriSLXddd(Long giaTriSLXddd) {
		this.giaTriSLXddd = giaTriSLXddd;
	}

	public Long getGiaTriSLXdddLuyKe() {
		return giaTriSLXdddLuyKe;
	}

	public void setGiaTriSLXdddLuyKe(Long giaTriSLXdddLuyKe) {
		this.giaTriSLXdddLuyKe = giaTriSLXdddLuyKe;
	}

	public Double getGiaTriSLXdddTyLe() {
		return giaTriSLXdddTyLe;
	}

	public void setGiaTriSLXdddTyLe(Double giaTriSLXdddTyLe) {
		this.giaTriSLXdddTyLe = giaTriSLXdddTyLe;
	}

	public Long getGiaTriSLXdddXepThu() {
		return giaTriSLXdddXepThu;
	}

	public void setGiaTriSLXdddXepThu(Long giaTriSLXdddXepThu) {
		this.giaTriSLXdddXepThu = giaTriSLXdddXepThu;
	}

	public Long getGiaTriSLXdddNguyenNhan() {
		return giaTriSLXdddNguyenNhan;
	}

	public void setGiaTriSLXdddNguyenNhan(Long giaTriSLXdddNguyenNhan) {
		this.giaTriSLXdddNguyenNhan = giaTriSLXdddNguyenNhan;
	}
	
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date approveRevenueDate;

	public Date getApproveRevenueDate() {
		return approveRevenueDate;
	}

	public void setApproveRevenueDate(Date approveRevenueDate) {
		this.approveRevenueDate = approveRevenueDate;
	}
	
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date approveCompleteDate;

	public Date getApproveCompleteDate() {
		return approveCompleteDate;
	}

	public void setApproveCompleteDate(Date approveCompleteDate) {
		this.approveCompleteDate = approveCompleteDate;
	}
	
	private Long ttktId;

	public Long getTtktId() {
		return ttktId;
	}

	public void setTtktId(Long ttktId) {
		this.ttktId = ttktId;
	}
	
	private List<String> groupIdList;

	public List<String> getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(List<String> groupIdList) {
		this.groupIdList = groupIdList;
	}
	
	private Long quantityHtXl;
	private Long quantityHtCp;
	private Long quantityHtNtd;
	private Long quantityHtHtctXdm;
	private Long quantityHtHtctHt;
	private Long quantityHtXddd;

	public Long getQuantityHtXl() {
		return quantityHtXl;
	}

	public void setQuantityHtXl(Long quantityHtXl) {
		this.quantityHtXl = quantityHtXl;
	}

	public Long getQuantityHtCp() {
		return quantityHtCp;
	}

	public void setQuantityHtCp(Long quantityHtCp) {
		this.quantityHtCp = quantityHtCp;
	}

	public Long getQuantityHtNtd() {
		return quantityHtNtd;
	}

	public void setQuantityHtNtd(Long quantityHtNtd) {
		this.quantityHtNtd = quantityHtNtd;
	}

	public Long getQuantityHtHtctHt() {
		return quantityHtHtctHt;
	}

	public void setQuantityHtHtctHt(Long quantityHtHtctHt) {
		this.quantityHtHtctHt = quantityHtHtctHt;
	}

	public Long getQuantityHtHtctXdm() {
		return quantityHtHtctXdm;
	}

	public void setQuantityHtHtctXdm(Long quantityHtHtctXdm) {
		this.quantityHtHtctXdm = quantityHtHtctXdm;
	}

	public Long getQuantityHtXddd() {
		return quantityHtXddd;
	}

	public void setQuantityHtXddd(Long quantityHtXddd) {
		this.quantityHtXddd = quantityHtXddd;
	}

	private String workItemName;

	public String getWorkItemName() {
		return workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}
	
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	// Huy-end
	private Long sysGroupId;

	public Long getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}
	
	private String provinceCode;

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
}
