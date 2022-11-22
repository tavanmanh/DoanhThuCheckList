package com.viettel.coms.dto;

import com.viettel.service.base.model.BaseFWModelImpl;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.service.base.dto.BaseFWDTOImpl;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportBTSByDADTO extends ComsBaseFWDTO<BaseFWModelImpl>{

	private String provinceCode;
	private String stationVccCode;
	private String stationVtNetCode;
	private Double count;
	private Double tMB;
	private Double tKDT;
	private Double thamTKDT;
	private Double tonChuaCoTKDTTham;
	private Double vuong;
	private Double daKC;
	private Double dBHT;
	private Double daPS;
	private Double hSHCVeTTHT;
	private Double hSHCVeDTHT;
	private Double countTonHSHCChuaVeTTHT;
	private Double n1TonHSHCChuaVeTTHT;
	private Double n2TonHSHCChuaVeTTHT;
	private Double n3TonHSHCChuaVeTTHT;
	private Double n4TonHSHCChuaVeTTHT;
	private Double countTonHSHCChuaVeDHTH;
	private Double n1TonHSHCChuaVeDHTH;
	private Double n2TonHSHCChuaVeDHTH;
	private Double n3TonHSHCChuaVeDHTH;
	private Double n4TonHSHCChuaVeDHTH;
	private Double sumCount;
	private Double sumTMB;
	private Double sumTKDT;
	private Double sumThamTKDT;
	private Double sumTonChuaCoTKDTTham;
	private Double sumVuong;
	private Double sumDaKC;
	private Double sumDBHT;
	private Double sumDaPS;
	private Double sumHSHCVeTTHT;
	private Double sumHSHCVeDTHT;
	private Double sumCountTonHSHCChuaVeTTHT;
	private Double sumN1TonHSHCChuaVeTTHT;
	private Double sumN2TonHSHCChuaVeTTHT;
	private Double sumN3TonHSHCChuaVeTTHT;
	private Double sumN4TonHSHCChuaVeTTHT;
	private Double sumCountTonHSHCChuaVeDHTH;
	private Double sumN1TonHSHCChuaVeDHTH;
	private Double sumN2TonHSHCChuaVeDHTH;
	private Double sumN3TonHSHCChuaVeDHTH;
	private Double sumN4TonHSHCChuaVeDHTH;
	private String description;
	private String typeBc;
	private String monthYearD;
	private Integer totalRecord;
	private Integer pageSize;
	private String catProvince;
	private String keySearch;
	private String projectCode;
	private String yearPlan;
	private Long macroNumber;
	private Long rruNumber;
	private Long smcNumber;
	private Long thueMb;
	private Long tyLeHt;
	private Long daChotGia;
	private Long duyetTKDT;
	private Long xongDBHT;
	private Long dangTc;
	private Long chuaTk;
	private Long hSHCVeTc;
	private Long countTonHSHCChuaVeTC;
	private Long n1TonHSHCChuaVeTC;
	private Long n2TonHSHCChuaVeTC;
	private Long n3TonHSHCChuaVeTC;
	private Long n4TonHSHCChuaVeTC;
	private Long duDieuKienTk;
	private Long sumMacroNumber;
	private Long sumRruNumber;
	private Long sumSmcNumber;
	private Long sumThueMb;
	private Long sumTyLeHt;
	private Long sumDaChotGia;
	private Long sumDuyetTKDT;
	private Long sumXongDBHT;
	private Long sumDangTc;
	private Long sumChuaTk;
	private Long sumHSHCVeTc;
	private Long sumCountTonHSHCChuaVeTC;
	private Long sumN1TonHSHCChuaVeTC;
	private Long sumN2TonHSHCChuaVeTC;
	private Long sumN3TonHSHCChuaVeTC;
	private Long sumN4TonHSHCChuaVeTC;
	private Long sumDuDieuKienTk;
	private Long sumYearPlan;
	private String name;
	private String code;
	
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getStationVccCode() {
		return stationVccCode;
	}
	public void setStationVccCode(String stationVccCode) {
		this.stationVccCode = stationVccCode;
	}
	public String getStationVtNetCode() {
		return stationVtNetCode;
	}
	public void setStationVtNetCode(String stationVtNetCode) {
		this.stationVtNetCode = stationVtNetCode;
	}
	public Double getCount() {
		return count;
	}
	public void setCount(Double count) {
		this.count = count;
	}
	public Double gettMB() {
		return tMB;
	}
	public void settMB(Double tMB) {
		this.tMB = tMB;
	}
	public Double gettKDT() {
		return tKDT;
	}
	public void settKDT(Double tKDT) {
		this.tKDT = tKDT;
	}
	public Double getThamTKDT() {
		return thamTKDT;
	}
	public void setThamTKDT(Double thamTKDT) {
		this.thamTKDT = thamTKDT;
	}
	public Double getTonChuaCoTKDTTham() {
		return tonChuaCoTKDTTham;
	}
	public void setTonChuaCoTKDTTham(Double tonChuaCoTKDTTham) {
		this.tonChuaCoTKDTTham = tonChuaCoTKDTTham;
	}
	public Double getVuong() {
		return vuong;
	}
	public void setVuong(Double vuong) {
		this.vuong = vuong;
	}
	public Double getDaKC() {
		return daKC;
	}
	public void setDaKC(Double daKC) {
		this.daKC = daKC;
	}
	public Double getdBHT() {
		return dBHT;
	}
	public void setdBHT(Double dBHT) {
		this.dBHT = dBHT;
	}
	public Double getDaPS() {
		return daPS;
	}
	public void setDaPS(Double daPS) {
		this.daPS = daPS;
	}
	public Double gethSHCVeTTHT() {
		return hSHCVeTTHT;
	}
	public void sethSHCVeTTHT(Double hSHCVeTTHT) {
		this.hSHCVeTTHT = hSHCVeTTHT;
	}
	public Double gethSHCVeDTHT() {
		return hSHCVeDTHT;
	}
	public void sethSHCVeDTHT(Double hSHCVeDTHT) {
		this.hSHCVeDTHT = hSHCVeDTHT;
	}
	public Double getCountTonHSHCChuaVeTTHT() {
		return countTonHSHCChuaVeTTHT;
	}
	public void setCountTonHSHCChuaVeTTHT(Double countTonHSHCChuaVeTTHT) {
		this.countTonHSHCChuaVeTTHT = countTonHSHCChuaVeTTHT;
	}
	public Double getN1TonHSHCChuaVeTTHT() {
		return n1TonHSHCChuaVeTTHT;
	}
	public void setN1TonHSHCChuaVeTTHT(Double n1TonHSHCChuaVeTTHT) {
		this.n1TonHSHCChuaVeTTHT = n1TonHSHCChuaVeTTHT;
	}
	public Double getN2TonHSHCChuaVeTTHT() {
		return n2TonHSHCChuaVeTTHT;
	}
	public void setN2TonHSHCChuaVeTTHT(Double n2TonHSHCChuaVeTTHT) {
		this.n2TonHSHCChuaVeTTHT = n2TonHSHCChuaVeTTHT;
	}
	public Double getN3TonHSHCChuaVeTTHT() {
		return n3TonHSHCChuaVeTTHT;
	}
	public void setN3TonHSHCChuaVeTTHT(Double n3TonHSHCChuaVeTTHT) {
		this.n3TonHSHCChuaVeTTHT = n3TonHSHCChuaVeTTHT;
	}
	public Double getN4TonHSHCChuaVeTTHT() {
		return n4TonHSHCChuaVeTTHT;
	}
	public void setN4TonHSHCChuaVeTTHT(Double n4TonHSHCChuaVeTTHT) {
		this.n4TonHSHCChuaVeTTHT = n4TonHSHCChuaVeTTHT;
	}
	public Double getCountTonHSHCChuaVeDHTH() {
		return countTonHSHCChuaVeDHTH;
	}
	public void setCountTonHSHCChuaVeDHTH(Double countTonHSHCChuaVeDHTH) {
		this.countTonHSHCChuaVeDHTH = countTonHSHCChuaVeDHTH;
	}
	public Double getN1TonHSHCChuaVeDHTH() {
		return n1TonHSHCChuaVeDHTH;
	}
	public void setN1TonHSHCChuaVeDHTH(Double n1TonHSHCChuaVeDHTH) {
		this.n1TonHSHCChuaVeDHTH = n1TonHSHCChuaVeDHTH;
	}
	public Double getN2TonHSHCChuaVeDHTH() {
		return n2TonHSHCChuaVeDHTH;
	}
	public void setN2TonHSHCChuaVeDHTH(Double n2TonHSHCChuaVeDHTH) {
		this.n2TonHSHCChuaVeDHTH = n2TonHSHCChuaVeDHTH;
	}
	public Double getN3TonHSHCChuaVeDHTH() {
		return n3TonHSHCChuaVeDHTH;
	}
	public void setN3TonHSHCChuaVeDHTH(Double n3TonHSHCChuaVeDHTH) {
		this.n3TonHSHCChuaVeDHTH = n3TonHSHCChuaVeDHTH;
	}
	public Double getN4TonHSHCChuaVeDHTH() {
		return n4TonHSHCChuaVeDHTH;
	}
	public void setN4TonHSHCChuaVeDHTH(Double n4TonHSHCChuaVeDHTH) {
		this.n4TonHSHCChuaVeDHTH = n4TonHSHCChuaVeDHTH;
	}
	
	public String getTypeBc() {
		return typeBc;
	}
	public void setTypeBc(String typeBc) {
		this.typeBc = typeBc;
	}
	
	public String getMonthYearD() {
		return monthYearD;
	}
	public void setMonthYearD(String monthYearD) {
		this.monthYearD = monthYearD;
	}
	public String getCatProvince() {
		return catProvince;
	}
	public void setCatProvince(String catProvince) {
		this.catProvince = catProvince;
	}
	public String getKeySearch() {
		return keySearch;
	}
	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}
	public Integer getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public Double getSumCount() {
		return sumCount;
	}
	public void setSumCount(Double sumCount) {
		this.sumCount = sumCount;
	}
	public Double getSumTMB() {
		return sumTMB;
	}
	public void setSumTMB(Double sumTMB) {
		this.sumTMB = sumTMB;
	}
	public Double getSumTKDT() {
		return sumTKDT;
	}
	public void setSumTKDT(Double sumTKDT) {
		this.sumTKDT = sumTKDT;
	}
	public Double getSumThamTKDT() {
		return sumThamTKDT;
	}
	public void setSumThamTKDT(Double sumThamTKDT) {
		this.sumThamTKDT = sumThamTKDT;
	}
	public Double getSumTonChuaCoTKDTTham() {
		return sumTonChuaCoTKDTTham;
	}
	public void setSumTonChuaCoTKDTTham(Double sumTonChuaCoTKDTTham) {
		this.sumTonChuaCoTKDTTham = sumTonChuaCoTKDTTham;
	}
	public Double getSumVuong() {
		return sumVuong;
	}
	public void setSumVuong(Double sumVuong) {
		this.sumVuong = sumVuong;
	}
	public Double getSumDaKC() {
		return sumDaKC;
	}
	public void setSumDaKC(Double sumDaKC) {
		this.sumDaKC = sumDaKC;
	}
	public Double getSumDBHT() {
		return sumDBHT;
	}
	public void setSumDBHT(Double sumDBHT) {
		this.sumDBHT = sumDBHT;
	}
	public Double getSumDaPS() {
		return sumDaPS;
	}
	public void setSumDaPS(Double sumDaPS) {
		this.sumDaPS = sumDaPS;
	}
	public Double getSumHSHCVeTTHT() {
		return sumHSHCVeTTHT;
	}
	public void setSumHSHCVeTTHT(Double sumHSHCVeTTHT) {
		this.sumHSHCVeTTHT = sumHSHCVeTTHT;
	}
	public Double getSumHSHCVeDTHT() {
		return sumHSHCVeDTHT;
	}
	public void setSumHSHCVeDTHT(Double sumHSHCVeDTHT) {
		this.sumHSHCVeDTHT = sumHSHCVeDTHT;
	}
	public Double getSumCountTonHSHCChuaVeTTHT() {
		return sumCountTonHSHCChuaVeTTHT;
	}
	public void setSumCountTonHSHCChuaVeTTHT(Double sumCountTonHSHCChuaVeTTHT) {
		this.sumCountTonHSHCChuaVeTTHT = sumCountTonHSHCChuaVeTTHT;
	}
	public Double getSumN1TonHSHCChuaVeTTHT() {
		return sumN1TonHSHCChuaVeTTHT;
	}
	public void setSumN1TonHSHCChuaVeTTHT(Double sumN1TonHSHCChuaVeTTHT) {
		this.sumN1TonHSHCChuaVeTTHT = sumN1TonHSHCChuaVeTTHT;
	}
	public Double getSumN2TonHSHCChuaVeTTHT() {
		return sumN2TonHSHCChuaVeTTHT;
	}
	public void setSumN2TonHSHCChuaVeTTHT(Double sumN2TonHSHCChuaVeTTHT) {
		this.sumN2TonHSHCChuaVeTTHT = sumN2TonHSHCChuaVeTTHT;
	}
	public Double getSumN3TonHSHCChuaVeTTHT() {
		return sumN3TonHSHCChuaVeTTHT;
	}
	public void setSumN3TonHSHCChuaVeTTHT(Double sumN3TonHSHCChuaVeTTHT) {
		this.sumN3TonHSHCChuaVeTTHT = sumN3TonHSHCChuaVeTTHT;
	}
	public Double getSumN4TonHSHCChuaVeTTHT() {
		return sumN4TonHSHCChuaVeTTHT;
	}
	public void setSumN4TonHSHCChuaVeTTHT(Double sumN4TonHSHCChuaVeTTHT) {
		this.sumN4TonHSHCChuaVeTTHT = sumN4TonHSHCChuaVeTTHT;
	}
	public Double getSumCountTonHSHCChuaVeDHTH() {
		return sumCountTonHSHCChuaVeDHTH;
	}
	public void setSumCountTonHSHCChuaVeDHTH(Double sumCountTonHSHCChuaVeDHTH) {
		this.sumCountTonHSHCChuaVeDHTH = sumCountTonHSHCChuaVeDHTH;
	}
	public Double getSumN1TonHSHCChuaVeDHTH() {
		return sumN1TonHSHCChuaVeDHTH;
	}
	public void setSumN1TonHSHCChuaVeDHTH(Double sumN1TonHSHCChuaVeDHTH) {
		this.sumN1TonHSHCChuaVeDHTH = sumN1TonHSHCChuaVeDHTH;
	}
	public Double getSumN2TonHSHCChuaVeDHTH() {
		return sumN2TonHSHCChuaVeDHTH;
	}
	public void setSumN2TonHSHCChuaVeDHTH(Double sumN2TonHSHCChuaVeDHTH) {
		this.sumN2TonHSHCChuaVeDHTH = sumN2TonHSHCChuaVeDHTH;
	}
	public Double getSumN3TonHSHCChuaVeDHTH() {
		return sumN3TonHSHCChuaVeDHTH;
	}
	public void setSumN3TonHSHCChuaVeDHTH(Double sumN3TonHSHCChuaVeDHTH) {
		this.sumN3TonHSHCChuaVeDHTH = sumN3TonHSHCChuaVeDHTH;
	}
	public Double getSumN4TonHSHCChuaVeDHTH() {
		return sumN4TonHSHCChuaVeDHTH;
	}
	public void setSumN4TonHSHCChuaVeDHTH(Double sumN4TonHSHCChuaVeDHTH) {
		this.sumN4TonHSHCChuaVeDHTH = sumN4TonHSHCChuaVeDHTH;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	public String getYearPlan() {
		return yearPlan;
	}
	public void setYearPlan(String yearPlan) {
		this.yearPlan = yearPlan;
	}
	public Long getMacroNumber() {
		return macroNumber;
	}
	public void setMacroNumber(Long macroNumber) {
		this.macroNumber = macroNumber;
	}
	public Long getRruNumber() {
		return rruNumber;
	}
	public void setRruNumber(Long rruNumber) {
		this.rruNumber = rruNumber;
	}
	public Long getSmcNumber() {
		return smcNumber;
	}
	public void setSmcNumber(Long smcNumber) {
		this.smcNumber = smcNumber;
	}
	public Long getThueMb() {
		return thueMb;
	}
	public void setThueMb(Long thueMb) {
		this.thueMb = thueMb;
	}
	public Long getTyLeHt() {
		return tyLeHt;
	}
	public void setTyLeHt(Long tyLeHt) {
		this.tyLeHt = tyLeHt;
	}
	public Long getDaChotGia() {
		return daChotGia;
	}
	public void setDaChotGia(Long daChotGia) {
		this.daChotGia = daChotGia;
	}
	public Long getDuyetTKDT() {
		return duyetTKDT;
	}
	public void setDuyetTKDT(Long duyetTKDT) {
		this.duyetTKDT = duyetTKDT;
	}
	public Long getXongDBHT() {
		return xongDBHT;
	}
	public void setXongDBHT(Long xongDBHT) {
		this.xongDBHT = xongDBHT;
	}
	public Long getDangTc() {
		return dangTc;
	}
	public void setDangTc(Long dangTc) {
		this.dangTc = dangTc;
	}
	public Long getChuaTk() {
		return chuaTk;
	}
	public void setChuaTk(Long chuaTk) {
		this.chuaTk = chuaTk;
	}
	public Long gethSHCVeTc() {
		return hSHCVeTc;
	}
	public void sethSHCVeTc(Long hSHCVeTc) {
		this.hSHCVeTc = hSHCVeTc;
	}
	public Long getCountTonHSHCChuaVeTC() {
		return countTonHSHCChuaVeTC;
	}
	public void setCountTonHSHCChuaVeTC(Long countTonHSHCChuaVeTC) {
		this.countTonHSHCChuaVeTC = countTonHSHCChuaVeTC;
	}
	public Long getN1TonHSHCChuaVeTC() {
		return n1TonHSHCChuaVeTC;
	}
	public void setN1TonHSHCChuaVeTC(Long n1TonHSHCChuaVeTC) {
		this.n1TonHSHCChuaVeTC = n1TonHSHCChuaVeTC;
	}
	public Long getN2TonHSHCChuaVeTC() {
		return n2TonHSHCChuaVeTC;
	}
	public void setN2TonHSHCChuaVeTC(Long n2TonHSHCChuaVeTC) {
		this.n2TonHSHCChuaVeTC = n2TonHSHCChuaVeTC;
	}
	public Long getN3TonHSHCChuaVeTC() {
		return n3TonHSHCChuaVeTC;
	}
	public void setN3TonHSHCChuaVeTC(Long n3TonHSHCChuaVeTC) {
		this.n3TonHSHCChuaVeTC = n3TonHSHCChuaVeTC;
	}
	public Long getN4TonHSHCChuaVeTC() {
		return n4TonHSHCChuaVeTC;
	}
	public void setN4TonHSHCChuaVeTC(Long n4TonHSHCChuaVeTC) {
		this.n4TonHSHCChuaVeTC = n4TonHSHCChuaVeTC;
	}
	
	public Long getDuDieuKienTk() {
		return duDieuKienTk;
	}
	public void setDuDieuKienTk(Long duDieuKienTk) {
		this.duDieuKienTk = duDieuKienTk;
	}
	
	public Long getSumMacroNumber() {
		return sumMacroNumber;
	}
	public void setSumMacroNumber(Long sumMacroNumber) {
		this.sumMacroNumber = sumMacroNumber;
	}
	public Long getSumRruNumber() {
		return sumRruNumber;
	}
	public void setSumRruNumber(Long sumRruNumber) {
		this.sumRruNumber = sumRruNumber;
	}
	public Long getSumSmcNumber() {
		return sumSmcNumber;
	}
	public void setSumSmcNumber(Long sumSmcNumber) {
		this.sumSmcNumber = sumSmcNumber;
	}
	public Long getSumThueMb() {
		return sumThueMb;
	}
	public void setSumThueMb(Long sumThueMb) {
		this.sumThueMb = sumThueMb;
	}
	public Long getSumTyLeHt() {
		return sumTyLeHt;
	}
	public void setSumTyLeHt(Long sumTyLeHt) {
		this.sumTyLeHt = sumTyLeHt;
	}
	public Long getSumDaChotGia() {
		return sumDaChotGia;
	}
	public void setSumDaChotGia(Long sumDaChotGia) {
		this.sumDaChotGia = sumDaChotGia;
	}
	public Long getSumDuyetTKDT() {
		return sumDuyetTKDT;
	}
	public void setSumDuyetTKDT(Long sumDuyetTKDT) {
		this.sumDuyetTKDT = sumDuyetTKDT;
	}
	public Long getSumXongDBHT() {
		return sumXongDBHT;
	}
	public void setSumXongDBHT(Long sumXongDBHT) {
		this.sumXongDBHT = sumXongDBHT;
	}
	public Long getSumDangTc() {
		return sumDangTc;
	}
	public void setSumDangTc(Long sumDangTc) {
		this.sumDangTc = sumDangTc;
	}
	public Long getSumChuaTk() {
		return sumChuaTk;
	}
	public void setSumChuaTk(Long sumChuaTk) {
		this.sumChuaTk = sumChuaTk;
	}
	public Long getSumHSHCVeTc() {
		return sumHSHCVeTc;
	}
	public void setSumHSHCVeTc(Long sumHSHCVeTc) {
		this.sumHSHCVeTc = sumHSHCVeTc;
	}
	public Long getSumCountTonHSHCChuaVeTC() {
		return sumCountTonHSHCChuaVeTC;
	}
	public void setSumCountTonHSHCChuaVeTC(Long sumCountTonHSHCChuaVeTC) {
		this.sumCountTonHSHCChuaVeTC = sumCountTonHSHCChuaVeTC;
	}
	public Long getSumN1TonHSHCChuaVeTC() {
		return sumN1TonHSHCChuaVeTC;
	}
	public void setSumN1TonHSHCChuaVeTC(Long sumN1TonHSHCChuaVeTC) {
		this.sumN1TonHSHCChuaVeTC = sumN1TonHSHCChuaVeTC;
	}
	public Long getSumN2TonHSHCChuaVeTC() {
		return sumN2TonHSHCChuaVeTC;
	}
	public void setSumN2TonHSHCChuaVeTC(Long sumN2TonHSHCChuaVeTC) {
		this.sumN2TonHSHCChuaVeTC = sumN2TonHSHCChuaVeTC;
	}
	public Long getSumN3TonHSHCChuaVeTC() {
		return sumN3TonHSHCChuaVeTC;
	}
	public void setSumN3TonHSHCChuaVeTC(Long sumN3TonHSHCChuaVeTC) {
		this.sumN3TonHSHCChuaVeTC = sumN3TonHSHCChuaVeTC;
	}
	public Long getSumN4TonHSHCChuaVeTC() {
		return sumN4TonHSHCChuaVeTC;
	}
	public void setSumN4TonHSHCChuaVeTC(Long sumN4TonHSHCChuaVeTC) {
		this.sumN4TonHSHCChuaVeTC = sumN4TonHSHCChuaVeTC;
	}
	public Long getSumDuDieuKienTk() {
		return sumDuDieuKienTk;
	}
	public void setSumDuDieuKienTk(Long sumDuDieuKienTk) {
		this.sumDuDieuKienTk = sumDuDieuKienTk;
	}
	
	public Long getSumYearPlan() {
		return sumYearPlan;
	}
	public void setSumYearPlan(Long sumYearPlan) {
		this.sumYearPlan = sumYearPlan;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
