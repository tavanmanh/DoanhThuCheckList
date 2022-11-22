package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import com.viettel.service.base.model.BaseFWModelImpl;

public class ReportDTO extends ComsBaseFWDTO<BaseFWModelImpl> {

	private Long tangentCustomerId;
	private String provinceCode;
	private String employeeCode;
	private String fullName;
	private String phoneNumber;
	private String employeeCodeCTV;
	private String fullNameCTV;
	private String cmtnd;
	private String vtpay;
	private Double giaTruocThue;
	private Double hoaHong;
	private Double thueTNCN;
	private Double thucNhan;
	private String thang;
	private Double sumGiaTruocThue;
	private Double sumHoaHong;
	private Double sumThueTNCN;
	private Double sumThucNhan;
	private String contractCode;
	private Double soTien;
	private String tinNhan;
	
	//Huypq-24052021-start
	private Long sysGroupId;
	private String sysGroupCode;
	private String sysGroupName;
//	Còn hoạt động đầu kỳ
	private Long numberCtvActiveFirstStage;
//	Tuyển mới trong kỳ
	private Long numberCtvNewInsideStage;
//	Chấm dứt hoạt động trong kỳ
	private Long numberCtvEndInsideStage;
//	Còn hoạt động cuối kỳ
	private Long numberCtvActiveFinalStage;
//	Số lượng xã/phường
	private Long numberCommune;
//	Số lượng CTV
	private Long numberCtvCurrent;
//	% độ phủ quy hoạch
	private Double percentZoningCoverage;
//	Ghi chú
	private String description;
//	Cụm đội
	private String teamCluster;
//	Quận/huyện
	private String districtName;
//	Xã/phường
	private String communeName;
//	Đối tượng
	private String object;
//	Lĩnh vực/ngành hàng
	private String industry;
//	Trạng thái
	private String status;
//	Số CTV có doanh thu
	private Long numberCtvRevenueIncurred;
//	Tổng doanh thu kênh XHH
	private Double sumRevenueXhh;
//	Trung bình/CTV doanh thu kênh XHH
	private Double mediumRevenueXhh;
//	Tổng hoa hồng kênh XHH
	private Double sumDiscountXhh;
//	Trung bình/CTV hoa hồng kênh XHH
	private Double mediumDiscountXhh;
//	Loại báo cáo
	private String typeBc;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date dateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date dateTo;
	//Huy-end
	//Huypq-21062021-start
	private String provinceName;
	private Double planTMB;
	private Double performTMB;
	private Double ratioTMB;
	private Double planKC;
	private Double performKC;
	private Double ratioKC;
	private Double planDBHT;
	private Double performDBHT;
	private Double ratioDBHT;
	private Double planPS;
	private Double performPS;
	private Double ratioPS;
	private Double planHSHC;
	private Double performHSHC;
	private Double ratioHSHC;
	private String tramVTN;
	private String tramVCC;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayThueMb;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayHoanThanhTkdt;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayHoanThanhThamDinh;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayRaQuyetDinhTkdt;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayKhoiCong;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayDongBo;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayPhatSong;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayHshcGuiTtht;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date ngayKpiHshc;
	private String quaHanKpi;
	private String dateType;
	//Huy-end
	private String month;
	private String year;
	private Double sumPlanTMB;
	private Double sumPerformTMB;
	private Double sumRatioTMB;
	private Double sumPlanKC;
	private Double sumPerformKC;
	private Double sumRatioKC;
	private Double sumPlanDBHT;
	private Double sumPerformDBHT;
	private Double sumRatioDBHT;
	private Double sumPlanPS;
	private Double sumPerformPS;
	private Double sumRatioPS;
	private Double sumPlanHSHC;
	private Double sumPerformHSHC;
	private Double sumRatioHSHC;
	private String soTienChuyenChu;
	private String typeTime;	
	//
	private String woCode;
	private String woName;
//	@JsonSerialize(using = JsonDateSerializer.class)
//    @JsonDeserialize(using = JsonDateDeserializer.class)
    private String updateCdApproveWo;
//	@JsonSerialize(using = JsonDateSerializer.class)
//    @JsonDeserialize(using = JsonDateDeserializer.class)
    private String updateTthtApproveWo;
//	@JsonSerialize(using = JsonDateSerializer.class)
//	@JsonDeserialize(using = JsonDateDeserializer.class)
	private String updateTcBranchApproveWo;
//	@JsonSerialize(using = JsonDateSerializer.class)
//    @JsonDeserialize(using = JsonDateDeserializer.class)
    private String updateTcTctApproveWo;
//	@JsonSerialize(using = JsonDateSerializer.class)
//    @JsonDeserialize(using = JsonDateDeserializer.class)
    private String approveDateReportWo;
	private String constructionCode;
	private String stationCode;
	private String apWorkSrc;
	private String apConstructionType;
	private String woState;
	private Double moneyValue;
	private List<String> lstWoState;
	//
	
