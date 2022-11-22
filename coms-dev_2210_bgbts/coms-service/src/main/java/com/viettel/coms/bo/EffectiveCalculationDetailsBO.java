package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.EffectiveCalculationDetailsDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "EFFECTIVE_CALCULATION_DETAILS")
public class EffectiveCalculationDetailsBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "EFFECTIVE_CALCULATION_DETAILS_SEQ") })
	@Column(name = "EFFECTIVE_CALCULATION_DETAILS_ID", length = 10)
	private Long effectiveCalculationDetailsId;
	@Column(name = "AREA_CODE", length = 100)
	private String areaCode;
	@Column(name = "PROVINCE_CODE", length = 100)
	private String provinceCode;
	@Column(name = "STATION_CODE_VTN", length = 100)
	private String stationCodeVtn;
	@Column(name = "STATION_CODE_VCC", length = 100)
	private String stationCodeVcc;
	@Column(name = "ADDRESS", length = 1000)
	private String address;
	@Column(name = "STATION_TYPE", length = 100)
	private String stationType;
	@Column(name = "MAI_DAT", length = 100)
	private String maiDat;
	@Column(name = "DO_CAO_COT", length = 5)
	private Double doCaoCot;
	@Column(name = "LOAI_COT", length = 1000)
	private String loaiCot;
	@Column(name = "MONG_CO", length = 10)
	private String mongCo;
	@Column(name = "LOAI_NHA", length = 1000)
	private String loaiNha;
	@Column(name = "TIEP_DIA", length = 1000)
	private String tiepDia;
	@Column(name = "DIEN_CNKT", length = 10)
	private String dienCnkt;
	@Column(name = "SO_COT_DIEN", length = 10)
	private String soCotDien;
	@Column(name = "VAN_CHUYEN_BO", length = 10)
	private String vanChuyenBo;
	@Column(name = "THUE_ACQUY", length = 100)
	private String thueAcquy;
	@Column(name = "GIA_THUE_MB_THUC_TE", length = 15)
	private Long giaThueMbThucTe;
	@Column(name = "GIA_THUE_MB_THEO_DINH_MUC", length = 15)
	private Long giaThueMbTheoDinhMuc;
	@Column(name = "CAPEX_COT", length = 15)
	private Long capexCot;
	@Column(name = "CAPEX_TIEP_DIA", length = 15)
	private Long capexTiepDia;
	@Column(name = "CAPEX_AC", length = 15)
	private Long capexAc;
	@Column(name = "CAPEX_PHONG_MAY", length = 15)
	private Long capexPhongMay;
	@Column(name = "TONG_CAPEX_HT", length = 15)
	private Long tongCapexHt;
	@Column(name = "VCC_CHAO_GIA_HT", length = 15)
	private Double vccChaoGiaHt;
	@Column(name = "VCC_CHAO_GIA_ACQUY", length = 15)
	private Double vccChaoGiaAcquy;
	@Column(name = "TONG_CONG", length = 15)
	private Long tongCong;
	@Column(name = "DESCRIPTION", length = 1000)
	private String description;
	@Column(name = "NGAY_GUI", length = 22)
	private Date ngayGui;
	@Column(name = "NGAY_HOAN_THANH", length = 22)
	private Date ngayHoanThanh;
	@Column(name = "NGUOI_LAP", length = 200)
	private String nguoiLap;
	@Column(name = "CAPEX_HT_VCC", length = 15)
	private Long capexHtVcc;
	@Column(name = "NPV", length = 15)
	private Long npv;
	@Column(name = "IRR", length = 3)
	private Double irr;
	@Column(name = "THOI_GIAN_HV", length = 3)
	private Double thoiGianHv;
	@Column(name = "LNST_DT", length = 3)
	private Double lnstDt;
	@Column(name = "CONCLUDE", length = 200)
	private String conclude;
	@Column(name = "WO_ID", length = 200)
	private Long woId;
	@Column(name = "WO_CODE", length = 200)
	private String woCode;
	@Column(name = "STATUS", length = 2)
	private String status;
	@Column(name = "NPV_STRING", length = 15)
	private String npvString;
	@Column(name = "IRR_STRING", length = 3)
	private String irrString;
	@Column(name = "THOI_GIAN_HV_STRING", length = 3)
	private String thoiGianHvString;
	@Column(name = "LNST_DT_STRING", length = 3)
	private String lnstDtString;
	@Column(name = "CAPEX_TRUOC_VAT_STRING", length = 3)
	private String capexTruocVatString;

	public Long getEffectiveCalculationDetailsId() {
		return effectiveCalculationDetailsId;
	}

	public void setEffectiveCalculationDetailsId(Long effectiveCalculationDetailsId) {
		this.effectiveCalculationDetailsId = effectiveCalculationDetailsId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getStationCodeVtn() {
		return stationCodeVtn;
	}

	public void setStationCodeVtn(String stationCodeVtn) {
		this.stationCodeVtn = stationCodeVtn;
	}

	public String getStationCodeVcc() {
		return stationCodeVcc;
	}

	public void setStationCodeVcc(String stationCodeVcc) {
		this.stationCodeVcc = stationCodeVcc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStationType() {
		return stationType;
	}

	public void setStationType(String stationType) {
		this.stationType = stationType;
	}

	public String getMaiDat() {
		return maiDat;
	}

	public void setMaiDat(String maiDat) {
		this.maiDat = maiDat;
	}

	public Double getDoCaoCot() {
		return doCaoCot;
	}

	public void setDoCaoCot(Double doCaoCot) {
		this.doCaoCot = doCaoCot;
	}

	public String getLoaiCot() {
		return loaiCot;
	}

	public void setLoaiCot(String loaiCot) {
		this.loaiCot = loaiCot;
	}

	public String getMongCo() {
		return mongCo;
	}

	public void setMongCo(String mongCo) {
		this.mongCo = mongCo;
	}

	public String getLoaiNha() {
		return loaiNha;
	}

	public void setLoaiNha(String loaiNha) {
		this.loaiNha = loaiNha;
	}

	public String getTiepDia() {
		return tiepDia;
	}

	public void setTiepDia(String tiepDia) {
		this.tiepDia = tiepDia;
	}

	public String getDienCnkt() {
		return dienCnkt;
	}

	public void setDienCnkt(String dienCnkt) {
		this.dienCnkt = dienCnkt;
	}

	public String getSoCotDien() {
		return soCotDien;
	}

	public void setSoCotDien(String soCotDien) {
		this.soCotDien = soCotDien;
	}

	public String getVanChuyenBo() {
		return vanChuyenBo;
	}

	public void setVanChuyenBo(String vanChuyenBo) {
		this.vanChuyenBo = vanChuyenBo;
	}

	public String getThueAcquy() {
		return thueAcquy;
	}

	public void setThueAcquy(String thueAcquy) {
		this.thueAcquy = thueAcquy;
	}

	public Long getGiaThueMbThucTe() {
		return giaThueMbThucTe;
	}

	public void setGiaThueMbThucTe(Long giaThueMbThucTe) {
		this.giaThueMbThucTe = giaThueMbThucTe;
	}

	public Long getGiaThueMbTheoDinhMuc() {
		return giaThueMbTheoDinhMuc;
	}

	public void setGiaThueMbTheoDinhMuc(Long giaThueMbTheoDinhMuc) {
		this.giaThueMbTheoDinhMuc = giaThueMbTheoDinhMuc;
	}

	public Long getCapexCot() {
		return capexCot;
	}

	public void setCapexCot(Long capexCot) {
		this.capexCot = capexCot;
	}

	public Long getCapexTiepDia() {
		return capexTiepDia;
	}

	public void setCapexTiepDia(Long capexTiepDia) {
		this.capexTiepDia = capexTiepDia;
	}

	public Long getCapexAc() {
		return capexAc;
	}

	public void setCapexAc(Long capexAc) {
		this.capexAc = capexAc;
	}

	public Long getCapexPhongMay() {
		return capexPhongMay;
	}

	public void setCapexPhongMay(Long capexPhongMay) {
		this.capexPhongMay = capexPhongMay;
	}

	public Long getTongCapexHt() {
		return tongCapexHt;
	}

	public void setTongCapexHt(Long tongCapexHt) {
		this.tongCapexHt = tongCapexHt;
	}

	public Long getTongCong() {
		return tongCong;
	}

	public void setTongCong(Long tongCong) {
		this.tongCong = tongCong;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getNgayGui() {
		return ngayGui;
	}

	public void setNgayGui(Date ngayGui) {
		this.ngayGui = ngayGui;
	}

	public Date getNgayHoanThanh() {
		return ngayHoanThanh;
	}

	public void setNgayHoanThanh(Date ngayHoanThanh) {
		this.ngayHoanThanh = ngayHoanThanh;
	}

	public String getNguoiLap() {
		return nguoiLap;
	}

	public void setNguoiLap(String nguoiLap) {
		this.nguoiLap = nguoiLap;
	}

	public Long getCapexHtVcc() {
		return capexHtVcc;
	}

	public void setCapexHtVcc(Long capexHtVcc) {
		this.capexHtVcc = capexHtVcc;
	}

	public Long getNpv() {
		return npv;
	}

	public void setNpv(Long npv) {
		this.npv = npv;
	}

	public Double getIrr() {
		return irr;
	}

	public void setIrr(Double irr) {
		this.irr = irr;
	}

	public Double getThoiGianHv() {
		return thoiGianHv;
	}

	public void setThoiGianHv(Double thoiGianHv) {
		this.thoiGianHv = thoiGianHv;
	}

	public Double getLnstDt() {
		return lnstDt;
	}

	public void setLnstDt(Double lnstDt) {
		this.lnstDt = lnstDt;
	}

	public String getConclude() {
		return conclude;
	}

	public void setConclude(String conclude) {
		this.conclude = conclude;
	}

	public Long getWoId() {
		return woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	public String getNpvString() {
		return npvString;
	}

	public void setNpvString(String npvString) {
		this.npvString = npvString;
	}

	public String getIrrString() {
		return irrString;
	}

	public void setIrrString(String irrString) {
		this.irrString = irrString;
	}

	public String getThoiGianHvString() {
		return thoiGianHvString;
	}

	public void setThoiGianHvString(String thoiGianHvString) {
		this.thoiGianHvString = thoiGianHvString;
	}

	public String getLnstDtString() {
		return lnstDtString;
	}

	public void setLnstDtString(String lnstDtString) {
		this.lnstDtString = lnstDtString;
	}

	public String getCapexTruocVatString() {
		return capexTruocVatString;
	}

	public void setCapexTruocVatString(String capexTruocVatString) {
		this.capexTruocVatString = capexTruocVatString;
	}

	public Double getVccChaoGiaHt() {
		return vccChaoGiaHt;
	}

	public void setVccChaoGiaHt(Double vccChaoGiaHt) {
		this.vccChaoGiaHt = vccChaoGiaHt;
	}

	public Double getVccChaoGiaAcquy() {
		return vccChaoGiaAcquy;
	}

	public void setVccChaoGiaAcquy(Double vccChaoGiaAcquy) {
		this.vccChaoGiaAcquy = vccChaoGiaAcquy;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		EffectiveCalculationDetailsDTO dto = new EffectiveCalculationDetailsDTO();
		dto.setEffectiveCalculationDetailsId(this.getEffectiveCalculationDetailsId());
		dto.setAreaCode(this.getAreaCode());
		dto.setProvinceCode(this.getProvinceCode());
		dto.setStationCodeVtn(this.getStationCodeVtn());
		dto.setStationCodeVcc(this.getStationCodeVcc());
		dto.setAddress(this.getAddress());
		dto.setStationType(this.getStationType());
		dto.setMaiDat(this.getMaiDat());
		dto.setDoCaoCot(this.getDoCaoCot());
		dto.setLoaiCot(this.getLoaiCot());
		dto.setMongCo(this.getMongCo());
		dto.setLoaiNha(this.getLoaiNha());
		dto.setTiepDia(this.getTiepDia());
		dto.setDienCnkt(this.getDienCnkt());
		dto.setSoCotDien(this.getSoCotDien());
		dto.setVanChuyenBo(this.getVanChuyenBo());
		dto.setThueAcquy(this.getThueAcquy());
		dto.setGiaThueMbThucTe(this.getGiaThueMbThucTe());
		dto.setGiaThueMbTheoDinhMuc(this.getGiaThueMbTheoDinhMuc());
		dto.setCapexCot(this.getCapexCot());
		dto.setCapexTiepDia(this.getCapexTiepDia());
		dto.setCapexAc(this.getCapexAc());
		dto.setCapexPhongMay(this.getCapexPhongMay());
		dto.setTongCapexHt(this.getTongCapexHt());
		dto.setVccChaoGiaHt(this.getVccChaoGiaHt());
		dto.setVccChaoGiaAcquy(this.getVccChaoGiaAcquy());
		dto.setTongCong(this.getTongCong());
		dto.setDescription(this.getDescription());
		dto.setNgayGui(this.getNgayGui());
		dto.setNgayHoanThanh(this.getNgayHoanThanh());
		dto.setNguoiLap(this.getNguoiLap());
		dto.setCapexHtVcc(this.getCapexHtVcc());
		dto.setNpv(this.getNpv());
		dto.setIrr(this.getIrr());
		dto.setThoiGianHv(this.getThoiGianHv());
		dto.setLnstDt(this.getLnstDt());
		dto.setConclude(this.getConclude());
		dto.setWoId(this.woId);
		dto.setStatus(this.status);
		dto.setWoCode(this.woCode);
		dto.setNpvString(this.getNpvString());
		dto.setIrrString(this.getIrrString());
		dto.setThoiGianHvString(this.getThoiGianHvString());
		dto.setLnstDtString(this.getLnstDtString());
		dto.setCapexTruocVatString(this.capexTruocVatString);
		return dto;
	}

}