	public Double getSoTien() {
		return soTien;
	}

	public void setSoTien(Double soTien) {
		this.soTien = soTien;
	}

	public String getTinNhan() {
		return tinNhan;
	}

	public void setTinNhan(String tinNhan) {
		this.tinNhan = tinNhan;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Double getSumGiaTruocThue() {
		return sumGiaTruocThue;
	}

	public void setSumGiaTruocThue(Double sumGiaTruocThue) {
		this.sumGiaTruocThue = sumGiaTruocThue;
	}

	public Double getSumHoaHong() {
		return sumHoaHong;
	}

	public void setSumHoaHong(Double sumHoaHong) {
		this.sumHoaHong = sumHoaHong;
	}

	public Double getSumThueTNCN() {
		return sumThueTNCN;
	}

	public void setSumThueTNCN(Double sumThueTNCN) {
		this.sumThueTNCN = sumThueTNCN;
	}

	public Double getSumThucNhan() {
		return sumThucNhan;
	}

	public void setSumThucNhan(Double sumThucNhan) {
		this.sumThucNhan = sumThucNhan;
	}

	public Long getTangentCustomerId() {
		return tangentCustomerId;
	}

	public void setTangentCustomerId(Long tangentCustomerId) {
		this.tangentCustomerId = tangentCustomerId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmployeeCodeCTV() {
		return employeeCodeCTV;
	}

	public void setEmployeeCodeCTV(String employeeCodeCTV) {
		this.employeeCodeCTV = employeeCodeCTV;
	}

	public String getFullNameCTV() {
		return fullNameCTV;
	}

	public void setFullNameCTV(String fullNameCTV) {
		this.fullNameCTV = fullNameCTV;
	}

	public String getCmtnd() {
		return cmtnd;
	}

	public void setCmtnd(String cmtnd) {
		this.cmtnd = cmtnd;
	}

	public String getVtpay() {
		return vtpay;
	}

	public void setVtpay(String vtpay) {
		this.vtpay = vtpay;
	}

	public Double getGiaTruocThue() {
		return giaTruocThue;
	}

	public void setGiaTruocThue(Double giaTruocThue) {
		this.giaTruocThue = giaTruocThue;
	}

	public Double getHoaHong() {
		return hoaHong;
	}

	public void setHoaHong(Double hoaHong) {
		this.hoaHong = hoaHong;
	}

	public Double getThueTNCN() {
		return thueTNCN;
	}

	public void setThueTNCN(Double thueTNCN) {
		this.thueTNCN = thueTNCN;
	}

	public Double getThucNhan() {
		return thucNhan;
	}

	public void setThucNhan(Double thucNhan) {
		this.thucNhan = thucNhan;
	}

	public String getThang() {
		return thang;
	}

	public void setThang(String thang) {
		this.thang = thang;
	}

	public String getSysGroupCode() {
		return sysGroupCode;
	}

	public void setSysGroupCode(String sysGroupCode) {
		this.sysGroupCode = sysGroupCode;
	}

	public String getSysGroupName() {
		return sysGroupName;
	}

	public void setSysGroupName(String sysGroupName) {
		this.sysGroupName = sysGroupName;
	}

	public Long getNumberCtvActiveFirstStage() {
		return numberCtvActiveFirstStage;
	}

	public void setNumberCtvActiveFirstStage(Long numberCtvActiveFirstStage) {
		this.numberCtvActiveFirstStage = numberCtvActiveFirstStage;
	}

	public Long getNumberCtvNewInsideStage() {
		return numberCtvNewInsideStage;
	}

	public void setNumberCtvNewInsideStage(Long numberCtvNewInsideStage) {
		this.numberCtvNewInsideStage = numberCtvNewInsideStage;
	}

	public Long getNumberCtvEndInsideStage() {
		return numberCtvEndInsideStage;
	}

	public void setNumberCtvEndInsideStage(Long numberCtvEndInsideStage) {
		this.numberCtvEndInsideStage = numberCtvEndInsideStage;
	}

	public Long getNumberCtvActiveFinalStage() {
		return numberCtvActiveFinalStage;
	}

	public void setNumberCtvActiveFinalStage(Long numberCtvActiveFinalStage) {
		this.numberCtvActiveFinalStage = numberCtvActiveFinalStage;
	}

	public Long getNumberCommune() {
		return numberCommune;
	}

	public void setNumberCommune(Long numberCommune) {
		this.numberCommune = numberCommune;
	}

	public Long getNumberCtvCurrent() {
		return numberCtvCurrent;
	}

	public void setNumberCtvCurrent(Long numberCtvCurrent) {
		this.numberCtvCurrent = numberCtvCurrent;
	}

	public Double getPercentZoningCoverage() {
		return percentZoningCoverage;
	}

	public void setPercentZoningCoverage(Double percentZoningCoverage) {
		this.percentZoningCoverage = percentZoningCoverage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTeamCluster() {
		return teamCluster;
	}

	public void setTeamCluster(String teamCluster) {
		this.teamCluster = teamCluster;
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

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getNumberCtvRevenueIncurred() {
		return numberCtvRevenueIncurred;
	}

	public void setNumberCtvRevenueIncurred(Long numberCtvRevenueIncurred) {
		this.numberCtvRevenueIncurred = numberCtvRevenueIncurred;
	}

	public Double getSumRevenueXhh() {
		return sumRevenueXhh;
	}

	public void setSumRevenueXhh(Double sumRevenueXhh) {
		this.sumRevenueXhh = sumRevenueXhh;
	}

	public Double getMediumRevenueXhh() {
		return mediumRevenueXhh;
	}

	public void setMediumRevenueXhh(Double mediumRevenueXhh) {
		this.mediumRevenueXhh = mediumRevenueXhh;
	}

	public Double getSumDiscountXhh() {
		return sumDiscountXhh;
	}

	public void setSumDiscountXhh(Double sumDiscountXhh) {
		this.sumDiscountXhh = sumDiscountXhh;
	}

	public Double getMediumDiscountXhh() {
		return mediumDiscountXhh;
	}

	public void setMediumDiscountXhh(Double mediumDiscountXhh) {
		this.mediumDiscountXhh = mediumDiscountXhh;
	}

	public String getTypeBc() {
		return typeBc;
	}

	public void setTypeBc(String typeBc) {
		this.typeBc = typeBc;
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

	public Long getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}

	public Double getPlanTMB() {
		return planTMB;
	}

	public void setPlanTMB(Double planTMB) {
		this.planTMB = planTMB;
	}

	public Double getPerformTMB() {
		return performTMB;
	}

	public void setPerformTMB(Double performTMB) {
		this.performTMB = performTMB;
	}

	public Double getRatioTMB() {
		return ratioTMB;
	}

	public void setRatioTMB(Double ratioTMB) {
		this.ratioTMB = ratioTMB;
	}

	public Double getPlanKC() {
		return planKC;
	}

	public void setPlanKC(Double planKC) {
		this.planKC = planKC;
	}

	public Double getPerformKC() {
		return performKC;
	}

	public void setPerformKC(Double performKC) {
		this.performKC = performKC;
	}

	public Double getRatioKC() {
		return ratioKC;
	}

	public void setRatioKC(Double ratioKC) {
		this.ratioKC = ratioKC;
	}

	public Double getPlanDBHT() {
		return planDBHT;
	}

	public void setPlanDBHT(Double planDBHT) {
		this.planDBHT = planDBHT;
	}

	public Double getPerformDBHT() {
		return performDBHT;
	}

	public void setPerformDBHT(Double performDBHT) {
		this.performDBHT = performDBHT;
	}

	public Double getRatioDBHT() {
		return ratioDBHT;
	}

	public void setRatioDBHT(Double ratioDBHT) {
		this.ratioDBHT = ratioDBHT;
	}

	public Double getPlanPS() {
		return planPS;
	}

	public void setPlanPS(Double planPS) {
		this.planPS = planPS;
	}

	public Double getPerformPS() {
		return performPS;
	}

	public void setPerformPS(Double performPS) {
		this.performPS = performPS;
	}

	public Double getRatioPS() {
		return ratioPS;
	}

	public void setRatioPS(Double ratioPS) {
		this.ratioPS = ratioPS;
	}

	public Double getPlanHSHC() {
		return planHSHC;
	}

	public void setPlanHSHC(Double planHSHC) {
		this.planHSHC = planHSHC;
	}

	public Double getPerformHSHC() {
		return performHSHC;
	}

	public void setPerformHSHC(Double performHSHC) {
		this.performHSHC = performHSHC;
	}

	public Double getRatioHSHC() {
		return ratioHSHC;
	}

	public void setRatioHSHC(Double ratioHSHC) {
		this.ratioHSHC = ratioHSHC;
	}

	public String getTramVTN() {
		return tramVTN;
	}

	public void setTramVTN(String tramVTN) {
		this.tramVTN = tramVTN;
	}

	public String getTramVCC() {
		return tramVCC;
	}

	public void setTramVCC(String tramVCC) {
		this.tramVCC = tramVCC;
	}

	public Date getNgayThueMb() {
		return ngayThueMb;
	}

	public void setNgayThueMb(Date ngayThueMb) {
		this.ngayThueMb = ngayThueMb;
	}

	public Date getNgayHoanThanhTkdt() {
		return ngayHoanThanhTkdt;
	}

	public void setNgayHoanThanhTkdt(Date ngayHoanThanhTkdt) {
		this.ngayHoanThanhTkdt = ngayHoanThanhTkdt;
	}

	public Date getNgayHoanThanhThamDinh() {
		return ngayHoanThanhThamDinh;
	}

	public void setNgayHoanThanhThamDinh(Date ngayHoanThanhThamDinh) {
		this.ngayHoanThanhThamDinh = ngayHoanThanhThamDinh;
	}

	public Date getNgayRaQuyetDinhTkdt() {
		return ngayRaQuyetDinhTkdt;
	}

	public void setNgayRaQuyetDinhTkdt(Date ngayRaQuyetDinhTkdt) {
		this.ngayRaQuyetDinhTkdt = ngayRaQuyetDinhTkdt;
	}

	public Date getNgayKhoiCong() {
		return ngayKhoiCong;
	}

	public void setNgayKhoiCong(Date ngayKhoiCong) {
		this.ngayKhoiCong = ngayKhoiCong;
	}

	public Date getNgayDongBo() {
		return ngayDongBo;
	}

	public void setNgayDongBo(Date ngayDongBo) {
		this.ngayDongBo = ngayDongBo;
	}

	public Date getNgayPhatSong() {
		return ngayPhatSong;
	}

	public void setNgayPhatSong(Date ngayPhatSong) {
		this.ngayPhatSong = ngayPhatSong;
	}

	public Date getNgayHshcGuiTtht() {
		return ngayHshcGuiTtht;
	}

	public void setNgayHshcGuiTtht(Date ngayHshcGuiTtht) {
		this.ngayHshcGuiTtht = ngayHshcGuiTtht;
	}

	public Date getNgayKpiHshc() {
		return ngayKpiHshc;
	}

	public void setNgayKpiHshc(Date ngayKpiHshc) {
		this.ngayKpiHshc = ngayKpiHshc;
	}

	public String getQuaHanKpi() {
		return quaHanKpi;
	}

	public void setQuaHanKpi(String quaHanKpi) {
		this.quaHanKpi = quaHanKpi;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Double getSumPlanTMB() {
		return sumPlanTMB;
	}

	public void setSumPlanTMB(Double sumPlanTMB) {
		this.sumPlanTMB = sumPlanTMB;
	}

	public Double getSumPerformTMB() {
		return sumPerformTMB;
	}

	public void setSumPerformTMB(Double sumPerformTMB) {
		this.sumPerformTMB = sumPerformTMB;
	}

	public Double getSumRatioTMB() {
		return sumRatioTMB;
	}

	public void setSumRatioTMB(Double sumRatioTMB) {
		this.sumRatioTMB = sumRatioTMB;
	}

	public Double getSumPlanKC() {
		return sumPlanKC;
	}

	public void setSumPlanKC(Double sumPlanKC) {
		this.sumPlanKC = sumPlanKC;
	}

	public Double getSumPerformKC() {
		return sumPerformKC;
	}

	public void setSumPerformKC(Double sumPerformKC) {
		this.sumPerformKC = sumPerformKC;
	}

	public Double getSumRatioKC() {
		return sumRatioKC;
	}

	public void setSumRatioKC(Double sumRatioKC) {
		this.sumRatioKC = sumRatioKC;
	}

	public Double getSumPlanDBHT() {
		return sumPlanDBHT;
	}

	public void setSumPlanDBHT(Double sumPlanDBHT) {
		this.sumPlanDBHT = sumPlanDBHT;
	}

	public Double getSumPerformDBHT() {
		return sumPerformDBHT;
	}

	public void setSumPerformDBHT(Double sumPerformDBHT) {
		this.sumPerformDBHT = sumPerformDBHT;
	}

	public Double getSumRatioDBHT() {
		return sumRatioDBHT;
	}

	public void setSumRatioDBHT(Double sumRatioDBHT) {
		this.sumRatioDBHT = sumRatioDBHT;
	}

	public Double getSumPlanPS() {
		return sumPlanPS;
	}

	public void setSumPlanPS(Double sumPlanPS) {
		this.sumPlanPS = sumPlanPS;
	}

	public Double getSumPerformPS() {
		return sumPerformPS;
	}

	public void setSumPerformPS(Double sumPerformPS) {
		this.sumPerformPS = sumPerformPS;
	}

	public Double getSumRatioPS() {
		return sumRatioPS;
	}

	public void setSumRatioPS(Double sumRatioPS) {
		this.sumRatioPS = sumRatioPS;
	}

	public Double getSumPlanHSHC() {
		return sumPlanHSHC;
	}

	public void setSumPlanHSHC(Double sumPlanHSHC) {
		this.sumPlanHSHC = sumPlanHSHC;
	}

	public Double getSumPerformHSHC() {
		return sumPerformHSHC;
	}

	public void setSumPerformHSHC(Double sumPerformHSHC) {
		this.sumPerformHSHC = sumPerformHSHC;
	}

	public Double getSumRatioHSHC() {
		return sumRatioHSHC;
	}

	public void setSumRatioHSHC(Double sumRatioHSHC) {
		this.sumRatioHSHC = sumRatioHSHC;
	}

	public String getSoTienChuyenChu() {
		return soTienChuyenChu;
	}

	public void setSoTienChuyenChu(String soTienChuyenChu) {
		this.soTienChuyenChu = soTienChuyenChu;
	}

	public String getTypeTime() {
		return typeTime;
	}

	public void setTypeTime(String typeTime) {
		this.typeTime = typeTime;
	}

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	public String getWoName() {
		return woName;
	}

	public void setWoName(String woName) {
		this.woName = woName;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getApWorkSrc() {
		return apWorkSrc;
	}

	public void setApWorkSrc(String apWorkSrc) {
		this.apWorkSrc = apWorkSrc;
	}

	public String getApConstructionType() {
		return apConstructionType;
	}

	public void setApConstructionType(String apConstructionType) {
		this.apConstructionType = apConstructionType;
	}

	public String getWoState() {
		return woState;
	}

	public void setWoState(String woState) {
		this.woState = woState;
	}

	public Double getMoneyValue() {
		return moneyValue;
	}

	public void setMoneyValue(Double moneyValue) {
		this.moneyValue = moneyValue;
	}

	public List<String> getLstWoState() {
		return lstWoState;
	}

	public void setLstWoState(List<String> lstWoState) {
		this.lstWoState = lstWoState;
	}

	public String getUpdateCdApproveWo() {
		return updateCdApproveWo;
	}

	public void setUpdateCdApproveWo(String updateCdApproveWo) {
		this.updateCdApproveWo = updateCdApproveWo;
	}

	public String getUpdateTthtApproveWo() {
		return updateTthtApproveWo;
	}

	public void setUpdateTthtApproveWo(String updateTthtApproveWo) {
		this.updateTthtApproveWo = updateTthtApproveWo;
	}

	public String getUpdateTcBranchApproveWo() {
		return updateTcBranchApproveWo;
	}

	public void setUpdateTcBranchApproveWo(String updateTcBranchApproveWo) {
		this.updateTcBranchApproveWo = updateTcBranchApproveWo;
	}

	public String getUpdateTcTctApproveWo() {
		return updateTcTctApproveWo;
	}

	public void setUpdateTcTctApproveWo(String updateTcTctApproveWo) {
		this.updateTcTctApproveWo = updateTcTctApproveWo;
	}

	public String getApproveDateReportWo() {
		return approveDateReportWo;
	}

	public void setApproveDateReportWo(String approveDateReportWo) {
		this.approveDateReportWo = approveDateReportWo;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseFWModelImpl toModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
